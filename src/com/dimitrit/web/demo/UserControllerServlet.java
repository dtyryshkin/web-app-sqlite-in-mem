package com.dimitrit.web.demo;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation, class UserControllerServlet
 */
@WebServlet("/UserControllerServlet")
public class UserControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private UserDbUtil userDbUtil;
	
	/**
	 * Implements init function for HttpServlet.
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("Start Servlet init ...");
		 
		try {
			// Create UserDbUtil object.
			userDbUtil = new UserDbUtil();
			// setup DB for the first time to run.
			userDbUtil.setupDb();
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
		System.out.println("Servlet init is complete!");
	}
	
	/**
	 * Implements doGet function.
	 * Executes a different function per received command.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// Read the "command" parameter
			String theCommand = request.getParameter("command");
			System.out.println(theCommand);
			// If the command is missing, then default to listing users
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			// Switch to the appropriate method, based on the command.
			switch (theCommand) {
			
			case "LIST":
				listUsers(request, response);
				break;
				
			case "ADD":
				addUser(request, response);
				break;
				
			case "LOAD":
				loadUser(request, response);
				break;
				
			case "UPDATE":
				updateUser(request, response);
				break;
			
			case "DELETE":
				deleteUser(request, response);
				break;
				
			default:
				listUsers(request, response);
			}
				
		}
		catch (Exception exc) {
			System.out.println("Error: " + exc.getMessage());
			throw new ServletException(exc);
		}
		
	}

	/**
	 * Displays the list of users from the DataBase on the Web Page.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void listUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Read users from the DataBase via userDbUtil object.
		List<DbUser> userList = userDbUtil.getUsers();

		// Add the list of users to the request
		request.setAttribute("USER_LIST", userList);
		
		System.out.println("Listing users.");
		// Send the request to JSP page.
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-users.jsp");
		dispatcher.forward(request, response);
		
	}
	
	/**
	 * Adds new user to the DataBase
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Read user information from the Web Form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String strAge = request.getParameter("age");
			
		if (firstName.length() > 0 && lastName.length() > 0 && strAge.length() > 0) {
			int age = Integer.parseInt(strAge);	
			// Create a new DbUser object
			DbUser addUser = new DbUser(firstName, lastName, age);

			// Add the new DbUser to the DataBase
			userDbUtil.addUser(addUser);
			System.out.println("Controller: Added User.");
		}
		// Go back to main page and display the updated user list
		listUsers(request, response);
		
	}
	
	/**
	 * Implements DELETE command, deletes the user from the DataBase
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// Read userId from form web data
		String dbUserId = request.getParameter("userId");
		
		// Delete user from the database
		userDbUtil.deleteUser(dbUserId);
		
		// Go back to main page and display the updated user list
		listUsers(request, response);
		
	}

	/**
	 * Updates user data in the DataBase
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void updateUser(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// Read user information from the Web Form data
		int id = Integer.parseInt(request.getParameter("userId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String strAge = request.getParameter("age");
		
		if (firstName.length() > 0 && lastName.length() > 0 && strAge.length() > 0) {
			int age = Integer.parseInt(strAge);

			// Create a new DbUser object
			DbUser dbUser = new DbUser(id, firstName, lastName, age);

			// Update the database
			userDbUtil.updateUser(dbUser);
		}
		// Go back to main page and display the updated user list
		listUsers(request, response);
		
	}

	/**
	 * Reads the user ID from the Web Form and loads the user from the DataBase by the user ID
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void loadUser(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// Read user ID from form the Web Form
		String dbUserId = request.getParameter("userId");
		System.out.println(dbUserId);
		
		// Get user from the DataBase by ID
		DbUser dbUser = userDbUtil.getUser(dbUserId);
		
		// Place the user in the request attribute, to update the web form.
		request.setAttribute("DB_USER", dbUser);
		
		// Send to "update-user-form.jsp" .jsp page. 
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-user-form.jsp");
		dispatcher.forward(request, response);		
	}
}













