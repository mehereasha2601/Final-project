import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import model.Portfolio;
import model.User;
import org.junit.Test;


/**
 * This class all has tests to validate the functionality of the Portfolio class. Thoroughly test
 * the functionality of the class against valid and invalid inputs.
 */
public class TestPortfolio {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");

  private User testRandomUser = new User("Random", "User",
      "random@gmail.com");

  /**
   * This method checks if a Portfolio is created properly using the constructor and validates the
   * data in the object.
   */
  @Test
  public void testCreatePortfolioConstructor() {
    Portfolio portfolio = createTestPortfolio(testUser);
    HashMap<String, ArrayList<String>> stockData = portfolio.getStockData();
    assertEquals("testPortfolio1", portfolio.getPortfolioName());
    for (String ticker : stockData.keySet()) {
      assertTrue(portfolio.getStockData().containsKey(ticker));
      assertEquals(stockData.get(ticker).get(0), portfolio.getStockData().get(ticker).get(0));
      assertEquals(stockData.get(ticker).get(1), portfolio.getStockData().get(ticker).get(1));
      assertEquals(stockData.get(ticker).get(2), portfolio.getStockData().get(ticker).get(2));
    }
  }

  /**
   * This method validates getStockData() method in Portfolio class.
   */
  @Test
  public void testGetStockData() {
    String[] tickers = {"AAPL", "GOOGL"};
    HashMap<String, ArrayList<String>> stockDataold = new HashMap<String, ArrayList<String>>();
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    stockvalue1.add(0, "Apple Inc");
    stockvalue1.add(1, "3");
    stockvalue1.add(2, dateString);
    stockDataold.put(tickers[0], stockvalue1);
    stockvalue2.add(0, "Alphabet Inc - Class A");
    stockvalue2.add(1, "10");
    stockvalue2.add(2, dateString);
    stockDataold.put(tickers[1], stockvalue2);
    Portfolio portfolio = createTestPortfolio(testUser);
    HashMap<String, ArrayList<String>> stockDataNew = portfolio.getStockData();
    for (String ticker : stockDataold.keySet()) {
      assertTrue(stockDataNew.containsKey(ticker));
      for (String s : stockDataold.get(ticker)) {
        assertTrue(stockDataNew.get(ticker).contains(s));
      }
    }
  }


  /**
   * This method validates getComposition() method in Portfolio class when a specific portfolio is
   * given.
   */
  @Test
  public void testGetComposition() {
    String[] company = {"AAPL", "GOOGL"};
    String[] quantity = {"3", "10"};
    LocalDate date = LocalDate.parse("2023-03-09");
    Portfolio portfolio = createTestPortfolio(testUser);
    HashMap<String, String> composition = portfolio.getComposition(date);
    for (int i = 0; i < 2; i++) {
      assertEquals(quantity[i], composition.get(company[i]));
    }
    System.out.println(composition);
  }


  /**
   * This method validates totalValue() method in Portfolio class when data for a given date
   * exists.
   */
  @Test
  public void testTotalValue() {
    LocalDate date = LocalDate.parse("2023-03-09");
    Portfolio portfolio = createTestPortfolio(testUser);
    try {
      assertEquals(1401.17, portfolio.totalValue(date), 0.01);
    } catch (Exception e) {
      e.getMessage();
    }
  }


  /**
   * This method validates totalValue() method in Portfolio class when data for a given date does
   * not exist.
   */
  @Test
  public void testTotalValueNoData() {
    LocalDate date = LocalDate.parse("2024-04-30");
    Portfolio portfolio = createTestPortfolio(testUser);
    try {
      assertEquals(0.0, portfolio.totalValue(date), 0.01);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  /**
   * This method validates getClosingValue() method in Portfolio class when data for a given date
   * exists.
   */
  @Test
  public void testGetClosingValue() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2023-03-06");
    try {
      float closingValue = portfolio.getClosingValue("GOOGL", date);
      assertEquals(94.01499938964844, closingValue, 0.001);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  /**
   * This method validates getClosingValue() method in Portfolio class when data does not exist for
   * a given date.
   */
  @Test
  public void testGetClosingValueNoData() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2024-04-30");
    try {
      float closingValue = portfolio.getClosingValue("GOOGL", date);
      assertEquals(0.0, closingValue, 0.001);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  /**
   * This method validates getClosingValue() method in Portfolio class when invalid ticker is passed
   * a given date.
   */
  @Test
  public void testGetClosingValueNoDataInvalidTicker() {
    Portfolio portfolio = createTestPortfolio(testUser);
    String ticker = "AJNKDDFSEDG";
    LocalDate date = LocalDate.parse("2024-04-30");
    float closingValue = 0;
    try {
      closingValue = portfolio.getClosingValue(ticker, date);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(0.0, closingValue, 0.001);

  }

  /**
   * This method validates getHistoricalData() method in Portfolio class when data for a given date
   * exists.
   */
  @Test
  public void testGetHistoricalData() throws IOException {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2023-03-10");
    portfolio.getHistoricalData("AAPL");
    assertEquals(150.21, portfolio.getClosingValue("AAPL", date), 0.01);
    assertEquals(1372.32, portfolio.totalValue(date), 0.01);
  }


  /**
   * This method validates getHistoricalData() method in Portfolio class when an invalid ticker is
   * passed as input.
   */
  @Test
  public void testGetHistoricalDataInvalidTicker() throws IOException {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2023-03-10");
    portfolio.getHistoricalData("JBHADB");
    assertEquals(0.0, portfolio.getClosingValue("JBHADB", date), 0.01);
  }


  /**
   * This method validates getHistoricalData() method in Portfolio class when data does not exist
   * for a given date.
   */
  @Test
  public void testGetHistoricalDataNoData() throws IOException {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2024-03-09");
    portfolio.getHistoricalData("MSFT");
    assertEquals(0.0, portfolio.getClosingValue("MSFT", date), 0.01);
    assertEquals(0.0, portfolio.totalValue(date), 0.01);
  }


  /**
   * This method validates updateHistoricalData() method in Portfolio class.
   */
  @Test
  public void testUpdateHistoricalData() throws IOException {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2024-03-12");
    portfolio.getHistoricalData("MSFT");
    assertEquals(407.6199951171875, portfolio.getClosingValue("MSFT", date),
        0.01);
    portfolio.updateHistoricalData("MSFT", date);
    assertEquals(407.619, portfolio.getClosingValue("MSFT", date), 0.01);
  }


  @Test
  public void testUpdateHistoricalDataInvalidTicker() throws IOException {
    String ticker = "Aaid";
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate date = LocalDate.parse("2024-03-12");
    portfolio.getHistoricalData(ticker);
    assertEquals(0.0, portfolio.getClosingValue(ticker, date), 0.01);
    portfolio.updateHistoricalData(ticker, date);
    assertEquals(0.0, portfolio.getClosingValue(ticker, date), 0.01);
  }


  /**
   * This helper method creates and returns a Portfolio with test data.
   */
  private Portfolio createTestPortfolio(User user) {
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    HashMap<String, ArrayList<String>> stockData = new HashMap<String, ArrayList<String>>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    stockvalue1.add(0, "Apple Inc");
    stockvalue1.add(1, "3");
    stockvalue1.add(2, dateString);
    stockData.put("AAPL", stockvalue1);
    stockvalue2.add(0, "Alphabet Inc - Class A");
    stockvalue2.add(1, "10");
    stockvalue2.add(2, dateString);
    stockData.put("GOOGL", stockvalue2);
    Portfolio portfolio = new Portfolio(user, "testPortfolio1", stockData);
    return portfolio;
  }

  private int linesInChart(String str) {
    if (str == null || str.isEmpty()) {
      return 0;
    }
    String[] lines = str.split("\\r?\\n");
    int chartLines = lines.length - 1;
    return chartLines;
  }

  private int maxStarsInChart(String str) {
    if (str == null || str.isEmpty()) {
      return 0;
    }
    String[] lines = str.split("\\r?\\n");
    int maxStars = 0;
    for (String line : lines) {
      int count = 0;
      for (int i = 0; i < line.length(); i++) {
        if (line.charAt(i) == '*') {
          count++;
        }
      }
      if (count > maxStars) {
        maxStars = count;
      }
    }
    return maxStars;
  }

  @Test
  public void testPortfolioPerformanceIntervalLessThanFiveDays() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-03-12");
    String performance = portfolio.getPortfolioPerformance(
        portfolio.getPortfolioName(), startDate, endDate);
    int intervals = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(intervals < 5);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceWeekInterval() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-03-17");
    String performance = portfolio.getPortfolioPerformance(
        portfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceMonthInterval() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-04-12");
    String performance = portfolio.getPortfolioPerformance(
        portfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceYearInterval() {
    Portfolio portfolio = createTestPortfolio(testUser);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2024-04-12");
    String performance = portfolio.getPortfolioPerformance(
        portfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

}

