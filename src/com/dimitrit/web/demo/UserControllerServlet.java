package com.dimitrit.web.demo;

import java.io.IOException;
import java.util.List;
import com.dimitrit.web.demo.Person;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class UserControllerServlet
 */
@WebServlet("/UserControllerServlet")
public class UserControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserDbUtil userDbUtil;
	
	//@Resource(name="jdbc/sqlite")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("Start init ...");
		// create our user db util ... and pass in the conn pool / datasource
		try {
			userDbUtil = new UserDbUtil(dataSource);
			// setup DB for the first time to run.
			userDbUtil.setupDb();
			System.out.println("The db is setup!");
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing users
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			// route to the appropriate method
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
			throw new ServletException(exc);
		}
		
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read user id from form data
		String theUserId = request.getParameter("userId");
		
		// delete user from database
		userDbUtil.deleteUser(theUserId);
		
		// send them back to "list users" page
		listUsers(request, response);
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read user info from form data
		int id = Integer.parseInt(request.getParameter("userId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		int age = Integer.parseInt(request.getParameter("age"));
		
		// create a new user object
		Person user = new Person(id, firstName, lastName, age);
		
		// perform update on database
		userDbUtil.updateUsert(user);
		
		// send them back to the "list users" page
		listUsers(request, response);
		
	}

	private void loadUser(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// read user id from form data
		String theUserId = request.getParameter("userId");
		System.out.println(theUserId);
		
		// get user from database (db util)
		Person theStudent = userDbUtil.getUser(theUserId);
		
		// place user in the request attribute
		request.setAttribute("THE_USER", theStudent);
		
		// send to jsp page: update-user-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-user-form.jsp");
		dispatcher.forward(request, response);		
	}

	private void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read user info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		int age = Integer.parseInt(request.getParameter("age"));		
		
		// create a new user object
		Person theUser = new Person(firstName, lastName, age);
		
		// add the user to the database
		userDbUtil.addStudent(theUser);
				
		// send back to main page (the user list)
		listUsers(request, response);
	}

	private void listUsers(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		// get users from db util
		List<Person> users = userDbUtil.getUsers();
		
		// add users to the request
		request.setAttribute("USER_LIST", users);
				
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-users.jsp");
		dispatcher.forward(request, response);
	}

}













