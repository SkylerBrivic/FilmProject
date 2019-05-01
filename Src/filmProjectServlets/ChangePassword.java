
package filmProjectServlets;
import filmObjects.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
    public ChangePassword() {
        super();
        }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	//the old password entered in by the user is stored in the parameter oldPassword
	//the new password the user has requested is stored in the parameter newPassword
	//if the value of oldPassword matches the password stored in the database, then the password
	//in the database is updated to be the new password, and 0 is returned. Otherwise, the password
	//is not changed and 1 is returned.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		
		DatabaseInterface databaseInterface = new DatabaseInterface();
		if(databaseInterface.updatePassword(oldPassword, newPassword) == false)
		{
			response.getWriter().println("1");
			return;
		}
		
		
		response.getWriter().println("0");
}
}
