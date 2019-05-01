package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
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
	
		
		int filterStatus = Integer.parseInt(request.getParameter("filterStatus"));
		String TransactionNumber = request.getParameter("TransactionNumber");
		String ProductNumber = request.getParameter("ProductNumber");
		String Manufacturer = request.getParameter("manufacturer");
		String ProductName = request.getParameter("product");
		String StudentName = request.getParameter("studentName");
		String StudentNumber = request.getParameter("studentNumber");
		String OrganizationName = request.getParameter("organizationName");
		String Email = request.getParameter("email");
		
		
		
		String userPassword = request.getParameter("password");
		String sortOrder = request.getParameter("sortOrder");
		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		DatabaseInterface databaseInterface = new DatabaseInterface();
		TransactionComparator transactionComparator = new TransactionComparator();
		
		if(databaseInterface.validatePassword(userPassword) == false)
		{
			response.getWriter().println("2");
			return;
		}
		
		
			char sortCriteria = sortOrder.charAt(0);
			
			ArrayList<Transaction>  allTransactions = databaseInterface.selectTransaction(filterStatus);
			
			for(int i = 0; i < allTransactions.size(); ++i)
			{
				if(TransactionNumber != null && !TransactionNumber.equals("") && !TransactionNumber.equals(allTransactions.get(i).transactionNumber))
					continue;
				if(ProductNumber != null && !ProductNumber.equals("") && !ProductNumber.equals(allTransactions.get(i).QR_Code))
					continue;
				if(Manufacturer != null && !Manufacturer.equals("") && !keywordMatcher.matchDataStrings(allTransactions.get(i).manufacturerName, Manufacturer))
					continue;
				if(ProductName != null && !ProductName.equals("") && !keywordMatcher.matchDataStrings(allTransactions.get(i).productName, ProductName))
					continue;
				if(StudentName != null && !StudentName.equals("") && !StudentName.equalsIgnoreCase(allTransactions.get(i).studentName))
					continue;
				if(StudentNumber != null && !StudentNumber.equals("") && !StudentNumber.equalsIgnoreCase(allTransactions.get(i).studentNumber))
					continue;
				if(OrganizationName != null && !OrganizationName.equals("") && !keywordMatcher.matchDataStrings(allTransactions.get(i).organizationName, OrganizationName))
					continue;
				if(Email != null && !Email.equals("") && !Email.equals(allTransactions.get(i).studentEmail))
					continue;
				
				transactionList.add(allTransactions.get(i));
			}
			
			
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
