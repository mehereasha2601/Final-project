package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface specifies methods for portfolio creation, valuation, and management, enabling the
 * tracking and analysis of investment portfolios. Implementing classes provides the logic for
 * operations such as checking the existence of portfolios, calculating their values, saving and
 * retrieving portfolio data, and searching within portfolio contents.
 * </p>
 */
public interface PortfolioManagerInterface {

  /**
   * Checks if a portfolio with the specified name already exists.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return true if the portfolio exists; false otherwise.
   */
  public boolean portfolioExists(String portfolioName);

  /**
   * Retrieves the total value of a specified portfolio on a given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for which the total value is calculated.
   * @return A string representing the total value of the portfolio.
   * @throws IOException If there is an issue accessing the necessary data.
   */
  public String getTotalPortfolioValue(String portfolioName, LocalDate date) throws IOException;

  /**
   * Saves the provided stock data under a specified portfolio name.
   *
   * @param portfolioName The name of the portfolio.
   * @param stockData     The stock data to be saved in the portfolio.
   * @return A message indicating the result of the save operation.
   * @throws IOException If there is an issue saving the data.
   */
  public String savePortfolio(String portfolioName,
                              HashMap<String, ArrayList<String>> stockData)
          throws IOException;


  /**
   * Retrieves the composition of a specified portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @return A map representing the portfolio's composition.
   */
  public HashMap<String, ArrayList<String>> getPortfolioComposition(String portfolioName,
                                                                    LocalDate date);

  /**
   * Gets the number of portfolios of the user.
   *
   * @return The total number of portfolios.
   */
  public int getNumberOfPortfolios();

  /**
   * Creates a new flexible portfolio with the specified name and associates it with the current
   * user.The created portfolio is added to a collection of user's flexible portfolios, allowing for
   * dynamic management and transactions within the portfolio.
   *
   * @param portfolioName The name for the new flexible portfolio.
   */
  public void createFlexiblePortfolio(String portfolioName);

  /**
   * Buys a specified number of shares of a given stock on a certain date in a flexible portfolio.
   *
   * @param ticker         The ticker symbol of the stock to buy.
   * @param numberOfShares The number of shares to buy.
   * @param date           The date of the purchase.
   */
  public void buyPortfolioStock(String portfolioName, String ticker, double numberOfShares,
                                LocalDate date);

  /**
   * Sells a specified number of shares of a given stock on a certain date in a flexible portfolio.
   *
   * @param ticker         The ticker symbol of the stock to sell.
   * @param numberOfShares The number of shares to sell.
   * @param date           The date of the sale.
   */
  public void sellPortfolioStock(String portfolioName, String ticker, double numberOfShares,
                                 LocalDate date);

  /**
   * Calculates the cost basis of the flexible portfolio up to a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date up to which to calculate the cost basis.
   * @return The cost basis of the portfolio.
   */
  public float getCostPortfolioBasis(String portfolioName, LocalDate date);

  /**
   * Generates a performance chart for the portfolio between two dates, showing the portfolio value
   * over time.
   *
   * @param portfolioName The name of the portfolio.
   * @param startDate     The start date of the period to analyze.
   * @param endDate       The end date of the period to analyze.
   * @return A string representing a simple text-based chart of the portfolio's performance.
   */
  public String getPortfolioPerformance(String portfolioName,
                                        LocalDate startDate, LocalDate endDate);

  /**
   * Checks if the specified portfolio is mutable, meaning it can be modified.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return true if the portfolio exists and is mutable; false otherwise.
   */
  public boolean isPortfolioMutable(String portfolioName);

  /**
   * Invests a specified amount into a portfolio using a given investment strategy.
   * This method applies the investment immediately based on the specified stock ratios
   * and target date.
   *
   * @param portfolioName The name of the portfolio where the amount will be invested.
   * @param strategyName  The name of the investment strategy to apply for this investment.
   * @param amount        The total amount to be invested.
   * @param stockRatio    A hashmap representing the ratio of investment for each stock,
   *                      where the key is the stock symbol and the value is the ratio of
   *                      the total amount to be invested in that stock.
   * @param date          The date when the investment should be executed.
   */

  public void invest(String portfolioName, String strategyName, double amount,
                     HashMap<String, Double> stockRatio, LocalDate date);

  /**
   * Schedules periodic investments into a portfolio using a specified investment strategy,
   * starting and ending on specified dates, and repeating at a defined interval.
   * This method allows for setting up a recurring investment strategy over a period,
   * applying the same stock ratios for each investment.
   *
   * @param portfolioName The name of the portfolio to receive the periodic investments.
   * @param strategyName  The name of the investment strategy to use for these investments.
   * @param amount        The amount to be invested in each period.
   * @param stockRatio    A hashmap with stock symbols as keys and their corresponding
   *                      investment ratios as values.
   * @param startDate     The start date of the investment period.
   * @param endDate       The end date of the investment period.
   * @param intervalDays  The number of days between each investment. This defines the frequency
   *                      of the investment over the specified period.
   */

  public void investPeriodically(String portfolioName, String strategyName,
                                 double amount, HashMap<String, Double> stockRatio,
                                 LocalDate startDate, LocalDate endDate, long intervalDays);
}
