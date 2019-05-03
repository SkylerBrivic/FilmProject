package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;


@WebServlet("/UpdateProduct")
public class UpdateProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public UpdateProduct() {
        super();
        }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);	
		
	}
	
	//the parameter QR_Code contains the QR Code of the product to be updated
	//the parameter manufacturer contains the new manufacturer name of the product
	//the parameter product contains the new name for the product
	//if manufacturer or product name is null or consists only of white space (if isEmpty returns true) then that particular attribute is not updated for the product
	
	//a return value of 0 means the product's information was successfully updated
	//a return value of 1 means that the product's QR Code was invalid.
	//a return value of 2 means that the user entered in an invalid password, and needs to login again.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
	String QR_Code = request.getParameter("QR_Code");
	String manufacturer = request.getParameter("manufacturer");
	String product = request.getParameter("product");
	String userPassword = request.getParameter("password");
	String conditionList = "";
	DatabaseInterface databaseInterface = new DatabaseInterface();
	KeywordMatcher keywordMatcher = new KeywordMatcher();
	if(databaseInterface.validatePassword(userPassword) == false)
	{
		response.getWriter().println("2");
		return;
	}
	
	if(keywordMatcher.isEmpty(QR_Code))
	{
		response.getWriter().println("1");
		return;
	}
	
	ArrayList<Product> productName = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code);
	if(productName.size() == 0)
	{
		response.getWriter().println("1");
		return;
	}
	//quoting any single quotes that may be in the product name or manufacturer name so that they can be inserted into the database
	CharSequence target = (String)"\'";
	CharSequence replacement = (String)"\'\'";
	
	manufacturer = manufacturer.replace(target, replacement);
	product = product.replace(target, replacement);
	if((manufacturer == null || keywordMatcher.isEmpty(manufacturer)) && (product == null  || keywordMatcher.isEmpty(product)))
	{
		response.getWriter().println("0");
		return;
	}
	else if(manufacturer == null || keywordMatcher.isEmpty(manufacturer))
		conditionList = " productName = '" + product + "' ";
	else if(product == null || keywordMatcher.isEmpty(product))
		conditionList = " manufacturer = '" + manufacturer + "' ";
	else
		conditionList = " productName = '" + product + "', manufacturer = '" + manufacturer + "' ";
	
	databaseInterface.updateProduct(QR_Code, conditionList);
	response.getWriter().println("0");
}
}
