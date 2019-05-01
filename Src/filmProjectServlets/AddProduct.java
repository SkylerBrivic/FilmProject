package filmProjectServlets;
import java.io.IOException;
import filmObjects.*;

import javax.servlet.ServletException;
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
	//a value of "1" for isAvailable means the product is available for rental, while a value of "0" means the product is checked out
	//if the user entered in a valid password earlier to log in, then the function returns 0. Otherwise, it returns 1 and the user's
	//request is denied
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		
		String manufacturer = request.getParameter("manufacturer");
		String product = request.getParameter("productName");
		String userPassword = request.getParameter("password");
		
		DatabaseInterface databaseInterface = new DatabaseInterface();
		
		if(databaseInterface.validatePassword(userPassword) == false)
		{
			response.getWriter().println("1");
			return;
		}
		
		CharSequence target = "\'";
		CharSequence replacement = "\'\'";
		manufacturer = manufacturer.replace(target, replacement);
		product = product.replace(target, replacement);
		databaseInterface.addProduct(manufacturer,  product);
		response.getWriter().println("0");
		return;
		}		
	}


