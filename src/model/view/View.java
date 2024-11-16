package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the ViewInterface, providing methods to display information to the user.
 * <p>
 * This class offers functionalities to print various types of data to the console, including simple
 * messages, key-value pairs, and structured stock data. It also includes a method for displaying
 * error messages.
 * </p>
 */
public class View implements ViewInterface {

  /**
   * Prints a simple message to the console.
   *
   * @param value The message to be printed.
   */
  @Override
  public void printValue(String value) {
    System.out.println(value);
    System.out.flush();
    System.err.flush();
  }

  /**
   * Prints each entry of a HashMap as a key-value pair.
   *
   * @param hashMap The HashMap containing the data to be printed.
   */
  @Override
  public void printValue(HashMap<String, String> hashMap) {
    for (String s : hashMap.keySet()) {
      printValue(s + ":" + hashMap.get(s));
    }
  }

  /**
   * Prints stock data contained within a HashMap in a tabulated format.
   *
   * @param hashMap The HashMap with stock data to be displayed.
   */
  @Override
  public void printStockData(HashMap<String, ArrayList<String>> hashMap) {
    StringBuilder tableBuilder = new StringBuilder();
    String header = String.format("%-20s %-20s%n", "Ticker Symbol", "Number of Shares");
    tableBuilder.append(header);
    for (Map.Entry<String, ArrayList<String>> entry : hashMap.entrySet()) {
      String ticker = entry.getKey();
      ArrayList<String> stockInfo = entry.getValue();
      String numberOfShares = stockInfo.get(0);
      String row = String.format("%-20s  %-20s%n", ticker, numberOfShares);
      tableBuilder.append(row);
    }
    printValue(tableBuilder.toString());
  }

  /**
   * Prints an error message to the console using the standard error stream.
   *
   * @param value The error message to be printed.
   */
  public void printError(String value) {

    System.err.println(value);
    System.out.flush();
    System.err.flush();
  }


}
