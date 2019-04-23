package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;


@WebServlet("/CheckoutProduct")
public class CheckoutProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public CheckoutProduct() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	//a return value of 0 means the product was succesfully checked out
	//a return value of 1 means the specified QR Code was invalid
	//a return value of 2 means that the product is already checked out by somebody else
	//a return value of 3 means the user does not have permission to check this product out (due to an invalid password)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		String QR_Code = request.getParameter("QR_Code");
		String studentNumber = request.getParameter("StudentNumber");
		String studentName = request.getParameter("StudentName");
		String organizationName = request.getParameter("OrganizationName");
		String email = request.getParameter("Email");
		String userPassword = request.getParameter("password");
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		DatabaseInterface databaseInterface = new DatabaseInterface();
		if(databaseInterface.validatePassword(userPassword) == false)
		{
			response.getWriter().println("3");
			return;
		}
		ArrayList<Product> productCheck = databaseInterface.selectProduct(" WHERE QR_Code = " + QR_Code);
		if(productCheck.size() == 0)
		{
			response.getWriter().println("1");
			return;
		}
		if(!productCheck.get(0).checkoutDate.equals("N/A"))
		{
			response.getWriter().println("2");
			return;
		}
		if(studentNumber == null || keywordMatcher.isEmpty(studentNumber))
			studentNumber = "N/A";
		if(studentName == null || keywordMatcher.isEmpty(studentName))
			studentName = "N/A";
		if(organizationName == null || keywordMatcher.isEmpty(organizationName))
			organizationName = "N/A";
		if(email == null || keywordMatcher.isEmpty(email))
			email = "N/A";
		
		databaseInterface.checkoutProduct(QR_Code, studentNumber, studentName, organizationName, email);
		response.getWriter().println("0");
	}
}
