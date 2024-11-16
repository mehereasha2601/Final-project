import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.GuiController;
import controller.GuiControllerInterface;
import controller.PortfolioReadWrite;
import controller.PortfolioReadWriteInterface;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import model.PortfolioManagerInterface;
import org.junit.Before;
import org.junit.Test;
import view.GuiView;
import view.GuiViewInterface;

/**
 * This class provides tests for GUIController. It validates the GUIController functionality.
 */
public class GUIControllerTests {

  private MockModelInterface modelInterface;
  private GuiViewInterface viewInterface;

  private MockStockAnalysisInterface stockAnalysisInterface;

  private PortfolioManagerInterface portfolioManagerInterface;

  private PortfolioReadWriteInterface portfolioReadWriteInterface;

  private GuiControllerInterface guiControllerInterface;

  @Before
  public void setUp() throws IOException {
    this.modelInterface = new MockModelInterface(new StringBuilder(""));
    this.viewInterface = new GuiView();
    MockModelInterface mockModelInterface = new MockModelInterface(new StringBuilder(""));
    this.portfolioManagerInterface = mockModelInterface.createPortfolioManager(
        "mehereasha2601@gmail.com");
    this.stockAnalysisInterface = mockModelInterface.stockAnalysis("AAPL");
    this.guiControllerInterface = new GuiController(modelInterface, viewInterface);
    this.portfolioReadWriteInterface = new PortfolioReadWrite();
  }

  @Test
  public void testNewUserWorkflow_ExistingUser() {
    ArrayList<String> userData = new ArrayList<>();
    userData.add("First");
    userData.add("Last");
    userData.add("mehereasha2601@gmail.com");

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "getExistingUserData method called with input mehereasha2601@gmail.com.";

    String result = guiControllerInterface.newUserWorkflow(userData);
    assertEquals("User already exists logging you in.", result);
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testExistingUserWorkflow_UserExists() {
    String emailID = "mehereasha2601@gmail.com";
    String expected = "Hey mehereasha2601@gmail.com! Logging you in";
    assertEquals(expected, guiControllerInterface.existingUserWorkflow(emailID));
    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "getExistingUserData method called with input mehereasha2601@gmail.com.";
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testCreatePortfolioWorkflow_PortfolioExists() {
    String portfolioName = "immutable";

    assertFalse(portfolioManagerInterface.portfolioExists(portfolioName));
    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called " +
        "Create portfolio called with input" + portfolioName +
        "getExistingUserData method called with input mehereasha2601@gmail.com" +
        "Cannot create portfolio because this portfolio already exists. ";
    String result = guiControllerInterface.createPortfolioWorkflow(portfolioName);
    assertEquals(expectedLog, result);
  }

  @Test
  public void testCreateFlexiblePortfolioWorkflow() {
    String portfolioName = "NewFlexible";
    guiControllerInterface.createFlexiblePortfolioWorkflow(portfolioName);

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "createFlexiblePortfolio called with input" + portfolioName;
    String result = guiControllerInterface.createPortfolioWorkflow(portfolioName);
    assertEquals(expectedLog, result);
  }

  @Test
  public void testImmutablePortfolio() {
    String portfolioName = "Immutable";
    HashMap<String, ArrayList<String>> stockData = new HashMap<>();
    stockData.put("AAPL", new ArrayList<>(Arrays.asList("100", "2021-01-01")));

    String expected = "An immutable portfolio with name Immutable"
        + " has been created and saved. Taking you back to main menu.";
    try {
      assertEquals(expected, guiControllerInterface.immutablePortfolio(portfolioName, stockData));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "savePortfolio called with input" + portfolioName;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testViewPortfolioComposition() {
    String portfolioName = "flexible";
    LocalDate date = LocalDate.parse("2021-01-01");

    portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
    HashMap<String, ArrayList<String>> stockData = new HashMap<>();
    stockData.put("AAPL", new ArrayList<>(Arrays.asList("100", "2021-01-01")));

    String result = guiControllerInterface.viewPortfolioCompositionWorkflow(portfolioName, date);
    assertTrue(
        result.contains("Displaying the composition of the portfolio MyPortfolio " + stockData));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input " + portfolioName
        + "getPortfolioComposition called with input " + portfolioName;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testTotalValueOfPortfolio() {
    String portfolioName = "flexible";
    LocalDate date = LocalDate.parse("2021-01-01");

    portfolioManagerInterface.createFlexiblePortfolio(portfolioName);

    String result = null;
    try {
      result = guiControllerInterface.totalValueOfPortfolioWorkflow(portfolioName, date);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The Total value of portfolio is 1432.06 dollars"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input " + portfolioName
        + "getTotalPortfolioValue called with inputs " + portfolioName + " and " + date;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testExportPortfolio() {
    String portfolioName = "flexible";
    String directoryPath = "/exports/";
    LocalDate date = LocalDate.parse("2021-01-01");

    String result = guiControllerInterface.exportPortfolioWorkflow(portfolioName, directoryPath,
        date);
    assertTrue(result.contains("Your portfolios have been exported to"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input " + portfolioName
        + "exportPortfolioToCSVFile called with inputs " + portfolioName + " and " + directoryPath;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testImportPortfolio() {
    String filePath = "/imports/myportfolio.csv";
    int option = 1;

    String result = null;
    try {
      result = guiControllerInterface.importPortfolioWorkflow(filePath, option);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("Your portfolio has been successfully imported"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input myportfolio"
        + "exportPortfolioToCSVFile called with input" + filePath;
    assertEquals(expectedLog, modelInterface.log.toString());
  }


  @Test
  public void testInvest() {
    String portfolioName = "flexible";
    String strategyName = "dollarCostAveraging";
    double amount = 1000.0;
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 1.0);
    LocalDate date = LocalDate.now();

    String result = guiControllerInterface.invest(portfolioName, strategyName, amount, stockRatio,
        date);
    assertTrue(result.contains("Investing is done for portfolio InvestPortfolio"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input flexible"
        + "invest called with inputs" + amount + stockRatio + date;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testBuyStocksNonZeroPortfoliosWorkflow() {
    String portfolioName = "flexible";
    String ticker = "AAPL";
    int noOfShares = 100;
    LocalDate date = LocalDate.now();
    String result = guiControllerInterface.buyOrSellStocksNonZeroPortfoliosWorkflow(portfolioName,
        ticker, noOfShares, date, "buy");
    assertTrue(
        result.contains("The stock with ticker AAPL has been added to portfolio TestPortfolio"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input flexible"
        + "buy called with inputs" + ticker + noOfShares + date;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testSellStocksNonZeroPortfoliosWorkflow() {
    String portfolioName = "TestPortfolio";
    String ticker = "AAPL";
    int noOfShares = 50;
    LocalDate date = LocalDate.now();

    String result = guiControllerInterface.buyOrSellStocksNonZeroPortfoliosWorkflow(portfolioName,
        ticker, noOfShares, date, "sell");
    assertTrue(
        result.contains("The stock with ticker AAPL has been sold from portfolio TestPortfolio"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input flexible"
        + "sell called with inputs" + ticker + noOfShares + date;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testInvestPeriodically() {
    String portfolioName = "PeriodicPortfolio";
    String strategyName = "dollarCostAveraging";
    double amount = 2000.0;
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusMonths(6);
    long intervalDays = 30;

    String result = guiControllerInterface.investPeriodically(portfolioName, strategyName, amount,
        stockRatio, startDate, endDate, intervalDays);
    assertTrue(
        result.contains("Investing periodically is done for the new portfolio PeriodicPortfolio"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input PeriodicPortfolio"
        + "invest periodically called with inputs" + amount + stockRatio + startDate + endDate;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testComputeStockPerformance() {
    String ticker = "AAPL";
    LocalDate startDate = LocalDate.of(2023, 1, 3);
    LocalDate endDate = LocalDate.of(2023, 13, 13);

    String result = null;
    try {
      result = guiControllerInterface.computeStockPerformance(ticker, startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The performance analysis for the stock AAPL between "
        + startDate + " and " + endDate + " is "
        + "Jan 03 2023: *******************************************\n"
        + "Jan 04 2023: ******************************************\n"
        + "Jan 05 2023: ******************************************\n"
        + "Jan 06 2023: ******************************************\n"
        + "Jan 09 2023: *******************************************\n"
        + "Jan 10 2023: *******************************************\n"
        + "Jan 11 2023: *******************************************\n"
        + "Jan 12 2023: ********************************************\n"
        + "Jan 13 2023: ********************************************\n"
        + "Scale: * = 3, representing the value in dollars.\n"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "stock performance called with inputs" + ticker + startDate + endDate;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testComputeMovingAverage() {
    String ticker = "AAPL";
    int days = 50;

    String result = null;
    try {
      result = guiControllerInterface.computeMovingAverage(ticker, days);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The 50-day moving average for the stock AAPL is 223.84 dollars."));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "moving average called with inputs" + ticker + days;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testComputeCrossoverDays() {
    String ticker = "AAPL";
    ArrayList<LocalDate> dates = new ArrayList<>();
    dates.add(LocalDate.of(2021, 1, 1));
    dates.add(LocalDate.of(2021, 12, 31));

    String result = null;
    try {
      result = guiControllerInterface.computeCrossoverDays(ticker, dates);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The crossover days for the stock AAPL between"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "crossover days called with inputs" + ticker + dates;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testComputeMovingCrossoverDays() {
    String ticker = "AAPL";
    ArrayList<LocalDate> dates = new ArrayList<>();
    dates.add(LocalDate.of(2021, 1, 1));
    dates.add(LocalDate.of(2021, 12, 31));
    int x = 50;
    int y = 200;

    String result = null;
    try {
      result = guiControllerInterface.computeMovingCrossoverDays(ticker, dates, x, y);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The moving crossover days for the stock AAPL between"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "crossover days called with inputs" + ticker + dates + x + y;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testComputeStockGainsOrLoss() {
    String ticker = "AAPL";
    LocalDate startDate = LocalDate.of(2021, 1, 1);
    LocalDate endDate = LocalDate.of(2021, 12, 31);

    String result = null;
    try {
      result = guiControllerInterface.checkStockGainsOrLoss(ticker, 2, startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(result.contains("The gains/loss for the stock AAPL between"));

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "gain or loss method called with inputs" + ticker + startDate + endDate;
    assertEquals(expectedLog, modelInterface.log.toString());
  }

  @Test
  public void testPortfolioCostBasis() {
    String portfolioName = "flexible";
    LocalDate date = LocalDate.parse("2021-01-01");

    portfolioManagerInterface.createFlexiblePortfolio(portfolioName);
    String expectedMessage =
        "The cost basis for the portfolio " + portfolioName + " on " + date + " is $5000 dollars.";

    String result = guiControllerInterface.portfolioCostBasisWorkflow(portfolioName, date);
    assertEquals(expectedMessage, result);

    String expectedLog = "Create User Manager called. " +
        "isExistingUser method called with input mehereasha2601@gmail.com. " +
        "Create portfolio called" +
        "getExistingUserData method called with input mehereasha2601@gmail.com."
        + "portfolioExists called with input " + portfolioName
        + "cost basis method called with inputs" + portfolioName + date;
    assertEquals(expectedLog, modelInterface.log.toString());

  }


}
