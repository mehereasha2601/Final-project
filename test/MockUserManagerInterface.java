import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.User;
import model.UserManagerInterface;

/**
 * Mock implementation of UserManager to test controller inputs and outputs.
 */
public class MockUserManagerInterface implements UserManagerInterface {

  private ArrayList<User> users;

  public StringBuilder log;

  /**
   * This is a public constructor that initialises the users object and log.
   *
   * @param log is a string builder that keeps log of all methods called.
   */
  public MockUserManagerInterface(StringBuilder log) {
    this.log = log;
    users = this.getAllUsers();

  }


  protected ArrayList<User> getAllUsers() {
    log.append(" getAllUsers method called.");
    ArrayList<User> userData = new ArrayList<User>();
    String line;
    String filePath = "res/Data/UserData/TestUserData.csv";
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          String[] values = line.split(",");
          User user = new User(values[1], values[2], values[0]);
          userData.add(user);
        }
      }
    } catch (IOException e) {
      System.out.println("An error occurred while reading the CSV file.");
      e.printStackTrace();

    } catch (Exception e) {
      System.out.println("An exception has occured in getAllUsers" + e.getMessage());
    }
    return userData;
  }

  /**
   * Simulates checking if a user exists based on the provided email ID.
   *
   * @param emailId The email ID of the user to check.
   * @return true if the user exists, false otherwise.
   */
  @Override
  public boolean isExistingUser(String emailId) {
    log.append(" isExistingUser method called with input " + emailId + ".");
    return emailId.equalsIgnoreCase("mehereasha2601@gmail.com") ? true : false;
  }

  /**
   * Returns a mock number of users in the system.
   *
   * @return The number of users.
   */
  @Override
  public int getNumberOfUsers() {
    return 10;
  }

  /**
   * Simulates retrieving existing user data based on an email ID.
   *
   * @param emailId The email ID of the user whose data is to be retrieved.
   * @return A list containing user data.
   */
  @Override
  public ArrayList<String> getExistingUserData(String emailId) {
    log.append(" getExistingUserData method called with input " + emailId + ".");
    ArrayList<String> existingUserData = new ArrayList<String>();
    for (User user : users) {
      if (user.getEmailId().equalsIgnoreCase(emailId)) {
        existingUserData.add(user.getFirstName());
        existingUserData.add(user.getLastName());
        existingUserData.add(user.getEmailId());
      }
    }
    return existingUserData;
  }


  /**
   * Simulates saving user data to a persistent storage.
   *
   * @param firstName The first name of the user.
   * @param lastName  The last name of the user.
   * @param emailID   The email ID of the user.
   */
  @Override
  public void saveUserData(String firstName, String lastName, String emailID) {
    log.append(
        " saveUserData method called with input " + firstName + " , " + lastName + " , "
            + emailID
            + ".");
    User user = new User(firstName, lastName, emailID);
    String filePath = "Data/UserData/UserData.csv";
    File csvFile = new File(filePath);
    boolean appendData = csvFile.exists() && csvFile.length() > 0;

    try (FileWriter writer = new FileWriter(filePath, true)) {
      if (!appendData) {
        writer.append("EmailID,FirstName,LastName\n");
      }
      writer.append(
          user.getEmailId() + "," + user.getFirstName() + "," + user.getLastName() + "\n");
      System.out.println("Data was appended to the CSV file successfully.");
    } catch (IOException e) {
      System.out.println("An error occurred while appending to the CSV file.");
      e.printStackTrace();
    }
    this.users.add(new User(firstName, lastName, emailID));
  }
}

