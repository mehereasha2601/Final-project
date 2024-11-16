package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class represents a user's investment portfolio, containing stocks and their details. It also
 * displays composition and returns total value on a given date.
 */
public class Portfolio implements PortfolioInterface {

  private User user;
  private String portfolioName;
  private HashMap<String, ArrayList<String>> stockData;

  /**
   * Constructs a Portfolio instance with specified user, name, and stock data.
   *
   * @param user          The user to whom the portfolio belongs.
   * @param portfolioName The name of the portfolio.
   * @param stockData     The data of stocks contained in the portfolio.
   */
  public Portfolio(User user, String portfolioName, HashMap<String,
      ArrayList<String>> stockData) {
    this.user = new User(user.getFirstName(), user.getLastName(), user.getEmailId());
    this.portfolioName = portfolioName;
    this.stockData = new HashMap<String, ArrayList<String>>();
    for (String ticker : stockData.keySet()) {
      this.stockData.put(ticker, stockData.get(ticker));
    }
  }

  /**
   * Calculates the total value of the portfolio on a given date.
   *
   * @param date The date for which the total value is calculated.
   * @return The total value of the portfolio on the given date.
   * @throws IOException If there is an issue accessing stock data.
   */
  @Override
  public float totalValue(LocalDate date) throws IOException {
    float totalValue = 0;
    LocalDate currentDate = LocalDate.now();
    HashMap<String, String> portfolioCompostion = getComposition(currentDate);
    for (String ticker : portfolioCompostion.keySet()) {
      float stockValue = 0;
      float numShares = 0;
      float tickerValue = 0;
      tickerValue = this.getClosingValue(ticker, date);
      numShares = Float.valueOf(portfolioCompostion.get(ticker));
      if (tickerValue == 0.0) {
        throw new IllegalArgumentException("No data found for the given date");
      } else {
        stockValue = tickerValue * numShares;
        totalValue = totalValue + stockValue;
      }
    }
    return totalValue;
  }

  /**
   * Retrieves the composition of the portfolio.
   *
   * @return A map of the stock symbols to their company names and number of shares.
   */
  @Override
  public HashMap<String, String> getComposition(LocalDate date) {
    HashMap<String, String> portfolioCompostion = new HashMap<String, String>();
    for (String ticker : stockData.keySet()) {
      portfolioCompostion.put(ticker,
          stockData.get(ticker).get(0));
    }
    return portfolioCompostion;
  }

  /**
   * Gets the user who owns the portfolio.
   *
   * @return The user.
   */
  @Override
  public User getUser() {
    return this.user;
  }

  /**
   * Gets the stock data of the portfolio.
   *
   * @return A map of the stock symbols to their detailed data.
   */
  @Override
  public HashMap<String, ArrayList<String>> getStockData() {
    return this.stockData;
  }

  /**
   * Gets the name of the portfolio.
   *
   * @return The name of the portfolio.
   */
  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }


  protected String exportPortfolio(String directory) {
    String message;
    try {
      Path portfolioPath = Paths.get(directory);
      Path filePath = portfolioPath.resolve(this.portfolioName + ".csv");
      if (Files.exists(filePath)) {
        message = "Portfolio already exists!";
      } else {
        Files.createFile(filePath);
        HashMap<String, ArrayList<String>> composition = this.getStockData();
        List<String> lines = new ArrayList<>();
        lines.add("Ticker Symbol, Number of Shares, Date of Purchase");
        for (String ticker : composition.keySet()) {
          String line = ticker + "," + composition.get(ticker).get(0) + ","
              + composition.get(ticker).get(1);
          lines.add(line);
        }
        Files.write(filePath, lines, StandardOpenOption.WRITE);
        message = "Portfolio successfully saved at " + filePath;
      }
    } catch (Exception e) {
      message = "Invalid directory path. Please provide valid path";
    }
    return message;
  }

  /**
   * Fetches historical data for a stock and saves it to a CSV file.
   *
   * @param ticker The stock symbol.
   * @throws IOException If fetching or saving the data fails.
   */
  @Override
  public void getHistoricalData(String ticker) throws IOException {
    StockData data = new StockData(ticker);
    data.getHistoricalStockData();
  }

  /**
   * Updates the historical data for a stock if newer data is available.
   *
   * @param ticker The stock symbol.
   * @param date   The date from which to update the data.
   * @throws IOException If updating the data fails.
   */
  @Override
  public void updateHistoricalData(String ticker, LocalDate date) throws IOException {
    StockData data = new StockData(ticker);
    data.updateHistoricalStockData(date);
  }

  /**
   * Retrieves the closing value of a stock on a specific date.
   *
   * @param ticker The stock symbol.
   * @param date   The date for which the closing value is requested.
   * @return The closing value of the stock on the given date.
   * @throws IOException If retrieving the data fails.
   */
  @Override
  public float getClosingValue(String ticker, LocalDate date) throws IOException {
    StockData data = new StockData(ticker);
    return data.getStockClosingValue(date);
  }

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
  @Override
  public String getPortfolioPerformance(String portfolioName,
      LocalDate startDate, LocalDate endDate) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
    StringBuilder chart = new StringBuilder();
    TreeMap<LocalDate, Float> portfolioValues = new TreeMap<>();
    float lastValue = 0;

    for (LocalDate date = startDate; !date.isAfter(endDate);
        date = date.plusDays(1)) {
      HashMap<String, String> composition = getComposition(date);
      float totalValue = 0;
      boolean dataAvailable = false;

      for (Map.Entry<String, String> entry : composition.entrySet()) {
        String ticker = entry.getKey();
        int shares = Integer.parseInt(entry.getValue());
        try {
          float closingValue = getClosingValue(ticker, date);
          if (closingValue > 0) {
            dataAvailable = true;
          }
          totalValue += closingValue * shares;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      lastValue = dataAvailable ? totalValue : lastValue;
      portfolioValues.put(date, lastValue);
    }
    List<LocalDate> selectedDates = new ArrayList<>(portfolioValues.keySet());
    final int maxLines = 30;
    int interval = (int) Math.ceil(selectedDates.size() / (double) maxLines);

    List<LocalDate> datesForChart = IntStream.range(0,
            selectedDates.size()).filter(i -> i % interval == 0)
        .mapToObj(selectedDates::get).collect(Collectors.toList());

    float maxPortfolioValue = Collections.max(portfolioValues.values());
    int scale = (int) Math.ceil(maxPortfolioValue / 50);
    scale = Math.max(1, scale);

    for (LocalDate date : datesForChart) {
      float value = portfolioValues.get(date);
      int stars = (int) (value / scale);
      chart.append(formatter.format(date)).append(": ")
          .append(String.join("", Collections.nCopies(stars, "*")))
          .append("\n");
    }

    chart.append("Scale: * = ").append(scale).append(" USD.\n");
    return chart.toString();
  }
}
