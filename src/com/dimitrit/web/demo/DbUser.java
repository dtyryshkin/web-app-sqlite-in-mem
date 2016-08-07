package com.dimitrit.web.demo;

/**
 * DbUser class - defines DbUser class to contain User's data.
 * It has fields
 * 	id - database assigned user id.
 * 	firstName - First Name
 * 	lastName
 * 	age
 */
public class DbUser {

	private int id;
	private String firstName;
	private String lastName;
	private int age;

	/**
	 * DbUser constructor - Constructs DbUser object with three input parameters.
	 *
	 * @param firstName - First Name of the user.
	 * @param lastName - Last Name of the user.
     * @param age - input age of the user.
     */
	public DbUser(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}/**
	 * DbUser constructor - Constructs DbUser object with four input parameters.
	 *
	 * @param id - DataBase assigned ID of the user.
	 * @param firstName - First Name of the user.
	 * @param lastName - Last Name of the user.
	 * @param age - input age of the user.
	 */

	public DbUser(int id, String firstName, String lastName, int age) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	/**
	 * Returns the value of id property.
	 * @return  - returns the value of id property.
     */

	public int getId() {
		return id;
	}

	/**
	 * Sets the id property to the provided Id
	 * @param id - DataBase assigned ID of the user.
     */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the First Name of the user.
	 * @return - returns the First Name of the user
     */

	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the user.
	 * @param firstName - First Name of the user.
     */

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the Last Name of the user.
	 * @return - returns the Last Name of the user
     */

	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the Last Name of the user.
	 * @param lastName - String Last Name of the user.
     */

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the age of the user.
	 * @return - returns the age of the user.
     */

	public int getAge() {
		return age;
	}

	/**
	 * Sets the age of the user.
	 * @param age - the age of the user.
     */

	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Prints out the fields of the user object as a string.
	 * @return - Content of the object as a String.
     */
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
	}	
}
