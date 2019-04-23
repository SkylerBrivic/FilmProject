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
	//if manufacturer or product name is null or consists only of white space (if isEmpty returns true) then that particulular attribute is not updated for the product
	
	//a return value of 0 means the product's information was successfully updated
	//a return value of 1 means that the product's QR Code was invalid.
	//a return value of 2 means that the user entered an invalid password, and needs to login again.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
	System.out.println("In UpdateProduct servlet");
	String QR_Code = request.getParameter("QR_Code");
	String manufacturer = request.getParameter("manufacturer");
	String product = request.getParameter("product");
	String userPassword = request.getParameter("password");
	DatabaseInterface databaseInterface = new DatabaseInterface();
	if(databaseInterface.validatePassword(userPassword) == false)
	{
		response.getWriter().println("2");
		return;
	}
	ArrayList<Product> productName = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code);
	if(productName.size() == 0)
	{
		response.getWriter().println("1");
		return;
	}
	CharSequence target = (String)"\'";
	CharSequence replacement = (String)"\'\'";
	try
	{
		manufacturer = manufacturer.replace(target, replacement);
	}
	catch(NullPointerException e)
	{
	e.printStackTrace();
	}
	product = product.replace(target, replacement);
	databaseInterface.updateProduct(QR_Code, manufacturer, product);
	response.getWriter().println("0");
}
}
