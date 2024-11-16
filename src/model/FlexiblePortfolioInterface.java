package model;

import java.time.LocalDate;

/**
 * Extends PortfolioInterface to include operations for buying and selling stocks, and calculating
 * the cost basis of the portfolio in history. These operations allow for dynamic management of the
 * portfolio's composition over time.
 */
public interface FlexiblePortfolioInterface extends PortfolioInterface {

  /**
   * Buys a specified number of shares of a given stock on a certain date. If the stock already
   * exists in the portfolio for that date, adds to the share count; otherwise, a new stock entry is
   * created. Future dates reflect this purchase.
   *
   * @param ticker The ticker symbol of the stock to buy.
   * @param shares The number of shares to buy.
   * @param date   The date of the purchase.
   */
  public void buy(String ticker, double shares, LocalDate date);

  /**
   * Sells a specified number of shares of a given stock on a certain date. Updates the portfolio to
   * reflect the sale on the given date and adjusts future portfolio compositions accordingly.
   *
   * @param ticker The ticker symbol of the stock to sell.
   * @param shares The number of shares to sell.
   * @param date   The date of the sale.
   */
  public void sell(String ticker, double shares, LocalDate date);

  /**
   * Calculates the cost basis of the portfolio up to a specific date.
   *
   * @param portfolioName The name of the portfolio.
   * @param date          The date up to which to calculate the cost basis.
   * @return The cost basis of the portfolio.
   */
  public float costBasis(String portfolioName, LocalDate date);
}
