package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.*;
import java.util.*;


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
	//a return value of 4 means that studentName was not filled in
	//a return value of 5 means that the organizationName was not filled in
	//a return value of 6 means that an expected return date was not filled in
	//a return value of 7 means that the date was not in yyyy-MM-dd format
	//a return value of 8 means that the studentNumber was not filled in
	//the parameter QR_Code stores the products QR Code, the parameter StudentNumber stores the student's number, the parameter OrganizationName
	//stores the organization's name, the parameter Email stores the email of the person checking the product out, password stores the password for the
	//website, and ExpectedReturnDate stores the product's expected return date in yyyy-MM-dd format.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		String QR_Code = request.getParameter("QR_Code");
		String studentNumber = request.getParameter("StudentNumber");
		String studentName = request.getParameter("StudentName");
		String organizationName = request.getParameter("OrganizationName");
		String email = request.getParameter("Email");
		String userPassword = request.getParameter("password");
		String userDate = request.getParameter("ExpectedReturnDate");
		KeywordMatcher keywordMatcher = new KeywordMatcher();
		DatabaseInterface databaseInterface = new DatabaseInterface();
		if(keywordMatcher.isEmpty(QR_Code))
		{
			response.getWriter().println("1");
			return;
		}
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
		if(!productCheck.get(0).checkoutDate.equalsIgnoreCase("N/A"))
		{
			response.getWriter().println("2");
			return;
		}
		
		if(studentName == null || keywordMatcher.isEmpty(studentName))
		{
			response.getWriter().println("4");
			return;
		}
		if(organizationName == null || keywordMatcher.isEmpty(organizationName))
		{
			response.getWriter().println("5");
			return;
		}
		if(userDate == null || keywordMatcher.isEmpty(userDate))
		{
			response.getWriter().println("6");
			return;
		}
		else
		{
			try
			{
				@SuppressWarnings("unused")
				DateFormat firstDate = new SimpleDateFormat("yyyy-MM-dd");
				userDate = firstDate.format(firstDate.parse(userDate));
		
			}
			catch(ParseException e)
			{
				response.getWriter().println("7");
				return;
			}
		}
		
		if(studentNumber == null || keywordMatcher.isEmpty(studentNumber))
			{
			response.getWriter().println("8");
			return;
			}
		
		if(email == null || keywordMatcher.isEmpty(email))
			email = "N/A";
		
		CharSequence target = "\'";
		CharSequence replacement = "\'\'";
		QR_Code = QR_Code.replace(target, replacement);
		studentNumber = studentNumber.replace(target, replacement);
		studentName = studentName.replace(target, replacement);
		organizationName = organizationName.replace(target, replacement);
		email = email.replace(target, replacement);
		
		databaseInterface.checkoutProduct(QR_Code, studentNumber, studentName, organizationName, email, userDate);
		response.getWriter().println("0");
	}
}
