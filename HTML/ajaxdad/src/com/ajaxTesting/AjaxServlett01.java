package com.ajaxTesting;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AjaxServlett01
 */
@WebServlet("/AjaxServlett01")
public class AjaxServlett01 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AjaxServlett01() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		if(request.getParameter("username").equals("mamoore4")) {
			
			response.getWriter().println(
						"<user>" + 
								"<userName>mamoore4</userName>" + 
								"<password>cse480</password>" + 
						"</user>"
					);
			
		} else if(request.getParameter("userName").equals("tim")) {
			
			response.getWriter().println(
					"<user>" + 
							"<userName>tim</userName>" + 
							"<password>cse480</password>" + 
					"</user>"
				);
			
		} else {
			
		}
	}

}
