package model;

/**
 * This interface specifies the basic requirements for a user object, focusing on personal
 * information retrieval. It ensures that implementing classes provide methods to access a user's
 * first name, last name, and email ID.
 * </p>
 */
public interface UserInterface {

  /**
   * Retrieves the first name of the user.
   *
   * @return A string representing the user's first name.
   */
  public String getFirstName();

  /**
   * Retrieves the last name of the user.
   *
   * @return A string representing the user's last name.
   */
  public String getLastName();

  /**
   * Retrieves the email ID of the user.
   *
   * @return A string representing the user's email ID.
   */
  public String getEmailId();
}
