import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


@WebServlet("/CheckoutProduct")
public class CheckoutProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public CheckoutProduct() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	//a return value of 0 means the product was succesfully checked out
	//a return value of 1 means the specified QR Code was invalid
	//a return value of 2 means that the product is already checked out by somebody else
	//a return value of 3 means the user does not have permission to check this product out (due to an invalid password)
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "productList";
		String checkoutTable = "checkoutList";
		String studentTable = "studentList";
		int availabilityStatus = -1;
		

		String QR_Code = request.getParameter("QR_Code");
		String studentNumber = request.getParameter("StudentNumber");
		String studentName = request.getParameter("StudentName");
		String organizationName = request.getParameter("OrganizationName");
		String email = request.getParameter("Email");
		
		String userPassword = request.getParameter("password");
		
		KeywordMatcher keywordMatcher = new KeywordMatcher();
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
			String myQuery = "Select * from " + productTable + " where QR_Code = " + QR_Code + ";" ;
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
				myQuery = "UPDATE " + productTable + " set isAvailable = 0 where QR_Code = " + QR_Code + ";";
				statement.executeUpdate(myQuery);
				
				Statement otherStatement = connection.createStatement();
				ResultSet otherSet = otherStatement.executeQuery("SELECT * FROM " + studentTable + " WHERE studentNumber = '" + studentNumber + "'");
			
				if(!otherSet.next())
				{
					myQuery = "INSERT INTO " + studentTable + " VALUES( ";
					if(keywordMatcher.isEmpty(studentNumber))
						myQuery += " null, ";
					else myQuery += ("'" + studentNumber + "', ");
					
					myQuery += ("'" + studentName + "', ");
					
					if(keywordMatcher.isEmpty(organizationName))
						myQuery += "null, ";
					else
						myQuery += ("'" + organizationName + "', ");
					
					if(keywordMatcher.isEmpty(email))
						myQuery += "null);";
					else
						myQuery += ("'" + email + "');");
					
					otherStatement.executeUpdate(myQuery);
					
					
				}
				
				
				myQuery = "INSERT INTO " + checkoutTable + "(QR_Code, StudentNumber, checkoutDate, checkinDate) Values(" + QR_Code + ", '" + studentNumber + "', CURRENT_DATE(), null);";
				statement.executeUpdate(myQuery);
				
				
				response.getWriter().println("0");
				return;	
			}
			
			else 
			{
				response.getWriter().println("2");
				return;
			}
				
		}
		
		
		
		
		catch(SQLException e)
		{
		}
		
		
		
	}

}
