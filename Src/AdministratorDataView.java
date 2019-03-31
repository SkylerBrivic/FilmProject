import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.sql.*;


@WebServlet("/AdministratorDataView")
public class AdministratorDataView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AdministratorDataView() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);	
		}

	
	
	//this servlet returns a list of transactions, which can be filtered based on user input. However, if an invalid password is entered, this servlet will instead return 2 and will not return any data.
	//the parameter TransactionNumber stores the number of the transaction
	//the parameter ProductNumber stores the QR Code of the product that the user requested
	//the parameter StudentNumber stores the Student Number of the product that the user requested
	//the parameter manufacturer stores the name of the manufacturer of the product that the user requested
	//the parameter product stores the name of the product that the user requested.
	//the parameter password stores the login password that the user entered in.
	//the parameter filterStatus stores 0 if the program should display all transactions, 1 if only completed transactions should be shown (the product has been returned), and 2 if only ongoing
	//transactions (where the product has not yet been returned) should be shown.
	//the parameter sortOrder contains a String, whose first character represents what category to sort by (A for the transactionNumber, B for the second column, and so forth)
	//and the rest of the string after that contains either 1 (for forward sort) or -1 (for backwards sort)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "ProductList";
		String checkoutTable = "checkoutList";
		String studentTable = "studentList";
		
		int filterStatus = Integer.parseInt(request.getParameter("filterStatus"));
		
		String TransactionNumber = request.getParameter("TransactionNumber");
		String ProductNumber = request.getParameter("ProductNumber");
		String Manufacturer = request.getParameter("manufacturer");
		String ProductName = request.getParameter("product");
		String StudentName = request.getParameter("studentName");
		String StudentNumber = request.getParameter("studentNumber");
		String OrganizationName = request.getParameter("organizationName");
		String Email = request.getParameter("email");
		
		
		String finalTransactionNum = "", finalProductNum = "", finalManufacturer = "", finalProduct = "", finalStudentName = "", finalStudentNum = "", finalOrganizationName = "", finalEmail = "", finalCheckinDate = "", finalCheckoutDate = "", myQuery = "";
		
		String userPassword = request.getParameter("password");
		String sortOrder = request.getParameter("sortOrder");
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		PasswordValidator validator = new PasswordValidator();
		TransactionComparator transactionComparator = new TransactionComparator();
		
		if(validator.validate(userPassword) == false)
		{
			response.getWriter().println("2");
			return;
		}
		
		
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			}
			
			catch(ClassNotFoundException e)
			{	
			}
			
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName + "?serverTimezone=UTC", userName, password))
			{
				Statement statement = connection.createStatement();
				ResultSet mainResultSet;
				String transactionNumCriteria, productNumCriteria, manufacturerCriteria, productNameCriteria, studentNameCriteria, studentNumberCriteria, emailCriteria, organizationCriteria, checkoutDateCriteria, checkindateCriteria;
				 transactionNumCriteria = productNumCriteria = manufacturerCriteria = productNameCriteria = studentNameCriteria = studentNumberCriteria = emailCriteria = organizationCriteria = checkoutDateCriteria =  checkindateCriteria = "";

				 if(keywordMatcher.isEmpty(TransactionNumber) && keywordMatcher.isEmpty(ProductNumber) && keywordMatcher.isEmpty(StudentNumber))
					 myQuery = "Select * from " + checkoutTable + ";";
				
				 else
					 {
				 if(!keywordMatcher.isEmpty(TransactionNumber))
					 
					transactionNumCriteria = " TransactionNumber = " + TransactionNumber + " AND"; 
				if(!keywordMatcher.isEmpty(ProductNumber))
					productNumCriteria = " QR_Code = " + ProductNumber + " AND";
				if(!keywordMatcher.isEmpty(StudentNumber))
						studentNumberCriteria = " StudentNumber = '" + StudentNumber + "' AND";
			
				myQuery = "SELECT * FROM " + checkoutTable + " WHERE " + transactionNumCriteria + productNumCriteria + studentNumberCriteria;
				int indexOfLastAnd = myQuery.lastIndexOf("AND");
				
				if(indexOfLastAnd != -1)
				{
					if(indexOfLastAnd >= myQuery.length() - 3)
						myQuery = myQuery.substring(0, indexOfLastAnd);
				}
				myQuery += ";";
					 }
				 
				mainResultSet = statement.executeQuery(myQuery);	
				
					
				
				while(mainResultSet.next())
				{
					if(filterStatus == 0 || (filterStatus == 1 && mainResultSet.getString(5) != null) || (filterStatus == 2 && mainResultSet.getString(5) == null))
					{
						
						finalTransactionNum = mainResultSet.getString(1);
						finalProductNum = mainResultSet.getString(2);
						finalStudentNum = mainResultSet.getString(3);
						Statement otherStatement = connection.createStatement();
						ResultSet tempResultSet = otherStatement.executeQuery("SELECT * FROM " + productTable + " WHERE QR_Code = " + finalProductNum + ";");
						while(tempResultSet.next())
						{
						finalManufacturer = tempResultSet.getString(2);
						finalProduct = tempResultSet.getString(3);
						}
						if(keywordMatcher.matchDataStrings(finalManufacturer, Manufacturer) && keywordMatcher.matchDataStrings(finalProduct, ProductName))
						{
							Statement thirdStatement = connection.createStatement();
							ResultSet thirdResultSet = thirdStatement.executeQuery("SELECT * FROM " + studentTable + " WHERE StudentNumber = '" + finalStudentNum + "';");
							
							if(thirdResultSet.next())
							{
								finalStudentName = thirdResultSet.getString(2);
							if(!keywordMatcher.isEmpty(StudentName) && !keywordMatcher.matchDataStrings(finalStudentName, StudentName))
								continue;
								finalOrganizationName = thirdResultSet.getString(3);
							if(!keywordMatcher.isEmpty(OrganizationName) && !keywordMatcher.matchDataStrings(finalOrganizationName, OrganizationName))
								continue;
								finalEmail = thirdResultSet.getString(4);
							if(!keywordMatcher.isEmpty(Email) && !keywordMatcher.matchDataStrings(finalEmail, Email))
								continue;	
							}
							finalCheckoutDate = mainResultSet.getString(4);
							finalCheckinDate = mainResultSet.getString(5);
							if(finalCheckinDate == null)
								finalCheckinDate = "N/A";
							if(finalStudentName == null)
								finalStudentName = "N/A";
							if(finalOrganizationName == null)
								finalOrganizationName = "N/A";
							if(finalEmail == null)
								finalEmail = "N/A";
								
							transactionList.add(new Transaction(finalTransactionNum, finalProductNum, finalManufacturer, finalProduct, finalStudentNum, finalStudentName, finalEmail, finalOrganizationName, finalCheckoutDate, finalCheckinDate));
							

						}
						
					}
					
					
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			char sortCriteria = sortOrder.charAt(0);
			
		if(Integer.parseInt(sortOrder.substring(1)) == 1)
		{
		if(sortCriteria == 'A')
			Collections.sort(transactionList,  transactionComparator.new SortByTransactionLow());
		else if(sortCriteria == 'B')
			Collections.sort(transactionList, transactionComparator.new SortByProductNumLow());
		else if(sortCriteria == 'C')
			Collections.sort(transactionList, transactionComparator.new SortByManufacturerNameLow());
		else if(sortCriteria == 'D')
			Collections.sort(transactionList, transactionComparator.new SortByProductNameLow());
		else if(sortCriteria == 'E')
			Collections.sort(transactionList, transactionComparator.new SortByStudentNameLow());
		else if(sortCriteria == 'F')
			Collections.sort(transactionList, transactionComparator.new SortByStudentNumberLow());
		else if(sortCriteria == 'G')
			Collections.sort(transactionList, transactionComparator.new SortByEmailLow());
		else if(sortCriteria == 'H')
			Collections.sort(transactionList, transactionComparator.new SortByOrganizationLow());
		else if(sortCriteria == 'I')
			Collections.sort(transactionList, transactionComparator.new SortByCheckoutDateLow());
		else
			Collections.sort(transactionList, transactionComparator.new SortByCheckinDateLow());
			
		}
		
		else
		{
			if(sortCriteria == 'A')
				Collections.sort(transactionList,  transactionComparator.new SortByTransactionHigh());
			else if(sortCriteria == 'B')
				Collections.sort(transactionList, transactionComparator.new SortByProductNumHigh());
			else if(sortCriteria == 'C')
				Collections.sort(transactionList, transactionComparator.new SortByManufacturerNameHigh());
			else if(sortCriteria == 'D')
				Collections.sort(transactionList, transactionComparator.new SortByProductNameHigh());
			else if(sortCriteria == 'E')
				Collections.sort(transactionList, transactionComparator.new SortByStudentNameHigh());
			else if(sortCriteria == 'F')
				Collections.sort(transactionList, transactionComparator.new SortByStudentNumberHigh());
			else if(sortCriteria == 'G')
				Collections.sort(transactionList, transactionComparator.new SortByEmailHigh());
			else if(sortCriteria == 'H')
				Collections.sort(transactionList, transactionComparator.new SortByOrganizationHigh());
			else if(sortCriteria == 'I')
				Collections.sort(transactionList, transactionComparator.new SortByCheckoutDateHigh());
			else
				Collections.sort(transactionList, transactionComparator.new SortByCheckinDateHigh());
		}
			
			
			
			Gson gson = new Gson();
			String returnString = gson.toJson(transactionList);
			response.getWriter().println(returnString);
		
	}

	
}
