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

	
	//the parameter QR_Code contains the QR Code of the product to be deleted
	//a return value of 0 means the product was succesfully deleted, and any entries associated with it in the checkoutList table have been deleted.
	//a return value of 1 means the QR Code was invalid or already deleted
	//a return value of 2 means that the user used an invalid password, and needs to log in again.
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	String QR_Code = request.getParameter("QR_Code");
	String password = request.getParameter("password");
	DatabaseInterface databaseInterface = new DatabaseInterface();
	if(databaseInterface.validatePassword(password) == false)
	{
		response.getWriter().println("2");
		return;
	}
	
	ArrayList<Product> myProduct = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code);
	if(myProduct.size() == 0)
	{
		response.getWriter().println("1");
		return;
	}
	databaseInterface.deleteProduct(QR_Code);
	response.getWriter().println("0");
		
	}
}

