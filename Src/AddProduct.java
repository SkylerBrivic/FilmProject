import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


@WebServlet("/AddProduct")
public class AddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public AddProduct() 
    {
        super();   
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	
	
	//the parameter manufacturer stores the manufacturer's name
	//the parameter productName stores the product's name
	//a value of "1" for isAvailable means the product is available for rental, while a value of "0" means the product is checked out
	//if the user entered in a valid password earlier to log in, then the function returns 0. Otherwise, it returns 1 and the user's
	//request is denied
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "productList";
		
		
		String manufacturer = request.getParameter("manufacturer");
		String product = request.getParameter("productName");
		String userPassword = request.getParameter("password");
		
		PasswordValidator validator = new PasswordValidator();
		
		if(validator.validate(userPassword) == false)
		{
			response.getWriter().println("1");
			return;
		}
		
		
		
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{	
		}
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName + "?serverTimezone=UTC", userName, password))
		{
			Statement statement = connection.createStatement();
			
			String myQuery = "INSERT INTO " + productTable + "(manufacturer, productName, isAvailable) Values( '" + manufacturer + "', '" + product + "', 1);";   
			statement.executeUpdate(myQuery);	
		}
		
		
		
		
		catch(SQLException e)
		{
		}
		
		
		response.getWriter().println("0");
		return;
		}

			
	}



