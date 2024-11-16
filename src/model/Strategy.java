package model;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Defines the strategy for investing in portfolios. Implementations of this interface should
 * specify how investments are made, including one-time and periodic investments.
 */
public interface Strategy {

  /**
   * Invests a specified amount across stocks in a portfolio according to a given ratio on a
   * specific date.
   *
   * @param amount     The amount of money to invest.
   * @param stockRatio A map where keys are stock tickers and values are the ratio of the total
   *                   investment to allocate to each ticker.
   * @param date       The date on which the investment occurs.
   */
  void invest(double amount, HashMap<String, Double> stockRatio,
      LocalDate date);

  /**
   * Periodically invests a specified amount across stocks in a portfolio according to a given
   * ratio, starting and ending on specified dates, with a specific interval between investments.
   *
   * @param amount       The amount of money to invest in each interval.
   * @param stockRatio   A map where keys are stock tickers and values are the ratio of the total
   *                     investment to allocate to each ticker.
   * @param startDate    The start date for the periodic investment.
   * @param endDate      The end date for the periodic investment.
   * @param intervalDays The number of days between each investment.
   */
  void investPeriodically(double amount, HashMap<String, Double> stockRatio,
      LocalDate startDate, LocalDate endDate, long intervalDays);
}
