package model;

/**
 * Represents a user with basic personal information.
 * <p>
 * This class stores information about a user, including their first name, last name, and email ID.
 * It implements the {@link UserInterface} to provide a standard way of accessing the user's
 * information.
 * </p>
 */
public class User implements UserInterface {

  private String firstName;
  private String lastName;
  private String emailId;


  /**
   * Constructs a new User instance with the specified personal information.
   *
   * @param firstName The first name of the user.
   * @param lastName  The last name of the user.
   * @param emailId   The email ID of the user.
   */
  public User(String firstName, String lastName, String emailId) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
  }

  /**
   * Retrieves the first name of the user.
   *
   * @return The user's first name.
   */
  @Override
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Retrieves the last name of the user.
   *
   * @return The user's last name.
   */

  @Override
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Retrieves the email ID of the user.
   *
   * @return The user's email ID.
   */
  @Override
  public String getEmailId() {
    return this.emailId;
  }


}

