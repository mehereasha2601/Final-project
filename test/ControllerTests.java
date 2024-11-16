import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.Controller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class provides tests for Controller. It validates the inputs and outputs passed for all
 * controller flows.
 */
public class ControllerTests {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private MockModelInterface modelInterface;
  private MockViewInterface viewInterface;

  private List<String> simulatedUserInputs = new ArrayList<>();


  @Before
  public void setUp() {
    modelInterface = new MockModelInterface(new StringBuilder(""));
    viewInterface = new MockViewInterface(); // Use this for capturing output for verification
    System.setOut(new PrintStream(outContent)); // Capture System.out for verification
    simulatedUserInputs.clear(); // Ensure inputs list is clear at the start of each test
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out); // Restore System.out
    System.setIn(System.in); // Restore System.in
  }

  private void simulateUserInputs() {
    // Combine all user inputs using a newline as a separator
    StringJoiner joiner = new StringJoiner("\n");
    for (String input : simulatedUserInputs) {
      joiner.add(input);
    }
    ByteArrayInputStream in = new ByteArrayInputStream(joiner.toString().getBytes());
    System.setIn(in);
  }

  @Test
  public void testWelcomeWithExistingUserInput() {

    simulatedUserInputs.add("1"); // User selects option 1 (existing user)
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();

    assertTrue(
        viewInterface.getMessages().contains("Welcome!")
            && viewInterface.getMessages().contains("Please enter 1 if you are existing user"));
  }

  @Test
  public void testWelcomeWithNewUserInput() {
    simulatedUserInputs.add("2");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertTrue(
        viewInterface.getMessages().contains("Welcome!")
            && viewInterface.getMessages().contains("Please enter 2 if you are a new user"));
  }

  @Test
  public void testWelcomeWithInvalidUserInput() {
    simulatedUserInputs.add("3");
    simulatedUserInputs.add("1");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();

    assertTrue(
        viewInterface.getErrors().contains("Invalid input given. Please try again."));
  }


  @Test
  public void testExistingUserWorkflow() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals(modelInterface.log.toString(), "Create User Manager called. "
        + "getAllUsers method called. "
        + "isExistingUser method called with input mehereasha2601@gmail.com."
        + " getExistingUserData method called with input mehereasha2601@gmail.com.");
    assertTrue(
        viewInterface.getMessages()
            .contains("Hey Easha")); // Adjust the expected message as per actual implementation
    String s = modelInterface.log.toString();

  }

  @Test
  public void testgetComposition() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("2");
    simulatedUserInputs.add("technology");
    simulatedUserInputs.add("6");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. "
            + "getAllUsers method called. "
            + "isExistingUser method called with input mehereasha2601@gmail.com. "
            + "getExistingUserData method called with input mehereasha2601@gmail.com."
            + "Create Portfolio Manager called with input mehereasha2601@gmail.com."
            + " getNumberOfPortfolios method called. "
            + "portfolioExists method called with input technology."
            + " getPortfolioComposition method called with input technology.",
        modelInterface.log.toString());
    String s = viewInterface.getMessages();

    assertTrue(s.contains("Hey Easha!")
        && s.contains("Enter the portfolio name")
        && s.contains("Displaying the composition of the portfolio technology:")
        && s.contains("MLP:[Apple Inc, 7, 03-10-2024]")
        && s.contains("AAPL:[Apple Inc, 7, 03-10-2024]")
        && s.contains("You have been logged out. Thank you!"));
  }

  @Test
  public void testGetTotalValue() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("3");
    simulatedUserInputs.add("technology");
    simulatedUserInputs.add("2024-03-11");
    simulatedUserInputs.add("6");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. getAllUsers method called. "
        + "isExistingUser method called with input mehereasha2601@gmail.com. "
        + "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "Create Portfolio Manager called with input mehereasha2601@gmail.com. "
        + "getNumberOfPortfolios method called. "
        + "portfolioExists method called with input technology. "
        + "getTotalPortfolioValue method called with input technology "
        + ", 2024-03-11.", modelInterface.log.toString());
    String s = viewInterface.getMessages();
    assertTrue(s.contains("Hey Easha!") && s.contains("Enter the portfolio name :")
        && s.contains("Please enter the date (yyyy-mm-dd) :")
        && s.contains("The Total value of portfolio technology on 2024-03-11 is 1000.00")
        && s.contains("You have been logged out. Thank you!"));
  }

  @Test
  public void testExportPortfolio() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("4");
    simulatedUserInputs.add("technology");
    simulatedUserInputs.add("/Users/koppisettyeashameher/Desktop/new");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. "
            + "getAllUsers method called. "
            + "isExistingUser method called with input mehereasha2601@gmail.com. "
            + "getExistingUserData method called with input mehereasha2601@gmail.com.",
        modelInterface.log.toString());
    String s = viewInterface.getMessages();

    assertTrue(s.contains("Hey Easha!")
        && s.contains("Enter the portfolio name :")
        && s.contains(
        "Please enter the directory path as input. All your portfolios will be saved here")
        && s.contains("Your portfolios have been exported."));
  }


  @Test
  public void testImportPortfolio() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("5");
    simulatedUserInputs.add("/Users/koppisettyeashameher/IdeaProjects/builder.csv");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. "
        + "getAllUsers method called. "
        + "isExistingUser method called with input mehereasha2601@gmail.com. "
        + "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "Create Portfolio Manager called with input mehereasha2601@gmail.com. "
        + "portfolioExists method called with input builder. "
        + "savePortfolio method called with input builder , "
        + "{TBLD=[Thornburg Income Builder Opportunities Trust, 5, 2024-03-14], "
        + "BLD=[TopBuild Corp, 34, 2024-03-14]}.", modelInterface.log.toString());
    String s = viewInterface.getMessages();
    assertTrue(s.contains("Hey Easha!") && s.contains(
        "Please enter the file path as input. "
            + "Your portfolio will be loaded from this file.")
        && s.contains("TBLD:[Thornburg Income Builder Opportunities Trust, 5, 2024-03-14]")
        && s.contains("BLD:[TopBuild Corp, 34, 2024-03-14]")
        && s.contains("Your portfolios have been imported and saved."));
  }

  @Test
  public void testCreatePortfolio() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("test");
    simulatedUserInputs.add("2");
    simulatedUserInputs.add("test");
    simulatedUserInputs.add("ZVZZT");
    simulatedUserInputs.add("50");
    simulatedUserInputs.add("test");
    simulatedUserInputs.add("CBO");
    simulatedUserInputs.add("100");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. getAllUsers method called. "
        + "isExistingUser method called with input mehereasha2601@gmail.com. "
        + "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "Create Portfolio Manager called with input mehereasha2601@gmail.com. "
        + "portfolioExists method called with input test. savePortfolio method "
        + "called with input test , {CBO=[NYSE LISTED TEST STOCK FOR CTS AND CQS, 100, 03-14-2024],"
        + " ZVZZT=[NASDAQ TEST STOCK, 50, 03-14-2024]}. "
        + "getPortfolioComposition method called with input test.", modelInterface.log.toString());
    String s = viewInterface.getMessages();
    assertTrue(s.contains("Hey Easha!")
        && s.contains("Enter a portfolio name.")
        && s.contains("How many stocks will be present in this portfolio?")
        && s.contains("Please enter stock name:")
        && s.contains("There are multiple stocks with this name.")
        && s.contains(
        "Please enter the ticker symbol of the stock you want to add from the list above")
        && s.contains("Please enter the amount of stock you want to add to your portfolio:")
        && s.contains("NASDAQ TEST STOCK has been added to test")
        && s.contains("Please enter the amount of stock you want to add to your portfolio:")
        && s.contains("NYSE LISTED TEST STOCK FOR CTS AND CQS has been added to test"));
  }

  @Test
  public void testExistingUserWorkflowInvalidEmailFormat() {

    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601gmail.com");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals(modelInterface.log.toString(), "Create User Manager called."
        + " getAllUsers method called. "
        + "isExistingUser method called with input mehereasha2601@gmail.com. "
        + "getExistingUserData method called with input mehereasha2601@gmail.com.");
    assertTrue(viewInterface.getErrors().contains("Invalid email format. Please try again.") &&
        viewInterface.getMessages().contains("Hey Easha"));
  }

  @Test
  public void testExistingUserWorkflowNewUser() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("nouser2@gmail.com");
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("Test");
    simulatedUserInputs.add("User");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals(modelInterface.log.toString(), "Create User Manager called. "
        + "getAllUsers method called. isExistingUser method called with input nouser2@gmail.com."
        + "Create User Manager called. getAllUsers method called. "
        + "saveUserData method called with input Test , User , nouser2@gmail.com.");
    assertTrue(viewInterface.getMessages().contains("Please enter 1 if you are existing user")
        && viewInterface.getMessages().contains("Please enter your email address.")
        && viewInterface.getErrors().contains("Invalid email. This email does not exist.")
        && viewInterface.getMessages()
        .contains("Press 1 if you want to create a new user with this email.")
        && viewInterface.getMessages().contains("Press 2 to enter email again.")
        && viewInterface.getMessages().contains("Please enter your first name.")
        && viewInterface.getMessages().contains("Please enter your last name.")
        && viewInterface.getMessages().contains("Hey Test!"));
  }

  @Test
  public void testExistingUserWorkflowForIncorrectEmail() {

    simulatedUserInputs.add("1");
    simulatedUserInputs.add("nouser@gmail.com");
    simulatedUserInputs.add("1");
    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals(modelInterface.log.toString(), "Create User Manager called. "
        + "getAllUsers method called. isExistingUser method called with input nouser@gmail.com.");
    assertTrue(
        viewInterface.getErrors().contains("Invalid email. This email does not exist.")
            && viewInterface.getMessages()
            .contains("Press 1 if you want to create a new user with this email.")
            && viewInterface.getMessages().contains("Press 2 to enter email again."));
  }

  @Test
  public void testLogout() {
    simulatedUserInputs.add("1");
    simulatedUserInputs.add("mehereasha2601@gmail.com");
    simulatedUserInputs.add("6");

    simulateUserInputs();
    Controller controller = new Controller(modelInterface, viewInterface, System.in);
    controller.start();
    assertEquals("Create User Manager called. getAllUsers method called."
            + " isExistingUser method called with input mehereasha2601@gmail.com. "
            + "getExistingUserData method called with input mehereasha2601@gmail.com.",
        modelInterface.log.toString());
    String s = viewInterface.getMessages();
    assertTrue(s.contains("Please enter your email address.") && s.contains("Hey Easha!")
        && s.contains("Please select one of the options from main menu: ")
        && s.contains("6. Logout")
        && s.contains("You have been logged out. Thank you!"));
  }


}
