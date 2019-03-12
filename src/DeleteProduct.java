import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


@WebServlet("/DeleteProduct")
public class DeleteProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public DeleteProduct() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	//the parameter QR_Code contains the QR Code of the product to be deleted
	//a return value of 0 means the product was succesfully deleted, and any entries associated with it in the checkoutList table have been deleted.
	//a return value of 1 means the QR Code was invalid or already deleted
	//a return value of 2 means that the user used an invalid password, and needs to log in again.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "productList";
		String checkoutTable = "checkoutList";
		
		
		String QR_Code = request.getParameter("QR_Code");
		boolean isValidCode = false;
		
		String userPassword = request.getParameter("password");
		
		PasswordValidator validator = new PasswordValidator();
		
		if(validator.validate(userPassword) == false)
		{
			response.getWriter().println("2");
			return;
		}
		
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{	
		}
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, userName, password))
		{
			Statement statement = connection.createStatement();
			String myQuery = "Select * from " + productTable + " where QR_Code = " + QR_Code + ";" ;
			ResultSet resultSet = statement.executeQuery(myQuery);
			
			while(resultSet.next())
			{
				isValidCode = true;
			}
			
		if(!isValidCode)
		{
			response.getWriter().println("1");
			return;
		}
		
		myQuery = "DELETE FROM " + checkoutTable + " where QR_Code = " + QR_Code + ";";
		statement.executeUpdate(myQuery);
		
		myQuery = "DELETE FROM " + productTable + " where QR_Code = " + QR_Code + ";";
		statement.executeUpdate(myQuery);
		
		response.getWriter().println("0");	
	}
		
		catch(SQLException e)
		{}

}
}

