package controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import model.ModelInterface;
import model.PortfolioManager;
import model.PortfolioManagerInterface;
import model.StockAnalysisInterface;
import model.UserManagerInterface;
import view.GuiViewInterface;

/**
 * The GuiController class provides the connection between the view and the model in a GUI-based
 * application for managing user portfolios. It facilitates user interactions such as creating and
 * viewing portfolios, buying or selling stocks, and analyzing stock performance.
 */
public class GuiController implements GuiControllerInterface {

  private final ModelInterface modelInterface;
  private GuiViewInterface viewInterface;

  private PortfolioManagerInterface portfolioManagerInterface;

  private PortfolioReadWriteInterface portfolioReadWriteInterface;


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
   */
  public GuiController(ModelInterface modelInterface, GuiViewInterface viewInterface) {
    this.modelInterface = modelInterface;
    this.viewInterface = viewInterface;
    this.portfolioReadWriteInterface = new PortfolioReadWrite();
  }


  @Override
  public void start() {
    try {
      viewInterface.setController(this);
      welcome();
    } catch (IllegalArgumentException e) {
      viewInterface.printValue(e.getMessage());
      welcome();
    } catch (Exception e) {
      viewInterface.printValue(e.getMessage());
    }
  }

  private void welcome() {
    viewInterface.showLoginScreen();
  }

  @Override
  public String newUserWorkflow(ArrayList<String> userData) {
    String s;
    UserManagerInterface userManagerInterface = modelInterface.createUserManager();
    String emailID = userData.get(2);

    if (userManagerInterface.isExistingUser(emailID)) {
      this.portfolioManagerInterface = new PortfolioManager(emailID);
      s = "User already exists logging you in.";
    } else {
      createNewUserWorkflow(userData.get(0), userData.get(1), emailID);
      s = "New user created.logging you in.";
    }
    return s;
  }

  private void createNewUserWorkflow(String firstName, String lastName,
      String emailID) {
    UserManagerInterface userManagerInterface = modelInterface.createUserManager();
    userManagerInterface.saveUserData(firstName, lastName, emailID);
    this.portfolioManagerInterface = new PortfolioManager(emailID);
  }


  @Override
  public String existingUserWorkflow(String emailID) {
    String s = "There is no user with " + emailID + ". Please create a user first.";

    UserManagerInterface userManagerInterface = modelInterface.createUserManager();
    if (userManagerInterface.isExistingUser(emailID)) {
      viewInterface.printValue("\nHey "
          + (userManagerInterface.getExistingUserData(emailID)).get(0) + "!");
      this.portfolioManagerInterface = new PortfolioManager(emailID);
      s = "Hey " + emailID + "! Logging you in";
    }
    return s;
  }

  @Override
  public String createPortfolioWorkflow(String portfolioName) {
    String s = "";
    try {
      if (this.portfolioManagerInterface.portfolioExists(portfolioName)) {
        s = "Cannot create portfolio because this portfolio already exists."
            + " Taking you back to main menu.";
      }
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public String createFlexiblePortfolioWorkflow(String portfolioName) {
    this.portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
    return "A flexible portfolio with name " + portfolioName
        + " has been created and saved.";
  }

  @Override
  public String immutablePortfolio(String portfolioName,
      HashMap<String, ArrayList<String>> stockData) {
    String s;
    try {
      for (String ticker : stockData.keySet()) {
        if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
          stockData.remove(ticker, stockData.get(ticker));
        }
      }
      portfolioManagerInterface.savePortfolio(portfolioName, stockData);
      s = "An immutable portfolio with name " + portfolioName
          + " has been created and saved. Taking you back to main  menu.";
      viewInterface.printStockData(
          portfolioManagerInterface.getPortfolioComposition(portfolioName, LocalDate.now()));
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public String viewPortfolioCompositionWorkflow(String portfolioName, LocalDate date) {
    String s;
    try {
      if (portfolioManagerInterface.portfolioExists(portfolioName)) {
        s = viewCompositionPortfolioExistsWorkflow(portfolioName, date);
      } else {
        return "No portfolio with portfolio name "
            + portfolioName + " exists.\nTaking you back to main menu "
            + "\n-------------------------------\n";
      }
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  private String viewCompositionPortfolioExistsWorkflow(String portfolioName,
      LocalDate date) {
    StringBuilder s = new StringBuilder();
    HashMap<String, ArrayList<String>> composition;
    boolean isMutable = portfolioManagerInterface.isPortfolioMutable(portfolioName);

    if (isMutable) {
      composition = portfolioManagerInterface.getPortfolioComposition(portfolioName, date);
    } else {
      composition = portfolioManagerInterface.getPortfolioComposition(portfolioName,
          LocalDate.now());
    }
    s.append("\nDisplaying the composition of the portfolio ").append(portfolioName).append("\n")
        .append("Ticker   ").append("No of shares").append("\n");
    for (String ticker : composition.keySet()) {
      s.append(ticker).append("    ").append(composition.get(ticker).get(0)).append("\n");
    }

    return s.toString();
  }

  @Override
  public String totalValueOfPortfolioWorkflow(String portfolioName, LocalDate date) {
    String s;
    try {
      if (portfolioManagerInterface.portfolioExists(portfolioName)) {
        s = totalValuePortfolioExistsWorkflow(portfolioName, date);
      } else {
        return "No portfolio with portfolio name "
            + portfolioName + " exits.\nTaking you back to main menu ";
      }
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  private String totalValuePortfolioExistsWorkflow(String portfolioName, LocalDate date)
      throws IOException {
    String totalValue = portfolioManagerInterface.getTotalPortfolioValue(portfolioName, date);
    if (!totalValue.equals("0.0")) {
      totalValue = "The Total value of portfolio "
          + portfolioName + " on " + date + " is " + totalValue + " dollars.";

    } else {
      totalValue = "There is no data available for this given date.";
    }
    return totalValue;
  }


  @Override
  public String exportPortfolioWorkflow(String portfolioName,
      String directoryPath, LocalDate date) {
    if (portfolioReadWriteInterface.strategyExists(portfolioName,
            "dollarCostAveraging")) {
      portfolioReadWriteInterface.exportStrategyToCSV(portfolioName,
              "dollarCostAveraging", directoryPath);
    }
    HashMap<String, ArrayList<String>> portfolioComposition;
    try {
      portfolioComposition =
          portfolioManagerInterface.getPortfolioComposition(portfolioName, date);
      portfolioReadWriteInterface.exportPortfolioToCSVFile(date, portfolioName,
          portfolioComposition, directoryPath);
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return "Your portfolios have been exported to " + directoryPath + portfolioName + ".csv";
  }

  @Override
  public String importPortfolioWorkflow(String filePath, int option) {
    String portfolioName = portfolioReadWriteInterface.portfolioNameFromCSVFile(filePath);
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      return "This portfolio already exists.";
    } else {
      return importPortfolioPortfolioDoesNotExistWorkflow(option,
          filePath, portfolioName, portfolioManagerInterface, portfolioReadWriteInterface);
    }
  }

  private String importPortfolioPortfolioDoesNotExistWorkflow(
      int userInput, String filePath, String portfolioName,
      PortfolioManagerInterface portfolioManagerInterface, PortfolioReadWriteInterface p) {
    try {
      HashMap<String, ArrayList<String>> stockData = new HashMap<>();
      if (userInput == 1) {
        stockData = p.importPortfolio(filePath);
        portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
        for (String ticker : stockData.keySet()) {
          ArrayList<String> data = stockData.get(ticker);
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate date = LocalDate.parse(data.get(1), formatter);
          double shares = Double.parseDouble(data.get(0));
          portfolioManagerInterface.buyPortfolioStock(portfolioName, ticker,
              shares, date);
        }
      } else if (userInput == 2) {
        stockData = p.importPortfolio(filePath);
        portfolioManagerInterface.savePortfolio(portfolioName, stockData);
      } else if (userInput == 3) {
        importPortfolioWithStrategy(filePath);
      }
      return "Your portfolio has been successfully imported";
    } catch (NoSuchFileException e) {
      return "Invalid file path.You can try importing again.";
    } catch (Exception e2) {
      return "Failed to import.You can try importing again. "
          + "Recheck if proper ticker values are present in your file.";
    }
  }

  @Override
  public String portfolioCostBasisWorkflow(String portfolioName, LocalDate date) {
    String s;
    try {
      boolean isMutable = portfolioManagerInterface.isPortfolioMutable(portfolioName);
      if (portfolioManagerInterface.portfolioExists(portfolioName)) {
        if (isMutable) {
          s = "The cost basis for the portfolio " + portfolioName + " on " + date + " is "
              + portfolioManagerInterface.getCostPortfolioBasis(portfolioName, date)
              + " dollars.";
        } else {
          return "The cost basis can only be computed for flexible portfolios";
        }
      } else {
        return "No portfolio with portfolio name "
            + portfolioName + " exits.\nTaking you back to main menu ";
      }
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }


  @Override
  public String portfolioPerformanceWorkflow(String portfolioName, ArrayList<LocalDate> dates) {
    String s;
    try {
      if (portfolioManagerInterface.portfolioExists(portfolioName)) {
        s = portfolioPerformancePortfolioExistsWorkflow(portfolioName,
            dates);
      } else {
        return "No portfolio with portfolio name "
            + portfolioName + " exits.\nTaking you back to main menu "
            + "\n-------------------------------\n";
      }
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  private String portfolioPerformancePortfolioExistsWorkflow(String portfolioName
      , ArrayList<LocalDate> dates) {
    String s = "";
    s = s + "The performance for the portfolio " + portfolioName
        + " between " + dates.get(0)
        + " and "
        + dates.get(1) + " is \n";
    s = s + portfolioManagerInterface.getPortfolioPerformance(portfolioName, dates.get(0),
        dates.get(1));
    return s;
  }


  @Override
  public String buyOrSellStocksNonZeroPortfoliosWorkflow(
      String portfolioName, String ticker, int noOfShares
      , LocalDate date, String cause) {
    String s = "";
    try {
      if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
        return "Invalid ticker given. Please try again with a valid ticker.";
      }
      if (portfolioManagerInterface.portfolioExists(portfolioName)) {
        if (!portfolioManagerInterface.isPortfolioMutable(portfolioName)) {
          s = "Cannot " + cause + " stocks for immutable portfolio.";
          return s;
        }
        if (cause.equalsIgnoreCase("buy")) {
          portfolioManagerInterface.buyPortfolioStock(portfolioName, ticker, noOfShares, date);
          s = "The stock with ticker "
              + ticker + " has been added to portfolio " + portfolioName + " with quantity as "
              + noOfShares + " and date as " + date + ".";
        } else if (cause.equalsIgnoreCase("sell")) {
          portfolioManagerInterface.sellPortfolioStock(portfolioName, ticker, noOfShares, date);
          s = "The stock with ticker "
              + ticker + " has been sold from portfolio " + portfolioName + " with quantity as "
              + noOfShares + " and date as " + date + ".";
        }
      } else {
        return "No portfolio with portfolio name "
            + portfolioName + " exits.\nTaking you back to main menu ";
      }
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public String computeCrossoverDays(String ticker
      , ArrayList<LocalDate> dates) throws IOException {

    StringBuilder s = new StringBuilder();
    if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
      return "Invalid ticker given. Please try again with a valid ticker.";
    }
    StockAnalysisInterface stockAnalysisInterface = modelInterface.stockAnalysis(ticker);
    try {
      s.append("The crossover days for the stock ").append(ticker).append(" between ")
          .append(dates.get(0)).append(" and ").append(dates.get(1)).append(" are \n");

      HashMap<LocalDate, String> movingCrossovers = stockAnalysisInterface.getCrossoversDays(
          dates.get(0), dates.get(1));

      for (LocalDate date : movingCrossovers.keySet()) {
        s.append(date).append(" : ").append(movingCrossovers.get(date)).append("\n");
      }
    } catch (Exception e) {
      s = new StringBuilder(e.getMessage() + "\n Taking you back to main menu");
    }
    return s.toString();
  }


  @Override
  public String computeMovingCrossoverDays(String ticker
      , ArrayList<LocalDate> dates, int x, int y) throws IOException {
    StringBuilder s = new StringBuilder();
    if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
      return "Invalid ticker given. Please try again with a valid ticker.";
    }
    StockAnalysisInterface stockAnalysisInterface = modelInterface.stockAnalysis(ticker);
    try {
      s.append("The moving crossover days for the stock ").append(ticker).append(" between ")
          .append(dates.get(0)).append(" and ").append(dates.get(1)).append(" are \n");
      HashMap<LocalDate, String> movingCrossoversDays =
          stockAnalysisInterface.getMovingCrossoversDays(
              dates.get(0), dates.get(1), x, y);
      for (LocalDate date : movingCrossoversDays.keySet()) {
        s.append(date).append(" : ").append(movingCrossoversDays.get(date)).append("\n");
      }
    } catch (Exception e) {
      s = new StringBuilder(e.getMessage() + " Taking you back to main menu");
    }
    return s.toString();

  }

  @Override
  public String computeMovingAverage(String ticker, int days) {
    String s;
    try {
      if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
        return "Invalid ticker given. Please try again with a valid ticker.";
      }
      LocalDate currentDate = LocalDate.now();
      StockAnalysisInterface stockAnalysisInterface =
          modelInterface.stockAnalysis(ticker);
      s = "The " + days + "-day moving average for the stock " + ticker + " is "
          + stockAnalysisInterface.getMovingAverage(currentDate, days);
    } catch (Exception e) {
      return "error: " + e.getMessage();
    }
    return s;
  }


  @Override
  public String checkStockGainsOrLoss(String ticker, int userInput
      , LocalDate startDate, LocalDate endDate) throws IOException {
    String s;
    if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
      return "Invalid ticker given. Please try again with a valid ticker.";
    }
    StockAnalysisInterface stockAnalysisInterface = modelInterface.stockAnalysis(ticker);
    try {
      s = stockGainsOrLossUserInputWorkflow(userInput, ticker, stockAnalysisInterface, startDate,
          endDate);
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }


  private String stockGainsOrLossUserInputWorkflow(int userInput, String ticker,
      StockAnalysisInterface stockAnalysisInterface, LocalDate startDate, LocalDate endDate)
      throws IOException {
    String s;
    if (userInput == 1) {
      s = "The gains/loss for the stock " + ticker + " on " + startDate + " is "
          + stockAnalysisInterface.getStockTrendOnDate(startDate);
    } else if (userInput == 2) {
      s = "The gains/loss for the stock " + ticker + " between " + startDate + " and "
          + endDate + " is " + stockAnalysisInterface.getStockTrend(startDate,
          endDate);
    } else {
      return "Invalid input given. Please try again.";
    }
    return s;
  }

  @Override
  public String computeStockPerformance(String ticker
      , LocalDate startDate, LocalDate endDate) throws IOException {
    String s;
    if (!portfolioReadWriteInterface.isValidTicker(ticker)) {
      return "Invalid ticker given. Please try again with a valid ticker.";
    }
    StockAnalysisInterface stockAnalysisInterface =
        modelInterface.stockAnalysis(ticker);
    try {
      s = "The performance analysis for the stock "
          + ticker + " between " + startDate + " and "
          + endDate + " is \n"
          + stockAnalysisInterface.getStockPerformance(startDate, endDate);
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public String invest(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate date) {
    String s;
    try {
      portfolioManagerInterface.invest(portfolioName, strategyName, amount, stockRatio, date);
      s = "Investing is done for portfolio " + portfolioName;
    } catch (Exception e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public HashMap<String, ArrayList<String>> getTickersForInvest(String portfolioName) {
    return portfolioManagerInterface.getPortfolioComposition(portfolioName, LocalDate.now());
  }

  @Override
  public String portfolioCheckForInvesting(String portfolioName) {
    if (portfolioManagerInterface.portfolioExists(portfolioName)) {
      if (portfolioManagerInterface.isPortfolioMutable(portfolioName)) {
        return "mutable";
      } else {
        return "Invest can only be done for flexible portfolios.";
      }
    } else {
      return "This portfolio does not exist.";
    }
  }

  @Override
  public String investPeriodically(String portfolioName, String strategyName,
      double amount, HashMap<String, Double> stockRatio,
      LocalDate startDate, LocalDate endDate, long intervalDays) {
    String s;
    try {
      portfolioManagerInterface.investPeriodically(portfolioName, strategyName, amount
          , stockRatio, startDate, endDate, intervalDays);
      s = "Investing periodically is done for the new portfolio " + portfolioName;
      portfolioReadWriteInterface.saveStrategy(portfolioName, strategyName,
              amount, stockRatio, startDate, endDate, intervalDays);
    } catch (Exception e) {
      s = e.getMessage() + "\n Taking you back to main menu";
    }
    return s;
  }

  @Override
  public boolean isValidTicker(String ticker) {
    return portfolioReadWriteInterface.isValidTicker(ticker);
  }

  private String importPortfolioWithStrategy(String filePath) {
    HashMap<String, Object> params = null;
    try {
      params = portfolioReadWriteInterface.importStrategyFromCSV(filePath);
    } catch (IOException e) {
      return e.getMessage() + "\n Taking you back to main menu";
    }
    String portfolioName = (String) params.get("portfolioName");
    String strategyName = (String) params.get("strategyName");
    double amount = (Double) params.get("amount");
    HashMap<String, Double> stockRatio = (HashMap<String, Double>) params.get("stockRatio");
    LocalDate startDate = (LocalDate) params.get("startDate");
    LocalDate endDate = params.get("endDate") != null ? (LocalDate) params.get("endDate") : null;
    long intervalDays = (Long) params.get("intervalDays");
    String message = investPeriodically(portfolioName, strategyName, amount, stockRatio, startDate,
            endDate, intervalDays);
    return message;
  }

}
