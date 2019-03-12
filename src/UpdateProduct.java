import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.sql.*;


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
	String QR_Code, manufacturer, product;
	String serverName = "localhost:3306";
	String databaseName = "FilmProject";
	String userName = "root";
	String password = "PondFish";
	String productTable = "productList";
	Boolean isValidCode = false;
	
	QR_Code = request.getParameter("QR_Code");
	manufacturer = request.getParameter("manufacturer");
	product = request.getParameter("product");
	
	String userPassword = request.getParameter("password");
	
	KeywordMatcher keywordMatcher = new KeywordMatcher();
	PasswordValidator validator = new PasswordValidator();
	
	if(validator.validate(userPassword) == false)
	{
		response.getWriter().println("2");
		return;
	}
	
	
	try{
		Class.forName("com.mysql.jdbc.Driver");
	}
	catch(ClassNotFoundException e)
	{	
	}
	
	try(Connection connection = DriverManager.getConnection("jdbc:mysql://" + serverName + "/" + databaseName, userName, password))
	{
		Statement statement = connection.createStatement();
		String myQuery = "Select * from " + productTable + " where QR_Code = " + QR_Code + ";" ;
		ResultSet resultSet = statement.executeQuery(myQuery);
		
		while(resultSet.next())
		{
			isValidCode = true;
		}
		
		if(!isValidCode)
		{
			response.getWriter().println("1");
			return;
		}
		
		if(!keywordMatcher.isEmpty(manufacturer))
		{
			myQuery = "UPDATE " + productTable + " SET manufacturer = '" + manufacturer + "' WHERE QR_Code = " + QR_Code + ";";
			statement.executeUpdate(myQuery);
		}
		if(!keywordMatcher.isEmpty(product))
		{
			myQuery = "UPDATE " + productTable + " SET productName = '" + product + "' WHERE QR_Code = " + QR_Code + ";";
			statement.executeUpdate(myQuery);
		}
		
		response.getWriter().println("0");
		}	
		
	catch(SQLException e)
	{}

}
}
