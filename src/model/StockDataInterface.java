package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Interface defining methods for fetching and managing stock data.
 */
public interface StockDataInterface {

  /**
   * Fetches historical stock data from a data source and stores it.
   *
   * @throws IOException If there is an error in fetching the data.
   */
  public void getHistoricalStockData() throws IOException;

  /**
   * Updates historical stock data up to a specific date.
   *
   * @param date The date up to which historical stock data should be updated.
   * @throws IOException If there is an error in updating the data.
   */
  public void updateHistoricalStockData(LocalDate date) throws IOException;

  /**
   * Retrieves the closing value of a stock on a specific date.
   *
   * @param date The date for which the closing value is requested.
   * @return The closing value of the stock on the given date.
   * @throws IOException If there is an error in fetching the data.
   */
  public float getStockClosingValue(LocalDate date) throws IOException;

  /**
   * Retrieves the opening value of a stock on a specific date.
   *
   * @param date The date for which the opening value is requested.
   * @return The opening value of the stock on the given date.
   * @throws IOException If there is an error in fetching the data.
   */
  public float getStockOpeningValue(LocalDate date) throws IOException;

  /**
   * Retrieves the closing values of a stock for a number of days before a specific date.
   *
   * @param date The end date for the period.
   * @param days The number of days before the end date for which data is requested.
   * @return A map of dates to closing values.
   * @throws IOException If there is an error in fetching the data.
   */
  public HashMap<LocalDate, Float> getStockClosingValues(LocalDate date, int days)
      throws IOException;

  /**
   * Retrieves the closing values of a stock between two dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A map of dates to closing values.
   * @throws IOException If there is an error in fetching the data.
   */
  public HashMap<LocalDate, Float> getStockClosingValues(LocalDate startDate, LocalDate endDate)
      throws IOException;

}
