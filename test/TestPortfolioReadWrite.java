import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.PortfolioReadWrite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import model.ModelService;
import model.Portfolio;
import model.PortfolioManagerInterface;
import model.User;
import org.junit.Test;

/**
 * Contains all functionality tests for PortfolioReadWrite.
 */
public class TestPortfolioReadWrite {

  private User testUser = new User("John", "Doe", "johndoe@gmail.com");

  @Test
  public void testExportPortfolio() {
    String[] tickers = {"AAPL", "GOOGL"};
    HashMap<String, ArrayList<String>> stockDataold = new HashMap<String, ArrayList<String>>();
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    stockvalue1.add(0, "Apple Inc");
    stockvalue1.add(1, "3");
    stockvalue1.add(2, dateString);
    stockDataold.put(tickers[0], stockvalue1);
    stockvalue2.add(0, "Alphabet Inc - Class A");
    stockvalue2.add(1, "10");
    stockvalue2.add(2, dateString);
    stockDataold.put(tickers[1], stockvalue2);
    ModelService modelService = new ModelService();
    PortfolioManagerInterface portfolioManager = modelService.createPortfolioManager(
        "johndoe@gmail.com");
    Portfolio portfolio = createTestPortfolio();
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    String directoryPath = "/Users/koppisettyeashameher/IdeaProjects";
    portfolioReadWrite.exportPortfolioToCSVFile(LocalDate.now(), "technology",
        stockDataold, directoryPath);

    File file = new File(directoryPath + "/" + portfolio.getPortfolioName() + ".csv");
    String[] values1 = {"GOOGL", "Alphabet", "10", "03-12-2024"};
    String[] values2 = {"AAPL", "Apple Inc.", "3", "03-12-2024"};
    String line;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      int i = 0;
      while ((line = br.readLine()) != null) {
        String ticker = "";
        ArrayList<String> stocks = new ArrayList<>();
        if (!line.isEmpty() && i == 1) {
          String[] values = line.split(",");
          assertEquals(values1[0], values[0]);
          assertEquals(values1[1], values[1]);
          assertEquals(values1[2], values[2]);
          assertEquals(values1[3], values[3]);

        } else if (!line.isEmpty() && i == 2) {
          String[] values = line.split(",");
          assertEquals(values2[0], values[0]);
          assertEquals(values2[1], values[1]);
          assertEquals(values2[2], values[2]);
          assertEquals(values2[3], values[3]);
        }
        i++;
      }
    } catch (IOException e) {
      System.out.println("An error occurred while reading the CSV file.");
      e.printStackTrace();

    } catch (Exception e) {
      System.out.println("An exception has occured in getAllUsers" + e.getMessage());
    }
  }

  /**
   * This method validates exportPortfolio() method in PortfolioManager class when an invalid
   * directory path is given.
   */
  @Test
  public void testExportPortfolioInvalidPath() {
    String[] tickers = {"AAPL", "GOOGL"};
    HashMap<String, ArrayList<String>> stockDataold = new HashMap<String, ArrayList<String>>();
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    stockvalue1.add(0, "Apple Inc");
    stockvalue1.add(1, "3");
    stockvalue1.add(2, dateString);
    stockDataold.put(tickers[0], stockvalue1);
    stockvalue2.add(0, "Alphabet Inc - Class A");
    stockvalue2.add(1, "10");
    stockvalue2.add(2, dateString);
    stockDataold.put(tickers[1], stockvalue2);
    Portfolio portfolio = createTestPortfolio();
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    String directoryPath = "/Users/koppisettyeashameher/Ideajhbjh";
    portfolioReadWrite.exportPortfolioToCSVFile(LocalDate.now(), "technology",
        stockDataold, directoryPath);
    assertEquals("technology", portfolio.getPortfolioName());
  }

  /**
   * This method validates importPortfolio() method in PortfolioManager class.
   */
  @Test
  public void testImportPortfolio() {
    ModelService modelService = new ModelService();
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    PortfolioManagerInterface portfolioManager = modelService.createPortfolioManager(
        "johndoe@gmail.com");
    try {
      portfolioReadWrite.importPortfolio(
          "/Users/koppisettyeashameher/IdeaProjects/PDPAssignment4final/"
              + "Data/Portfolios/johndoe@gmail.com/testPortfolio2.csv");
    } catch (Exception e) {
      e.getMessage();
    }
    assertTrue(portfolioManager.portfolioExists("testPortfolio2"));
  }


  /**
   * This method validates importPortfolio() method in PortfolioManager class when the file path
   * entered is invalid.
   */
  @Test
  public void testImportPortfolioInvalidPath() {
    ModelService modelService = new ModelService();
    PortfolioReadWrite portfolioReadWrite = new PortfolioReadWrite();
    PortfolioManagerInterface portfolioManager = modelService.createPortfolioManager(
        "johndoe@gmail.com");
    try {
      portfolioReadWrite.importPortfolio(
          "/Users/koppisettyeashameher/IdeaProjects/PDPAssignment4final"
              + "/Data/Portfolios/johndoe@gmail.com/testPnjkortfolio2.csv");
    } catch (Exception e) {
      e.getMessage();
    }
    assertFalse(portfolioManager.portfolioExists("testPnjkortfolio2"));
  }

  /**
   * This helper method creates and returns a Portfolio with test data.
   */
  private Portfolio createTestPortfolio() {
    ArrayList<String> stockvalue1 = new ArrayList<String>();
    ArrayList<String> stockvalue2 = new ArrayList<String>();
    HashMap<String, ArrayList<String>> stockData = new HashMap<String, ArrayList<String>>();
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    String dateString = currentDate.format(formatter);
    stockvalue1.add(0, "Apple Inc");
    stockvalue1.add(1, "3");
    stockvalue1.add(2, dateString);
    stockData.put("AAPL", stockvalue1);
    stockvalue2.add(0, "Alphabet Inc - Class A");
    stockvalue2.add(1, "10");
    stockvalue2.add(2, dateString);
    stockData.put("GOOGL", stockvalue2);
    Portfolio portfolio = new Portfolio(testUser, "testPortfolio1", stockData);
    return portfolio;
  }


}
