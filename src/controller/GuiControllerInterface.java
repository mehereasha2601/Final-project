package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for a GUI Controller in an application managing stock portfolios. Defines workflows for
 * user interactions such as creating new users, managing portfolios, and analyzing stock data.
 */
public interface GuiControllerInterface extends ControllerInterface {

  /**
   * Processes a new user creation or logs in an existing user based on provided user data.
   *
   * @param userData An ArrayList containing user's first name, last name, and email ID.
   * @return A string message indicating the outcome (e.g., user creation or login).
   */
  public String newUserWorkflow(ArrayList<String> userData);

  /**
   * Handles the login workflow for an existing user identified by email ID.
   *
   * @param emailID The email ID of the user trying to log in.
   * @return A string message indicating the login status.
   */
  public String existingUserWorkflow(String emailID);

  /**
   * Initiates the creation of a new portfolio with a given name.
   *
   * @param portfolioName The name of the new portfolio to be created.
   * @return A string message indicating the outcome of the portfolio creation attempt.
   */
  public String createPortfolioWorkflow(String portfolioName);

  /**
   * Creates a new flexible portfolio with the specified name.
   *
   * @param portfolioName The name of the flexible portfolio to create.
   * @return A message indicating the successful creation of a flexible portfolio.
   */
  public String createFlexiblePortfolioWorkflow(String portfolioName);

  /**
   * Creates an immutable portfolio based on provided stock data.
   *
   * @param portfolioName The name of the portfolio.
   * @param stockData     A map containing stock tickers and associated data for the portfolio.
   * @return A message indicating the outcome of the immutable portfolio creation.
   * @throws IOException If an I/O error occurs during portfolio creation.
   */
  public String immutablePortfolio(String portfolioName,
      HashMap<String, ArrayList<String>> stockData) throws IOException;

  /**
   * Displays the composition of a portfolio on a given date.
   *
   * @param portfolioName The name of the portfolio to view.
   * @param date          The date for which the composition is requested.
   * @return A string representation of the portfolio composition.
   */
  public String viewPortfolioCompositionWorkflow(String portfolioName, LocalDate date);

  /**
   * Calculates the total value of a portfolio on a given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for the value calculation.
   * @return A message indicating the total value of the portfolio.
   * @throws IOException If an I/O error occurs during the calculation.
   */
  public String totalValueOfPortfolioWorkflow(String portfolioName, LocalDate date)
      throws IOException;

  /**
   * Manages buying or selling stocks for portfolios that are not empty.
   *
   * @param portfolioName The name of the portfolio.
   * @param ticker        The stock ticker.
   * @param noOfShares    The number of shares to buy or sell.
   * @param date          The date of the transaction.
   * @param cause         The action to be performed (buy or sell).
   * @return A message indicating the outcome of the transaction.
   */
  public String buyOrSellStocksNonZeroPortfoliosWorkflow(
      String portfolioName, String ticker, int noOfShares, LocalDate date, String cause);

  /**
   * Computes the cost basis of a portfolio on a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for the cost basis computation.
   * @return A message indicating the cost basis of the portfolio.
   */
  public String portfolioCostBasisWorkflow(String portfolioName, LocalDate date);

  /**
   * Calculates the performance of a portfolio between two dates.
   *
   * @param portfolioName The name of the portfolio.
   * @param dates         A list containing the start and end dates for the performance
   *                      calculation.
   * @return A message indicating the portfolio's performance.
   */
  public String portfolioPerformanceWorkflow(String portfolioName, ArrayList<LocalDate> dates);

  /**
   * Identifies the crossover days for a stock between two dates.
   *
   * @param ticker The stock ticker.
   * @param dates  A list containing the start and end dates for the analysis.
   * @return A message listing the crossover days and types.
   * @throws IOException If an I/O error occurs during the analysis.
   */
  public String computeCrossoverDays(String ticker, ArrayList<LocalDate> dates) throws IOException;

  /**
   * Computes moving average crossover days for a given stock, using specified short and long
   * periods.
   *
   * @param ticker The stock ticker.
   * @param dates  A list of start and end dates for the analysis.
   * @param x      The short period for the moving average.
   * @param y      The long period for the moving average.
   * @return A message listing moving average crossover days.
   * @throws IOException If an I/O error occurs during the analysis.
   */
  public String computeMovingCrossoverDays(String ticker, ArrayList<LocalDate> dates, int x, int y)
      throws IOException;

  /**
   * Computes the moving average for a stock over a specified number of days.
   *
   * @param ticker The stock ticker.
   * @param days   The number of days for the moving average calculation.
   * @return A message indicating the moving average.
   * @throws IOException If an I/O error occurs during the calculation.
   */
  public String computeMovingAverage(String ticker, int days) throws IOException;

  /**
   * Analyzes stock performance over a specified period or on a specific date.
   *
   * @param ticker    The stock ticker.
   * @param startDate The start date for the analysis.
   * @param endDate   The end date for the analysis (can be the same as startDate for single-day
   *                  analysis).
   * @return A message indicating the stock's performance.
   * @throws IOException If an I/O error occurs during the analysis.
   */
  public String computeStockPerformance(String ticker, LocalDate startDate, LocalDate endDate)
      throws IOException;

  /**
   * Checks for gains or loss on a stock either on a specific date or over a period.
   *
   * @param ticker    The stock ticker.
   * @param userInput Specifies whether to analyze a single day or a period (1 for single day, 2 for
   *                  period).
   * @param startDate The start date for the analysis.
   * @param endDate   The end date for the period analysis.
   * @return A message indicating gains or loss.
   * @throws IOException If an I/O error occurs during the analysis.
   */
  public String checkStockGainsOrLoss(String ticker, int userInput, LocalDate startDate,
      LocalDate endDate) throws IOException;

  /**
   * Imports a portfolio from a CSV file.
   *
   * @param filePath The path to the CSV file.
   * @param option   Indicates the type of portfolio to create (flexible or immutable).
   * @return A message indicating the outcome of the import operation.
   * @throws IOException If an I/O error occurs during import.
   */
  public String importPortfolioWorkflow(String filePath, int option)
      throws IOException;

  /**
   * Exports the specified portfolio to a CSV file in the given directory.
   *
   * @param portfolioName The name of the portfolio to export.
   * @param directory     The directory where the CSV file will be saved.
   * @param date          The date of the portfolio composition to export.
   * @return A message indicating the outcome of the export operation.
   */
  public String exportPortfolioWorkflow(String portfolioName, String directory, LocalDate date);

  /**
   * Initiates an investment strategy for a specified portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @param strategyName  The name of the investment strategy.
   * @param amount        The amount of money to invest.
   * @param stockRatio    The distribution ratio of stocks to invest in.
   * @param date          The date of the investment.
   * @return A message indicating the outcome of the investment operation.
   */
  public String invest(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate date);

  /**
   * Performs periodic investments in a specified portfolio according to a strategy.
   *
   * @param portfolioName The name of the portfolio.
   * @param strategyName  The name of the investment strategy.
   * @param amount        The amount of money to invest periodically.
   * @param stockRatio    The distribution ratio of stocks to invest in.
   * @param startDate     The start date for periodic investments.
   * @param endDate       The end date for periodic investments.
   * @param intervalDays  The interval in days between investments.
   * @return A message indicating the outcome of the periodic investment operation.
   */
  public String investPeriodically(String portfolioName, String strategyName,
      double amount, HashMap<String, Double> stockRatio,
      LocalDate startDate, LocalDate endDate, long intervalDays);

  /**
   * Checks if a specified portfolio is eligible for investing.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return A message indicating if the portfolio is eligible for investing.
   */
  public String portfolioCheckForInvesting(String portfolioName);

  /**
   * Retrieves the tickers of stocks included in a specified portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @return A map of stock tickers and their associated data.
   */
  public HashMap<String, ArrayList<String>> getTickersForInvest(String portfolioName);

  /**
   * Validates if a given ticker symbol is valid.
   *
   * @param ticker The ticker symbol to validate.
   * @return True if the ticker is valid, false otherwise.
   */
  public boolean isValidTicker(String ticker);

}
