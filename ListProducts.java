import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


import java.sql.*;


@WebServlet("/ListProducts")
public class ListProducts extends HttpServlet {
	
	
	
	private static final long serialVersionUID = 1L;
       
    public ListProducts() {
        super();
    }


   
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);	
		}

	
	//the parameter manufacturer stores the requested manufacturer's name
	//the parameter product stores the requested product's name
	//the parameter filterOn is true if only available products are to be shown, and false otherwise.
	//the parameter sortOrder contains a String, whose first character represents what category to sort by (A for the Product ID, B for the second column, and so forth)
	//and the rest of the string after that contains either 1 (for forward sort) or -1 (for backwards sort)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String tempQR, tempProduct, tempManufacturer, tempOutDate, tempInDate;
		tempOutDate = tempInDate = "";
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		
		
		ArrayList<Product> productList = new ArrayList<Product>();
		ProductComparator productComparator = new ProductComparator();

		String serverName = "localhost:3306";
		String databaseName = "FilmProject";
		String userName = "root";
		String password = "PondFish";
		String productTable = "productList";
		String checkoutTable = "checkoutList";
		
		String manufactureSearchString = request.getParameter("manufacturer");
		String productSearchString = request.getParameter("product");
		boolean showAll = true;
		if(Boolean.parseBoolean(request.getParameter("filterOn")) == true)
			showAll = false;
			
		String sortOrder = request.getParameter("sortOrder");
		
		try {
		Class.forName("com.mysql.jdbc.Driver");
		}
		
		catch(ClassNotFoundException e)
		{	
		}
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName + "?serverTimezone=UTC", userName, password))
		{
			Statement statement = connection.createStatement();
			ResultSet mainResultSet = statement.executeQuery("SELECT * FROM " + productTable + ";");
			while(mainResultSet.next())
			{
				if(showAll == true || Integer.parseInt(mainResultSet.getString(4)) == 1)
				{
				tempManufacturer = mainResultSet.getString(2);
				tempProduct = mainResultSet.getString(3);
				if(keywordMatcher.matchDataStrings(tempManufacturer, manufactureSearchString) && keywordMatcher.matchDataStrings(tempProduct, productSearchString))
				{
					tempQR = mainResultSet.getString(1);
					if(Integer.parseInt(mainResultSet.getString(4)) == 0)
					{
						Statement otherStatement = connection.createStatement();
						ResultSet tempResultSet = otherStatement.executeQuery("SELECT * FROM " + checkoutTable + " WHERE QR_Code = " + tempQR + " AND checkinDate IS NULL;");
						
						while(tempResultSet.next())
						{
							tempInDate = tempResultSet.getString(5);
							tempOutDate = tempResultSet.getString(4);
						}
					}
					else
					{
					
						tempOutDate = "N/A";
					}
					
					if(tempInDate == null)
						tempInDate = "N/A";
					productList.add(new Product(tempQR, tempManufacturer, tempProduct, tempOutDate, tempInDate));
				}
				
				}
			}
			
			
		}
		
		
		
		catch(SQLException e)
		{
		}
		
		char sortCriteria = sortOrder.charAt(0);

		if(Integer.parseInt(sortOrder.substring(1)) == 1)
		{
		if(sortCriteria == 'A')
			Collections.sort(productList,  productComparator.new SortByProductNumLow());
		else if(sortCriteria == 'B')
			Collections.sort(productList, productComparator.new SortByManufacturerNameLow());
		else if(sortCriteria == 'C')
			Collections.sort(productList,  productComparator.new SortByProductNameLow());
		else if(sortCriteria == 'D')
			Collections.sort(productList,  productComparator.new SortByCheckoutDateLow());
		else
			Collections.sort(productList,  productComparator.new SortByCheckoutDateLow());
		}
		else
		{
			if(sortCriteria == 'A')
				Collections.sort(productList,  productComparator.new SortByProductNumHigh());
			else if(sortCriteria == 'B')
				Collections.sort(productList, productComparator.new SortByManufacturerNameHigh());
			else if(sortCriteria == 'C')
				Collections.sort(productList,  productComparator.new SortByProductNameHigh());
			else if(sortCriteria == 'D')
				Collections.sort(productList,  productComparator.new SortByCheckoutDateHigh());
			else
				Collections.sort(productList,  productComparator.new SortByCheckoutDateHigh());
			
			
		}
		
		Gson gson = new Gson();
		String returnString = gson.toJson(productList);
		response.getWriter().println(returnString);
		
		
	}

}

