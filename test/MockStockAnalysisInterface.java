import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import model.StockAnalysisInterface;

/**
 * Mock implementation of StockAnalysis to test controller inputs and outputs.
 */
public class MockStockAnalysisInterface implements StockAnalysisInterface {

  String ticker;

  public StringBuilder log;

  /**
   * Constructs a mock stock analysis class with a log to record method calls and parameters.
   *
   * @param log    StringBuilder to log method calls for verification.
   * @param ticker a ticker string to perform stock analysis.
   */
  public MockStockAnalysisInterface(String ticker, StringBuilder log) {
    this.ticker = ticker;
    this.log = log;
  }

  @Override
  public String getStockTrendOnDate(LocalDate date) throws IOException {
    return "gain";
  }

  @Override
  public String getStockTrend(LocalDate startDate, LocalDate endDate) throws IOException {
    return "neither gain nor lose";
  }

  @Override
  public float getMovingAverage(LocalDate date, int days) throws IOException {
    return 100.0f;
  }

  @Override
  public HashMap<LocalDate, String> getCrossoversDays(LocalDate startDate, LocalDate endDate)
      throws IOException {
    return null;
  }

  @Override
  public HashMap<LocalDate, String> getMovingCrossoversDays(LocalDate startDate, LocalDate endDate,
      int x, int y) throws IOException {
    return null;
  }

  @Override
  public String getStockPerformance(LocalDate startDate, LocalDate endDate) throws IOException {
    return "Jan 03 2023: *******************************************\n"
        + "Jan 04 2023: ******************************************\n"
        + "Jan 05 2023: ******************************************\n"
        + "Jan 06 2023: ******************************************\n"
        + "Jan 09 2023: *******************************************\n"
        + "Jan 10 2023: *******************************************\n"
        + "Jan 11 2023: *******************************************\n"
        + "Jan 12 2023: ********************************************\n"
        + "Jan 13 2023: ********************************************\n"
        + "Scale: * = 3, representing the value in dollars.\n";
  }
}
