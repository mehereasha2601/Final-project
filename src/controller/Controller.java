package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.ModelInterface;
import model.PortfolioManager;
import model.PortfolioManagerInterface;
import model.StockAnalysisInterface;
import model.UserManagerInterface;
import view.ViewInterface;


/**
 * This class represents the controller part of the MVC (Model-View-Controller) architecture
 * responsible for orchestrating the application's flow based on user input.
 * <p>
 * It bridges the Model and View layers by handling user interactions, processing input, and
 * invoking the corresponding actions on the Model. It updates the View with changes from the Model
 * or user input validation messages.
 * </p>
 */
public class Controller implements ControllerInterface {

  private final ModelInterface modelInterface;
  private final ViewInterface viewInterface;
  private final Scanner scanner;

  /**
   * Constructs a Controller with instances of specified Model and View interfaces, and input
   * stream.
   * <p>
   * Initializes the controller with the given Model and View, allowing for interaction between the
   * user and the application. It sets up a Scanner on the provided InputStream to read user input,
   * facilitating interaction with the console or other input sources.
   * </p>
   *
   * @param modelInterface the instance of model interface that handles business logic.
   * @param viewInterface  the instance of view interface that handles user interface.
   * @param in             the InputStream from which user input will be read.
   */
  public Controller(ModelInterface modelInterface, ViewInterface viewInterface,
      InputStream in) {
    this.modelInterface = modelInterface;
    this.viewInterface = viewInterface;
    this.scanner = new Scanner(in);
  }

  /**
   * Starts the application's main workflow, handling user interactions and application flow.
   * <p>
   * This method orchestrates the application's initial actions and responds to user inputs. It
   * employs exception handling to manage various scenarios, including invalid input or operational
   * errors, by displaying appropriate messages to the user and prompting for re-entry of data or
   * actions as necessary.
   * </p>
   */
  @Override
  public void start() {
    try {
      welcome();
    } catch (IllegalArgumentException e) {
      viewInterface.printValue(e.getMessage());
      welcome();
    } catch (Exception e) {
      viewInterface.printValue(e.getMessage());
    }
  }

  private void welcome() {
    viewInterface.printValue("Welcome!" + "\n" + "Please enter 1 if you are existing user"
        + "\n" + "Please enter 2 if you are a new user");
    int userInput = 0;
    try {
      userInput = Integer.parseInt(scanner.nextLine());
      if (userInput == 1) {
        existingUserWorkflow();
      } else if (userInput == 2) {
        newUserWorkflow();
      } else {
        throw new IllegalArgumentException("Invalid input");
      }
    } catch (Exception e) {
      viewInterface.printValue("Some exception occured. Please try again.");
      welcome();
    }
  }

  private void newUserWorkflow() throws IOException {
    UserManagerInterface userManagerInterface = modelInterface.createUserManager();
    viewInterface.printValue("Please enter your email address.");
    String emailID = scanner.nextLine();
    if (isValidEmail(emailID)) {
      if (userManagerInterface.isExistingUser(emailID)) {
        viewInterface.printValue("This email already exists. Logging you in!\n");
        viewInterface.printValue("Hey "
            + (userManagerInterface.getExistingUserData(emailID)).get(0) + "!");
        PortfolioManagerInterface portfolioManagerInterface = new PortfolioManager(emailID);
        mainMenuWorkflow(portfolioManagerInterface);
      } else {
        getUserDataWorkflow(emailID);
      }
    } else {
      viewInterface.printError("Invalid email format. Please try again.");
      newUserWorkflow();
    }
  }

  private void existingUserWorkflow() throws IOException {
    viewInterface.printValue("Please enter your email address.");
    String emailID = scanner.nextLine();
    if (isValidEmail(emailID)) {
      UserManagerInterface userManagerInterface = modelInterface.createUserManager();
      if (userManagerInterface.isExistingUser(emailID)) {
        viewInterface.printValue("\nHey "
            + (userManagerInterface.getExistingUserData(emailID)).get(0) + "!");
        PortfolioManagerInterface portfolioManagerInterface = new PortfolioManager(emailID);
        mainMenuWorkflow(portfolioManagerInterface);
      } else {
        existingUserEmailNotPresentWorkflow(emailID);
      }
    } else {
      viewInterface.printError("Invalid email format. Please try again.");
      existingUserWorkflow();
    }
  }

  private void existingUserEmailNotPresentWorkflow(String emailID) throws IOException {
    viewInterface.printValue("Invalid email. This email does not exist. \n");
    viewInterface.printValue("Press 1 if you want to create a new user with this email. \n"
        + "Press 2 to enter email again.");
    int j = 0;
    try {
      j = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printError("Invalid input given. Please try again.");
      existingUserWorkflow();
    }
    if (j == 1) {
      getUserDataWorkflow(emailID);
    } else if (j == 2) {
      existingUserWorkflow();
    } else {
      viewInterface.printError("Invalid input given. Please try again.");
      welcome();
    }
  }

  private PortfolioManagerInterface createNewUserWorkflow(String firstName, String lastName,
      String emailID) {
    UserManagerInterface userManagerInterface = modelInterface.createUserManager();
    userManagerInterface.saveUserData(firstName, lastName, emailID);
    PortfolioManagerInterface portfolioManagerInterface = new PortfolioManager(emailID);
    viewInterface.printValue("Hey " + firstName + "!");
    return portfolioManagerInterface;
  }

  private void getUserDataWorkflow(String emailID) throws IOException {
    String firstName;
    String lastName;
    viewInterface.printValue("Please enter your first name.");
    firstName = scanner.nextLine();
    viewInterface.printValue("Please enter your last name.");
    lastName = scanner.nextLine();
    PortfolioManagerInterface portfolioManagerInterface = createNewUserWorkflow(firstName, lastName,
        emailID);
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void mainMenuWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {

    viewInterface.printValue("Please select one of the options from main menu: \n" +
        "1. Create a new portfolio. \n" +
        "2. View composition of an existing portfolio. \n" +
        "3. View total value of your portfolio on a date. \n" +
        "4. Export your portfolio. \n" +
        "5. Import your portfolio \n" +
        "6. View your portfolio performance. \n" +
        "7. Buy stocks. \n" +
        "8. Sell stocks.\n" +
        "9. View your portfolio cost basis.\n" +
        "10. Display stock analysis menu.\n" +
        "11. Logout \n");
    int menuInput = 0;
    try {
      menuInput = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printError("Invalid input, taking you back to main menu.");
      mainMenuWorkflow(portfolioManagerInterface);
    }
    try {
      switch (menuInput) {
        case 1:
          createPortfolioWorkflow(portfolioManagerInterface);
          break;
        case 2:
          viewPortfolioCompositionWorkflow(portfolioManagerInterface);
          break;
        case 3:
          totalValueOfPortfolioWorkflow(portfolioManagerInterface);
          break;
        case 4:
          exportPortfolioWorkflow(portfolioManagerInterface);
          break;
        case 5:
          importPortfolioWorkflow(portfolioManagerInterface);
          break;
        case 6:
          portfolioPerformanceWorkflow(portfolioManagerInterface);
          break;
        case 7:
          buyStocks(portfolioManagerInterface);
          break;
        case 8:
          sellStocks(portfolioManagerInterface);
          break;
        case 9:
          portfolioCostBasisWorkflow(portfolioManagerInterface);
          break;
        case 10:
          displayStockAnalysisMenu(portfolioManagerInterface);
          break;
        case 11:
          logoutWorkflow();
          break;
        default:
          viewInterface.printError("Invalid input, taking you back to main menu.");
          mainMenuWorkflow(portfolioManagerInterface);
      }
    } catch (Exception e) {
      viewInterface.printValue("Some exception occured");
      viewInterface.printValue("Taking you back to main menu "
          + "\n-------------------------------\n");
      mainMenuWorkflow(portfolioManagerInterface);
    }
  }

  private void displayStockAnalysisMenu(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("Please select one of the options from main menu: \n" +
        "1. Display stock performance. \n" +
        "2. Display stock gain/loss. \n" +
        "3. Display stock crossover days. \n" +
        "4. Check stock moving crossover days. \n" +
        "5. Check stock moving average. \n");
    int menuInput = 0;
    try {
      menuInput = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printValue("Invalid input, taking you back to main menu.");
      displayStockAnalysisMenu(portfolioManagerInterface);
    }
    switch (menuInput) {
      case 1:
        computeStockPerformance();
        break;
      case 2:
        checkStockGainsOrLoss();
        break;
      case 3:
        computeCrossoverDays();
        break;
      case 4:
        computeMovingCrossoverDays();
        break;
      case 5:
        computeMovingAverage();
        break;
      default:
        viewInterface.printError("Invalid input, taking you back to main menu.");
    }
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void createPortfolioWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("Enter a portfolio name.");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      viewInterface.printError("This portfolio already exists. ");
    } else {
      createPortfolioFlexibleNonFlexibleWorkflow(portfolioName, portfolioManagerInterface);
    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void createPortfolioFlexibleNonFlexibleWorkflow(String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("Press 1 if you want to create a flexible portfolio\n" +
        "Press 2 if you want to create a immutable portfolio");
    int userInput = 0;
    try {
      userInput = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printError("Invalid input given. Please try again.");
      createPortfolioWorkflow(portfolioManagerInterface);
    }
    if (userInput == 1) {
      portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
      viewInterface.printValue("A flexible portfolio with name " + portfolioName
          + " has been created and saved.");
    } else if (userInput == 2) {
      immutablePortfolio(portfolioName, portfolioManagerInterface);
    } else {
      viewInterface.printValue("Invalid input.");
    }
  }

  private void immutablePortfolio(String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("How many stocks do you want to add in this portfolio?");
    int noOfStocks = getStockIntegerInput();
    HashMap<String, ArrayList<String>> stockData = getStockInputsForCreatingPortfolio(
        noOfStocks, portfolioName);
    portfolioManagerInterface.savePortfolio(portfolioName, stockData);
    viewInterface.printValue("An immutable portfolio with name " + portfolioName
        + " has been created and saved.");
    viewInterface.printStockData(
        portfolioManagerInterface.getPortfolioComposition(portfolioName, LocalDate.now()));
  }


  private void viewPortfolioCompositionWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {

    viewInterface.printValue("Enter the portfolio name");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      viewCompositionPortfolioExistsWorkflow(portfolioName, portfolioManagerInterface);
    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exists.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }

    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void viewCompositionPortfolioExistsWorkflow(String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface) {
    boolean isMutable = portfolioManagerInterface.isPortfolioMutable(portfolioName);
    viewInterface.printValue("\nDisplaying the composition of the portfolio "
        + portfolioName);
    if (isMutable) {
      viewInterface.printValue("Please enter the date for checking composition:");
      LocalDate date = getDateObject(getDateInput());
      viewInterface.printStockData(
          portfolioManagerInterface.getPortfolioComposition(portfolioName, date));
    } else {
      viewInterface.printStockData(
          portfolioManagerInterface.getPortfolioComposition(portfolioName, LocalDate.now()));
    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
  }

  private void totalValueOfPortfolioWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      totalValuePortfolioExistsWorkflow(portfolioName, portfolioManagerInterface);
    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exits.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }

    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void totalValuePortfolioExistsWorkflow(String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue("Please enter the date (yyyy-mm-dd) :");
    String stringDate = getDateInput();
    LocalDate date = LocalDate.parse(stringDate);
    String totalValue =
        portfolioManagerInterface.getTotalPortfolioValue(portfolioName, date);
    if (!totalValue.equals("0.0")) {
      viewInterface.printValue("The Total value of portfolio "
          + portfolioName + " on " + date + " is " + totalValue + " dollars.");
      viewInterface.printValue(
          "Taking you back to main menu \n-------------------------------");
    } else {
      viewInterface.printValue("There is no data available for this given date.");
      viewInterface.printValue(
          "Taking you back to main menu \n-------------------------------");
    }
  }

  private void logoutWorkflow() {
    viewInterface.printValue("You have been logged out. Thank you!");
  }

  private void exportPortfolioWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      exportPortfolioPortfolioExistsWorkflow(portfolioName, portfolioManagerInterface,
          portfolioReadWrite);
    } else {
      viewInterface.printValue("This portfolio does not exist. "
          + "\nTaking you back to main menu \n-------------------------------\n");
    }

    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void exportPortfolioPortfolioExistsWorkflow(String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface, PortfolioReadWrite portfolioReadWrite) {
    viewInterface.printValue("Please enter the directory path as input."
        + " Your portfolio will be saved here");
    String directoryPath = scanner.nextLine();
    boolean isMutable = portfolioManagerInterface.isPortfolioMutable(portfolioName);
    HashMap<String, ArrayList<String>> portfolioComposition;
    LocalDate date = LocalDate.now();
    if (isMutable) {
      viewInterface.printValue("Please enter the date for exporting composition:");
      date = getDateObject(getDateInput());
      portfolioComposition =
          portfolioManagerInterface.getPortfolioComposition(portfolioName, date);
    } else {
      portfolioComposition =
          portfolioManagerInterface.getPortfolioComposition(portfolioName, date);
    }
    portfolioReadWrite.exportPortfolioToCSVFile(date, portfolioName,
        portfolioComposition, directoryPath);
    viewInterface.printValue("Your portfolios have been exported."
        + "\nTaking you back to main menu \n-------------------------------\n");
  }

  private void importPortfolioWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {
    viewInterface.printValue(
        "Please enter 1 if you want to import your portfolio as a flexible Portfolio");
    viewInterface.printValue(
        "Please enter 2 if you want to import your portfolio as a immutable Portfolio");
    int userInput = getStockIntegerInput();
    viewInterface.printValue("Please enter the file path as input. "
        + "Your portfolio will be loaded from this file.");
    String filePath = scanner.nextLine();
    PortfolioReadWrite p = new PortfolioReadWrite();
    String portfolioName = p.portfolioNameFromCSVFile(filePath);
    HashMap<String, ArrayList<String>> stockData = new HashMap<>();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      viewInterface.printError("This portfolio already exists. "
          + "\nTaking you back to main menu \n-------------------------------\n");
    } else {
      importPortfolioPortfolioDoesNotExistWorkflow(stockData, userInput, filePath, portfolioName,
          portfolioManagerInterface, p);
    }
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void importPortfolioPortfolioDoesNotExistWorkflow(
      HashMap<String, ArrayList<String>> stockData,
      int userInput, String filePath, String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface, PortfolioReadWrite p)
      throws IOException {
    try {
      stockData = p.importPortfolio(filePath);
      viewInterface.printValue(portfolioName);
      viewInterface.printStockData(stockData);
      if (userInput == 1) {
        portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
        for (String ticker : stockData.keySet()) {
          ArrayList<String> data = stockData.get(ticker);
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate date = LocalDate.parse(data.get(1), formatter);
          portfolioManagerInterface.buyPortfolioStock(portfolioName, ticker,
              Integer.parseInt(data.get(0)), date);
        }
      } else if (userInput == 2) {
        portfolioManagerInterface.savePortfolio(portfolioName, stockData);
      }

      viewInterface.printValue("Your portfolio has been imported and saved."
          + "\nTaking you back to main menu \n-------------------------------\n");
    } catch (NoSuchFileException e) {
      viewInterface.printValue("Invalid file path.You can try importing again.");
      importPortfolioWorkflow(portfolioManagerInterface);
    } catch (Exception e2) {
      viewInterface.printValue("Failed to import.You can try importing again. "
          + "Recheck if proper ticker values are present in your file.");
      importPortfolioWorkflow(portfolioManagerInterface);
    }
  }

  private void portfolioCostBasisWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {

    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    viewInterface.printValue(
        "Please enter the date in (yyyy-mm-dd) format to calculate cost basis:");
    String stringDate = getDateInput();
    LocalDate date = getDateObject(stringDate);
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      viewInterface.printValue(
          "The cost basis for the portfolio " + portfolioName + " on " + date + " is "
              + portfolioManagerInterface.getCostPortfolioBasis(portfolioName, date)
              + " dollars.");
    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exits.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }

    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
    mainMenuWorkflow(portfolioManagerInterface);
  }


  private void portfolioPerformanceWorkflow(PortfolioManagerInterface portfolioManagerInterface)
      throws IOException {

    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    ArrayList<LocalDate> dates = getStartAndEndDates();
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {

      portfolioPerformancePortfolioExistsWorkflow(portfolioManagerInterface, portfolioName,
          dates);

    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exits.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }

    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void portfolioPerformancePortfolioExistsWorkflow(PortfolioManagerInterface
      portfolioManagerInterface, String portfolioName, ArrayList<LocalDate> dates) {
    try {
      viewInterface.printValue(
          "The performance for the portfolio " + portfolioName + " between " + dates.get(0)
              + " and "
              + dates.get(1) + " is ");
      viewInterface.printValue(
          portfolioManagerInterface.getPortfolioPerformance(portfolioName, dates.get(0),
              dates.get(1)));
    } catch (IllegalArgumentException e) {
      viewInterface.printError(e.getMessage());
      viewInterface.printValue("Taking you back to main menu"
          + " \n-------------------------------");
    }
  }

  private void buyStocks(PortfolioManagerInterface portfolioManagerInterface) throws IOException {

    buyStocksNonZeroPortfoliosWorkflow(portfolioManagerInterface);
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void buyStocksNonZeroPortfoliosWorkflow(
      PortfolioManagerInterface portfolioManagerInterface) {
    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (!portfolioManagerInterface.isPortfolioMutable(portfolioName)) {
      viewInterface.printValue("Cannot buy stocks for immutable portfolio.");
      return;
    }
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {

      String ticker = getValidticker();
      viewInterface.printValue(
          "Please enter the date on which you want to buy stocks in YYYY-MM-dd format:");
      LocalDate date = getDateObject(getDateInput());
      viewInterface.printValue("Please enter the number of stocks you want to buy");
      int noOfShares = getStockIntegerInput();
      portfolioManagerInterface.buyPortfolioStock(portfolioName, ticker, noOfShares, date);
      viewInterface.printValue("The stock with ticker "
          + ticker + " has been bought to portfolio " + portfolioName + " with quantity as "
          + noOfShares + " and date of purchase as " + date + ".");
    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exits.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }
  }

  private String getValidticker() {
    viewInterface.printValue("Please enter ticker of the stock:");
    String ticker = scanner.nextLine().toUpperCase();
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    if (!portfolioReadWrite.isValidTicker(ticker)) {
      viewInterface.printValue("Invalid ticker.Please try again");
      getValidticker();
    }
    return ticker;
  }


  private void sellStocks(PortfolioManagerInterface portfolioManagerInterface) throws IOException {

    sellStocksNonZeroPortfoliosWorkflow(portfolioManagerInterface);

    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
    mainMenuWorkflow(portfolioManagerInterface);
  }

  private void sellStocksNonZeroPortfoliosWorkflow(
      PortfolioManagerInterface portfolioManagerInterface) {
    viewInterface.printValue("Enter the portfolio name :");
    String portfolioName = scanner.nextLine().toLowerCase();
    if (!portfolioManagerInterface.isPortfolioMutable(portfolioName)) {
      viewInterface.printValue("Cannot sell stocks for immutable portfolio.");
      return;
    }
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {

      String ticker = getValidticker();
      viewInterface.printValue(
          "Please enter the date on which you want to sell stocks in YYYY-MM-dd format:");
      LocalDate date = getDateObject(getDateInput());
      viewInterface.printValue("Please enter the number of stocks you want to sell");
      int noOfShares = getStockIntegerInput();
      portfolioManagerInterface.sellPortfolioStock(portfolioName, ticker, noOfShares, date);
      viewInterface.printValue("The stock with ticker "
          + ticker + " has been sold from portfolio " + portfolioName + " with quantity as"
          + noOfShares + " and date of purchase as " + date + ".");
    } else {
      viewInterface.printError("No portfolio with portfolio name "
          + portfolioName + " exits.\nTaking you back to main menu "
          + "\n-------------------------------\n");
    }
  }

  private void computeCrossoverDays() throws IOException {

    String ticker = getValidticker();
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    ArrayList<LocalDate> dates = getStartAndEndDates();

    try {
      viewInterface.printValue("The crossover days for the stock "
          + ticker + " between " + dates.get(0) + " and "
          + dates.get(1) + " are ");
      HashMap<LocalDate, String> movingCrossovers = stockAnalysisInterface.getCrossoversDays(
          dates.get(0), dates.get(1));
      for (LocalDate date : movingCrossovers.keySet()) {
        viewInterface.printValue("" + date + " " + movingCrossovers.get(date));
      }
    } catch (IllegalArgumentException | IOException e) {
      viewInterface.printError(e.getMessage());
      viewInterface.printValue("Taking you back to main menu"
          + " \n-------------------------------");
    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
  }


  private void computeMovingCrossoverDays() throws IOException {
    String ticker = getValidticker();
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    ArrayList<LocalDate> dates = getStartAndEndDates();
    viewInterface.printValue("Please enter the short moving average period in days.");
    int x = getStockIntegerInput();
    viewInterface.printValue("Please enter the long moving average period in days.");
    int y = getStockIntegerInput();
    try {
      viewInterface.printValue("The moving crossover days for the stock "
          + ticker + " between " + dates.get(0) + " and "
          + dates.get(1) + " are ");
      HashMap<LocalDate, String> movingCrossoversDays =
          stockAnalysisInterface.getMovingCrossoversDays(
              dates.get(0), dates.get(1), x, y);
      for (LocalDate date : movingCrossoversDays.keySet()) {
        viewInterface.printValue("" + date + " " + movingCrossoversDays.get(date));
      }
    } catch (IOException e) {
      viewInterface.printError(e.getMessage());
      viewInterface.printValue("Taking you back to main menu"
          + " \n-------------------------------");
    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");

  }

  private void computeMovingAverage() throws IOException {
    LocalDate currentDate = LocalDate.now();

    String ticker = getValidticker();
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    viewInterface.printValue(
        "Please enter the number of days for which you want to compute moving average.");
    int days = getStockIntegerInput(); //int that's greater than 0
    viewInterface.printValue(
        "The " + days + "-day moving average for the stock " + ticker + " is "
            + stockAnalysisInterface.getMovingAverage(currentDate, days));
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");

  }


  private void checkStockGainsOrLoss() throws IOException {

    String ticker = getValidticker();
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    viewInterface.printValue("Please enter 1 if you want to compute gains/loss for a single day.");
    viewInterface.printValue(
        "Please enter 2 if you want to compute gains/loss over a period of time.");
    int userInput = 0;
    try {
      userInput = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printError("Invalid input given. Please try again.");
      checkStockGainsOrLoss();
    }
    try {
      stockGainsOrLossUserInputWorkflow(userInput, ticker, stockAnalysisInterface);
    } catch (IOException e) {
      viewInterface.printError("An exception occurred. Please try again.");
    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
  }


  private void stockGainsOrLossUserInputWorkflow(int userInput, String ticker,
      StockAnalysisInterface stockAnalysisInterface)
      throws IOException {
    if (userInput == 1) {
      viewInterface.printValue("Please enter the date (yyyy-mm-dd) :");
      String stringDate = getDateInput();
      LocalDate date = getDateObject(stringDate);
      viewInterface.printValue(
          "The gains/loss for the stock " + ticker + " on " + date + " is "
              + stockAnalysisInterface.getStockTrendOnDate(date));
    } else if (userInput == 2) {
      ArrayList<LocalDate> dates = getStartAndEndDates();
      viewInterface.printValue(
          "The gains/loss for the stock " + ticker + " between " + dates.get(0) + " and "
              + dates.get(1) + " is " + stockAnalysisInterface.getStockTrend(dates.get(0),
              dates.get(1)));
    } else {
      viewInterface.printError("Invalid input given. Please try again.");
      checkStockGainsOrLoss();
    }
  }

  private void computeStockPerformance() throws IOException {
    String ticker = getValidticker();
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    ArrayList<LocalDate> dates = getStartAndEndDates();

    try {
      viewInterface.printValue("The performance analysis for the stock "
          + ticker + " between " + dates.get(0) + " and "
          + dates.get(1) + " is \n"
          + stockAnalysisInterface.getStockPerformance(dates.get(0), dates.get(1)));
    } catch (IllegalArgumentException | IOException e) {
      viewInterface.printError(e.getMessage());
      viewInterface.printValue("Taking you back to main menu"
          + " \n-------------------------------");

    }
    viewInterface.printValue("Taking you back to main menu "
        + "\n-------------------------------\n");
  }


  private boolean isValidEmail(String email) {
    String emailRegex = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  private HashMap<String, ArrayList<String>> getStockInputsForCreatingPortfolio(int noOfStocks,
      String portfolioName) {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    HashMap<String, ArrayList<String>> stockData = new HashMap<>();
    for (int i = 0; i < noOfStocks; i++) {
      viewInterface.printValue("Please enter the ticker of the stock you want to add:");
      String ticker = scanner.nextLine().toUpperCase();
      PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
      if (portfolioReadWrite.isValidTicker(ticker)) {
        ArrayList<String> arraylist = new ArrayList<>();
        viewInterface.printValue(
            "Please enter the number of shares you want to add to your portfolio:");
        int amount = getStockIntegerInput();
        arraylist.add("" + amount);
        arraylist.add(dateString);
        stockData.put(ticker, arraylist);
        viewInterface.printValue(
            ticker + " has been added to portfolio " + portfolioName + " with quantity as "
                + amount);
      } else {
        viewInterface.printError("No stocks found with this ticker."
            + " Please try with some other stock ticker");
        i--;
      }
    }
    return stockData;
  }


  private int getStockIntegerInput() {
    int noOfStocks = 0;
    try {
      noOfStocks = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      viewInterface.printError("Invalid input, "
          + "please enter an integer value greater than 0.");
      noOfStocks = getStockIntegerInput();
    }
    if (noOfStocks <= 0) {
      viewInterface.printError("Invalid input,"
          + " please enter an integer value greater than 0.");
      noOfStocks = getStockIntegerInput();
    }
    return noOfStocks;
  }


  private String getDateInput() {
    String dateString = "";
    LocalDate currentDate = LocalDate.now();
    while (true) {
      try {
        dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        if (date.isAfter(currentDate)) {
          viewInterface.printError("The date must not be after the current date.");
          viewInterface.printValue("Please try again.");
          continue;
        }
        break;
      } catch (DateTimeParseException e) {
        viewInterface.printError("Invalid date/date format.");
        viewInterface.printValue("Please use yyyy-MM-dd format and try again.");
      } catch (Exception e) {
        viewInterface.printError("An unexpected error occurred.");
        viewInterface.printValue("Please try again.");
      }
    }
    return dateString;
  }

  private LocalDate getDateObject(String date) {
    String[] dateComponents = date.split("-");
    int month = Integer.parseInt(dateComponents[1]);
    int day = Integer.parseInt(dateComponents[2]);
    int year = Integer.parseInt(dateComponents[0]);
    LocalDate dateObject = LocalDate.of(year, month, day);
    return dateObject;
  }


  private ArrayList<LocalDate> getStartAndEndDates() {
    ArrayList<LocalDate> dates = new ArrayList<>();
    viewInterface.printValue("Please enter the start date (yyyy-mm-dd) :");
    String stringStartDate = getDateInput();
    LocalDate startDate = getDateObject(stringStartDate);
    viewInterface.printValue("Please enter the end date (yyyy-mm-dd) :");
    String stringEndDate = getDateInput();
    LocalDate endDate = getDateObject(stringEndDate);
    dates.add(startDate);
    dates.add(endDate);
    return dates;
  }


}


