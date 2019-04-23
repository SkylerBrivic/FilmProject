package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;


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
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		
		String sortOrder = request.getParameter("sortOrder");
		String manufacturer = request.getParameter("manufacturer");
		String product = request.getParameter("product");
		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Product> allProducts = null;
		ArrayList<ProductAggregate> finalProductList = new ArrayList<ProductAggregate>();
		ProductComparator productComparator = new ProductComparator();
		ProductAggregateComparator productAggregateComparator = new ProductAggregateComparator();
		DatabaseInterface databaseInterface = new DatabaseInterface();
		allProducts = databaseInterface.selectProduct("");
		
		for(int i = 0; i < allProducts.size(); ++i)
		{
			if(manufacturer != null && !keywordMatcher.isEmpty(manufacturer) && !keywordMatcher.matchDataStrings(allProducts.get(i).ManufacturerName, manufacturer))
				continue;
			if(product != null && !keywordMatcher.isEmpty(product) && !keywordMatcher.matchDataStrings(allProducts.get(i).ProductName, product))
				continue;	
			productList.add(allProducts.get(i));	
		}

	Collections.sort(productList, productComparator.new SortByProductNameLow());
	Collections.sort(productList, productComparator.new SortByManufacturerNameLow());
	boolean firstEntryOfKind = true;
	for(int i = 0; i < productList.size(); ++i)
	{
		if(firstEntryOfKind)
		{
			if(productList.get(i).checkoutDate.equals("N/A"))
			finalProductList.add(new ProductAggregate(productList.get(i).ProductName, productList.get(i).ManufacturerName, 1, 1));
			else
			finalProductList.add(new ProductAggregate(productList.get(i).ProductName, productList.get(i).ManufacturerName, 0, 1));
			firstEntryOfKind = false;
		}
		else
		{
			if(finalProductList.get(finalProductList.size() - 1).Manufacturer.equalsIgnoreCase(productList.get(i).ManufacturerName) && finalProductList.get(finalProductList.size() - 1).ProductName.equalsIgnoreCase(productList.get(i).ProductName))
			{
						if(productList.get(i).checkoutDate.equalsIgnoreCase("N/A"))
						finalProductList.get(finalProductList.size()-1).numAvailable++;
					finalProductList.get(finalProductList.size() - 1).numInStock++;
			}
			else
			{
				if(productList.get(i).checkoutDate.equals("N/A"))
				finalProductList.add(new ProductAggregate(productList.get(i).ProductName, productList.get(i).ManufacturerName, 1, 1));
				else
					finalProductList.add(new ProductAggregate(productList.get(i).ProductName, productList.get(i).ManufacturerName, 0, 1));

				
			}
		}
		
		if(i < productList.size() - 1)
		{
			if(productList.get(i).ManufacturerName.equalsIgnoreCase(productList.get(i+1).ManufacturerName) && productList.get(i).ProductName.equalsIgnoreCase(productList.get(i+1).ManufacturerName))
				firstEntryOfKind = true;
		}
		
	}
		
		char sortCriteria = sortOrder.charAt(0);

		if(Integer.parseInt(sortOrder.substring(1)) == 1)
		{
		if(sortCriteria == 'A')
			Collections.sort(finalProductList,  productAggregateComparator.new SortByProductNameLow());
		else if(sortCriteria == 'B')
			Collections.sort(finalProductList, productAggregateComparator.new SortByManufacturerNameLow());
		
		}
		else
		{
			if(sortCriteria == 'A')
				Collections.sort(finalProductList,  productAggregateComparator.new SortByProductNameHigh());
			else if(sortCriteria == 'B')
				Collections.sort(finalProductList, productAggregateComparator.new SortByManufacturerNameHigh());
			
			
		}
		
		Gson gson = new Gson();
		String returnString = gson.toJson(finalProductList);
		response.getWriter().println(returnString);
		
		
	}

}
