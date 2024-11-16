package view;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines the View part of the MVC (Model-View-Controller) architecture.
 * <p>
 * This interface specifies methods for displaying various types of information to the user,
 * including simple text messages, key-value pairs from a HashMap, structured stock data, and error
 * messages.
 * </p>
 */
public interface ViewInterface {

  /**
   * Displays a simple text message to the user.
   *
   * @param value The text message to be displayed.
   */
  public void printValue(String value);

  /**
   * Displays the contents of a HashMap as key-value pairs.
   * <p>
   * Intended for displaying simple data structures in a readable format.
   * </p>
   *
   * @param hashMap The HashMap containing the data to be displayed.
   */
  public void printValue(HashMap<String, String> hashMap);

  /**
   * Displays an error message to the user.
   * <p>
   * This method should be used to differentiate error messages from regular information.
   * </p>
   *
   * @param value The error message to be displayed.
   */
  public void printError(String value);

  /**
   * Displays structured stock data from a HashMap in a tabulated format.
   * <p>
   * Each entry in the HashMap represents a stock, with the key being the ticker symbol and the
   * value being an ArrayList containing the stock's details, such as company name and number of
   * shares.
   * </p>
   *
   * @param hashMap The HashMap with the stock data to be displayed.
   */
  public void printStockData(HashMap<String, ArrayList<String>> hashMap);
}
