package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for performing various stock analysis operations. It implements
 * {@link StockAnalysisInterface} to provide methods for stock trend analysis, moving average
 * calculations, and generating performance charts,etc.
 */
public class StockAnalysis implements StockAnalysisInterface {

  private StockData data;

  /**
   * Constructs a new StockAnalysis instance for a given ticker. Initializes the stock data for the
   * specified ticker.
   *
   * @param ticker The stock ticker symbol.
   * @throws IOException If there is an error in fetching the historical stock data.
   */
  public StockAnalysis(String ticker) throws IOException {
    StockData data = new StockData(ticker);
    LocalDate currentDate = LocalDate.now();
    data.updateHistoricalStockData(currentDate);
    this.data = data;
  }


  /**
   * Determines the stock trend on a specific date.
   *
   * @param date The date for which the stock trend is analyzed.
   * @return A string describing the stock trend (e.g., "Gain", "Lose").
   * @throws IOException If there is an error in fetching the data.
   */
  @Override
  public String getStockTrendOnDate(LocalDate date) throws IOException {
    if (date.isAfter(LocalDate.now()) || date == null) {
      throw new IllegalArgumentException("Invalid date.");
    }
    try {
      float openingValue = data.getStockOpeningValue(date);
      float closingValue = data.getStockClosingValue(date);
      if (openingValue > closingValue) {
        return "Gain";
      } else if (openingValue < closingValue) {
        return "Lose";
      } else {
        return "Neither Gain nor Lose";
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Determines the overall stock trend between two dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A string describing the overall stock trend (e.g., "Gain", "Lose").
   * @throws IOException If there is an error in fetching the data.
   */
  @Override
  public String getStockTrend(LocalDate startDate, LocalDate endDate) throws IOException {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must not be after end date.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid end date. End date cannot be in future");
    }

    try {
      float openingValue = data.getStockOpeningValue(startDate);
      float closingValue = data.getStockClosingValue(endDate);
      if (openingValue > closingValue) {
        return "Gain";
      } else if (openingValue < closingValue) {
        return "Lose";
      } else {
        return "Neither Gain nor Lose";
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Calculates the moving average of a stock's closing value over a specified number of days.
   *
   * @param date The end date for the calculation.
   * @param days The number of days over which to calculate the moving average.
   * @return The moving average value.
   * @throws IOException If there is an error in fetching the data.
   */
  @Override
  public float getMovingAverage(LocalDate date, int days) throws IOException {
    if (date.isAfter(LocalDate.now()) || date == null) {
      throw new IllegalArgumentException("Invalid date.");
    }
    HashMap<LocalDate, Float> values = data.getStockClosingValues(date, days);
    if (values.isEmpty()) {
      throw new IllegalArgumentException("No stock data available for the specified range.");
    }
    float sum = 0;
    for (Float value : values.values()) {
      sum += value;
    }
    float average = sum / values.size();
    return average;
  }

  /**
   * Identifies days when a stock's closing price crosses over its moving average between two
   * dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A map of dates to "Buy" or "Sell" signals based on crossovers.
   * @throws IOException If there is an error in fetching the data.
   */
  @Override
  public HashMap<LocalDate, String> getCrossoversDays(LocalDate startDate, LocalDate endDate)
      throws IOException {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must not be after end date.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid end date. End date cannot be in future");
    }

    HashMap<LocalDate, Float> closingValues = data.getStockClosingValues(startDate, endDate);
    HashMap<LocalDate, String> crossoverDays = new HashMap<>();

    List<LocalDate> sortedDates = new ArrayList<>(closingValues.keySet());
    Collections.sort(sortedDates);

    if (sortedDates.size() < 2) {
      return crossoverDays;
    }

    LocalDate previousDate = sortedDates.get(0);
    Float previousMovingAverage = getMovingAverage(previousDate, 30);
    Float previousClosingPrice = closingValues.get(previousDate);

    for (int i = 1; i < sortedDates.size(); i++) {
      LocalDate currentDate = sortedDates.get(i);
      float currentClosingPrice = closingValues.get(currentDate);
      float currentMovingAverage = getMovingAverage(currentDate, 30);
      if (previousClosingPrice < previousMovingAverage
          && currentClosingPrice > currentMovingAverage) {
        crossoverDays.put(currentDate, "Buy");
      } else if (previousClosingPrice > previousMovingAverage
          && currentClosingPrice < currentMovingAverage) {
        crossoverDays.put(currentDate, "Sell");
      }
      previousDate = currentDate;
      previousClosingPrice = currentClosingPrice;
      previousMovingAverage = currentMovingAverage;
    }
    return crossoverDays;
  }

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
  @Override
  public HashMap<LocalDate, String> getMovingCrossoversDays(LocalDate startDate, LocalDate endDate,
      int x, int y) throws IOException {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must not be after end date.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid end date. End date cannot be in future");
    }
    if (x <= 0 || y <= 0) {
      throw new IllegalArgumentException("Moving average periods must be positive.");
    }
    if (x >= y) {
      throw new IllegalArgumentException(
          "The shorter moving average period 'x' must be less than the longer period 'y'.");
    }

    HashMap<LocalDate, String> crossoverDays = new HashMap<>();
    HashMap<LocalDate, Float> closingValues = data.getStockClosingValues(startDate, endDate);

    List<LocalDate> sortedDates = new ArrayList<>(closingValues.keySet());
    Collections.sort(sortedDates);

    Float previousXDayMA = null;
    Float previousYDayMA = null;

    for (int i = 0; i < sortedDates.size(); i++) {
      LocalDate currentDate = sortedDates.get(i);
      float currentXDayMA = getMovingAverage(currentDate, x);
      float currentYDayMA = getMovingAverage(currentDate, y);

      if (previousXDayMA != null && previousYDayMA != null) {
        if (previousXDayMA < previousYDayMA && currentXDayMA > currentYDayMA) {
          crossoverDays.put(currentDate, "Buy");
        } else if (previousXDayMA > previousYDayMA && currentXDayMA < currentYDayMA) {
          crossoverDays.put(currentDate, "Sell");
        }
      }

      previousXDayMA = currentXDayMA;
      previousYDayMA = currentYDayMA;
    }

    return crossoverDays;
  }


  /**
   * Generates a text-based performance chart for the stock between two dates, showing the closing
   * value over time.
   *
   * @param startDate The start date of the period for the performance chart.
   * @param endDate   The end date of the period for the performance chart.
   * @return A string representing a bar chart of the stock's performance.
   * @throws IOException If there is an error in fetching or processing the data.
   */
  @Override
  public String getStockPerformance(LocalDate startDate, LocalDate endDate) throws IOException {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must not be after end date.");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid end date. End date cannot be in future");
    }

    HashMap<LocalDate, Float> closingValues = data.getStockClosingValues(startDate, endDate);
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
    StringBuilder chart = new StringBuilder();
    List<LocalDate> validDates = closingValues.keySet().stream()
        .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
        .sorted()
        .collect(Collectors.toList());

    // Desired max lines in the chart
    final int maxLines = 30;
    int totalPoints = validDates.size();
    int interval = (int) Math.ceil(totalPoints / (double) maxLines);

    List<LocalDate> selectedDates = new ArrayList<>();
    for (int i = 0; i < totalPoints; i += interval) {
      selectedDates.add(validDates.get(i));
    }

    float maxClosingValue = closingValues.values().stream().max(Float::compare).orElse(0f);
    int scale = (int) Math.ceil(maxClosingValue / 50);
    scale = Math.max(1, scale); // Ensure scale is at least 1

    for (LocalDate date : selectedDates) {
      float closingValue = closingValues.get(date);
      int stars = (int) (closingValue / scale);
      chart.append(formatter.format(date)).append(": ")
          .append("*".repeat(Math.max(0, stars))).append("\n");
    }

    chart.append("Scale: * = ").append(scale).append(", representing the value in dollars.\n");
    return chart.toString();

  }

}