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

	
	
	//the parameter "QR_Code" stores the string representing the number of the QR Code for the product that is about to be checked in.
	//a return value of 0 means the product was succesfully checked back in
	//a return value of 1 means that the QR Code entered in was invalid
	//a return value of 2 means that the product wasn't checked out to anyone.
	//a return value of 3 indicates an invalid password was used, and the product has not been checked back in.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	String QR_Code = request.getParameter("QR_Code");
	String password = request.getParameter("password");
	DatabaseInterface databaseInterface = new DatabaseInterface();
	KeywordMatcher keywordMatcher = new KeywordMatcher();
	if(databaseInterface.validatePassword(password) == false)
	{
		response.getWriter().println("3");
		return;
	}
	if(keywordMatcher.isEmpty(QR_Code))
	{
		response.getWriter().println("1");
		return;
	}
	
	ArrayList<Product> productExistence = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code);
	if(productExistence.size() == 0)
	{
		response.getWriter().println("1");
		return;
		
	}
	
	ArrayList<Product> productList = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code + " AND isAvailable = 0");
	if(productList.size() == 0)
	{
		response.getWriter().println("2");
		return;
	}
	
	databaseInterface.checkinProduct(QR_Code);
	response.getWriter().println("0");
	}
	

}
