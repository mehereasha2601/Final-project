package model;

import java.io.IOException;

/**
 * Model Interfaces specifies the model's responsibilities in managing the application's data and
 * logic. It provides methods to create instances of portfolio manager and user manager as well as
 * providing stock analysis functionalities.
 */
public interface ModelInterface {

  /**
   * Creates a new PortfolioManager instance associated with a specific email ID. The
   * PortfolioManager is responsible for managing portfolios, including operations like buying and
   * selling stocks.
   *
   * @param emailId The email ID associated with the portfolio manager to be created.
   * @return A PortfolioManagerInterface instance for managing portfolios.
   */
  public PortfolioManagerInterface createPortfolioManager(String emailId);

  /**
   * Creates a new UserManager instance for managing user accounts. The UserManager is responsible
   * for operations such as user profile management, and access control.
   *
   * @return A UserManagerInterface instance for managing user accounts.
   */
  public UserManagerInterface createUserManager();

  /**
   * Provides functionality for analyzing stock data based on a given ticker symbol. This method
   * facilitates accessing detailed stock analysis, including historical data and trends.
   *
   * @param ticker The ticker symbol of the stock to be analyzed.
   * @return A StockAnalysisInterface instance for conducting stock analysis.
   * @throws IOException If there is an issue retrieving stock data.
   */
  public StockAnalysisInterface stockAnalysis(String ticker) throws IOException;
}
