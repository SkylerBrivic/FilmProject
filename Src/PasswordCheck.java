import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@WebServlet("/PasswordCheck")
public class PasswordCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	
    public PasswordCheck() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	//the parameter password stores the password the user entered in.
	//a return value of 1 means the password for the site was correct.
	//a return value of 0 means the password for the site was wrong.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String userPassword = request.getParameter("password");
		PasswordValidator validator = new PasswordValidator();
		if(validator.validate(userPassword) == true)
			response.getWriter().println("1");
		else
			response.getWriter().println("0");
		
		
	}

}
