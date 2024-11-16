import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import model.PortfolioManagerInterface;

/**
 * Mock implementation of PortfolioManager to test controller inputs and outputs.
 */
public class MockPortfolioManagerInterface implements PortfolioManagerInterface {

  public StringBuilder log;

  /**
   * Constructs a mock portfolio manager with a log to record method calls and parameters.
   *
   * @param log StringBuilder to log method calls for verification.
   */
  public MockPortfolioManagerInterface(StringBuilder log) {
    this.log = log;
  }

  /**
   * Checks if a portfolio exists by name in a mocked context.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return true if the portfolio exists, false otherwise.
   */
  @Override
  public boolean portfolioExists(String portfolioName) {
    log.append(" portfolioExists method called with input " + portfolioName + ".");
    return portfolioName.equalsIgnoreCase("technology") ? true : false;
  }

  /**
   * Retrieves a mocked total value of a portfolio for a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the value is queried.
   * @return The mocked total value of the portfolio as a string.
   * @throws IOException If simulating a scenario where retrieving the portfolio value fails.
   */
  @Override
  public String getTotalPortfolioValue(String portfolioName, LocalDate date) throws IOException {
    log.append(
        " getTotalPortfolioValue method called with input " + portfolioName
            + " , " + date + ".");
    return "1000.00";
  }

  /**
   * Simulates saving a portfolio with given stock data.
   *
   * @param portfolioName The name of the portfolio.
   * @param stockData     The stock data to save.
   * @return A string indicating the result of the operation.
   * @throws IOException If simulating a failure to save the portfolio.
   */
  @Override
  public String savePortfolio(String portfolioName, HashMap<String, ArrayList<String>> stockData)
      throws IOException {
    log.append(
        " savePortfolio method called with input " + portfolioName + " , " + stockData + ".");
    return "Portfolio saved";
  }

  /**
   * Retrieves a mocked composition of a portfolio for a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for the composition query.
   * @return A hashmap representing the mocked portfolio composition.
   */
  @Override
  public HashMap<String, ArrayList<String>> getPortfolioComposition(String portfolioName,
      LocalDate date) {
    log.append(" getPortfolioComposition method called with input " + portfolioName + ".");
    HashMap<String, ArrayList<String>> composition = new HashMap<>();
    ArrayList<String> details = new ArrayList<>();
    details.add("Maui Land & Pineapple Co. Inc");
    details.add("50"); // number of shares
    details.add("03-10-2024");
    composition.put("MLP", details);
    details.clear();
    details.add("Apple Inc");
    details.add("7"); // number of shares
    details.add("03-10-2024");
    composition.put("AAPL", details);
    return composition;
  }


  /**
   * Returns a mocked number of portfolios managed.
   *
   * @return The number of portfolios.
   */
  @Override
  public int getNumberOfPortfolios() {
    log.append(" getNumberOfPortfolios method called.");
    // Return a mock number of portfolios
    return 1;
  }

  /**
   * Mocks the creation of a flexible portfolio with a given name.
   *
   * @param portfolioName The name of the portfolio to be created.
   */
  @Override
  public void createFlexiblePortfolio(String portfolioName) {
    log.append("createFlexiblePortfolio method called.");
  }

  /**
   * Mocks the process of buying a specific number of shares of a stock for a portfolio on a given
   * date.
   *
   * @param portfolioName  The name of the portfolio.
   * @param ticker         The ticker symbol of the stock to buy.
   * @param numberOfShares The number of shares to buy.
   * @param date           The date of the transaction.
   */
  @Override
  public void buyPortfolioStock(String portfolioName, String ticker, double numberOfShares,
      LocalDate date) {
    log.append("buyPortfolioStock method called.");
  }

  /**
   * Mocks the process of selling a specific number of shares of a stock from a portfolio on a given
   * date.
   *
   * @param portfolioName  The name of the portfolio.
   * @param ticker         The ticker symbol of the stock to sell.
   * @param numberOfShares The number of shares to sell.
   * @param date           The date of the transaction.
   */
  @Override
  public void sellPortfolioStock(String portfolioName, String ticker, double numberOfShares,
      LocalDate date) {
    log.append("sellPortfolioStock method called.");
  }

  /**
   * Returns a mocked cost basis of a portfolio on a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the cost basis is calculated.
   * @return The mocked cost basis value.
   */
  @Override
  public float getCostPortfolioBasis(String portfolioName, LocalDate date) {
    log.append("getCostPortfolioBasis method called.");
    return 0;
  }

  /**
   * Returns a mocked string representing the performance of a portfolio between two dates.
   *
   * @param ticker    The ticker symbol of the portfolio to analyze.
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A mocked string detailing the portfolio's performance.
   */
  @Override
  public String getPortfolioPerformance(String ticker, LocalDate startDate, LocalDate endDate) {
    log.append("getPortfolioPerformance method called.");
    return null;
  }

  /**
   * Mocks the check for a portfolio's mutability based on its name.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return A mocked boolean indicating if the portfolio is mutable.
   */
  @Override
  public boolean isPortfolioMutable(String portfolioName) {
    log.append("isPortfolioMutable method called.");
    return false;
  }

  @Override
  public void invest(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate date) {
    log.append("invest method called.");
  }

  @Override
  public void investPeriodically(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate startDate, LocalDate endDate,
      long intervalDays) {
    log.append("investPeriodically method called.");
  }

}

