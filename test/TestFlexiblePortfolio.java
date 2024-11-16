import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import model.FlexiblePortfolio;
import model.User;
import org.junit.Test;

/**
 * This class all has tests to validate the functionality of the Portfolio class. Thoroughly tests
 * the functionality of the class against valid and invalid inputs.
 */
public class TestFlexiblePortfolio {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");
  private String portfolioName = "TestPortfolio1";

  @Test
  public void testCreatePortfolioConstructor() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    assertEquals(portfolioName, flexiblePortfolio.getPortfolioName());
  }

  @Test
  public void testGetComposition() {
    String[] ticker = {"AAPL", "GOOGL"};
    String[] quantity = {"3.0", "10.0"};
    LocalDate date = LocalDate.parse("2023-03-09");
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    for (int i = 0; i < 2; i++) {
      flexiblePortfolio.buy(ticker[i], Double.parseDouble(quantity[i]), date);
    }
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    for (int i = 0; i < 2; i++) {
      assertTrue(composition.containsKey(ticker[i]));
      assertEquals(quantity[i], composition.get(ticker[i]));
    }
  }

  @Test
  public void testBuy() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    assertTrue(composition.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition.get(ticker)), 0.01);
  }

  @Test
  public void testSell() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double buyShares = 20;
    LocalDate date = LocalDate.parse("2023-03-09");
    double sellShares = 10;
    flexiblePortfolio.buy(ticker, buyShares, date);
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    assertTrue(composition.containsKey(ticker));
    assertEquals(buyShares, Double.parseDouble(composition.get(ticker)), 0.01);
    flexiblePortfolio.sell(ticker, sellShares, date);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(date);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(buyShares - sellShares, Double.parseDouble(composition2.get(ticker))
        , 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuyInvalidDate() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 10;
    LocalDate date = LocalDate.parse("2033-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellInvalidDate() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double buyShares = 20;
    LocalDate date = LocalDate.parse("2023-03-09");
    double sellShares = 10;
    flexiblePortfolio.buy(ticker, buyShares, date);
    LocalDate sellDate = LocalDate.parse("2033-03-09");
    flexiblePortfolio.sell(ticker, sellShares, sellDate);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testBuyInvalidInputShares() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = -10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellInvalidInputShares() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double buyShares = 20;
    LocalDate date = LocalDate.parse("2023-03-09");
    double sellShares = -10;
    flexiblePortfolio.buy(ticker, buyShares, date);
    flexiblePortfolio.sell(ticker, sellShares, date);
  }

  @Test
  public void testCompositionBetweenTwoBuys() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    assertTrue(composition.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition.get(ticker)), 0.01);
    LocalDate dateInBetween = LocalDate.parse("2023-04-09");
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(dateInBetween);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition2.get(ticker)), 0.01);
    double newShares = 10;
    LocalDate newDate = LocalDate.parse("2023-05-09");
    flexiblePortfolio.buy(ticker, newShares, newDate);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(newDate);
    assertTrue(composition3.containsKey(ticker));
    assertEquals(shares + newShares, Double.parseDouble(composition3.get(ticker)),
        0.01);
  }

  @Test
  public void testCompositionBetweenBuySell() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 20;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    assertTrue(composition.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition.get(ticker)), 0.01);
    LocalDate dateInBetween = LocalDate.parse("2023-04-09");
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(dateInBetween);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition2.get(ticker)), 0.01);
    double newShares = 10;
    LocalDate newDate = LocalDate.parse("2023-05-09");
    flexiblePortfolio.sell(ticker, newShares, newDate);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(newDate);
    assertTrue(composition3.containsKey(ticker));
    assertEquals(shares - newShares, Double.parseDouble(composition3.get(ticker)),
        0.01);
  }

  @Test
  public void testCompositionBetweenTwoSells() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 20;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
    HashMap<String, String> composition = flexiblePortfolio.getComposition(date);
    assertTrue(composition.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition.get(ticker)), 0.01);
    LocalDate dateInBetween = LocalDate.parse("2023-04-09");
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(dateInBetween);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(shares, Double.parseDouble(composition2.get(ticker)), 0.01);
    double newShares = 10;
    LocalDate newDate = LocalDate.parse("2023-05-09");
    flexiblePortfolio.sell(ticker, newShares, newDate);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(newDate);
    assertTrue(composition3.containsKey(ticker));
    assertEquals(shares - newShares, Double.parseDouble(composition3.get(ticker)),
        0.01);
    LocalDate afterSellDate = LocalDate.parse("2023-06-09");
    HashMap<String, String> composition4 = flexiblePortfolio.getComposition(afterSellDate);
    assertTrue(composition4.containsKey(ticker));
    assertEquals(shares - newShares, Double.parseDouble(composition4.get(ticker)),
        0.01);
  }

  @Test
  public void testUpdateCompositionOnEarlierBuy() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(date);
    assertTrue(composition1.containsKey(ticker));
    assertEquals(firstBuyShares, Double.parseDouble(composition1.get(ticker)), 0.01);
    double secondBuyShares = 20;
    LocalDate secondBuyDate = LocalDate.parse("2023-02-09");
    flexiblePortfolio.buy(ticker, secondBuyShares, secondBuyDate);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(secondBuyDate);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(secondBuyShares, Double.parseDouble(composition2.get(ticker)), 0.01);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(date);
    assertTrue(composition3.containsKey(ticker));
    assertEquals(firstBuyShares + secondBuyShares,
        Double.parseDouble(composition3.get(ticker)), 0.01);
  }

  @Test
  public void testUpdateCompositionLaterDatePostSell() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(date);
    assertTrue(composition1.containsKey(ticker));
    assertEquals(firstBuyShares, Double.parseDouble(composition1.get(ticker)), 0.01);
    double secondBuyShares = 20;
    LocalDate secondBuyDate = LocalDate.parse("2023-02-09");
    flexiblePortfolio.buy(ticker, secondBuyShares, secondBuyDate);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(secondBuyDate);
    assertTrue(composition2.containsKey(ticker));
    assertEquals(secondBuyShares, Double.parseDouble(composition2.get(ticker)), 0.01);
    double firstSellShares = 10;
    flexiblePortfolio.sell(ticker, firstSellShares, secondBuyDate);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(secondBuyDate);
    assertTrue(composition3.containsKey(ticker));
    assertEquals(10,
        Double.parseDouble(composition3.get(ticker)), 0.01);
    HashMap<String, String> composition4 = flexiblePortfolio.getComposition(date);
    assertTrue(composition4.containsKey(ticker));
    assertEquals(20, Double.parseDouble(composition4.get(ticker)), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellWithoutBuyingStock() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstSellShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.sell(ticker, firstSellShares, date);
  }

  @Test
  public void testUpdateCompositionSameDayMultipleTickerBuys() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker1 = "AAPL";
    double shares1 = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker1, shares1, date);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(date);
    assertTrue(composition1.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition1.get(ticker1)), 0.01);
    String ticker2 = "GOOGL";
    double shares2 = 10;
    flexiblePortfolio.buy(ticker2, shares2, date);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(date);
    assertTrue(composition2.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition2.get(ticker1)), 0.01);
    assertTrue(composition2.containsKey(ticker2));
    assertEquals(shares2, Double.parseDouble(composition2.get(ticker2)), 0.01);
  }

  @Test
  public void testUpdateCompositionLaterDateMultipleTickerBuys() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker1 = "AAPL";
    double shares1 = 10;
    LocalDate date1 = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker1, shares1, date1);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(date1);
    assertTrue(composition1.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition1.get(ticker1)), 0.01);
    String ticker2 = "GOOGL";
    double shares2 = 10;
    LocalDate date2 = LocalDate.parse("2023-04-09");
    flexiblePortfolio.buy(ticker2, shares2, date2);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(date2);
    assertTrue(composition2.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition2.get(ticker1)), 0.01);
    assertTrue(composition2.containsKey(ticker2));
    assertEquals(shares2, Double.parseDouble(composition2.get(ticker2)), 0.01);
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(date1);
    assertTrue(composition3.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition3.get(ticker1)), 0.01);
    assertFalse(composition3.containsKey(ticker2));
  }

  @Test
  public void testUpdateCompositionEarlierDateMultipleTickerBuys() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker1 = "AAPL";
    double shares1 = 10;
    LocalDate date1 = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker1, shares1, date1);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(date1);
    assertTrue(composition1.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition1.get(ticker1)), 0.01);
    String ticker2 = "GOOGL";
    double shares2 = 20;
    LocalDate date2 = LocalDate.parse("2023-02-09");
    flexiblePortfolio.buy(ticker2, shares2, date2);
    HashMap<String, String> composition2 = flexiblePortfolio.getComposition(date2);
    assertTrue(composition2.containsKey(ticker2));
    assertEquals(shares2, Double.parseDouble(composition2.get(ticker2)), 0.01);
    assertFalse(composition2.containsKey(ticker1));
    HashMap<String, String> composition3 = flexiblePortfolio.getComposition(date1);
    assertTrue(composition3.containsKey(ticker1));
    assertEquals(shares1, Double.parseDouble(composition3.get(ticker1)), 0.01);
    assertTrue(composition3.containsKey(ticker2));
    assertEquals(shares2, Double.parseDouble(composition3.get(ticker2)), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellBeforeBuyDate() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 10;
    LocalDate buyDate = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, buyDate);
    LocalDate sellDate = LocalDate.parse("2023-02-09");
    flexiblePortfolio.sell(ticker, shares, sellDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExcessSellOnEarlierBuyDate() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    double secondBuyShares = 20;
    LocalDate secondBuyDate = LocalDate.parse("2023-04-09");
    flexiblePortfolio.buy(ticker, secondBuyShares, secondBuyDate);
    HashMap<String, String> composition1 = flexiblePortfolio.getComposition(secondBuyDate);
    assertTrue(composition1.containsKey(ticker));
    assertEquals(firstBuyShares + secondBuyShares,
        Double.parseDouble(composition1.get(ticker)), 0.01);
    double firstSellShares = 30;
    flexiblePortfolio.sell(ticker, firstSellShares, secondBuyDate);
    HashMap<String, String> composition4 = flexiblePortfolio.getComposition(date);
    assertTrue(composition4.containsKey(ticker));
    assertEquals(firstBuyShares, Double.parseDouble(composition4.get(ticker)), 0.01);
    double secondSellShares = 10;
    flexiblePortfolio.sell(ticker, secondSellShares, date);
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
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-03-12");
    String performance = flexiblePortfolio.getPortfolioPerformance(
        flexiblePortfolio.getPortfolioName(), startDate, endDate);
    int intervals = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(intervals < 5);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceWeekInterval() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-03-17");
    String performance = flexiblePortfolio.getPortfolioPerformance(
        flexiblePortfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceMonthInterval() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2023-04-12");
    String performance = flexiblePortfolio.getPortfolioPerformance(
        flexiblePortfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

  @Test
  public void testPortfolioPerformanceYearInterval() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    LocalDate startDate = LocalDate.parse("2023-03-09");
    LocalDate endDate = LocalDate.parse("2024-04-12");
    String performance = flexiblePortfolio.getPortfolioPerformance(
        flexiblePortfolio.getPortfolioName(), startDate, endDate);
    int lines = linesInChart(performance);
    int stars = maxStarsInChart(performance);
    assertTrue(lines > 5 && lines < 31);
    assertTrue(stars < 50);
  }

  @Test
  public void testTotalValue() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    try {
      float totalValue = flexiblePortfolio.totalValue(date);
      assertEquals(1535.59, totalValue, 0.01);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTotalValueBeforePurchaseDate() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    LocalDate pastDate = LocalDate.parse("2022-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    try {
      float totalValue = flexiblePortfolio.totalValue(pastDate);
      assertEquals(0.00, totalValue, 0.01);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCostBasis() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    float costBasis = flexiblePortfolio.costBasis(portfolioName, date);
    assertEquals(1535.59, costBasis, 0.01);
  }

  @Test
  public void testCostBasisBeforeBuy() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double firstBuyShares = 10;
    LocalDate date = LocalDate.parse("2023-03-09");
    LocalDate pastDate = LocalDate.parse("2022-03-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date);
    float costBasis = flexiblePortfolio.costBasis(portfolioName, pastDate);
    assertEquals(0.0, costBasis, 0.01);
  }

  @Test
  public void testCostBasisAfterSell() {
    FlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio(testUser, portfolioName);
    String ticker = "AAPL";
    double shares = 30;
    LocalDate date = LocalDate.parse("2023-03-09");
    flexiblePortfolio.buy(ticker, shares, date);
    double firstBuyShares = 10;
    LocalDate date1 = LocalDate.parse("2023-04-09");
    flexiblePortfolio.buy(ticker, firstBuyShares, date1);
    double newShares = 10;
    flexiblePortfolio.sell(ticker, newShares, date1);
    float costBasis = flexiblePortfolio.costBasis(portfolioName, date1);
    assertEquals(4606.77, costBasis, 0.01);
  }


}
