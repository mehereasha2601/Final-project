import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import model.Portfolio;
import model.PortfolioManager;
import model.User;
import org.junit.Test;

/**
 * This class all has tests to validate the functionality of the PortfolioManager class.
 */
public class TestPortfolioManager {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");


  @Test
  public void testPortfolioManagerConstructor() {
    PortfolioManager portfolioManager = new PortfolioManager("johndoe@gmail.com");
    assertTrue(portfolioManager.portfolioExists("p10"));
    assertTrue(portfolioManager.portfolioExists("testPortfolio1"));
    assertTrue(portfolioManager.portfolioExists("testPortfolio2"));
  }


  @Test
  public void testGetTotalPortfolioValue() {
    LocalDate date = LocalDate.parse("2023-03-09");
    PortfolioManager portfolioManager = createPortfolioManager();
    try {
      String totalValue = portfolioManager.getTotalPortfolioValue("testPortfolio1",
          date);
      assertEquals(1374.96, Integer.parseInt(totalValue), 0.01);
    } catch (Exception e) {
      e.getMessage();
    }
  }


  @Test
  public void testGetPortfolioComposition() {
    String[] tickers = {"AAPL", "GOOGL"};
    HashMap<String, ArrayList<String>> stockDataold = new HashMap<String, ArrayList<String>>();
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    stockvalue1.add(0, "3");
    stockDataold.put(tickers[0], stockvalue1);
    stockvalue2.add(0, "10");
    stockDataold.put(tickers[1], stockvalue2);
    PortfolioManager portfolioManager = createPortfolioManager();
    HashMap<String, ArrayList<String>> stockDataNew = portfolioManager.getPortfolioComposition(
        "testPortfolio1", currentDate);
    for (String ticker : stockDataold.keySet()) {
      assertTrue(stockDataNew.containsKey(ticker));
      for (String s : stockDataold.get(ticker)) {
        assertTrue(stockDataNew.get(ticker).contains(s));
      }
    }
  }


  @Test
  public void testSavePortfolio() throws IOException {
    Portfolio portfolio = createTestPortfolio();
    PortfolioManager portfolioManager = createPortfolioManager();
    portfolioManager.savePortfolio(portfolio.getPortfolioName(), portfolio.getStockData());
    assertTrue(portfolioManager.portfolioExists(portfolio.getPortfolioName()));
  }


  @Test
  public void testPortfolioExists() {
    PortfolioManager portfolioManager = createPortfolioManager();
    assertTrue(!portfolioManager.portfolioExists("test"));
    assertTrue(portfolioManager.portfolioExists("testPortfolio1"));
  }

  /**
   * This helper method creates and returns a PortfolioManager with test data.
   */
  private PortfolioManager createPortfolioManager() {
    PortfolioManager portfolioManager = new PortfolioManager("johndoe@gmail.com");
    return portfolioManager;
  }

  /**
   * This helper method creates and returns a Portfolio with test data.
   */
  private Portfolio createTestPortfolio() {
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
    Portfolio portfolio = new Portfolio(testUser, "testPortfolio1", stockData);
    return portfolio;
  }

}
