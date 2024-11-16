package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the portfolio manager of a user. It creates portfolios for the user and
 * allows user to view the composition of a portfolio and offer more insights for the portfolio.
 */
public class PortfolioManager implements PortfolioManagerInterface {

  private User user;

  private ArrayList<PortfolioInterface> userPortfolios;

  private ArrayList<FlexiblePortfolioInterface> userFlexiblePortfolios;

  private User getUser() {
    return this.user;
  }

  /**
   * Initializes a new portfolio manager for a user identified by their email ID. Loads all existing
   * portfolios for the user from storage.
   *
   * @param emailId The email ID of the user.
   */
  public PortfolioManager(String emailId) {
    UserManagerInterface userManager = new UserManager();
    User user1 = ((UserManager) userManager).getUser(emailId);
    this.user = user1;
    this.userPortfolios = this.getAllPortfolios();
    this.userFlexiblePortfolios = new ArrayList<>();
  }

  /**
   * Returns the number of portfolios managed by this manager.
   *
   * @return The total count of portfolios.
   */
  public int getNumberOfPortfolios() {
    return userPortfolios.size();
  }


  /**
   * Creates a new flexible portfolio with the specified name and associates it with the current
   * user.The created portfolio is added to a collection of user's flexible portfolios, allowing for
   * dynamic management and transactions within the portfolio.
   *
   * @param portfolioName The name for the new flexible portfolio.
   */
  @Override
  public void createFlexiblePortfolio(String portfolioName) {
    FlexiblePortfolioInterface flexiblePortfolio = new FlexiblePortfolio(user, portfolioName);
    userFlexiblePortfolios.add(flexiblePortfolio);
  }

  /**
   * Buys a specified number of shares of a given stock on a certain date in a flexible portfolio.
   *
   * @param ticker         The ticker symbol of the stock to buy.
   * @param numberOfShares The number of shares to buy.
   * @param date           The date of the purchase.
   */
  @Override
  public void buyPortfolioStock(String portfolioName, String ticker, double numberOfShares,
      LocalDate date) {
    FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
    flexiblePortfolio.buy(ticker, numberOfShares, date);
  }

  /**
   * Sells a specified number of shares of a given stock on a certain date in a flexible portfolio.
   *
   * @param ticker         The ticker symbol of the stock to sell.
   * @param numberOfShares The number of shares to sell.
   * @param date           The date of the sale.
   */
  @Override
  public void sellPortfolioStock(String portfolioName, String ticker, double numberOfShares,
      LocalDate date) {
    FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
    flexiblePortfolio.sell(ticker, numberOfShares, date);
  }

  /**
   * Calculates the cost basis of the flexible portfolio up to a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date up to which to calculate the cost basis.
   * @return The cost basis of the portfolio.
   */
  @Override
  public float getCostPortfolioBasis(String portfolioName, LocalDate date) {
    FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
    return flexiblePortfolio.costBasis(portfolioName, date);
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
    if (isPortfolioMutable(portfolioName)) {
      FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
      return flexiblePortfolio.getPortfolioPerformance(portfolioName, startDate, endDate);
    } else {
      PortfolioInterface portfolio = getPortfolio(portfolioName);
      return portfolio.getPortfolioPerformance(portfolioName, startDate, endDate);
    }
  }


  /**
   * Checks if the specified portfolio is mutable, meaning it can be modified.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return true if the portfolio exists and is mutable; false otherwise.
   */
  @Override
  public boolean isPortfolioMutable(String portfolioName) {
    FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
    return flexiblePortfolio != null;
  }

  @Override
  public void invest(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate date) {
    FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
    if (strategyName.equalsIgnoreCase("dollarCostAveraging")) {
      Strategy strategy = new DollarCostAveragingStrategy(flexiblePortfolio);
      strategy.invest(amount, stockRatio, date);
    }
  }

  @Override
  public void investPeriodically(String portfolioName, String strategyName, double amount,
      HashMap<String, Double> stockRatio, LocalDate startDate,
      LocalDate endDate, long intervalDays) {
    FlexiblePortfolioInterface flexiblePortfolio = new FlexiblePortfolio(user, portfolioName);
    userFlexiblePortfolios.add(flexiblePortfolio);
    if (strategyName.equalsIgnoreCase("dollarCostAveraging")) {
      Strategy strategy = new DollarCostAveragingStrategy(flexiblePortfolio);
      strategy.investPeriodically(amount, stockRatio, startDate, endDate, intervalDays);
    }
  }


  /**
   * Checks if a portfolio with the specified name exists.
   *
   * @param portfolioName The name of the portfolio to check.
   * @return True if the portfolio exists, false otherwise.
   */
  @Override
  public boolean portfolioExists(String portfolioName) {
    ArrayList<PortfolioInterface> list1 = this.userPortfolios;
    for (int i = 0; i < list1.size(); i++) {
      if (list1.get(i).getPortfolioName().equalsIgnoreCase(portfolioName)) {
        return true;
      }
    }
    ArrayList<FlexiblePortfolioInterface> list2 = this.userFlexiblePortfolios;
    for (int i = 0; i < list2.size(); i++) {
      if (list2.get(i).getPortfolioName().equalsIgnoreCase(portfolioName)) {
        return true;
      }
    }
    return false;
  }


  private PortfolioInterface getPortfolio(String portfolioName) {
    ArrayList<PortfolioInterface> list = this.userPortfolios;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPortfolioName().equalsIgnoreCase(portfolioName)) {
        return list.get(i);
      }
    }
    return null;
  }

  private FlexiblePortfolioInterface getFlexiblePortfolio(String portfolioName) {
    ArrayList<FlexiblePortfolioInterface> list = this.userFlexiblePortfolios;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPortfolioName().equalsIgnoreCase(portfolioName)) {
        return list.get(i);
      }
    }
    return null;
  }

  /**
   * Calculates the total value of a specified portfolio on a given date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date for valuation.
   * @return The total value of the portfolio as a string.
   * @throws IOException If there is an error calculating the value.
   */
  @Override
  public String getTotalPortfolioValue(String portfolioName,
      LocalDate date) throws IOException {
    float totalValue = 0.0F;
    if (isPortfolioMutable(portfolioName)) {
      FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
      totalValue = flexiblePortfolio.totalValue(date);
      return "" + totalValue;
    } else {
      totalValue = this.getPortfolio(portfolioName).totalValue(date);
      return "" + totalValue;
    }

  }

  /**
   * Saves a new or existing portfolio with the given name and stock data.
   *
   * @param portfolioName The name of the portfolio.
   * @param stockData     The stock data for the portfolio.
   * @return A message indicating the result of the operation.
   * @throws IOException If there is an error saving the portfolio.
   */
  public String savePortfolio(String portfolioName, HashMap<String,
      ArrayList<String>> stockData) throws IOException {
    PortfolioInterface portfolio = new Portfolio(this.user, portfolioName, stockData);
    String message;
    if (portfolioExists(portfolio.getPortfolioName())) {
      message = "Portfolio already saved!";
    } else {
      String directory = "res/Data/Portfolios/";
      File directoryFile = new File(directory);
      if (!directoryFile.exists()) {
        directoryFile.mkdirs();
      }
      String userPath = directory + portfolio.getUser().getEmailId() + "/";
      Path userFolder = Paths.get(userPath);
      if (Files.exists(userFolder)) {
        ((Portfolio) portfolio).exportPortfolio(userPath);
      } else {
        Files.createDirectories(userFolder);
        ((Portfolio) portfolio).exportPortfolio(userPath);
      }
      message = "Portfolio saved to system successfully!";
    }
    userPortfolios.add(portfolio);
    return message;
  }


  /**
   * Retrieves the composition of a specified portfolio.
   *
   * @param portfolioName The name of the portfolio.
   * @return A map representing the composition of the portfolio.
   */
  @Override
  public HashMap<String, ArrayList<String>> getPortfolioComposition(String portfolioName,
      LocalDate date) {
    HashMap<String, String> composition = new HashMap<>();
    HashMap<String, ArrayList<String>> portfolioCompostion =
        new HashMap<>();
    if (isPortfolioMutable(portfolioName)) {
      FlexiblePortfolioInterface flexiblePortfolio = getFlexiblePortfolio(portfolioName);
      composition = flexiblePortfolio.getComposition(date);
    } else {
      PortfolioInterface portfolio = getPortfolio(portfolioName);
      composition = portfolio.getComposition(date);
    }
    String tickerSymbol = "";
    String shares = "";
    for (String ticker : composition.keySet()) {
      ArrayList<String> newStockData = new ArrayList<String>();
      tickerSymbol = ticker;
      shares = composition.get(ticker);
      newStockData.add(shares);
      portfolioCompostion.put(tickerSymbol, newStockData);
    }
    return portfolioCompostion;
  }

  private ArrayList<PortfolioInterface> getAllPortfolios() {
    ArrayList<PortfolioInterface> portfolios = new ArrayList<PortfolioInterface>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    String line;
    String directoryPath = "res/Data/Portfolios/" + getUser().getEmailId();
    File directory = new File(directoryPath);
    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles();
      for (File file : files) {
        if (file.isFile()) {
          HashMap<String, ArrayList<String>> stockdata = new HashMap<String, ArrayList<String>>();
          try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
              String ticker = "";
              ArrayList<String> stocks = new ArrayList<>();
              if (!line.isEmpty() && i != 0) {
                String[] values = line.split(",");
                ticker = values[0];
                stocks.add(values[1]);
                stocks.add(values[2]);
              }
              if (!ticker.isEmpty()) {
                stockdata.put(ticker, stocks);
              }
              i++;
            }
          } catch (IOException e) {
            System.out.println("An error occurred while reading the CSV file.");
            e.printStackTrace();

          } catch (Exception e) {
            System.out.println("An exception has occured in getAllUsers"
                + e.getMessage());
          }
          Portfolio portfolio = new Portfolio(this.user,
              file.getName().toString().replaceFirst("[.][^.]+$",
                  ""), stockdata);
          portfolios.add(portfolio);
        }
      }
    }
    return portfolios;
  }
}
