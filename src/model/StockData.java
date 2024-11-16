package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Handles the retrieval and management of stock data for a given ticker symbol. This class provides
 * functionality to fetch historical stock data, update it, and query specific stock values by date.
 * It supports operations such as getting the opening and closing values of a stock, and fetching
 * closing values over a specified period.
 */
public class StockData implements StockDataInterface {

  private String ticker;

  /**
   * Constructs a new StockData instance for a given ticker.
   *
   * @param ticker The stock ticker symbol.
   */
  public StockData(String ticker) {
    this.ticker = ticker;
  }

  /**
   * Fetches and stores historical stock data from an external source for the specified ticker
   * symbol.
   *
   * @throws IOException If there is an error during data retrieval or file operations.
   */
  @Override
  public void getHistoricalStockData() throws IOException {
    String apiKey = "7MCT9MHFXA54XWEP";
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";
    File file = new File(fileName);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    try (FileWriter fileWriter = new FileWriter(fileName, false)) {
      fileWriter.append("Date,Open,Close,Volume");
      fileWriter.append("\n");
      URL url;
      try {
        url = new URL("https://www.alphavantage.co/query?function"
            + "=TIME_SERIES_DAILY&outputsize=full&symbol="
            + ticker + "&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("The AlphaVantage API has "
            + "either changed or no longer works.");
      }

      try (InputStream in = url.openStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
          String[] data = line.split(",");
          if (data.length > 4) {
            String date = data[0];
            String open = data[1];
            String close = data[4];
            String volume = data[5];
            fileWriter.append(date).append(",").append(open).append(",").append(close).append(",")
                .append(volume).append("\n");
          }
        }
      } catch (IOException e) {
        System.err.println("No price data found for " + ticker);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the local historical stock data for the specified ticker up to a given date if
   * necessary. If the local data is already up-to-date, no action is taken.
   *
   * @param date The date up to which the historical data should be updated.
   * @throws IOException If there is an error during data retrieval or file operations.
   */
  @Override
  public void updateHistoricalStockData(LocalDate date) throws IOException {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";
    Path filePath = Paths.get(fileName);

    if (Files.exists(filePath)) {
      try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        reader.readLine();
        String firstDataRow = reader.readLine();
        if (firstDataRow != null) {
          String[] data = firstDataRow.split(",");
          LocalDate fileDate = LocalDate.parse(data[0], formatter);
          if (!date.isBefore(currentDate.minusDays(1)) && date.isBefore(fileDate)) {
            return;
          } else {
            this.getHistoricalStockData();
          }
        } else {
          this.getHistoricalStockData();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      this.getHistoricalStockData();
    } else {
      this.getHistoricalStockData();
    }
  }

  /**
   * Retrieves the closing value of the stock for a specified date.
   *
   * @param date The date for which to retrieve the closing value.
   * @return The closing value of the stock on the specified date.
   * @throws IOException If there is an error reading the data.
   */
  @Override
  public float getStockClosingValue(LocalDate date) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String queryDate = date.format(formatter);
    String closingValue = "0";
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[0].equals(queryDate)) {
          closingValue = data[1];
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      closingValue = "0";
    }
    return Float.valueOf(closingValue);
  }

  /**
   * Retrieves the opening value of the stock for a specified date.
   *
   * @param date The date for which to retrieve the opening value.
   * @return The opening value of the stock on the specified date.
   * @throws IOException If there is an error reading the data.
   */
  @Override
  public float getStockOpeningValue(LocalDate date) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String queryDate = date.format(formatter);
    String openingValue = "0";
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[0].equals(queryDate)) {
          openingValue = data[2];
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      openingValue = "0";
    }
    return Float.valueOf(openingValue);
  }

  /**
   * Fetches the closing values of the stock for a number of days before a specified date.
   *
   * @param date The end date for the period.
   * @param days The number of days before the end date for which to retrieve closing values.
   * @return A map of dates to closing values for the specified number of days.
   * @throws IOException If there is an error reading the data or if insufficient data is found.
   */
  @Override
  public HashMap<LocalDate, Float> getStockClosingValues(LocalDate date, int days)
      throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    HashMap<LocalDate, Float> closingValues = new HashMap<>();
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      reader.readLine();

      int collectedDays = 0;
      while ((line = reader.readLine()) != null && collectedDays < days) {
        String[] data = line.split(",");
        LocalDate dataDate = LocalDate.parse(data[0], formatter);

        if (!dataDate.isBefore(date)) {
          continue;
        }

        Float value = Float.valueOf(data[1]);
        closingValues.put(dataDate, value);
        collectedDays++;

        if (collectedDays >= days) {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (closingValues.size() < days) {
      throw new IOException(
          "Insufficient data: requested " + days
              + " days, but only found " + closingValues.size());
    }

    return closingValues;
  }

  /**
   * Retrieves the closing values of the stock between two dates.
   *
   * @param startDate The start date of the period.
   * @param endDate   The end date of the period.
   * @return A map of dates to closing values within the specified period.
   * @throws IOException If there is an error reading the data.
   */
  @Override
  public HashMap<LocalDate, Float> getStockClosingValues(LocalDate startDate, LocalDate endDate)
      throws IOException {
    //updateHistoricalStockData(endDate);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    HashMap<LocalDate, Float> closingValues = new HashMap<>();
    String fileName = "res/Data/HistoricalData/" + ticker + ".csv";
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        LocalDate dataDate = LocalDate.parse(data[0], formatter);
        if (!dataDate.isBefore(startDate) && !dataDate.isAfter(endDate)) {
          Float value = Float.valueOf(data[1]);
          closingValues.put(dataDate, value);
        } else if (dataDate.isBefore(startDate)) {
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return closingValues;
  }
}
