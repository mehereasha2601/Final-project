package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface executes essential actions that can be performed on a user's investment portfolio,
 * including valuation, composition analysis, and data management. Implementing classes provides the
 * logic for these operations, interfacing with both the user data and the market data to provide
 * accurate and timely information about the portfolio.
 * </p>
 */
public interface PortfolioInterface {

  /**
   * Calculates the total value of the portfolio on a given date.
   *
   * @param date The date for which the total value is calculated.
   * @return The total value of the portfolio on the given date.
   * @throws IOException If there is an issue accessing stock data.
   */
  public float totalValue(LocalDate date) throws IOException;

  /**
   * Retrieves the composition of the portfolio.
   *
   * @return A map of the stock symbols to their company names and number of shares.
   */
  public HashMap<String, String> getComposition(LocalDate date);

  /**
   * Gets the user who owns the portfolio.
   *
   * @return The user.
   */
  public User getUser();

  /**
   * Gets the stock data of the portfolio.
   *
   * @return A map of the stock symbols to their detailed data.
   */
  public HashMap<String, ArrayList<String>> getStockData();

  /**
   * Gets the name of the portfolio.
   *
   * @return The name of the portfolio.
   */
  public String getPortfolioName();

  /**
   * Fetches historical data for a stock and saves it to a CSV file.
   *
   * @param ticker The stock symbol.
   * @throws IOException If fetching or saving the data fails.
   */
  public void getHistoricalData(String ticker) throws IOException;

  /**
   * Updates the historical data for a stock if newer data is available.
   *
   * @param ticker The stock symbol.
   * @param date   The date from which to update the data.
   * @throws IOException If updating the data fails.
   */
  public void updateHistoricalData(String ticker, LocalDate date) throws IOException;

  /**
   * Retrieves the closing value of a stock on a specific date.
   *
   * @param ticker The stock symbol.
   * @param date   The date for which the closing value is requested.
   * @return The closing value of the stock on the given date.
   * @throws IOException If retrieving the data fails.
   */
  public float getClosingValue(String ticker, LocalDate date) throws IOException;

  /**
   * Generates a performance chart for the portfolio between two dates, showing the portfolio value
   * over time.
   *
   * @param portfolioName The name of the portfolio.
   * @param startDate     The start date of the period to analyze.
   * @param endDate       The end date of the period to analyze.
   * @return A string representing a simple text-based chart of the portfolio's performance.
   * @throws IllegalArgumentException if the end date is before the start date.
   */
  public String getPortfolioPerformance(String portfolioName,
      LocalDate startDate, LocalDate endDate);


}
