package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides functionality to export a portfolio to a CSV file, search for stock names
 * within a predefined dataset, validate stock tickers, retrieve portfolio names from file paths,
 * and import portfolio data from a file.
 */
public class PortfolioReadWrite implements PortfolioReadWriteInterface {

  /**
   * Constructor for the portfolioReadWrite Class.
   */
  public PortfolioReadWrite() {
    // Creates portfolioReadWrite class object.
  }

  /**
   * Exports portfolio data to a CSV file within the specified directory.
   *
   * @param portfolioName        The name of the portfolio to be exported.
   * @param portfolioComposition A HashMap containing the portfolio's composition.
   * @param dataDirectory        The directory where the portfolio CSV file will be saved.
   * @return A message indicating the result of the operation.
   */
  @Override
  public String exportPortfolioToCSVFile(LocalDate date, String portfolioName,
                                         HashMap<String, ArrayList<String>> portfolioComposition,
                                         String dataDirectory) {
    String message;
    try {
      Path portfolioPath = Paths.get(dataDirectory);
      Path filePath = portfolioPath.resolve(portfolioName + ".csv");

      if (Files.exists(filePath)) {
        Files.delete(filePath);
        message = "Existing portfolio was overwritten. ";
      } else {
        message = "";
      }

      // Prepare the content to write
      List<String> lines = new ArrayList<>();
      lines.add("Ticker Symbol, Number of Shares, Date of Purchase");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      for (String ticker : portfolioComposition.keySet()) {
        String line = ticker + "," + portfolioComposition.get(ticker).get(0)
                + "," + date.format(formatter);
        lines.add(line);
      }

      Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      message += "Portfolio successfully saved at " + filePath;

    } catch (Exception e) {
      e.printStackTrace();
      message = "Error occurred while saving the portfolio."
              + " Please check the directory path and file permissions.";
    }
    return message;
  }

  /**
   * Checks if the provided ticker symbol exists within active stocks file.
   *
   * @param ticker The ticker symbol to validate.
   * @return true if the ticker symbol is valid, false otherwise.
   */

  public boolean isValidTicker(String ticker) {
    String apiKey = "7MCT9MHFXA54XWEP";
    try {
      URL url = new URL("https://www.alphavantage.co/query?function"
              + "=SYMBOL_SEARCH&keywords="
              + ticker + "&apikey=" + apiKey + "&datatype=csv");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) { // status code 200
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
        }
        in.close();
        return content.toString().contains(ticker);
      } else {
        System.out.println("Error in API request: " + responseCode);
        return false;
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (ProtocolException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Extracts the portfolio name from a given file path.
   *
   * @param filePath The file path containing the portfolio CSV file.
   * @return The portfolio name derived from the file name.
   */
  @Override
  public String portfolioNameFromCSVFile(String filePath) {
    String fileNameWithoutExtension = Paths.get(filePath).getFileName().toString()
            .replaceFirst("[.][^.]+$", "");
    return fileNameWithoutExtension.split("_", 2)[0];
  }

  /**
   * Imports portfolio data from a CSV file.
   *
   * @param filePath The path to the CSV file containing portfolio data.
   * @return A HashMap representing the portfolio's composition.
   * @throws IOException If there is an issue reading from the file.
   */
  @Override
  public HashMap<String, ArrayList<String>> importPortfolio(String filePath) throws IOException {
    HashMap<String, ArrayList<String>> stockData = new HashMap<>();
    try {
      List<String> lines = Files.readAllLines(Paths.get(filePath));
      for (String line : lines.subList(1, lines.size())) {
        String[] parts = line.split(",");
        if (parts.length >= 1) {
          String ticker = parts[0].trim();
          String date = parts[2].trim();
          if (isValidTicker(ticker)) {
            String shares = parts[1].trim();
            if (Double.parseDouble(shares) > 0.00) {
              ArrayList<String> details = new ArrayList<>();
              details.add(shares);
              details.add(date);
              stockData.put(ticker, details);
            } else {
              throw new IOException("Invalid Number of shares :" + shares);
            }
          } else {
            throw new IOException("Invalid ticker: " + ticker);
          }
        }
      }
    } catch (IOException e) {
      throw e;
    } catch (Exception e2) {
      throw e2;
    }
    return stockData;
  }

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
                           LocalDate startDate, LocalDate endDate, long intervalDays) {
    StringBuilder stockRatioStr = new StringBuilder();
    for (Map.Entry<String, Double> entry : stockRatio.entrySet()) {
      if (stockRatioStr.length() > 0) {
        stockRatioStr.append(";");
      }
      stockRatioStr.append(entry.getKey()).append(":").append(entry.getValue());
    }

    String startDateStr = startDate.toString();
    String endDateStr = (endDate != null) ? endDate.toString() : "";
    Path strategyPath = Paths.get("res/Data/Strategy/" + strategyName + "/");
    Path filePath = strategyPath.resolve(portfolioName + ".csv");
    String header = "amount,stockRatio,startDate,endDate,intervalDays\n";
    String data = amount + ",\"" + stockRatioStr + "\"," + startDateStr + ","
            + endDateStr + "," + intervalDays;

    try {
      Files.createDirectories(strategyPath);
      if (!Files.exists(filePath)) {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
          writer.write(header);
          writer.write(data);
        }
      }
    } catch (IOException e) {
      System.err.println("An error occurred while writing to the CSV file: " + e.getMessage());
    }
  }

  /**
   * Checks if a strategy file already exists for the given portfolio and strategy names.
   *
   * @param portfolioName The name of the portfolio.
   * @param strategyName  The name of the strategy.
   * @return True if the strategy file exists, false otherwise.
   */

  public boolean strategyExists(String portfolioName, String strategyName) {
    Path strategyPath = Paths.get("res/Data/Strategy/" + strategyName + "/");
    Path filePath = strategyPath.resolve(portfolioName + ".csv");
    return Files.exists(filePath);
  }

  /**
   * Exports an existing strategy CSV file to a specified directory. If a file with the same name
   * exists in the destination directory, it will be overwritten.
   *
   * @param portfolioName The name of the portfolio whose strategy is being exported.
   * @param strategyName  The name of the strategy being exported.
   * @param directoryPath The path to the directory where the strategy file should be exported.
   *                      The directory will be created if it does not exist.
   */

  public void exportStrategyToCSV(String portfolioName, String strategyName, String directoryPath) {
    if (!directoryPath.endsWith("/") && !directoryPath.endsWith("\\")) {
      directoryPath += System.getProperty("file.separator");
    }
    Path sourcePath = Paths.get("res/Data/Strategy/" + strategyName + "/"
            + portfolioName + ".csv");
    Path destinationPath = Paths.get(directoryPath + portfolioName + "_"
            + strategyName + ".csv");
    try {
      Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Imports strategy details from a CSV file into a map of parameters. The method assumes the CSV
   * follows a specific format with a header and a single row of data detailing the strategy.
   *
   * @param filePath The path to the CSV file to be imported.
   * @throws IOException If an I/O error occurs reading from the file.
   */

  public HashMap<String, Object> importStrategyFromCSV(String filePath) throws IOException {
    String line;
    HashMap<String, Object> params = new HashMap<>();
    String cvsSplitBy = ",";

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      br.readLine();

      if ((line = br.readLine()) != null) {
        String[] data = line.split(cvsSplitBy + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        params.put("amount", Double.parseDouble(data[0]));

        HashMap<String, Double> stockRatio = new HashMap<>();
        String stockRatioStr = data[1].replace("\"", "");
        for (String entry : stockRatioStr.split(";")) {
          String[] keyValue = entry.split(":");
          stockRatio.put(keyValue[0], Double.parseDouble(keyValue[1]));
        }
        params.put("stockRatio", stockRatio);
        params.put("startDate", LocalDate.parse(data[2]));
        params.put("endDate", !data[3].isEmpty() ? LocalDate.parse(data[3]) : null);
        params.put("intervalDays", Long.parseLong(data[4]));
      }
    }
    String fileName = Paths.get(filePath).getFileName().toString();
    String[] nameParts = fileName.replace(".csv", "").split("_", 2);
    if (nameParts.length == 2) {
      params.put("portfolioName", nameParts[0]);
      params.put("strategyName", nameParts[1]);
    }
    return params;
  }
}
