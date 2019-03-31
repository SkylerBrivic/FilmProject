import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


@WebServlet("/CheckinProduct")
public class CheckinProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public CheckinProduct() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	
	//the parameter "QR_Code" stores the string representing the number of the QR Code for the product that is about to be checked in.
	//a return value of 0 means the product was succesfully checked back in
	//a return value of 1 means that the QR Code entered in was invalid
	//a return value of 2 means that the product wasn't checked out to anyone.
	//a return value of 3 indicates an invalid password was used, and the product has not been checked back in.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "productList";
		String checkoutTable = "checkoutList";
		
		
		int availabilityStatus = -1;
		String QR_Code = request.getParameter("QR_Code");
		
		
		
		String userPassword = request.getParameter("password");
		
		PasswordValidator validator = new PasswordValidator();
		
		if(validator.validate(userPassword) == false)
		{
			response.getWriter().println("3");
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
			String myQuery = "SELECT * from " + productTable + " Where QR_Code = " + QR_Code + ";";
			ResultSet resultSet = statement.executeQuery(myQuery);
			
			while(resultSet.next())
			{
				availabilityStatus = Integer.parseInt(resultSet.getString(4));
			}
			
			if(availabilityStatus == -1)
			{
				response.getWriter().println("1");
				return;	
			}
			
			else if(availabilityStatus == 1)
			{
				response.getWriter().println("2");
				return;
				
			}
			
			myQuery = "UPDATE " + productTable + " set isAvailable = 1 where QR_Code = " + QR_Code + ";";
			statement.executeUpdate(myQuery);
			
			myQuery = "UPDATE " + checkoutTable + " set checkinDate = CURRENT_DATE() Where QR_Code = " + QR_Code + " AND checkinDate IS NULL;";
			statement.executeUpdate(myQuery);
			response.getWriter().println("0");
			
			
			
		}
		catch(SQLException e)
		{}
	
	}

}
