import java.sql.*;


public class PasswordValidator
{
	
	//validate returns true if the given password passed into the function matches the one stored in the PasswordTable of the database. 
	//if the passwords do not match (or the server can't be reached) then the function returns false
public boolean validate(String userPassword)
{
	String serverName = "localhost:3306";
	String databaseName = "FilmProject";
	String userName = "root";
	String databasePassword = "PondFish";
	
	String officialPassword = "";
	try{
		Class.forName("com.mysql.jdbc.Driver");
	}
	catch(ClassNotFoundException e)
	{	
	}
	
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName + "?serverTimezone=UTC", userName, databasePassword))
	{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * from PasswordTable;");
		while(resultSet.next())
		{
		officialPassword = resultSet.getString(1);	
		}
		
		if(officialPassword.equals(userPassword))
			return true;
		else
			return false;
	
}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
	return false;
	
}
}
