package com.dimitrit.web.demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.dimitrit.web.demo.Person;
import javax.sql.DataSource;

public class UserDbUtil {

	private DataSource dataSource;
	private String dbLocation = "jdbc:sqlite::memory:memdb1";//"jdbc:sqlite::memory:memdb1?cache=shared";
	public UserDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public void setupDb(){
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			// get a connection and create a memory db
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);
			statement = connection.createStatement();
			// Do some updates
			String sql = "CREATE TABLE user ( " +
					"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
					"first_name TEXT NOT NULL," +
					"last_name TEXT NOT NULL," +
					"age INTEGER NOT NULL);";
			int res = statement.executeUpdate(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contains("table user already exists")){
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
				// close JDBC objects
				close(connection, statement, null);
		}
	}

	public List<Person> getUsers() throws Exception {

		List<Person> users = new ArrayList<>();

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			// get a connection
			// myConn = dataSource.getConnection();
			Class.forName("org.sqlite.JDBC");
			//connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection = DriverManager.getConnection(dbLocation);
			// create the db

			// create sql statement
			String sql = "select * from user order by last_name";

			statement = connection.createStatement();

			// execute query
			result = statement.executeQuery(sql);

			// process result set
			while (result.next()) {

				// retrieve data from result set row
				int id = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				int age = result.getInt("age");

				// create new user object
				Person tempStudent = new Person(id, firstName, lastName, age);

				// add it to the list of users
				users.add(tempStudent);
			}

			return users;
		} finally {
			// close JDBC objects
			close(connection, statement, result);
		}
	}

	private void close(Connection connection, Statement statement, ResultSet result) {

		try {
			if (result != null) {
				result.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close(); // doesn't really close it ... just puts back in
								// connection pool
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addStudent(Person theUser) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// get db connection
			Class.forName("org.sqlite.JDBC");
			//connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection = DriverManager.getConnection(dbLocation);
			
			// create sql for insert
			String sql = "insert into user " + "(first_name, last_name, age) " + "values (?, ?, ?)";

			statement = connection.prepareStatement(sql);

			// set the param values for the user
			statement.setString(1, theUser.getFirstName());
			statement.setString(2, theUser.getLastName());
			statement.setInt(3, theUser.getAge());

			// execute sql insert
			statement.execute();
		} finally {
			// clean up JDBC objects
			close(connection, statement, null);
		}
	}

	public Person getUser(String theUserId) throws Exception {

		Person theUser = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int userId;

		try {
			// convert user id to int
			System.out.println(theUserId);
			userId = Integer.parseInt(theUserId);

			Class.forName("org.sqlite.JDBC");
			//connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection = DriverManager.getConnection(dbLocation);
			
			// create sql to get selected user
			String sql = "select * from user where id=?";

			// create prepared statement
			statement = connection.prepareStatement(sql);

			// set params
			statement.setInt(1, userId);

			// execute statement
			result = statement.executeQuery();

			// retrieve data from result set row
			if (result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				int age = result.getInt("age");

				// use the userId during construction
				theUser = new Person(userId, firstName, lastName, age);
			} else {
				throw new Exception("Could not find user id: " + userId);
			}

			return theUser;
		} finally {
			// clean up JDBC objects
			close(connection, statement, result);
		}
	}

	public void updateUsert(Person theUser) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// get db connection
			Class.forName("org.sqlite.JDBC");
			//connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection = DriverManager.getConnection(dbLocation);
			

			// create SQL update statement
			String sql = "update user " + "set first_name=?, last_name=?, age=? " + "where id=?";

			// prepare statement
			statement = connection.prepareStatement(sql);

			// set params
			statement.setString(1, theUser.getFirstName());
			statement.setString(2, theUser.getLastName());
			statement.setInt(3, theUser.getAge());
			statement.setInt(4, theUser.getId());

			// execute SQL statement
			statement.execute();
		} finally {
			// clean up JDBC objects
			close(connection, statement, null);
		}
	}

	public void deleteUser(String theUserId) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// convert user id to int
			int userId = Integer.parseInt(theUserId);

			// get connection to database
			Class.forName("org.sqlite.JDBC");
			//connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			connection = DriverManager.getConnection(dbLocation);
			
			// create sql to delete user
			String sql = "delete from user where id=?";

			// prepare statement
			statement = connection.prepareStatement(sql);

			// set params
			statement.setInt(1, userId);

			// execute sql statement
			statement.execute();
		} finally {
			// clean up JDBC code
			close(connection, statement, null);
		}
	}
}
