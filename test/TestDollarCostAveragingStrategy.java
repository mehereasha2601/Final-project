import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import model.DollarCostAveragingStrategy;
import model.FlexiblePortfolio;
import model.FlexiblePortfolioInterface;
import model.Strategy;
import model.User;
import org.junit.Test;

/**
 * This class all has tests to validate the functionality of the DollarCostAveragingStrategy class.
 * Thoroughly tests the functionality of the class against valid and invalid inputs.
 */
public class TestDollarCostAveragingStrategy {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");
  private String portfolioName = "TestPortfolio1";

  private FlexiblePortfolioInterface getPortfolio() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser, portfolioName);
    portfolio.buy("AAPL", 100, LocalDate.parse("2023-02-12"));
    portfolio.buy("GOOGL", 101, LocalDate.parse("2023-02-12"));
    portfolio.buy("MSFT", 102, LocalDate.parse("2023-02-12"));
    portfolio.buy("NFLX", 103, LocalDate.parse("2023-02-12"));
    portfolio.buy("NVDA", 104, LocalDate.parse("2023-02-12"));
    return portfolio;
  }

  private HashMap<String, Double> stockRatio(double ratio1, double ratio2, double ratio3,
      double ratio4, double ratio5) {
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", ratio1);
    stockRatio.put("GOOGL", ratio2);
    stockRatio.put("MSFT", ratio3);
    stockRatio.put("NFLX", ratio4);
    stockRatio.put("NVDA", ratio5);
    return stockRatio;
  }

  @Test
  public void testInvest() {
    FlexiblePortfolioInterface portfolio = getPortfolio();
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = stockRatio(0.2, 0.2, 0.2, 0.2, 0.2);
    LocalDate date = LocalDate.parse("2024-04-04");
    strategy.invest(1000000, stockRatio, date);
    HashMap<String, String> composition = portfolio.getComposition(date);
    String[] ticker = {"MSFT", "NFLX", "GOOGL", "NVDA", "AAPL"};
    String[] quantity = {"572.599", "418.851", "1403.932", "325.224", "1274.467"};
    for (int i = 0; i < 5; i++) {
      assertTrue(composition.containsKey(ticker[i]));
      assertEquals(quantity[i], composition.get(ticker[i]));
    }
  }

  @Test
  public void testInvestPeriodically() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = stockRatio(0.2, 0.2, 0.2, 0.2, 0.2);
    LocalDate startDate = LocalDate.parse("2020-04-04");
    LocalDate endDate = LocalDate.parse("2024-04-04");
    strategy.investPeriodically(1000000, stockRatio, startDate, endDate, 30);
    HashMap<String, String> composition = portfolio.getComposition(endDate);
    String[] ticker = {"MSFT", "NFLX", "GOOGL", "NVDA", "AAPL"};
    for (int i = 0; i < 5; i++) {
      assertTrue(composition.containsKey(ticker[i]));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestWithNegativeAmount() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate date = LocalDate.now();
    strategy.invest(-100.0, stockRatio, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestWithEmptyStockRatio() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    LocalDate date = LocalDate.now();
    strategy.invest(100.0, stockRatio, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestWithFutureDate() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate futureDate = LocalDate.now().plusDays(10);
    strategy.invest(100.0, stockRatio, futureDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithNegativeInterval() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate startDate = LocalDate.now().minusDays(30);
    LocalDate endDate = LocalDate.now();
    strategy.investPeriodically(100.0, stockRatio, startDate, endDate, -15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithStartDateAfterEndDate() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.minusDays(1);
    strategy.investPeriodically(100.0, stockRatio, startDate, endDate, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithEmptyStockRatio() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    LocalDate startDate = LocalDate.now().minusDays(30);
    LocalDate endDate = LocalDate.now();
    strategy.investPeriodically(100.0, stockRatio, startDate, endDate, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestWithMaxAmount() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio2");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate date = LocalDate.now();
    strategy.invest(Double.MAX_VALUE, stockRatio, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestWithInvalidStockRatios() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio3");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.6);
    stockRatio.put("GOOGL", 0.5);
    LocalDate date = LocalDate.now();
    strategy.invest(100.0, stockRatio, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithInvalidStockRatios() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio5");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.7);
    stockRatio.put("GOOGL", 0.4);
    LocalDate startDate = LocalDate.now().minusDays(30);
    LocalDate endDate = LocalDate.now();
    strategy.investPeriodically(100.0, stockRatio, startDate, endDate, 7);
  }

  @Test
  public void testInvestPeriodicallyOverLeapYear() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio6");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate startDate = LocalDate.of(2024, 2, 28);
    LocalDate endDate = LocalDate.of(2024, 3, 1);
    strategy.investPeriodically(1000000.0, stockRatio, startDate, endDate, 1);
    HashMap<String, String> composition = portfolio.getComposition(endDate);
    String[] ticker = {"GOOGL", "AAPL"};
    for (int i = 0; i < 2; i++) {
      assertTrue(composition.containsKey(ticker[i]));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithLargeInterval() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
        "testPortfolio7");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 1.0);
    LocalDate startDate = LocalDate.now().minusDays(30);
    LocalDate endDate = LocalDate.now();
    strategy.investPeriodically(100000.0, stockRatio, startDate, endDate, 31);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestPeriodicallyWithFutureDates() {
    FlexiblePortfolioInterface portfolio = new FlexiblePortfolio(testUser,
            "testPortfolio1");
    Strategy strategy = new DollarCostAveragingStrategy(portfolio);
    HashMap<String, Double> stockRatio = new HashMap<>();
    stockRatio.put("AAPL", 0.5);
    stockRatio.put("GOOGL", 0.5);
    LocalDate futureDate = LocalDate.now().plusDays(10);
    LocalDate endDate = LocalDate.of(2026, 3, 1);
    strategy.investPeriodically(10000.0, stockRatio, futureDate, endDate, 30);
  }


}
