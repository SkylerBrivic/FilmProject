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
		
		int filterStatus = Integer.parseInt(request.getParameter("filterStatus"));
		
		String TransactionNumber = request.getParameter("TransactionNumber");
		String ProductNumber = request.getParameter("ProductNumber");
		String StudentNumber = request.getParameter("StudentNumber");
		String manufacturerSearchString = request.getParameter("manufacturer");
		String productSearchString = request.getParameter("product");
		String userPassword = request.getParameter("password");
		String sortOrder = request.getParameter("sortOrder");
		ArrayList<Product> productList = new ArrayList<Product>();
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		PasswordValidator validator = new PasswordValidator();
		ProductComparator productComparator = new ProductComparator();
		String tempManufacturer = "";
		String tempProduct = "";
		
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
			
			try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, userName, password))
			{
				Statement statement = connection.createStatement();
				ResultSet mainResultSet;
				
				if(keywordMatcher.isEmpty(TransactionNumber) && keywordMatcher.isEmpty(ProductNumber) && keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + ";");
				
				else if(keywordMatcher.isEmpty(TransactionNumber) && keywordMatcher.isEmpty(ProductNumber) && !keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE StudentNumber = '" + StudentNumber + "';");
				
				else if(keywordMatcher.isEmpty(TransactionNumber) && !keywordMatcher.isEmpty(ProductNumber) && keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE QR_Code = " + ProductNumber + ";");
	
				else if(keywordMatcher.isEmpty(TransactionNumber) && !keywordMatcher.isEmpty(ProductNumber) && !keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE QR_Code = " + ProductNumber + " AND StudentNumber = '" + StudentNumber + "';");

				else if(!keywordMatcher.isEmpty(TransactionNumber) && keywordMatcher.isEmpty(ProductNumber) && keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE TransactionNum = " + TransactionNumber + ";");
				
				else if(!keywordMatcher.isEmpty(TransactionNumber) && keywordMatcher.isEmpty(ProductNumber) && !keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE TransactionNum = " + TransactionNumber + " AND StudentNumber = '" + StudentNumber + "';");
				
				else if(!keywordMatcher.isEmpty(TransactionNumber) && !keywordMatcher.isEmpty(ProductNumber) && keywordMatcher.isEmpty(StudentNumber))
					mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE TransactionNum = " + TransactionNumber + " AND QR_Code = " + ProductNumber + ";");

				else
				 mainResultSet = statement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE TransactionNum = " + TransactionNumber + " AND QR_Code = " + ProductNumber + " AND StudentNumber = '" + StudentNumber + "';");
				
				
				while(mainResultSet.next())
				{
					if(filterStatus == 0 || (filterStatus == 1 && mainResultSet.getString(5) != null) || (filterStatus == 2 && mainResultSet.getString(5) == null))
					{
						Statement otherStatement = connection.createStatement();
						ResultSet tempResultSet = otherStatement.executeQuery("SELECT * FROM " + productTable + " WHERE QR_Code = " + mainResultSet.getString(2) + ";");
						while(tempResultSet.next())
						{
						tempManufacturer = tempResultSet.getString(2);
						tempProduct = tempResultSet.getString(3);
						}
						if(keywordMatcher.matchDataStrings(tempManufacturer, manufacturerSearchString) && keywordMatcher.matchDataStrings(tempProduct, productSearchString))
						{
							
							if(mainResultSet.getString(5) == null)
							productList.add(new Product(mainResultSet.getString(2), mainResultSet.getString(1), tempProduct, tempManufacturer, mainResultSet.getString(3), mainResultSet.getString(4), "N/A"));
							else
								productList.add(new Product(mainResultSet.getString(2), mainResultSet.getString(1), tempProduct, tempManufacturer, mainResultSet.getString(3), mainResultSet.getString(4), mainResultSet.getString(5)));

						}
						
					}
					
					
				}
			}
			catch(SQLException e)
			{}
			
			char sortCriteria = sortOrder.charAt(0);
			
		if(Integer.parseInt(sortOrder.substring(1)) == 1)
		{
		if(sortCriteria == 'A')
			Collections.sort(productList,  productComparator.new SortByTransactionLow());
		else if(sortCriteria == 'B')
			Collections.sort(productList, productComparator.new SortByProductNumLow());
		else if(sortCriteria == 'C')
			Collections.sort(productList, productComparator.new SortByManufacturerNameLow());
		else if(sortCriteria == 'D')
			Collections.sort(productList, productComparator.new SortByProductNameLow());
		else if(sortCriteria == 'E')
			Collections.sort(productList, productComparator.new SortByStudentNumberLow());
		else if(sortCriteria == 'F')
			Collections.sort(productList, productComparator.new SortByCheckoutDateLow());
		else
			Collections.sort(productList, productComparator.new SortByCheckinDateLow());
			
		}
		
		else
		{
			if(sortCriteria == 'A')
				Collections.sort(productList,  productComparator.new SortByTransactionHigh());
			else if(sortCriteria == 'B')
				Collections.sort(productList, productComparator.new SortByProductNumHigh());
			else if(sortCriteria == 'C')
				Collections.sort(productList, productComparator.new SortByManufacturerNameHigh());
			else if(sortCriteria == 'D')
				Collections.sort(productList, productComparator.new SortByProductNameHigh());
			else if(sortCriteria == 'E')
				Collections.sort(productList, productComparator.new SortByStudentNumberHigh());
			else if(sortCriteria == 'F')
				Collections.sort(productList, productComparator.new SortByCheckoutDateHigh());
			else
				Collections.sort(productList, productComparator.new SortByCheckinDateHigh());
		}
			
			
			
			Gson gson = new Gson();
			String returnString = gson.toJson(productList);
			response.getWriter().println(returnString);
		
	}

	
}
