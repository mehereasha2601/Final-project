package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class represents a user's flexible investment portfolio, that facilitates buying, and
 * selling of stocks in history. It supports operations including cost basis, total value, and
 * performance over time. The portfolio adjusts with transactions and maintains composition.
 */
public class FlexiblePortfolio implements FlexiblePortfolioInterface {

  private User user;
  private String portfolioName;

  private TreeMap<LocalDate, ArrayList<Stock>> composition = new TreeMap<>();

  private Map<String, Double> netShares = new HashMap<>();

  /**
   * Constructs a new FlexiblePortfolio with the given user and portfolio name.
   *
   * @param user          The owner of the portfolio.
   * @param portfolioName The name of the portfolio.
   */
  public FlexiblePortfolio(User user, String portfolioName) {
    this.user = user;
    this.portfolioName = portfolioName;
  }

  /**
   * Represents a stock within the portfolio, tracking its ticker and shares owned.
   */
  private class Stock {

    private String ticker;
    private double shares = 0;

    /**
     * Constructs a Stock instance with the specified ticker.
     *
     * @param ticker The stock's ticker symbol.
     */
    public Stock(String ticker) {
      this.ticker = ticker;
    }

    /**
     * Adds shares to this stock.
     *
     * @param shares The number of shares to add.
     */
    public void addShares(double shares) {
      this.shares += shares;
    }

    /**
     * Removes shares from this stock.
     *
     * @param shares The number of shares to remove.
     */
    public void removeShares(double shares) {
      this.shares -= shares;
    }

    /**
     * Returns the number of shares owned.
     *
     * @return The number of shares.
     */
    public double getShares() {
      return shares;
    }

    /**
     * Returns the stock ticker symbol.
     *
     * @return The ticker symbol.
     */
    public String getTicker() {
      return ticker;
    }

  }

  /**
   * Buys a specified number of shares of a given stock on a certain date. If the stock already
   * exists in the portfolio for that date, adds to the share count; otherwise, a new stock entry is
   * created. Future dates reflect this purchase.
   *
   * @param ticker The ticker symbol of the stock to buy.
   * @param shares The number of shares to buy.
   * @param date   The date of the purchase.
   * @throws IllegalArgumentException if the number of shares is less than or equal to zero.
   */
  @Override
  public void buy(String ticker, double shares, LocalDate date) {

    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid date,can't buy shares in future");
    }

    if (shares <= 0.00) {
      throw new IllegalArgumentException("Invalid number of shares");
    }

    LocalDate latestDateBeforeOrEqual = composition.floorKey(date);
    ArrayList<Stock> stocksOnDate;

    if (latestDateBeforeOrEqual != null && latestDateBeforeOrEqual.equals(date)) {
      stocksOnDate = new ArrayList<>(composition.get(latestDateBeforeOrEqual));
    } else {
      stocksOnDate = new ArrayList<>();
      if (latestDateBeforeOrEqual != null) {
        for (Stock stock : composition.get(latestDateBeforeOrEqual)) {
          Stock newStock = new Stock(stock.getTicker());
          newStock.addShares(stock.getShares());
          stocksOnDate.add(newStock);
        }
      }
    }

    boolean stockFound = false;
    for (Stock stock : stocksOnDate) {
      if (stock.getTicker().equalsIgnoreCase(ticker)) {
        stock.addShares(shares);
        stockFound = true;
        break;
      }
    }
    if (!stockFound) {
      Stock newStock = new Stock(ticker);
      newStock.addShares(shares);
      stocksOnDate.add(newStock);
    }
    composition.put(date, stocksOnDate);

    composition.tailMap(date, false).forEach((futureDate, futureStocks) -> {
      for (Stock futureStock : futureStocks) {
        if (futureStock.getTicker().equalsIgnoreCase(ticker)) {
          futureStock.addShares(shares);
          return;
        }
      }
      Stock newStock = new Stock(ticker);
      newStock.addShares(shares);
      futureStocks.add(newStock);
    });
    netShares.put(ticker, netShares.getOrDefault(ticker, 0.0) + shares);
  }

  /**
   * Sells a specified number of shares of a given stock on a certain date. Updates the portfolio to
   * reflect the sale on the given date and adjusts future portfolio compositions accordingly.
   *
   * @param ticker The ticker symbol of the stock to sell.
   * @param shares The number of shares to sell.
   * @param date   The date of the sale.
   * @throws IllegalArgumentException if the number of shares to sell is invalid or not enough
   *                                  shares are available.
   */
  @Override
  public void sell(String ticker, double shares, LocalDate date) {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid date,can't buy shares in future");
    }

    if (shares <= 0.00) {
      throw new IllegalArgumentException("Invalid Number of shares");
    }
    double currentShares = netShares.getOrDefault(ticker, 0.0);
    if (currentShares < shares) {
      throw new IllegalArgumentException("Cannot sell the stock: not enough shares available for "
              + ticker);
    }
    netShares.put(ticker, currentShares - shares);

    boolean stockFound = false;
    double sharesToSell = shares;
    for (LocalDate key : composition.headMap(date, true).descendingMap().keySet()) {
      for (Stock existingStock : composition.get(key)) {
        if (existingStock.getTicker().equalsIgnoreCase(ticker)) {
          if (existingStock.getShares() < sharesToSell) {
            sharesToSell -= existingStock.getShares();
            existingStock.removeShares(existingStock.getShares());
          } else {
            existingStock.removeShares(sharesToSell);
            sharesToSell = 0.00;
          }
          stockFound = true;
          if (sharesToSell == 0.00) {
            break;
          }
        }
      }
      if (sharesToSell == 0.00) {
        break;
      }
    }

    if (!stockFound || sharesToSell > 0.00) {
      throw new IllegalArgumentException("Cannot sell the stock: "
              + "not enough shares or stock not found by " + date);
    } else {
      for (LocalDate futureDate : composition.tailMap(date, false).keySet()) {
        ArrayList<Stock> futureStocks = new ArrayList<>(composition.get(futureDate));
        for (Stock futureStock : futureStocks) {
          if (futureStock.getTicker().equalsIgnoreCase(ticker)) {
            futureStock.removeShares(shares);
          }
        }
        composition.put(futureDate, futureStocks);
      }
    }
  }

  /**
   * Calculates the cost basis of the portfolio up to a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date up to which to calculate the cost basis.
   * @return The cost basis of the portfolio.
   */
  @Override
  public float costBasis(String portfolioName, LocalDate date) {
    float totalCostBasis = 0.00F;

    Set<String> uniqueTickers = new HashSet<>();
    for (ArrayList<Stock> stocksOnDate : composition.values()) {
      for (Stock stock : stocksOnDate) {
        uniqueTickers.add(stock.getTicker());
      }
    }

    for (String ticker : uniqueTickers) {
      float tickerCostBasis = 0.00F;
      double previousShares = 0.00;

      for (Map.Entry<LocalDate, ArrayList<Stock>> entry : composition.entrySet()) {
        if (entry.getKey().isAfter(date)) {
          break;
        }

        double currentShares = 0.00;

        for (Stock stock : entry.getValue()) {
          if (stock.getTicker().equalsIgnoreCase(ticker)) {
            currentShares = stock.getShares();
            break;
          }
        }

        double sharesBought = currentShares - previousShares;
        if (sharesBought > 0.00) {
          try {
            float purchasePricePerShare = getClosingValue(ticker, entry.getKey());
            tickerCostBasis += sharesBought * purchasePricePerShare;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        previousShares = currentShares;
      }

      totalCostBasis += tickerCostBasis;
    }

    return totalCostBasis;
  }

  /**
   * Calculates the total value of the portfolio on a given date. This is the sum of the value of
   * all stocks in the portfolio, based on the closing value of each stock on that date.
   *
   * @param date The date on which to calculate the total value.
   * @return The total value of the portfolio on the given date.
   * @throws IOException if there is an error fetching stock values.
   */
  @Override
  public float totalValue(LocalDate date) throws IOException {
    float totalValue = 0.0F;
    if (!(composition.isEmpty())) {
      if (date.isBefore(composition.firstKey())) {
        return totalValue;
      }
    }
    HashMap<String, String> portfolioComposition = getComposition(date);
    for (Map.Entry<String, String> entry : portfolioComposition.entrySet()) {
      String ticker = entry.getKey();
      float numShares = Float.parseFloat(entry.getValue());
      float tickerValue = getClosingValue(ticker, date);
      if (tickerValue == 0.0) {
        throw new IllegalArgumentException("No data found for the given date");
      } else {
        float stockValue = tickerValue * numShares;
        totalValue += stockValue;
      }
    }
    return totalValue;
  }

  /**
   * Retrieves the composition of the portfolio on a given date, including the ticker symbols and
   * share counts of all stocks.
   *
   * @param date The date for which to retrieve the portfolio composition.
   * @return A mapping of stock ticker symbols to share counts on the given date.
   */
  @Override
  public HashMap<String, String> getComposition(LocalDate date) {
    LocalDate queryDate = composition.floorKey(date);
    if (queryDate == null) {
      queryDate = composition.isEmpty() ? null : composition.firstKey();
    }
    ArrayList<Stock> stocks = new ArrayList<>();
    HashMap<String, String> currentCompostion = new HashMap<>();
    stocks = composition.get(queryDate);
    for (int i = 0; i < stocks.size(); i++) {
      if (stocks.get(i).getShares() != 0.00) {
        currentCompostion.put(stocks.get(i).getTicker(), String.format("%.3f",
                stocks.get(i).getShares()));
      }
    }
    return currentCompostion;
  }

  /**
   * Returns the User object associated with this portfolio.
   *
   * @return The user who owns the portfolio.
   */
  @Override
  public User getUser() {
    return user;
  }


  /**
   * Returns stock data for the portfolio.
   *
   * @return null.
   */
  @Override
  public HashMap<String, ArrayList<String>> getStockData() {
    return null;
  }

  /**
   * Retrieves the name of the portfolio.
   *
   * @return The name of the portfolio.
   */
  @Override
  public String getPortfolioName() {
    return portfolioName;
  }

  /**
   * Fetches historical stock data for a given ticker symbol.
   *
   * @param ticker The ticker symbol for which historical data is fetched.
   * @throws IOException if there is an error during data retrieval.
   */
  @Override
  public void getHistoricalData(String ticker) throws IOException {
    StockData stockData = new StockData(ticker);
    stockData.getHistoricalStockData();
  }

  /**
   * Updates the historical data for a specific stock ticker up to the provided date.
   *
   * @param ticker The stock ticker to update.
   * @param date   The date up to which historical data should be updated.
   * @throws IOException if there is an error during the update process.
   */
  @Override
  public void updateHistoricalData(String ticker, LocalDate date) throws IOException {
    StockData stockData = new StockData(ticker);
    stockData.updateHistoricalStockData(date);
  }

  /**
   * Gets the closing value of a stock on a specified date. This could involve looking up the value
   * in a local cache or fetching it from a remote source.
   *
   * @param ticker The ticker symbol of the stock.
   * @param date   The date for which the closing value is requested.
   * @return The closing value of the stock on the specified date.
   * @throws IOException if there is an error retrieving the closing value.
   */
  @Override
  public float getClosingValue(String ticker, LocalDate date) throws IOException {
    StockData stockData = new StockData(ticker);
    stockData.updateHistoricalStockData(date);
    float value = stockData.getStockClosingValue(date);
    return value;
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
  public String getPortfolioPerformance(String portfolioName, LocalDate startDate,
                                        LocalDate endDate) {
    if (!endDate.isAfter(startDate)) {
      throw new IllegalArgumentException("End date should be after the start date.");
    }

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
        double shares = Double.parseDouble(entry.getValue());
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
