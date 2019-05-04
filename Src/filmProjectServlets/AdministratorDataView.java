package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


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
	//the parameter filterStatus stores 0 if the program should display all transactions, 1 if only completed transactions should be shown (the product has been returned), and 2 if only ongoing
	//transactions (where the product has not yet been returned) should be shown.
	//the parameter sortOrder contains a String, whose first character represents what category to sort by (A for the transactionNumber, B for the second column in the adminTable, and so forth)
	//and the rest of the string after that contains either 1 (for forward sort) or -1 (for reverse sort)
	//the parameter TransactionNumber stores the number of the transaction
	//the parameter ProductNumber stores the Product ID Number that the user requested
	//the parameter manufacturer stores the name of the manufacturer that the user requested
	//the parameter product stores the name of the product that the user requested.
	//the parameter studentName contains the Student Name that the user requested
	//the parameter studentNumber stores the Student Number that the user requested
	//the parameter organizationName stores the Organization Name that the user requested
	//the parameter email stores the Email that the user requested
	//the parameter password stores the login password that the user entered in.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		
		int filterStatus = Integer.parseInt(request.getParameter("filterStatus"));
		String sortOrder = request.getParameter("sortOrder");
		String TransactionNumber = request.getParameter("TransactionNumber");
		String ProductNumber = request.getParameter("ProductNumber");
		String Manufacturer = request.getParameter("manufacturer");
		String ProductName = request.getParameter("product");
		String StudentName = request.getParameter("studentName");
		String StudentNumber = request.getParameter("studentNumber");
		String OrganizationName = request.getParameter("organizationName");
		String Email = request.getParameter("email");
		String userPassword = request.getParameter("password");
		
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		DatabaseInterface databaseInterface = new DatabaseInterface();
		TransactionComparator transactionComparator = new TransactionComparator();
		
		if(databaseInterface.validatePassword(userPassword) == false)
		{
			response.getWriter().println("2");
			return;
		}
		
		if(!keywordMatcher.isEmpty(TransactionNumber) && !keywordMatcher.isPositiveInteger(TransactionNumber))
			TransactionNumber = "";
		if(!keywordMatcher.isEmpty(ProductNumber) && !keywordMatcher.isPositiveInteger(ProductNumber))
			ProductNumber = "";
		
			char sortCriteria = sortOrder.charAt(0);
			
			ArrayList<Transaction>  allTransactions = databaseInterface.selectTransaction(filterStatus);
			
			//filtering out transactions that don't match the user's search criteria
			for(int i = 0; i < allTransactions.size(); ++i)
			{
				if(TransactionNumber != null && !keywordMatcher.isEmpty(TransactionNumber) && !TransactionNumber.equals(allTransactions.get(i).transactionNumber))
					continue;
				if(ProductNumber != null && !keywordMatcher.isEmpty(ProductNumber) && !ProductNumber.equals(allTransactions.get(i).Product_ID))
					continue;
				if(Manufacturer != null && !keywordMatcher.isEmpty(Manufacturer) && !keywordMatcher.matchDataStrings(allTransactions.get(i).manufacturerName, Manufacturer))
					continue;
				if(ProductName != null && !keywordMatcher.isEmpty(ProductName) && !keywordMatcher.matchDataStrings(allTransactions.get(i).productName, ProductName))
					continue;
				if(StudentName != null && !keywordMatcher.isEmpty(StudentName) && !keywordMatcher.matchDataStrings(allTransactions.get(i).studentName, StudentName))
					continue;
				if(StudentNumber != null && !keywordMatcher.isEmpty(StudentNumber) && !StudentNumber.equalsIgnoreCase(allTransactions.get(i).studentNumber))
					continue;
				if(OrganizationName != null && !keywordMatcher.isEmpty(OrganizationName) && !keywordMatcher.matchDataStrings(allTransactions.get(i).organizationName, OrganizationName))
					continue;
				if(Email != null && !keywordMatcher.isEmpty(Email) && !keywordMatcher.matchDataStrings(allTransactions.get(i).studentEmail, Email))
					continue;
				
				transactionList.add(allTransactions.get(i));
			}
			
		//handling the case where the list should be sorted in order, then using the appropriate comparator to sort by the right criteria	
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
		else if(sortCriteria == 'J')
			Collections.sort(transactionList, transactionComparator.new SortByActualCheckinDateLow());
		else
			Collections.sort(transactionList, transactionComparator.new SortByExpectedCheckinDateLow());
			
		}
		
		//handling the case where the list should be sorted in reverse order, then using the appropriate comparator to sort by the right criteria
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
			else if(sortCriteria == 'J')
				Collections.sort(transactionList, transactionComparator.new SortByActualCheckinDateHigh());
			else
				Collections.sort(transactionList, transactionComparator.new SortByExpectedCheckinDateHigh());
		}
			
			
			
			Gson gson = new Gson();
			String returnString = gson.toJson(transactionList);
			response.getWriter().println(returnString);
		
	}

	
}
