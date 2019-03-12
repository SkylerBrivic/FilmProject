import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
    public ChangePassword() {
        super();
        }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	//the old password entered in by the user is stored in the parameter oldPassword
	//the new password the user has requested is stored in the parameter newPassword
	//if the value of oldPassword matches the password stored in the database, then the password
	//in the database is updated to be the new password, and 0 is returned. Otherwise, the password
	//is not changed and 1 is returned.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String passwordTable = "passwordTable";
		
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		
		PasswordValidator validator = new PasswordValidator();
		if(validator.validate(oldPassword) == false)
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
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, userName, password))
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE " + passwordTable + " SET Password = '" + newPassword + "';");
		
		}
		catch(SQLException e)
		{
			System.out.println("An Exception occured");
		}
		
		response.getWriter().println("0");

}
}
