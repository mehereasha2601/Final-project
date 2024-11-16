import java.util.ArrayList;
import java.util.HashMap;
import view.ViewInterface;

/**
 * Mock implementation of View to test store and test controller outputs.
 */
public class MockViewInterface implements ViewInterface {

  private StringBuilder messages = new StringBuilder();
  private StringBuilder errors = new StringBuilder();

  /**
   * Stores a given message for later retrieval.
   *
   * @param message The message to be stored.
   */
  @Override
  public void printValue(String message) {
    // Append the message to the messages StringBuilder
    messages.append(message).append("\n"); // Use newline to separate messages
  }

  /**
   * Stores key-value pairs from a given HashMap as messages for later retrieval.
   *
   * @param hashMap The HashMap containing the messages to be stored.
   */
  @Override
  public void printValue(HashMap<String, String> hashMap) {
    for (String s : hashMap.keySet()) {
      messages.append(s + ":" + hashMap.get(s)).append("\n");
    }

  }

  /**
   * Stores a given error message for later retrieval.
   *
   * @param message The error message to be stored.
   */
  @Override
  public void printError(String message) {
    // Append the message to the errors StringBuilder
    errors.append(message).append("\n"); // Use newline to separate messages
  }

  /**
   * Stores stock data from a given HashMap as messages for later retrieval.
   *
   * @param hashMap The HashMap containing the stock data to be stored.
   */
  @Override
  public void printStockData(HashMap<String, ArrayList<String>> hashMap) {
    for (String s : hashMap.keySet()) {
      messages.append(s + ":" + hashMap.get(s)).append("\n");
    }
  }

  /**
   * Retrieves all stored messages as a single concatenated String.
   *
   * @return A String containing all stored messages.
   */
  // Method to get all messages concatenated as a single String
  public String getMessages() {
    return messages.toString();
  }

  /**
   * Retrieves all stored error messages as a single concatenated String.
   *
   * @return A String containing all stored error messages.
   */
  // Method to get all error messages concatenated as a single String
  public String getErrors() {
    return errors.toString();
  }

  /**
   * Clears all stored messages and error messages. Useful for resetting the state between tests.
   */
  // Optional: reset method to clear messages and errors between tests
  public void reset() {
    messages.setLength(0);
    errors.setLength(0);
  }
}
