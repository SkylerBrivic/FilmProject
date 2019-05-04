package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;


@WebServlet("/DeleteProduct")
public class DeleteProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public DeleteProduct() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	//the parameter Product_ID contains the Product ID Number of the product to be deleted
	//the parameter password contains the website's password.
	//a return value of 0 means the product was succesfully deleted, and any entries associated with it in the checkoutList table have been deleted.
	//a return value of 1 means the QR Code was invalid or already deleted
	//a return value of 2 means that the user used an invalid password, and needs to log in again.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	String Product_ID = request.getParameter("Product_ID");
	String password = request.getParameter("password");
	DatabaseInterface databaseInterface = new DatabaseInterface();
	KeywordMatcher keywordMatcher = new KeywordMatcher();
	if(databaseInterface.validatePassword(password) == false)
	{
		response.getWriter().println("2");
		return;
	}
	
	if(keywordMatcher.isEmpty(Product_ID))
	{
		response.getWriter().println("1");
		return;
	}
	
	ArrayList<Product> myProduct = databaseInterface.selectProductByProductID(Product_ID);
	if(myProduct.size() == 0)
	{
		response.getWriter().println("1");
		return;
	}
	databaseInterface.deleteProduct(Product_ID);
	response.getWriter().println("0");
	}
}
