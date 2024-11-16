import static org.junit.Assert.assertEquals;

import java.util.Random;
import model.User;
import model.UserManager;
import org.junit.Test;


/**
 * This class all has tests to validate the functionality of the User class.
 */
public class TestUser {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");
  private UserManager userManager = new UserManager();

  @Test
  public void createUserConstructorTest() {
    assertEquals("John", testUser.getFirstName());
    assertEquals("Doe", testUser.getLastName());
    assertEquals("johndoe@gmail.com", testUser.getEmailId());
  }

  /**
   * Helper method that generates a random string of given length.
   *
   * @param length takes in the length of the string.
   * @return the randomly generated string.
   */
  private String generateRandomString(int length) {
    String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(charPool.length());
      char randomChar = charPool.charAt(index);
      sb.append(randomChar);
    }
    return sb.toString();
  }

}
