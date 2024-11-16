package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages user data, including creating, saving, and retrieving user information.
 * <p>
 * This class is responsible for interacting with the user data stored in a CSV file, allowing for
 * the creation of new user entries, retrieval of existing user data, and checking for the existence
 * of users based on their email ID.
 * </p>
 */
public class UserManager implements UserManagerInterface {

  private ArrayList<User> users;

  protected User getUser(String emailId) {
    ArrayList<String> userData = getExistingUserData(emailId);
    User user = new User(userData.get(0), userData.get(1), userData.get(2));
    return user;
  }

  /**
   * Constructs a UserManager, loading all users from storage upon initialization.
   */
  public UserManager() {
    users = this.getAllUsers();
  }

  private ArrayList<User> getAllUsers() {
    ArrayList<User> userData = new ArrayList<User>();
    if (getNumberOfUsers() != 0) {
      String line;
      String filePath = "res/Data/UserData/UserData.csv";
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
        System.out.println("An exception has occurred in getAllUsers" + e.getMessage());
      }
    }
    return userData;
  }

  /**
   * Retrieves the total number of users stored.
   *
   * @return The count of users.
   */
  @Override
  public int getNumberOfUsers() {
    String filePath = "res/Data/UserData/UserData.csv";
    File file = new File(filePath);
    int userCount = 0;
    if (!file.exists()) {
      return userCount;
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
          userCount++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userCount;
  }


  /**
   * Retrieves the existing user data for a specific email ID.
   * <p>
   * This method searches through the users list for a match on the email ID and returns an
   * ArrayList containing the user's first name, last name, and email ID.
   * </p>
   *
   * @param emailId The email ID of the user whose data is to be retrieved.
   * @return An ArrayList of strings containing the user's data.
   */
  public ArrayList<String> getExistingUserData(String emailId) {
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
   * Saves user data into a CSV file, creating a new entry for the user.
   *
   * @param firstName The first name of the user.
   * @param lastName  The last name of the user.
   * @param emailID   The email ID of the user.
   */
  @Override
  public void saveUserData(String firstName, String lastName, String emailID) {
    User user = new User(firstName, lastName, emailID);
    String filePath = "res/Data/UserData/UserData.csv";
    File csvFile = new File(filePath);
    boolean appendData = csvFile.exists() && csvFile.isFile() && csvFile.length() > 0;

    if (!csvFile.getParentFile().exists()) {
      csvFile.getParentFile().mkdirs();
    }

    try (FileWriter writer = new FileWriter(csvFile, appendData)) {
      if (!appendData) {
        writer.append("EmailID,FirstName,LastName\n");
      }
      writer.append(user.getEmailId()).append(",")
          .append(user.getFirstName()).append(",").append(user.getLastName())
          .append("\n");
    } catch (IOException e) {
      System.out.println("An error occurred while appending to the CSV file.");
      e.printStackTrace();
    }
    this.users.add(user);
  }

  /**
   * Checks if a user with the specified email ID exists in the system.
   *
   * @param emailId The email ID to check for existence.
   * @return True if the user exists, false otherwise.
   */
  @Override
  public boolean isExistingUser(String emailId) {
    boolean exists = false;
    for (User user : users) {
      if (user.getEmailId().equalsIgnoreCase(emailId)) {
        return true;
      }
    }
    return exists;
  }


}
