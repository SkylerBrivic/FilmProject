package filmObjects;

import java.sql.*;
import java.io.*;
import java.util.*;


public class DatabaseInterface 
{
private static String textFileName = "/Users/Skyler/Desktop/information.txt";
private boolean dataIsUpdated = false;
private String username = "", databasePassword = "", serverName = "", databaseName = "", productTableName = "", transactionTableName = "", studentTableName = "", passwordTableName = "";

public DatabaseInterface()
{
	try{
		Class.forName("com.mysql.jdbc.Driver");
	}
	catch(ClassNotFoundException e)
	{	
	}
	parseFile();
}

//table locations stores the tables the user wants to select from (ex. a tableLocation String equal to "productList, checkoutList" to select entries from a table named productList and a table named checkoutList
//conditionList is the portion of the conditions following the word WHERE in the query (ex. conditionList could be "studentName = 'John Smith'")
public ArrayList<Product> selectProduct(String conditionList)
{
	ArrayList<Product> returnList = new ArrayList<Product>();
	String tempQR = null, tempManufacturerName = null, tempProductName = null, tempCheckoutDate = null, tempCheckinDate = null;
	ResultSet mainResultSet = null;
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return null;
	}
	
	String myQuery = "SELECT * FROM " + productTableName + conditionList + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		mainResultSet = statement.executeQuery(myQuery);
		while(mainResultSet.next())
		{
			tempQR = mainResultSet.getString(productTableName + ".QR_Code");
			if(tempQR == null)
				tempQR = "N/A";
			tempManufacturerName = mainResultSet.getString(productTableName + ".manufacturer");
			if(tempManufacturerName == null)
				tempManufacturerName = "N/A";
			tempProductName = mainResultSet.getString(productTableName + ".productName");
			if(tempProductName == null)
				tempProductName = "N/A";
			if(Integer.parseInt(mainResultSet.getString(productTableName + ".isAvailable")) == 1)
			{
				tempCheckoutDate = "N/A";	
			}
			else
			{
				Statement otherStatement = connection.createStatement();
				ResultSet otherResultSet = otherStatement.executeQuery("SELECT * from " + transactionTableName + " WHERE QR_Code = " + tempQR + " AND checkinDate IS NULL;");
				if(otherResultSet.next())
			{
					tempCheckoutDate = otherResultSet.getString(transactionTableName + ".checkoutDate");
			}
			if(tempCheckoutDate == null)
				tempCheckoutDate = "N/A";
			}
			tempCheckinDate = "N/A";
			returnList.add(new Product(tempQR, tempManufacturerName, tempProductName, tempCheckoutDate, tempCheckinDate));
		}
	}
	catch(SQLException e)
	{
		System.out.println("SQL Exception occured in selectProduct function of DatabaseInterface class");
		e.printStackTrace();
		
	}
	
	return returnList;
	
}



public ArrayList<Transaction> selectTransaction(String conditionList)
{
	ArrayList<Transaction> returnList = new ArrayList<Transaction>();
	ResultSet mainResultSet = null;
	String tempTransactionNumber = null, tempQR = null, tempManufacturerName = null, tempProductName = null, tempCheckoutDate = null, tempCheckinDate = null, tempStudentName = null, tempStudentNumber = null, tempOrganizationName = null, tempEmail = null;
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return null;
	}
	
	String myQuery = "SELECT * FROM " + productTableName + ", " + transactionTableName + ", " + studentTableName  + conditionList + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		mainResultSet = statement.executeQuery(myQuery);
		while(mainResultSet.next())
		{
			tempTransactionNumber = mainResultSet.getString(transactionTableName + ".transactionNumber");
			if(tempTransactionNumber == null)
				tempTransactionNumber = "N/A";
			tempQR = mainResultSet.getString(productTableName + ".QR_Code");
			if(tempQR == null)
				tempQR = "N/A";
			tempManufacturerName = mainResultSet.getString(productTableName + ".manufacturer");
			if(tempManufacturerName == null)
				tempManufacturerName = "N/A";
			tempProductName = mainResultSet.getString(productTableName + ".productName");
			if(tempProductName == null)
				tempProductName = "N/A";
			tempCheckoutDate = mainResultSet.getString(transactionTableName + ".checkoutDate");
			if(tempCheckoutDate == null)
				tempCheckoutDate = "N/A";
			tempCheckinDate = mainResultSet.getString(transactionTableName + ".checkinDate");
			if(tempCheckinDate == null)
				tempCheckinDate = "N/A";
			tempStudentName = mainResultSet.getString(studentTableName + ".StudentName");
			if(tempStudentName == null)
				tempStudentName = "N/A";
			tempStudentNumber = mainResultSet.getString(studentTableName + ".StudentNumber");
			if(tempStudentNumber == null)
				tempStudentNumber = "N/A";
			tempOrganizationName = mainResultSet.getString(studentTableName + ".OrganizationName");
			if(tempOrganizationName == null)
				tempOrganizationName = "N/A";
			tempEmail = mainResultSet.getString(studentTableName + ".Email");
			if(tempEmail == null)
				tempEmail = "N/A";
			
			returnList.add(new Transaction(tempTransactionNumber, tempQR, tempManufacturerName, tempProductName, tempStudentNumber, tempStudentName, tempEmail, tempOrganizationName, tempCheckoutDate, tempCheckinDate));
			
		}
	}
		catch(SQLException e)
		{
			System.err.println("An SQLException occured while processing the list of data in the selectTransaction() function of the DatabaseInterface class.");
			System.err.println("Please check the syntax of your condition string, or use the wrapper function to help with making database queries");
			System.exit(1);
			return null;
		}
		
		return returnList;

	
}
//the parameter availabilityStatus stores 0 if the program should display all transactions, 1 if only 
//completed transactions should be shown (the product has been returned), and 2 if only ongoing
//transactions (where the product has not yet been returned) should be shown.
public ArrayList<Transaction> selectTransaction(int availabilityStatus)
{
	String myQuery = " WHERE ";
	if(availabilityStatus == 1)
		myQuery += " checkinDate IS NOT NULL AND ";
	else if(availabilityStatus == 2)
		myQuery += " checkinDate IS NULL AND ";
	myQuery += (productTableName + ".QR_Code = " + transactionTableName + ".QR_Code AND " + transactionTableName + ".StudentNumber = " + studentTableName + ".StudentNumber"); 
	return selectTransaction(myQuery);
}

public void addProduct(String manufacturerName, String productName)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	
	String myQuery = "Insert Into " + productTableName + "(manufacturer, productName, isAvailable) VALUES('" + manufacturerName + "', '" + productName + "', 1);";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
		
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}

public void updateProduct(String QR_Code, String newManufacturerName, String newProductName)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	
	String myQuery = "UPDATE " + productTableName + " SET manufacturer = '" + newManufacturerName + "', productName = '" + newProductName + "' WHERE QR_Code = " + QR_Code + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
}
public void checkoutProduct(String QR_Code, String studentNumber, String studentName, String organizationName, String email)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	String myQuery = "";
	myQuery = "SELECT * from " + studentTableName + " WHERE studentNumber = '" + studentNumber + "';";
	
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(myQuery);
		if(resultSet.next())
		{	
			myQuery = "UPDATE " + studentTableName + " SET studentName = '" + studentName + "', email = '" + email + "', organizationName = '" + organizationName + "' WHERE studentNumber = '" + studentNumber + "';";
			Statement otherStatement = connection.createStatement();
			otherStatement.executeUpdate(myQuery);
			
		}
		else
		{
			myQuery = "INSERT INTO " + studentTableName + " VALUES ('" + studentNumber + "', '" + studentName + "', '" + organizationName + "', '" + email + "');";	
			statement.executeUpdate(myQuery);
		}
		
		myQuery = "UPDATE " + productTableName + " SET isAvailable = 0 WHERE QR_Code = " + QR_Code + ";";
		statement.executeUpdate(myQuery);
		myQuery = "INSERT INTO " + transactionTableName + " (QR_Code, StudentNumber, checkoutDate, checkinDate) VALUES (" + QR_Code + ", '" + studentNumber + "', CURRENT_DATE, NULL);";
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
}

public void deleteProduct(String QR_Code)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	String myQuery = "Delete from " + transactionTableName + " WHERE  QR_Code = " + QR_Code + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
		myQuery = "DELETE FROM " + productTableName + " WHERE QR_Code = " + QR_Code + ";";
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}

public void checkinProduct(String QR_Code)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	
	String myQuery = "UPDATE " + transactionTableName + " SET checkinDate = CURRENT_DATE WHERE checkinDate IS NULL AND QR_Code = " + QR_Code + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
		myQuery = "UPDATE " + productTableName + " SET isAvailable = 1 WHERE QR_Code = " + QR_Code + ";";
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}

public boolean validatePassword(String userEnteredPassword)
{
	boolean isValid = false;
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return false;
	}
	
	String myQuery = "SELECT * from " + passwordTableName + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(myQuery);
		
		if(resultSet.next())
		{
			if(resultSet.getString(1).equals(userEnteredPassword))
				isValid = true;
		}
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}

	return isValid;
	
}

public boolean updatePassword(String oldPassword, String newPassword)
{
	boolean isValid = validatePassword(oldPassword);
	if(!isValid)
		return false;
	String myQuery = "UPDATE " + passwordTableName + " SET Password = '" + newPassword + "';";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		 statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{}
	
	return true;
	
}
private void parseFile()
{
	boolean usernameSet = false, databasePasswordSet = false, serverNameSet = false, databaseNameSet = false, productTableNameSet = false, transactionTableNameSet = false, studentTableNameSet = false, passwordTableNameSet = false;
	BufferedReader myReader = null;
	String currentLine = null;
	String currentFirstWord = null;
	
	try {
		
		myReader = new BufferedReader(new FileReader(textFileName));
		while(usernameSet == false || databasePasswordSet == false || serverNameSet == false || databaseNameSet == false || productTableNameSet == false || transactionTableNameSet == false || studentTableNameSet == false || passwordTableNameSet == false)
		{
		currentLine = myReader.readLine();
		if(currentLine == null)
			break;
		if(currentLine.indexOf(':') == -1)
		{
			System.err.print("An error has occured in the parseFile function. Was expecting a parameter name followed by a colon and a value\n");
			System.err.println("ex. username:myName No colon was found on the line containing " + currentLine);
			System.exit(1);
			
		}
		currentFirstWord = currentLine.split(":")[0];
		currentFirstWord = currentFirstWord.trim();
		
		if(currentFirstWord.equalsIgnoreCase("username"))
		{
			username = currentLine.split(":", 2)[1].trim();
			usernameSet = true;
			continue;
		}
		else if(currentFirstWord.equalsIgnoreCase("password"))
		{
			databasePassword = currentLine.split(":", 2)[1];
			databasePasswordSet = true;
			continue;
		}
		else if(currentFirstWord.equalsIgnoreCase("serverName"))
		{
			serverName = currentLine.split(":", 2)[1].trim();
			serverNameSet = true;
			continue;
		}
		else if(currentFirstWord.equalsIgnoreCase("databaseName"))
		{
			databaseName = currentLine.split(":", 2)[1].trim();
			databaseNameSet = true;
			continue;
		}
		else if(currentFirstWord.equalsIgnoreCase("productTableName"))
		{
		productTableName = currentLine.split(":", 2)[1].trim();
		productTableNameSet = true;
		continue;
		}
		
		else if(currentFirstWord.equalsIgnoreCase("transactionTableName"))
		{
		transactionTableName = currentLine.split(":", 2)[1].trim();
		transactionTableNameSet = true;
		continue;
		}
		
		else if(currentFirstWord.equalsIgnoreCase("studentTableName"))
		{
		studentTableName = currentLine.split(":", 2)[1].trim();
		studentTableNameSet = true;
		continue;
		}
		else if(currentFirstWord.equalsIgnoreCase("passwordTableName"))
		{
			passwordTableName = currentLine.split(":", 2)[1];
			passwordTableNameSet = true;
			continue;
			
		}
		
		
	
		}
	
	}
	
	
	catch(IOException e)
	{ 
		System.err.println("IOException in function parseFile() in file DatabaseInterface\n");
		e.printStackTrace();
	}
	
	if(usernameSet && databasePasswordSet && serverNameSet && databaseNameSet && productTableNameSet && transactionTableNameSet && studentTableNameSet && passwordTableNameSet)
		dataIsUpdated = true;
	else
		{
			System.err.println("Error: A key parameter was not initialized in the textfile stored at location " + textFileName);
			System.err.print("The following parameters were not set: ");
			if(!usernameSet)
				System.err.print("username ");
			if(!databasePasswordSet)
				System.err.print("password ");
			if(!serverNameSet)
				System.err.print("serverName ");
			if(!databaseNameSet)
				System.err.print("databaseName ");
			if(!productTableNameSet)
				System.err.print("productTableName ");
			if(!transactionTableNameSet)
				System.err.print("transactionTableName ");
			if(!studentTableNameSet)
				System.err.print("studentTableName ");
			if(!passwordTableNameSet)
				System.err.print("passwordTableName ");
			System.err.println();
		dataIsUpdated = false;
		}

}
}
