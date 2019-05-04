package filmProjectServlets;
import java.io.IOException;
import filmObjects.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






@WebServlet("/AddProduct")
public class AddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public AddProduct() 
    {
        super();   
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	
	
	//the parameter manufacturer stores the manufacturer's name
	//the parameter productName stores the product's name
	//the parameter quantity stores the number of items to insert into the database
	//the parameter password stores the website's password
	//if the user entered in a valid password earlier to log in and a valid quantity (along with a product name and a manufacturer name), then the function returns 0, and the product is added to the database. 
	//Otherwise, if the password was wrong, it returns 1 and the user's request is denied. If the user entered in an invalid quantity (ie. <= 0 or not an int) then 2 is returned and the product is not added to the database.
	//if the user did not enter in a product name or manufacturer name, then 3 is returned and the product is not added to the database.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		
		String manufacturer = request.getParameter("manufacturer");
		String product = request.getParameter("productName");
		String quantity = request.getParameter("quantity");
		String userPassword = request.getParameter("password");
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		int numProducts = 0;
		try
		{
			numProducts = Integer.parseInt(quantity);
			if(numProducts <= 0)
			{
				response.getWriter().println("2");
				return;
			}
		}
		
		catch(NumberFormatException e)
		{
			response.getWriter().println("2");
			return;
		}
		
		if(keywordMatcher.isEmpty(manufacturer) || keywordMatcher.isEmpty(product))
		{
			response.getWriter().println("3");
			return;
		}
		DatabaseInterface databaseInterface = new DatabaseInterface();
		
		if(databaseInterface.validatePassword(userPassword) == false)
		{
			response.getWriter().println("1");
			return;
		}
		
		//quoting single quotes so that they can appear in the product or manufacturer name
		CharSequence target = "\'";
		CharSequence replacement = "\'\'";
		manufacturer = manufacturer.replace(target, replacement);
		product = product.replace(target, replacement);
		databaseInterface.addProduct(manufacturer,  product, numProducts);
		response.getWriter().println("0");
		return;
		}		
	}

