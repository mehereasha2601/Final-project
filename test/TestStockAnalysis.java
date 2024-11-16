import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import model.StockAnalysis;
import org.junit.Before;
import org.junit.Test;


/**
 * This class all has tests to validate the functionality of the Stock Analysis class.
 */
public class TestStockAnalysis {

  private StockAnalysis stockAnalysis;

  @Before
  public void setUp() {
    try {
      stockAnalysis = new StockAnalysis("AAPL");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetStockTrendOnDate() {
    String trend = null;
    try {
      trend = stockAnalysis.getStockTrendOnDate(LocalDate.of(2023, 1, 1));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(trend);
    assertEquals("Neither Gain nor Lose", trend);
  }


  @Test
  public void testGetStockTrend() {
    String trend = null;
    try {
      trend = stockAnalysis.getStockTrend(LocalDate.of(2023, 1, 1),
          LocalDate.of(2023, 1, 31));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(trend);
    assertEquals("Lose", trend);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetStockTrendWithInvalidDates() {
    try {
      stockAnalysis.getStockTrend(LocalDate.of(2023, 2, 1),
          LocalDate.of(2023, 1, 31));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetMovingAverage() {
    float movingAverage = 0;
    try {
      movingAverage = stockAnalysis.getMovingAverage(LocalDate.of(2023, 1,
          1), 10);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertTrue(movingAverage > 0);
    assertEquals(131.88870, movingAverage, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMovingAverageWithInvalidDays() {
    try {
      stockAnalysis.getMovingAverage(LocalDate.of(2023, 1, 1), 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Test
  public void testGetCrossoversDaysSuccess() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 3, 31);
    HashMap<LocalDate, String> result = null;
    try {
      result = stockAnalysis.getCrossoversDays(startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(result);
    assertEquals(
        "{2023-03-14=Buy, 2023-03-13=Sell, 2023-01-20=Buy, "
            + "2023-01-19=Sell, 2023-03-03=Buy, 2023-01-18=Buy, 2023-03-02=Sell}",
        result.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCrossoversDaysWithNullStartDate() {
    LocalDate endDate = LocalDate.of(2023, 3, 31);
    try {
      stockAnalysis.getCrossoversDays(null, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCrossoversDaysWithNullEndDate() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    try {
      stockAnalysis.getCrossoversDays(startDate, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Test
  public void testGetMovingCrossoversDaysSuccess() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 3, 31);
    int x = 5;
    int y = 10;
    HashMap<LocalDate, String> result = null;
    try {
      result = stockAnalysis.getMovingCrossoversDays(startDate, endDate, x,
          y);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(result);
    assertEquals(
        "{2023-01-12=Buy, 2023-03-08=Buy,"
            + " 2023-02-23=Sell, 2023-03-21=Buy, 2023-03-17=Sell}",
        result.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMovingCrossoversDaysWithInvalidPeriods() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 3, 31);
    try {
      stockAnalysis.getMovingCrossoversDays(startDate, endDate, 10, 5);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Test
  public void testGetStockPerformanceDays() {
    // Assume this range has stock performance data
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 1, 15);
    String result = null;
    try {
      result = stockAnalysis.getStockPerformance(startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals("Jan 03 2023: *******************************************\n"
        + "Jan 04 2023: ******************************************\n"
        + "Jan 05 2023: ******************************************\n"
        + "Jan 06 2023: ******************************************\n"
        + "Jan 09 2023: *******************************************\n"
        + "Jan 10 2023: *******************************************\n"
        + "Jan 11 2023: *******************************************\n"
        + "Jan 12 2023: ********************************************\n"
        + "Jan 13 2023: ********************************************\n"
        + "Scale: * = 3, representing the value in dollars.\n", result);
  }

  @Test
  public void testGetStockPerformanceMonths() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 5, 16);
    String result = null;
    try {
      result = stockAnalysis.getStockPerformance(startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals("Jan 03 2023: ********************************\n"
        + "Jan 09 2023: ********************************\n"
        + "Jan 13 2023: *********************************\n"
        + "Jan 20 2023: *********************************\n"
        + "Jan 26 2023: ***********************************\n"
        + "Feb 01 2023: ***********************************\n"
        + "Feb 07 2023: *************************************\n"
        + "Feb 13 2023: *************************************\n"
        + "Feb 17 2023: **************************************\n"
        + "Feb 24 2023: ************************************\n"
        + "Mar 02 2023: ************************************\n"
        + "Mar 08 2023: **************************************\n"
        + "Mar 14 2023: *************************************\n"
        + "Mar 20 2023: **************************************\n"
        + "Mar 24 2023: ***************************************\n"
        + "Mar 30 2023: ****************************************\n"
        + "Apr 05 2023: *****************************************\n"
        + "Apr 12 2023: ****************************************\n"
        + "Apr 18 2023: *****************************************\n"
        + "Apr 24 2023: *****************************************\n"
        + "Apr 28 2023: ******************************************\n"
        + "May 04 2023: *****************************************\n"
        + "May 10 2023: *******************************************\n"
        + "May 16 2023: ******************************************\n"
        + "Scale: * = 4, representing the value in dollars.\n", result);
  }

  @Test
  public void testGetStockPerformanceYears() {
    LocalDate startDate = LocalDate.of(2001, 1, 1);
    LocalDate endDate = LocalDate.of(2002, 1, 16);
    String result = null;
    try {
      result = stockAnalysis.getStockPerformance(startDate, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals("Jan 02 2001: **************\n"
        + "Jan 16 2001: *****************\n"
        + "Jan 29 2001: *******************\n"
        + "Feb 09 2001: ********************\n"
        + "Feb 23 2001: ******************\n"
        + "Mar 08 2001: ********************\n"
        + "Mar 21 2001: *******************\n"
        + "Apr 03 2001: *********************\n"
        + "Apr 17 2001: *********************\n"
        + "Apr 30 2001: **************************\n"
        + "May 11 2001: ***********************\n"
        + "May 24 2001: ***********************\n"
        + "Jun 07 2001: ********************\n"
        + "Jun 20 2001: ********************\n"
        + "Jul 03 2001: ***********************\n"
        + "Jul 17 2001: ***********************\n"
        + "Jul 30 2001: *******************\n"
        + "Aug 10 2001: *******************\n"
        + "Aug 23 2001: ******************\n"
        + "Sep 06 2001: ******************\n"
        + "Sep 25 2001: ****************\n"
        + "Oct 08 2001: ***************\n"
        + "Oct 19 2001: *****************\n"
        + "Nov 01 2001: *****************\n"
        + "Nov 14 2001: *******************\n"
        + "Nov 28 2001: ********************\n"
        + "Dec 11 2001: **********************\n"
        + "Dec 24 2001: ********************\n"
        + "Jan 08 2002: **********************\n"
        + "Scale: * = 1, representing the value in dollars.\n", result);
  }

  @Test(expected = NullPointerException.class)
  public void testGetStockPerformanceWithNullStartDate() {
    LocalDate endDate = LocalDate.of(2023, 3, 31);
    try {
      stockAnalysis.getStockPerformance(null, endDate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = NullPointerException.class)
  public void testGetStockPerformanceWithNullEndDate() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    try {
      stockAnalysis.getStockPerformance(startDate, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
