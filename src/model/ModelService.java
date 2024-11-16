package model;

import java.io.IOException;

/**
 * Provides the concrete implementation of the {@link ModelInterface}, handling the creation and
 * management of portfolio and users.
 * <p>
 * Model service class represents the Model in the MVC architecture. It facilitates the creation of
 * managers for portfolios and users, linking data management capabilities with the rest of the
 * application.
 * </p>
 */
public class ModelService implements ModelInterface {

  /**
   * This method instantiates and returns a {@link PortfolioManager} dedicated to handling portfolio
   * operations associated with the specified email ID.
   * </p>
   *
   * @param emailId The email ID for which the portfolio manager is created.
   * @return An instance of {@link PortfolioManagerInterface}.
   */
  @Override
  public PortfolioManagerInterface createPortfolioManager(String emailId) {
    PortfolioManagerInterface portfolioManagerInterface = new PortfolioManager(emailId);
    return portfolioManagerInterface;
  }

  /**
   * This method instantiates and returns a {@link UserManager} dedicated to handling user account
   * operations, providing access to user data.
   * </p>
   *
   * @return An instance of {@link UserManagerInterface}.
   */
  @Override
  public UserManagerInterface createUserManager() {
    UserManagerInterface userManagerInterface = new UserManager();
    return userManagerInterface;
  }

  /**
   * This method creates and returns a StockAnalysis object capable of analyzing stock data,
   * including historical performance and trends.
   *
   * @param ticker The ticker symbol of the stock to analyze.
   * @return An instance of StockAnalysisInterface for the specified stock analysis.
   * @throws IOException If there is an error in retrieving or processing the stock data.
   */
  @Override
  public StockAnalysisInterface stockAnalysis(String ticker) throws IOException {
    StockAnalysisInterface stockAnalysisInterface = new StockAnalysis(ticker);
    return stockAnalysisInterface;
  }
}
