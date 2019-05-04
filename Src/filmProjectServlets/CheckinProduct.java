package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;


@WebServlet("/CheckinProduct")
public class CheckinProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public CheckinProduct() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	
	//the parameter "Product_ID" stores the string representing the Product ID Number for the product that is about to be checked in.
	//the parameter "password" stores the password that the user typed in to login to the website.
	//a return value of 0 means the product was succesfully checked back in
	//a return value of 1 means that the Product ID Number entered in was invalid
	//a return value of 2 means that the product wasn't checked out to anyone.
	//a return value of 3 indicates an invalid password was used, and the product has not been checked back in.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	String Product_ID = request.getParameter("Product_ID");
	String password = request.getParameter("password");
	DatabaseInterface databaseInterface = new DatabaseInterface();
	KeywordMatcher keywordMatcher = new KeywordMatcher();
	if(databaseInterface.validatePassword(password) == false)
	{
		response.getWriter().println("3");
		return;
	}
	if(keywordMatcher.isEmpty(Product_ID) || !keywordMatcher.isPositiveInteger(Product_ID))
	{
		response.getWriter().println("1");
		return;
	}
	
	ArrayList<Product> productExistence = databaseInterface.selectProductByProductID(Product_ID);
	if(productExistence.size() == 0)
	{
		response.getWriter().println("1");
		return;
		
	}
	
	if(productExistence.get(0).checkoutDate.equalsIgnoreCase("N/A"))
	{
		response.getWriter().println("2");
		return;
	}
	
	databaseInterface.checkinProduct(Product_ID);
	response.getWriter().println("0");
	}
	

}
