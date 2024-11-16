package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines the interface for reading and writing portfolio data. This interface provides the
 * blueprint for implementing classes to manage portfolio persistence, including operations for
 * loading portfolio information from a data source and saving updates back to it.
 */
public interface PortfolioReadWriteInterface {

  /**
   * Exports portfolio data to a CSV file within the specified directory.
   *
   * @param portfolioName        The name of the portfolio to be exported.
   * @param portfolioComposition A HashMap containing the portfolio's composition.
   * @param dataDirectory        The directory where the portfolio CSV file will be saved.
   * @return A message indicating the result of the operation.
   */
  public String exportPortfolioToCSVFile(LocalDate date, String portfolioName,
                                         HashMap<String, ArrayList<String>> portfolioComposition,
                                         String dataDirectory);


  /**
   * Extracts the portfolio name from a given file path.
   *
   * @param filePath The file path containing the portfolio CSV file.
   * @return The portfolio name derived from the file name.
   */
  public String portfolioNameFromCSVFile(String filePath);


  /**
   * Imports portfolio data from a CSV file.
   *
   * @param filePath The path to the CSV file containing portfolio data.
   * @return A HashMap representing the portfolio's composition.
   * @throws IOException If there is an issue reading from the file.
   */
  public HashMap<String, ArrayList<String>> importPortfolio(String filePath)
          throws IOException;


  /**
   * Checks if a given stock ticker symbol is valid.
   *
   * @param ticker The stock ticker symbol to be validated.
   * @return true if the ticker symbol is valid and exists; false otherwise.
   */
  public boolean isValidTicker(String ticker);


  /**
   * Saves the investment strategy details to a CSV file within the program.
   * The CSV file is named after the portfolio and contains details such as investment amount,
   * stock allocation ratios, start and end dates, and investment interval.
   *
   * @param portfolioName The name of the portfolio for which the strategy is being saved.
   * @param strategyName  The name of the strategy to be saved.
   * @param amount        The total investment amount.
   * @param stockRatio    A map containing stock tickers and their respective investment ratios.
   * @param startDate     The start date of the investment period.
   * @param endDate       The end date of the investment period.
   * @param intervalDays  The number of days between successive investments.
   */
  public void saveStrategy(String portfolioName, String strategyName,
                           double amount, HashMap<String, Double> stockRatio,
                           LocalDate startDate, LocalDate endDate, long intervalDays);

  /**
   * Imports strategy details from a CSV file into a map of parameters. The method assumes the CSV
   * follows a specific format with a header and a single row of data detailing the strategy.
   *
   * @param filePath The path to the CSV file to be imported.
   * @throws IOException If an I/O error occurs reading from the file.
   */
  public HashMap<String, Object> importStrategyFromCSV(String filePath) throws IOException;

  /**
   * Checks if a strategy file already exists for the given portfolio and strategy names.
   *
   * @param portfolioName The name of the portfolio.
   * @param strategyName  The name of the strategy.
   * @return True if the strategy file exists, false otherwise.
   */
  public boolean strategyExists(String portfolioName, String strategyName);

  /**
   * Exports an existing strategy CSV file to a specified directory. If a file with the same name
   * exists in the destination directory, it will be overwritten.
   *
   * @param portfolioName The name of the portfolio whose strategy is being exported.
   * @param strategyName  The name of the strategy being exported.
   * @param directoryPath The path to the directory where the strategy file should be exported.
   *                      The directory will be created if it does not exist.
   */
  public void exportStrategyToCSV(String portfolioName, String strategyName, String directoryPath);
}
