package filmObjects;

import java.sql.*;
import java.io.*;
import java.util.*;

//DatabaseInterface is a class which contains functions for interacting with a MySql database
//the String textFileName stores the absolute path name of the text file containing the login credentials for the database
//dataIsUpdated is a boolean variable that stores whether or not the file referenced by textFileName was succesfully fully parsed
//the database's username, password, the database's server name, the database's name, the product table name, the transaction table name,
//the student table name and the password table name are stored in String variables.
//the constructor of DatabaseInterface calls the parseFile() function, which reads in the database information from the file referenced
//by textFileName
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

//selectProductByProductID returns an ArrayList containing the product object representing the product with the Product ID specified by Product_ID
public ArrayList<Product> selectProductByProductID(String Product_ID)
{
	return selectProduct(" WHERE Product_ID = " + Product_ID);
}
//conditionList is the portion of the conditions following the word WHERE in the query (ex. conditionList could be " productName = 'RGB'")
//conditionList should start with a space
//this function returns an ArrayList of Products which represent all products that match the search criteria specified by the user in conditionList
//if the conditionList is the empty string ("") then an ArrayList of all products will be returned.
public ArrayList<Product> selectProduct(String conditionList)
{
	ArrayList<Product> returnList = new ArrayList<Product>();
	String tempProductID = null, tempManufacturerName = null, tempProductName = null, tempCheckoutDate = null, tempCheckinDate = null;
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
			tempProductID = mainResultSet.getString(productTableName + ".Product_ID");
			if(tempProductID == null)
				tempProductID = "N/A";
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
				ResultSet otherResultSet = otherStatement.executeQuery("SELECT * from " + transactionTableName + " WHERE Product_ID = " + tempProductID + " AND actualCheckinDate IS NULL;");
				if(otherResultSet.next())
			{
					tempCheckoutDate = otherResultSet.getString(transactionTableName + ".checkoutDate");
			}
			if(tempCheckoutDate == null)
				tempCheckoutDate = "N/A";
			}
			tempCheckinDate = "N/A";
			returnList.add(new Product(tempProductID, tempManufacturerName, tempProductName, tempCheckoutDate, tempCheckinDate));
		}
	}
	catch(SQLException e)
	{
		System.out.println("SQL Exception occured in selectProduct function of DatabaseInterface class");
		e.printStackTrace();	
	}
	
	return returnList;
	
}


//selectTransaction(String) takes as input a conditionList representing the portion of a MySql search query following the "WHERE" keyword
//conditionList should start with a space
//this function returns an ArrayList of all transactions that matched the search criteria specified in conditionList
public ArrayList<Transaction> selectTransaction(String conditionList)
{
	ArrayList<Transaction> returnList = new ArrayList<Transaction>();
	ResultSet mainResultSet = null;
	String tempTransactionNumber = null, tempProductID = null, tempManufacturerName = null, tempProductName = null, tempCheckoutDate = null, tempCheckinDate = null, tempStudentName = null, tempStudentNumber = null, tempOrganizationName = null, tempEmail = null, tempExpectedReturnDate = null;
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
			tempProductID = mainResultSet.getString(productTableName + ".Product_ID");
			if(tempProductID == null)
				tempProductID = "N/A";
			tempManufacturerName = mainResultSet.getString(productTableName + ".manufacturer");
			if(tempManufacturerName == null)
				tempManufacturerName = "N/A";
			tempProductName = mainResultSet.getString(productTableName + ".productName");
			if(tempProductName == null)
				tempProductName = "N/A";
			tempCheckoutDate = mainResultSet.getString(transactionTableName + ".checkoutDate");
			if(tempCheckoutDate == null)
				tempCheckoutDate = "N/A";
			tempCheckinDate = mainResultSet.getString(transactionTableName + ".actualCheckinDate");
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
			tempExpectedReturnDate = mainResultSet.getString(transactionTableName + ".expectedCheckinDate");
			if(tempExpectedReturnDate == null)
				tempExpectedReturnDate = "N/A";
			
			
			returnList.add(new Transaction(tempTransactionNumber, tempProductID, tempManufacturerName, tempProductName, tempStudentNumber, tempStudentName, tempEmail, tempOrganizationName, tempCheckoutDate, tempCheckinDate, tempExpectedReturnDate));
			
		}
	}
		catch(SQLException e)
		{
			System.err.println("An SQLException occured while processing the list of data in the selectTransaction() function of the DatabaseInterface class.");
			System.err.println("Please check the syntax of your condition string, or use the wrapper function to help with making database queries");
			e.printStackTrace();
			return null;
		}
		
		return returnList;

	
}
//the parameter availabilityStatus stores 0 if the program should display all transactions, 1 if only 
//completed transactions should be shown (the product has been returned), and 2 if only ongoing
//transactions (where the product has not yet been returned) should be shown.
//This function acts as a wrapper function that handles the call to selectTransaction(String) with the appropriate conditionList
//for each of the 3 possibilities for availabilityStatus. An ArrayList of all transactions matching availability status's criteria is returned
public ArrayList<Transaction> selectTransaction(int availabilityStatus)
{
	String myQuery = " WHERE ";
	if(availabilityStatus == 1)
		myQuery += " actualCheckinDate IS NOT NULL AND ";
	else if(availabilityStatus == 2)
		myQuery += " actualCheckinDate IS NULL AND ";
	myQuery += (productTableName + ".Product_ID = " + transactionTableName + ".Product_ID AND " + transactionTableName + ".PrimaryStudentKey = " + studentTableName + ".StudentPrimaryKey"); 
	return selectTransaction(myQuery);
}

//this function takes as input the name of a manufacturer to be added to the database, the name of a product to be added to the database,
//and the number of copies of that item that should be added into the database.
public void addProduct(String manufacturerName, String productName, int quantity)
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
		
		for(int i = 0; i < quantity; ++i)
		{
		statement.executeUpdate(myQuery);
		}
		
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}

//updateProduct takes as input the Product ID number of the product to be updated, and the name of the manufacturer and 
//the product name that the product should be changed to. The function updates the entry in the database with the specified Product ID number.
public void updateProduct(String Product_ID, String Manufacturer, String Product)
{
	String conditionList = "";
	if(Manufacturer.isEmpty() && Product.isEmpty())
		return;
	else if(Manufacturer.isEmpty())
		conditionList = "productName = '" + Product + "' ";
	else if(Product.isEmpty())
		conditionList = "Manufacturer = '" + Manufacturer + "' ";
	else
		conditionList = "productName = '" + Product + "', Manufacturer = '" + Manufacturer + "' ";
		
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	
	String myQuery = "UPDATE " + productTableName + " SET " +  conditionList + " WHERE Product_ID = " + Product_ID + ";";
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

//This function takes as input the Product ID Number of the product that the user is checking out, the student number of the student checking the product out,
//the name of the student checking the product out, the organization name of the student, the student's email, and the estimated return date
//for the product (in yyyy-MM-dd format). The function updates the database to reflect checking out the product specified by Product_ID
public void checkoutProduct(String Product_ID, String studentNumber, String studentName, String organizationName, String email, String returnDate)
{
	String studentKey = "";
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	String myQuery = "";
	
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
	
			myQuery = "INSERT INTO " + studentTableName + "(StudentNumber, StudentName, OrganizationName, Email) VALUES ('" + studentNumber + "', '" + studentName + "', '" + organizationName + "', '" + email + "');";	
			statement.executeUpdate(myQuery);
			
			Statement otherStatement = connection.createStatement();
			ResultSet mySet = otherStatement.executeQuery("SELECT MAX(StudentPrimaryKey) from " + studentTableName + ";");
			if(mySet.next())
				studentKey = mySet.getString(1);
			
		
		myQuery = "INSERT INTO " + transactionTableName + " (PrimaryStudentKey, Product_ID, checkoutDate, actualCheckinDate, expectedCheckinDate) VALUES (" + studentKey + ", " + Product_ID + ", CURRENT_DATE, NULL, '" + returnDate + "');";
		statement.executeUpdate(myQuery);
		myQuery = "UPDATE " + productTableName + " SET isAvailable = 0 WHERE Product_ID = " + Product_ID + ";";
		statement.executeUpdate(myQuery);
		
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
}

//this function takes as input a String representing the product ID Number of an item in the database.
//the function deletes that item from the product list table, along with any transactions that included the item.
public void deleteProduct(String Product_ID)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	String myQuery = "Delete from " + transactionTableName + " WHERE  Product_ID = " + Product_ID + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
		myQuery = "DELETE FROM " + productTableName + " WHERE Product_ID = " + Product_ID + ";";
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}
//this function takes as input the Product ID Number of the product that the user is going to check in.
//the function updates the database appropriately to reflect the fact that the product is now checked back in.
public void checkinProduct(String Product_ID)
{
	if(dataIsUpdated == false)
		parseFile();
	
	if(dataIsUpdated == false)
	{
		System.err.println("Attempt to access database failed due to missing or invalidly formatted credentials in the file " + textFileName);
		System.exit(1);
		return;
	}
	
	String myQuery = "UPDATE " + transactionTableName + " SET actualCheckinDate = CURRENT_DATE WHERE actualCheckinDate IS NULL AND Product_ID = " + Product_ID + ";";
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, username, databasePassword))
	{
		Statement statement = connection.createStatement();
		statement.executeUpdate(myQuery);
		myQuery = "UPDATE " + productTableName + " SET isAvailable = 1 WHERE Product_ID = " + Product_ID + ";";
		statement.executeUpdate(myQuery);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
}

//this function takes as input a String that the user has entered in for the password.
//the program checks the password table of the database to see if this is the correct password.
//If it is correct, then it returns true. Otherwise, it returns false.
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
			if(resultSet.getString("Password").equals(userEnteredPassword))
				isValid = true;
		}
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}

	return isValid;
	
}
//this function takes as input a String representing the site's current password and a String representing the new password for the site.
//if the old password was correctly entered in, then the new password is set for the password for the site, and true is returned.
//Otherwise, the password is not changed and false is returned.
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

//parseFile reads through the file stored in textFileName and parses the database's login credentials, name, and tables.
//if the file is succesfully parsed fully, then dataIsUpdated is set to true
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
