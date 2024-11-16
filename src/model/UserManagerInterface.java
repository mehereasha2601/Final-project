package model;

import java.util.ArrayList;

/**
 * This interface specifies the operations necessary for managing users, including checking for the
 * existence of users, counting the total number of users, retrieving specific user data, and saving
 * new user information.
 * </p>
 */
public interface UserManagerInterface {

  /**
   * Checks if a user with the specified email ID already exists.
   *
   * @param emailId The email ID of the user to check.
   * @return true if the user exists, false otherwise.
   */
  public boolean isExistingUser(String emailId);

  /**
   * Retrieves the total number of users managed by the implementation.
   *
   * @return The total count of users.
   */
  public int getNumberOfUsers();

  /**
   * Retrieves the existing user data for a specific email ID.
   * <p>
   * This method is intended to return a list containing the user's first name, last name, and email
   * ID, if the user exists.
   * </p>
   *
   * @param emailId The email ID of the user whose data is being requested.
   * @return An ArrayList containing the user's first name, last name, and email ID.
   */
  public ArrayList<String> getExistingUserData(String emailId);

  /**
   * Saves the data of a new user or updates an existing user's data.
   * <p>
   * This method takes the first name, last name, and email ID of a user and saves it to the
   * implementation's data store. If a user with the given email ID already exists, their data
   * should be updated; otherwise, a new user entry should be created.
   * </p>
   *
   * @param firstName The first name of the user.
   * @param lastName  The last name of the user.
   * @param emailID   The email ID of the user.
   */
  public void saveUserData(String firstName, String lastName, String emailID);
}
