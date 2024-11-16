package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * This interface contains methods for analyzing stock data.
 */
public interface StockAnalysisInterface {

  /**
   * Determines the stock trend on a specific date.
   *
   * @param date The date for which the stock trend is analyzed.
   * @return A string describing the stock trend (e.g., "Gain", "Lose").
   * @throws IOException If there is an error in fetching the data.
   */
  public String getStockTrendOnDate(LocalDate date) throws IOException;

  /**
   * Determines the overall stock trend between two dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A string describing the overall stock trend (e.g., "Gain", "Lose").
   * @throws IOException If there is an error in fetching the data.
   */
  public String getStockTrend(LocalDate startDate, LocalDate endDate) throws IOException;

  /**
   * Calculates the moving average of a stock's closing value over a specified number of days.
   *
   * @param date The end date for the calculation.
   * @param days The number of days over which to calculate the moving average.
   * @return The moving average value.
   * @throws IOException If there is an error in fetching the data.
   */
  public float getMovingAverage(LocalDate date, int days) throws IOException;

  /**
   * Identifies days when a stock's closing price crosses over its moving average between two
   * dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A map of dates to "Buy" or "Sell" signals based on crossovers.
   * @throws IOException If there is an error in fetching the data.
   */
  public HashMap<LocalDate, String> getCrossoversDays(LocalDate startDate, LocalDate endDate)
      throws IOException;


  /**
   * Identifies days when two moving averages of a stock crosses over between two dates.
   *
   * @param startDate The start date of the period for analysis.
   * @param endDate   The end date of the period for analysis.
   * @param x         The number of days for the shorter moving average.
   * @param y         The number of days for the longer moving average.
   * @return A map of dates to "Buy" or "Sell" signals.
   * @throws IOException If there is an error in fetching the data.
   */
  public HashMap<LocalDate, String> getMovingCrossoversDays(LocalDate startDate, LocalDate endDate,
      int x, int y) throws IOException;

  /**
   * Generates a text-based performance chart for the stock between two dates, showing the closing
   * value over time.
   *
   * @param startDate The start date of the period for the performance chart.
   * @param endDate   The end date of the period for the performance chart.
   * @return A string representing a bar chart of the stock's performance.
   * @throws IOException If there is an error in fetching or processing the data.
   */
  public String getStockPerformance(LocalDate startDate, LocalDate endDate) throws IOException;
}
