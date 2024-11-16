package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a dollar-cost averaging strategy for investing in a flexible portfolio.
 * This strategy involves investing a fixed amount of money at regular intervals, regardless of the
 * stock price, which can potentially reduce the impact of volatility.
 */
public class DollarCostAveragingStrategy implements Strategy {

  private FlexiblePortfolioInterface flexiblePortfolioInterface;

  /**
   * Constructs a DollarCostAveragingStrategy with a given flexible portfolio.
   *
   * @param flexiblePortfolioInterface An interface to the flexible portfolio where investments will
   *                                   be made.
   */
  public DollarCostAveragingStrategy(FlexiblePortfolioInterface flexiblePortfolioInterface) {
    this.flexiblePortfolioInterface = flexiblePortfolioInterface;
  }

  @Override
  public void invest(double amount, HashMap<String, Double> stockRatio, LocalDate date) {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid date. Try earlier Date");
    }

    HashMap<String, String> composition = flexiblePortfolioInterface.getComposition(date);
    for (Map.Entry<String, Double> entry : stockRatio.entrySet()) {
      String ticker = entry.getKey();
      if (!(composition.containsKey(ticker))) {
        throw new IllegalArgumentException("Trying to invest in a non-exiting stock" + ticker
                + ". Please buy the stock first or try investing on another date.");
      }
      Double ratio = entry.getValue();
      if (ratio < 0.00) {
        throw new IllegalArgumentException("Weighted ratio cannot be negative!"
                + "Please change the value for stock " + ticker + ".");
      }
    }
    buyWeightedRatioStocks(amount, stockRatio, date);
  }

  private void buyWeightedRatioStocks(double amount, HashMap<String, Double> stockRatio,
                                      LocalDate date) {
    double sum = 0.0;
    double stockShares = 0.0;
    for (Double ratio : stockRatio.values()) {
      sum += ratio;
    }
    if (Math.abs(sum - 1.0) > 0.000001) {
      throw new IllegalArgumentException("The sum of the investment ratios must equal 1.");
    }
    for (Map.Entry<String, Double> entry : stockRatio.entrySet()) {
      String ticker = entry.getKey();
      Double ratio = entry.getValue();
      double stockInvestment = amount * ratio;
      try {
        float stockClosingValue = flexiblePortfolioInterface.getClosingValue(ticker, date);
        if (stockClosingValue == 0.0) {
          throw new IllegalArgumentException("Cannot invest on this date," +
                  " please try again on another date.");
        }
        stockShares = stockInvestment / stockClosingValue;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      flexiblePortfolioInterface.buy(ticker, stockShares, date);
    }
  }

  @Override
  public void investPeriodically(double amount, HashMap<String, Double> stockRatio,
                                 LocalDate startDate, LocalDate endDate, long intervalDays) {
    if (endDate == null) {
      endDate = LocalDate.now();
    }

    if (startDate == null) {
      throw new IllegalArgumentException("Invalid start date.");
    }

    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }

    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Can't invest on this date. Try earlier Date");
    }

    if (intervalDays > ChronoUnit.DAYS.between(startDate, endDate)) {
      throw new IllegalArgumentException("Interval days cannot be more than the difference"
              + " between start date and end date.");
    }

    double sum = 0.0;
    for (Double ratio : stockRatio.values()) {
      sum += ratio;
    }
    if (Math.abs(sum - 1.0) > 0.000001) {
      throw new IllegalArgumentException("The sum of the investment ratios must equal 1.");
    }

    for (Map.Entry<String, Double> entry : stockRatio.entrySet()) {
      String ticker = entry.getKey();
      Double ratio = entry.getValue();
      if (ratio < 0.00) {
        throw new IllegalArgumentException("Weighted ratio cannot be negative!"
                + "Please change the value for stock " + ticker + ".");
      }
    }

    while (!endDate.isBefore(startDate)) {
      boolean success = false;
      int retryCount = 0;
      int maxRetries = 7;

      while (!success && retryCount < maxRetries) {
        try {
          buyWeightedRatioStocks(amount, stockRatio, startDate);
          success = true;
        } catch (Exception e) {
          startDate = startDate.plusDays(1);
          retryCount++;
        }
      }
      startDate = startDate.plusDays(intervalDays);
    }
  }

}
