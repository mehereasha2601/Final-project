import java.io.IOException;
import model.ModelInterface;
import model.PortfolioManagerInterface;
import model.UserManagerInterface;


/**
 * Mock implementation of ModelInterface to test controller inputs and outputs.
 */
public class MockModelInterface implements ModelInterface {

  public StringBuilder log;

  public MockPortfolioManagerInterface portfolioManager;

  public MockUserManagerInterface userManager;

  public MockStockAnalysisInterface stockAnalysis;

  public MockModelInterface(StringBuilder log) {
    this.log = log;
  }

  @Override
  public PortfolioManagerInterface createPortfolioManager(String emailId) {
    // Return a mock or simple stub of PortfolioManagerInterface
    log.append("Create Portfolio Manager called with input " + emailId + ".");
    MockPortfolioManagerInterface portfolioManager = new MockPortfolioManagerInterface(log);
    this.portfolioManager = portfolioManager;
    return portfolioManager;

  }

  @Override
  public UserManagerInterface createUserManager() {
    // Return a mock or simple stub of UserManagerInterface
    log.append("Create User Manager called.");
    MockUserManagerInterface userManager = new MockUserManagerInterface(log);
    this.userManager = userManager;
    return userManager;
  }

  @Override
  public MockStockAnalysisInterface stockAnalysis(String ticker) throws IOException {
    log.append("Create stock analysis called.");
    MockStockAnalysisInterface stockAnalysis = new MockStockAnalysisInterface(ticker, log);
    this.stockAnalysis = stockAnalysis;
    return stockAnalysis;
  }
}
