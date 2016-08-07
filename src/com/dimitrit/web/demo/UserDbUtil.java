package com.dimitrit.web.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDbUtil - Abstracts DataBase operation.
 */

public class UserDbUtil {
	// SQLite DB, in memory mode, named inMemoryDb.
	final private String dbLocation = "jdbc:sqlite::memory:inMemoryDb";

	/**
	 * UserDbUtil constructor
	 */
	public UserDbUtil() {
	}

	/**
	 * Sets up connection to the DB and creates the "user" table.
	 */
	public void setupDb() {
		Connection connection = null;
		Statement statement = null;

		try {
			// Get a connection and create SQLite DB
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);
			statement = connection.createStatement();
			// Create the table User and setup DB schema.
			String sql = "CREATE TABLE user ( " + "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
					+ "first_name TEXT NOT NULL," + "last_name TEXT NOT NULL," + "age INTEGER NOT NULL);";
			statement.executeUpdate(sql);

		} catch (SQLException e) {
			// Handle exception if the table already exists.
			if (!e.getMessage().contains("table user already exists")) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close JDBC objects
			close(connection, statement, null);
		}
	}

	/**
	 * Retrieves all the users from the DB.
	 * 
	 * @return List of users sorted by last name.
	 * @throws Exception
	 */
	public List<DbUser> getUsers() throws Exception {

		List<DbUser> users = new ArrayList<>();
		// JDBC objects
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;

		try {
			// Get a connection
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);

			// Create an SQL statement
			String sql = "select * from user order by last_name";
			statement = connection.createStatement();

			// Execute SQL query
			result = statement.executeQuery(sql);

			// Process the result returned from the query
			while (result.next()) {

				// Retrieve data from result set row
				int id = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				int age = result.getInt("age");

				// Create new DBUser object
				DbUser tempUser = new DbUser(id, firstName, lastName, age);

				// Add it to the list of users
				users.add(tempUser);
			}

			// Return the list of users
			return users;
		} finally {
			// Close JDBC objects
			close(connection, statement, result);
		}
	}

	/**
	 * Auxiliary function to close JDBC objects.
	 * 
	 * @param connection
	 * @param statement
	 * @param result
	 */
	private void close(Connection connection, Statement statement, ResultSet result) {

		try {
			if (result != null) {
				result.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Adds the DBUser object to the DataBase.
	 * 
	 * @param addedUser
	 * @throws Exception
	 */
	public void addUser(DbUser addedUser) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// Get DB connection
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);
			System.out.println("DB: About to add user.");
			// Create SQL statement for insert
			String sql = "insert into user " + "(first_name, last_name, age) " + "values (?, ?, ?)";
			statement = connection.prepareStatement(sql);
			System.out.println("DB: Insering User");
			// Set the parameter values for the DBUser
			if (addedUser.getFirstName() != null && addedUser.getLastName() != null && addedUser.getAge() >= 0) {
				statement.setString(1, addedUser.getFirstName());
				statement.setString(2, addedUser.getLastName());
				statement.setInt(3, addedUser.getAge());

				// Execute SQL insert
				statement.execute();
				System.out.println("DB: Added User.");
			}
		} finally {
			// Clean up JDBC objects
			close(connection, statement, null);
		}
	}

	/**
	 * Reads user from the DataBase by user ID.
	 * 
	 * @param inUserId
	 * @return DBUser object with values from the DataBase for the provided user
	 *         ID.
	 * @throws Exception
	 */
	public DbUser getUser(String inUserId) throws Exception {

		DbUser dbUser = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		int userId;

		try {
			// Convert user ID to int
			userId = Integer.parseInt(inUserId);

			// Create DB connection
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);

			// Create SQL statement to get the user by userId.
			String sql = "select * from user where id=?";

			// Create prepared statement
			statement = connection.prepareStatement(sql);

			// Set SQL parameters
			statement.setInt(1, userId);

			// Execute SQL statement
			result = statement.executeQuery();

			// Retrieve data from result set row
			if (result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				int age = result.getInt("age");

				// use the userId during construction
				dbUser = new DbUser(userId, firstName, lastName, age);
			} else {
				throw new Exception("Could not find user id: " + userId);
			}

			return dbUser;
		} finally {
			// clean up JDBC objects
			close(connection, statement, result);
		}
	}

	/**
	 * Updates user in the DataBase with new DbUser object.
	 * 
	 * @param dbUser
	 * @throws Exception
	 */
	public void updateUser(DbUser dbUser) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// Get DataBase connection
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);

			// Create SQL update statement
			String sql = "update user " + "set first_name=?, last_name=?, age=? " + "where id=?";

			// Prepare SQL statement
			statement = connection.prepareStatement(sql);

			// Set SQL parameters
			statement.setString(1, dbUser.getFirstName());
			statement.setString(2, dbUser.getLastName());
			statement.setInt(3, dbUser.getAge());
			statement.setInt(4, dbUser.getId());

			// Execute SQL statement
			statement.execute();
		} finally {
			// Clean up JDBC objects
			close(connection, statement, null);
		}
	}

	/**
	 * Deletes from the DataBase by user ID.
	 * 
	 * @param dbUserId
	 * @throws Exception
	 */

	public void deleteUser(String dbUserId) throws Exception {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			// Convert userID from String to int
			int userId = Integer.parseInt(dbUserId);

			// Get connection to DataBase
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(dbLocation);

			// Create SQL statement to delete user
			String sql = "delete from user where id=?";

			statement = connection.prepareStatement(sql);

			// Set ID parameter
			statement.setInt(1, userId);

			// Execute SQL statement
			statement.execute();
		} finally {
			// Clean up JDBC code
			close(connection, statement, null);
		}
	}
}
