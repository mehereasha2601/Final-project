package view;

import controller.GuiControllerInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Implements the graphical user interface (GUI) for the Stock Portfolio Manager application. This
 * class provides screens for user login, portfolio management, stock analysis, and more,
 * interacting with the user through graphical components.
 */
public class GuiView implements GuiViewInterface {

  private GuiControllerInterface guiControllerInterface;

  private final JFrame mainFrame;

  private HashMap<String, ArrayList<String>> stockData = new HashMap<>();

  /**
   * Constructs the GUI view initializing the main application window.
   */
  public GuiView() {
    mainFrame = new JFrame("Stock Portfolio Manager");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(800, 750);
    mainFrame.setLayout(new BorderLayout());
  }

  @Override
  public void setController(GuiControllerInterface guiControllerInterface) {
    this.guiControllerInterface = guiControllerInterface;
  }

  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setPreferredSize(new Dimension(150, 30));
    // Set preferred size to make buttons bigger
    Font labelFont = button.getFont();
    button.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 15));
    return button;
  }

  private JLabel createLabel(String text) {
    JLabel welcomeLabel = new JLabel(text);
    Font labelFont = welcomeLabel.getFont();
    welcomeLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 17));
    welcomeLabel.setBounds(10, 20, 160, 25);
    return welcomeLabel;
  }

  private JTextField createTextField(int size) {
    JTextField textField = new JTextField(size);
    textField.setPreferredSize(new Dimension(150, 40));
    return textField;
  }

  private GridBagConstraints createGridBagConstraints() {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER; // Make component take up the whole row
    gbc.fill = GridBagConstraints.HORIZONTAL; // Make the button expand to fill the horizontal space
    gbc.insets = new Insets(10, 10, 10, 10); // Add some padding for aesthetics
    gbc.anchor = GridBagConstraints.CENTER; // Center the component in the grid cell
    return gbc;
  }

  @Override
  public void showLoginScreen() {
    mainFrame.getContentPane().removeAll();
    JPanel loginPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel welcomeLabel = createLabel("Welcome to login page!");
    JButton existingUserButton = createButton("Existing User login");
    JButton newUserButton = createButton("New User login");

    loginPanel.add(welcomeLabel, gbc);
    loginPanel.add(existingUserButton, gbc);
    loginPanel.add(newUserButton, gbc);

    mainFrame.getContentPane().setLayout(new BorderLayout());
    mainFrame.getContentPane()
        .add(loginPanel, BorderLayout.CENTER); // Ensure it's centered in the window

    existingUserButton.addActionListener(e -> existingUserLogin());
    newUserButton.addActionListener(e -> newUserLogin());

    mainFrame.setSize(800, 750);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);

  }

  private void newUserLogin() {
    mainFrame.getContentPane().removeAll();

    GridBagConstraints gbc = createGridBagConstraints();
    JPanel formPanel = new JPanel(new GridBagLayout());

    formPanel.add(createLabel("First Name:"), gbc);
    JTextField firstNameField = createTextField(20);
    formPanel.add(firstNameField, gbc);

    formPanel.add(createLabel("Last Name:"), gbc);
    JTextField lastNameField = createTextField(20);
    formPanel.add(lastNameField, gbc);

    formPanel.add(createLabel("Email:"), gbc);
    JTextField emailField = createTextField(20);
    formPanel.add(emailField, gbc);

    JLabel emailErrorLabel = new JLabel("Email format is incorrect.");
    emailErrorLabel.setForeground(Color.RED);
    emailErrorLabel.setVisible(false); // Initially hide the error message
    formPanel.add(emailErrorLabel, gbc);

    JButton submitButton = createButton("SUBMIT");
    submitButton.setPreferredSize(new Dimension(70, 40));
    formPanel.add(submitButton, gbc);

    setupEmailValidation(emailField, emailErrorLabel, submitButton);

    mainFrame.setLayout(new GridBagLayout());
    mainFrame.add(formPanel);

    submitButton.addActionListener(e -> handleNewUserSubmit(firstNameField,
        lastNameField, emailField));

    mainFrame.revalidate();
    mainFrame.repaint();

  }

  private void handleNewUserSubmit(JTextField firstNameField, JTextField lastNameField,
      JTextField emailField) {
    ArrayList<String> userData = new ArrayList<>();
    userData.add(firstNameField.getText().trim());
    userData.add(lastNameField.getText().trim());
    userData.add(emailField.getText().trim());
    String message = guiControllerInterface.newUserWorkflow(userData);
    JOptionPane.showMessageDialog(mainFrame, message);
    showMainMenu();
  }


  private void existingUserLogin() {
    mainFrame.getContentPane().removeAll();
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.gridy = 0;
    JPanel formPanel = new JPanel(new GridBagLayout());
    JLabel welcomeLabel = createLabel("Welcome to login page");
    formPanel.add(welcomeLabel);
    gbc.gridy++;
    formPanel.add(new JLabel(), gbc);
    gbc.gridy++;
    formPanel.add(new JLabel("Email:"), gbc);
    JTextField emailField = createTextField(20);
    gbc.gridy++;
    formPanel.add(emailField, gbc);
    JLabel emailErrorLabel = new JLabel("Email format is incorrect.");
    emailErrorLabel.setForeground(Color.RED);
    emailErrorLabel.setVisible(false);
    gbc.gridy++;
    formPanel.add(emailErrorLabel, gbc);
    JButton submitButton = createButton("LOGIN");
    submitButton.setPreferredSize(new Dimension(70, 40));
    formPanel.add(submitButton, gbc);

    setupEmailValidation(emailField, emailErrorLabel, submitButton);
    mainFrame.setLayout(new GridBagLayout());
    mainFrame.add(formPanel);
    setupSubmitAction(emailField, submitButton);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void setupSubmitAction(JTextField emailField, JButton submitButton) {
    submitButton.addActionListener(e -> {
      String emailID = emailField.getText().trim();
      String s = guiControllerInterface.existingUserWorkflow(emailID);
      JOptionPane.showMessageDialog(mainFrame, s);
      if (s.equalsIgnoreCase(
          "There is no user with " + emailID + ". Please create a user first.")) {
        newUserLogin();
      } else {
        showMainMenu();
      }
    });
  }

  private void setupEmailValidation(JTextField emailField, JLabel emailErrorLabel,
      JButton submitButton) {
    emailField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        update();
      }

      public void update() {
        boolean isValid = isValidEmail(emailField.getText());
        emailErrorLabel.setVisible(!isValid);
        submitButton.setEnabled(isValid);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        update();
      }
    });
  }


  @Override
  public void showMainMenu() {
    mainFrame.getContentPane().removeAll();
    JPanel menuPanel = new JPanel(new GridBagLayout());
    menuPanel.setBorder(BorderFactory.createTitledBorder("Main Menu"));

    GridBagConstraints gbc = createGridBagConstraints();
    gbc.insets = new Insets(10, 50, 10, 50);

    JButton createPortfolioButton = createButton("Create New Portfolio");
    menuPanel.add(createPortfolioButton, gbc);

    JButton viewPortfolioCompositionButton = createButton("View Portfolio Composition");
    menuPanel.add(viewPortfolioCompositionButton, gbc);

    JButton viewPortfolioTotalValueButton = createButton("View Portfolio Total Value");
    menuPanel.add(viewPortfolioTotalValueButton, gbc);

    JButton exportPortfolioButton = createButton("Export Portfolio");
    menuPanel.add(exportPortfolioButton, gbc);

    JButton importPortfolioButton = createButton("Import Portfolio");
    menuPanel.add(importPortfolioButton, gbc);

    JButton investButton = createButton("Invest for Existing Portfolio");
    menuPanel.add(investButton, gbc);

    JButton investPeriodicallyButton = createButton("Invest Periodically for New Portfolio");
    menuPanel.add(investPeriodicallyButton, gbc);

    JButton viewPortfolioPerformanceButton = createButton("View Portfolio Performance");
    menuPanel.add(viewPortfolioPerformanceButton, gbc);

    JButton buyStockButton = createButton("Buy Stock");
    menuPanel.add(buyStockButton, gbc);

    JButton sellStockButton = createButton("Sell Stock");
    menuPanel.add(sellStockButton, gbc);

    JButton portfolioCostBasisButton = createButton("View Portfolio Cost basis");
    menuPanel.add(portfolioCostBasisButton, gbc);

    JButton stockAnalysisMenuButton = createButton("View Stock Analysis Menu");
    menuPanel.add(stockAnalysisMenuButton, gbc);

    JButton logoutButton = createButton("Logout");
    menuPanel.add(logoutButton, gbc);

    try {
      mainMenuActionListeners(createPortfolioButton
          , viewPortfolioCompositionButton, viewPortfolioTotalValueButton
          , exportPortfolioButton, importPortfolioButton
          , investButton, investPeriodicallyButton
          , viewPortfolioPerformanceButton, buyStockButton
          , sellStockButton, portfolioCostBasisButton
          , stockAnalysisMenuButton, logoutButton);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainFrame, e.getMessage()
          + ". Taking you back to main menu.");
      showMainMenu();
    }
    addMenuPanel(mainFrame, menuPanel);
  }

  private void mainMenuActionListeners(JButton createPortfolioButton
      , JButton viewPortfolioCompositionButton, JButton viewPortfolioTotalValueButton
      , JButton exportPortfolioButton, JButton importPortfolioButton
      , JButton investButton, JButton investPeriodicallyButton
      , JButton viewPortfolioPerformanceButton, JButton buyStockButton
      , JButton sellStockButton, JButton portfolioCostBasisButton
      , JButton stockAnalysisMenuButton, JButton logoutButton) {
    createPortfolioButton.addActionListener(e -> createPortfolio());
    viewPortfolioCompositionButton.addActionListener(e ->
        portfolioCompositionOrTotalValue("composition"));
    viewPortfolioTotalValueButton.addActionListener(e ->
        portfolioCompositionOrTotalValue("total value"));
    exportPortfolioButton.addActionListener(e -> exportPortfolio());
    importPortfolioButton.addActionListener(e -> importPortfolio());
    investButton.addActionListener(e -> invest());
    investPeriodicallyButton.addActionListener(e -> investPeriodically());
    viewPortfolioPerformanceButton.addActionListener(e -> portfolioPerformance());
    buyStockButton.addActionListener(e -> buyOrSell("buy"));
    sellStockButton.addActionListener(e -> buyOrSell("sell"));
    portfolioCostBasisButton.addActionListener(e -> costBasis());
    stockAnalysisMenuButton.addActionListener(e -> showStockAnalysisMenu());
    logoutButton.addActionListener(e -> {
      JOptionPane.showMessageDialog(mainFrame, "You have been logged out.");
      showLoginScreen();
    });
  }

  private void addMenuPanel(JFrame mainFrame, JPanel menuPanel) {
    mainFrame.getContentPane().setLayout(new GridBagLayout());
    GridBagConstraints frameGbc = new GridBagConstraints();
    frameGbc.gridwidth = GridBagConstraints.REMAINDER;
    frameGbc.fill = GridBagConstraints.BOTH;
    frameGbc.weightx = 1.0;
    frameGbc.weighty = 1.0;
    mainFrame.getContentPane().add(menuPanel, frameGbc);
    mainFrame.setSize(800, 700);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }

  private void invest() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel("Please enter the portfolio name for investing:");
    panel.add(instructionLabel, gbc);
    JTextField portfolioNameField = new JTextField(20);
    panel.add(portfolioNameField, gbc);
    JButton submitButton = new JButton("Submit");
    submitButton.setEnabled(false);
    panel.add(submitButton, gbc);
    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);

    investPortfolioNameActionListener(portfolioNameField, submitButton);
    investSubmitButtonActionListener(portfolioNameField, submitButton);
    mainMenuButton.addActionListener(e -> showMainMenu());

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void investPortfolioNameActionListener(JTextField portfolioNameField,
      JButton submitButton) {
    portfolioNameField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        String portfolioName = portfolioNameField.getText().trim();
        submitButton.setEnabled(!portfolioName.isEmpty());
      }
    });

  }

  private void investSubmitButtonActionListener(JTextField portfolioNameField,
      JButton submitButton) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String checkResult = guiControllerInterface.portfolioCheckForInvesting(portfolioName);
      if ("mutable".equals(checkResult)) {
        enterInvestDetails(portfolioName);
      } else {
        JOptionPane.showMessageDialog(mainFrame, checkResult, "Error",
            JOptionPane.ERROR_MESSAGE);
        invest();
      }
    });
  }

  private void enterInvestDetails(String portfolioName) {
    HashMap<String, ArrayList<String>> tickers = new HashMap<>();
    try {
      tickers = guiControllerInterface.getTickersForInvest(portfolioName);
      mainFrame.getContentPane().removeAll();
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = createGridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;

      panel.add(new JLabel("Please enter the following info to invest in "
          + portfolioName + ":"), gbc);
      gbc.gridy++;

      JLabel amountLabel = new JLabel("Amount to invest in dollars:");
      JTextField amountField = new JTextField(20);
      panel.add(amountLabel, gbc);
      gbc.gridy++;
      panel.add(amountField, gbc);
      gbc.gridy++;

      HashMap<String, JTextField> tickerFields = new HashMap<>();
      tickers.keySet().forEach(ticker -> {
        gbc.gridy++;
        JLabel tickerLabel = new JLabel("Stock Ratio for " + ticker + "(0.0-1.0):");
        JTextField tickerField = new JTextField(20);
        panel.add(tickerLabel, gbc);
        gbc.gridy++;
        panel.add(tickerField, gbc);
        tickerFields.put(ticker, tickerField);
      });

      gbc.gridy++;
      JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
      JTextField dateField = new JTextField(20);
      panel.add(dateLabel, gbc);
      gbc.gridy++;
      panel.add(dateField, gbc);

      gbc.gridy++;
      JLabel errorLabel = new JLabel();
      errorLabel.setForeground(Color.RED);
      panel.add(errorLabel, gbc);
      errorLabel.setVisible(false);

      gbc.gridy++;
      JButton submitButton = new JButton("Invest");
      panel.add(submitButton, gbc);
      submitButton.setEnabled(false);

      gbc.gridy++;
      JButton mainMenuButton = new JButton("Main Menu");
      panel.add(mainMenuButton, gbc);

      DocumentListener documentListener = investDetailsDocumentListener(amountField, dateField,
          tickerFields, submitButton, errorLabel);

      amountField.getDocument().addDocumentListener(documentListener);
      dateField.getDocument().addDocumentListener(documentListener);
      tickerFields.values()
          .forEach(field -> field.getDocument().addDocumentListener(documentListener));

      investDetailsSubmitActionListener(amountField, dateField, tickerFields, submitButton,
          portfolioName);
      mainMenuButton.addActionListener(e -> showMainMenu());

      mainFrame.getContentPane().add(panel);
      mainFrame.revalidate();
      mainFrame.repaint();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainFrame,
          "Cannot invest for an empty portfolio. Buy some stocks and try again",
          "Error", JOptionPane.ERROR_MESSAGE);
      showMainMenu();
    }
  }


  private void investDetailsSubmitActionListener(JTextField amountField, JTextField dateField,
      HashMap<String, JTextField> tickerFields,
      JButton submitButton, String portfolioName) {
    submitButton.addActionListener(e -> {
      double amount = Double.parseDouble(amountField.getText().trim());
      LocalDate date = LocalDate.parse(dateField.getText().trim(),
          DateTimeFormatter.ISO_LOCAL_DATE);
      HashMap<String, Double> stockRatio = new HashMap<>();
      tickerFields.forEach((ticker, field) ->
          stockRatio.put(ticker, Double.parseDouble(field.getText().trim())));
      String resultMessage = guiControllerInterface.invest(portfolioName,
          "dollarCostAveraging", amount, stockRatio, date);
      JOptionPane.showMessageDialog(mainFrame, resultMessage, "Investment Result",
          JOptionPane.INFORMATION_MESSAGE);
      if (resultMessage.equalsIgnoreCase("Investing is done for portfolio "
          + portfolioName)) {
        showMainMenu();
      }
    });
  }

  private DocumentListener investDetailsDocumentListener(JTextField amountField,
      JTextField dateField, HashMap<String,
      JTextField> tickerFields, JButton submitButton, JLabel errorLabel) {
    return new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        String amountText = amountField.getText().trim();
        boolean isAmountValid = !amountText.isEmpty()
            && amountText.matches("\\d+(\\.\\d{1,2})?");
        boolean isDateValid = dateField.getText().matches("\\d{4}-\\d{2}-\\d{2}");
        boolean areTickerFieldsValid = tickerFields.values().stream()
            .allMatch(field -> !field.getText().trim().isEmpty());

        if (!isAmountValid) {
          errorLabel.setText("Amount must be a positive number with up to 2 decimal places.");
          errorLabel.setVisible(true);
        } else if (!isDateValid) {
          errorLabel.setText("Invalid date format. Use YYYY-MM-DD.");
          errorLabel.setVisible(true);
        } else if (!areTickerFieldsValid) {
          errorLabel.setText("All stock ratios must be filled.");
          errorLabel.setVisible(true);
        } else {
          errorLabel.setText(""); // Clear the error message
          errorLabel.setVisible(false);
        }

        submitButton.setEnabled(isAmountValid && isDateValid && areTickerFieldsValid);
        boolean allFieldsValid = !amountField.getText().trim().isEmpty() &&
            dateField.getText().matches("\\d{4}-\\d{2}-\\d{2}") &&
            tickerFields.values().stream().noneMatch(field -> field.getText().trim().isEmpty());
        submitButton.setEnabled(allFieldsValid);
      }
    };
  }


  private void investPeriodically() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel("Please enter the portfolio name for investing:");
    panel.add(instructionLabel, gbc);

    JTextField portfolioNameField = new JTextField(20);
    panel.add(portfolioNameField, gbc);

    JButton submitButton = new JButton("Submit");
    submitButton.setEnabled(false); // Initially disabled
    panel.add(submitButton, gbc);

    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);

    investPeriodicallyPortfolioDocumentListener(portfolioNameField, submitButton);
    investPeriodicallySubmitButtonDocumentListener(portfolioNameField, submitButton);
    mainMenuButton.addActionListener(e -> showMainMenu());

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void investPeriodicallyPortfolioDocumentListener(JTextField portfolioNameField,
      JButton submitButton) {
    portfolioNameField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        String portfolioName = portfolioNameField.getText().trim();
        submitButton.setEnabled(!portfolioName.isEmpty());
      }
    });

  }

  private void investPeriodicallySubmitButtonDocumentListener(JTextField portfolioNameField,
      JButton submitButton) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String checkResult = guiControllerInterface.portfolioCheckForInvesting(portfolioName);
      if ("This portfolio does not exist.".equals(checkResult)) {
        enterPeriodicInvestDetails(portfolioName);
      } else {
        if (checkResult.equalsIgnoreCase("mutable")) {
          checkResult = "This portfolio already exists.Try with some other portfolio name";
        }
        JOptionPane.showMessageDialog(mainFrame, checkResult,
            "Error", JOptionPane.ERROR_MESSAGE);
        investPeriodically();
      }
    });
  }

  private void enterPeriodicInvestDetails(String portfolioName) {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;

    panel.add(new JLabel(
        "Please enter the following info to periodically invest in "
            + portfolioName + ":"), gbc);
    gbc.gridy++;

    JLabel stockCountLabel = new JLabel("Number of Stocks:");
    JTextField stockCountField = new JTextField(20);
    gbc.gridy++;
    panel.add(stockCountLabel, gbc);
    gbc.gridy++;
    panel.add(stockCountField, gbc);
    gbc.gridy++;

    JLabel amountLabel = new JLabel("Investment Amount in dollars:");
    JTextField amountField = new JTextField(20);
    gbc.gridy++;
    panel.add(amountLabel, gbc);
    gbc.gridy++;
    panel.add(amountField, gbc);
    gbc.gridy++;

    JButton addStockButton = new JButton("Add Stock");
    JPanel stockPanel = new JPanel(new FlowLayout());

    JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
    JTextField startDateField = new JTextField(20);
    gbc.gridy++;
    panel.add(startDateLabel, gbc);
    gbc.gridy++;
    panel.add(startDateField, gbc);
    gbc.gridy++;

    JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD)"
        + ". Leave empty for ongoing strategy:");
    JTextField endDateField = new JTextField(20);
    gbc.gridy++;
    panel.add(endDateLabel, gbc);
    gbc.gridy++;
    panel.add(endDateField, gbc);
    gbc.gridy++;

    JLabel intervalDaysLabel = new JLabel("Interval Days:");
    JTextField intervalDaysField = new JTextField(20);
    gbc.gridy++;
    panel.add(intervalDaysLabel, gbc);
    gbc.gridy++;
    panel.add(intervalDaysField, gbc);
    gbc.gridy++;

    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    panel.add(errorLabel, gbc);
    errorLabel.setVisible(false);
    gbc.gridy++;

    JButton submitButton = new JButton("Invest Periodically");
    submitButton.setEnabled(false);
    panel.add(submitButton, gbc);
    gbc.gridy++;

    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);
    gbc.gridy++;
    mainMenuButton.addActionListener(e -> showMainMenu());

    HashMap<String, Double> stockRatio = new HashMap<>();
    addStockButtonActionListener(stockRatio, addStockButton, submitButton, stockCountField,
        stockPanel, errorLabel);
    panel.add(addStockButton, gbc);
    gbc.gridy++;
    panel.add(stockPanel, gbc);
    gbc.gridy++;

    DocumentListener documentListener = enterPeriodicInvestDetailsDocumentListener(amountField
        , startDateField, stockCountField, endDateField, intervalDaysField, submitButton,
        stockRatio, errorLabel);
    amountField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    endDateField.getDocument().addDocumentListener(
        documentListener); // Note: End date is optional but still validated if provided
    intervalDaysField.getDocument().addDocumentListener(documentListener);
    enterPeriodicInvestSubmitButtonDocumentListener(amountField
        , startDateField, endDateField, intervalDaysField, submitButton, stockRatio,
        portfolioName);

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private DocumentListener enterPeriodicInvestDetailsDocumentListener(JTextField amountField,
      JTextField startDateField,
      JTextField stockCountField,
      JTextField endDateField,
      JTextField intervalDaysField,
      JButton submitButton,
      HashMap<String,
          Double> stockRatio,
      JLabel errorLabel) {
    return new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        StringBuilder errorMessage = new StringBuilder();
        boolean isValid = true;
        if (amountField.getText().trim().isEmpty() || !amountField.getText().trim()
            .matches("\\d+(\\.\\d{1,2})?")) {
          errorMessage.append("Investment Amount must be a valid number.\n");
          isValid = false;
        }
        if (startDateField.getText().trim().isEmpty() || !startDateField.getText().trim()
            .matches("\\d{4}-\\d{2}-\\d{2}")) {
          errorMessage.append("Start Date must be in YYYY-MM-DD format.\n");
          isValid = false;
        }
        if (!endDateField.getText().trim().isEmpty() && !endDateField.getText().trim()
            .matches("\\d{4}-\\d{2}-\\d{2}")) {
          errorMessage.append("End Date must be in YYYY-MM-DD format.\n");
          isValid = false;
        }
        if (intervalDaysField.getText().trim().isEmpty() || !intervalDaysField.getText().trim()
            .matches("\\d+")) {
          errorMessage.append("Interval Days must be a positive integer.\n");
          isValid = false;
        }
        if (!stockCountField.getText().isEmpty()) {
          try {
            int stockCount = Integer.parseInt(stockCountField.getText().trim());
            if (stockRatio.size() < stockCount) {
              errorMessage.append("Add " + stockCount
                  + " stocks to enable invest periodically.");
              isValid = false;
            } else if (stockCount < 0) {
              errorMessage.append("Stock count should be a positive integer");
              isValid = false;
            }
          } catch (Exception e) {
            errorMessage.append("Stock count should be a positive integer");
            isValid = false;
          }
        } else {
          errorMessage.append("Stock count should be positive integer.\n");
          isValid = false;
        }
        errorLabel.setText("<html>" + errorMessage.toString().replaceAll("\n",
            "<br>") + "</html>");
        errorLabel.setVisible(!isValid);
        submitButton.setEnabled(isValid);
      }
    };
  }

  private void enterPeriodicInvestSubmitButtonDocumentListener(JTextField amountField
      , JTextField startDateField, JTextField endDateField
      , JTextField intervalDaysField, JButton submitButton
      , HashMap<String, Double> stockRatio, String portfolioName) {
    submitButton.addActionListener(e -> {
      double amount = Double.parseDouble(amountField.getText().trim());
      LocalDate startDate = LocalDate.parse(startDateField.getText().trim(),
          DateTimeFormatter.ISO_LOCAL_DATE);
      LocalDate endDate;
      if (endDateField.getText().isEmpty()) {
        endDate = null;
      } else {
        endDate = LocalDate.parse(endDateField.getText().trim(),
            DateTimeFormatter.ISO_LOCAL_DATE);
      }
      long intervalDays = Long.parseLong(intervalDaysField.getText().trim());

      String resultMessage = guiControllerInterface.investPeriodically(portfolioName
          , "dollarCostAveraging", amount, stockRatio, startDate,
          endDate, intervalDays);
      JOptionPane.showMessageDialog(mainFrame, resultMessage
          , "Periodic Investment Result", JOptionPane.INFORMATION_MESSAGE);
      showMainMenu();
    });
  }

  private void addStockButtonActionListener(HashMap<String, Double> stockRatio,
      JButton addStockButton, JButton submitButton,
      JTextField stockCountField, JPanel stockPanel,
      JLabel errorLabel) {
    addStockButton.addActionListener(e -> {
      if (stockRatio.size() < Integer.parseInt(stockCountField.getText())) {
        String ticker = JOptionPane.showInputDialog("Enter Ticker:");
        if (guiControllerInterface.isValidTicker(ticker)) {
          String ratioString = JOptionPane.showInputDialog(
              "Enter Stock Ratio for " + ticker + " (0.0-1.0):");
          Double ratio = Double.valueOf(ratioString);
          if (stockRatio.containsKey(ticker)) {
            stockRatio.put(ticker, stockRatio.get(ticker) + ratio);
            stockPanel.removeAll();
            for (int i = 0; i < stockRatio.size(); i++) {
              stockPanel.add(new JLabel(ticker + ": " + stockRatio.get(ticker)));
            }

          } else {
            stockRatio.put(ticker, ratio);
            stockPanel.add(new JLabel(ticker + ": " + ratio));
          }
          if (stockRatio.size() == Integer.parseInt(stockCountField.getText())) {
            addStockButton.setEnabled(false);
            errorLabel.setVisible(false);
            submitButton.setEnabled(true);
          }
          mainFrame.revalidate();
          mainFrame.repaint();
        } else {
          JOptionPane.showMessageDialog(mainFrame,
              "Invalid ticker. Please enter a valid ticker.",
              "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  private void importPortfolio() {
    displayPortfolioImportOptions();
  }

  private void proceedWithFileSelection(int option) {
    String resultMessage;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Portfolio File to Import");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files",
        "csv");
    fileChooser.setFileFilter(filter);
    int userSelection = fileChooser.showOpenDialog(mainFrame);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToImport = fileChooser.getSelectedFile();
      if (getFileExtension(fileToImport).equals("csv")) {
        try {
          resultMessage = guiControllerInterface.importPortfolioWorkflow(
              fileToImport.getAbsolutePath(), option);
          JOptionPane.showMessageDialog(mainFrame, resultMessage, "Import Portfolio",
              JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(mainFrame, "Failed to import portfolio: "
              + e.getMessage(), "Import Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(mainFrame, "The selected file is not a CSV file."
                + " Please select a valid CSV file.",
            "Invalid File Type", JOptionPane.ERROR_MESSAGE);
      }
    }
    showMainMenu();
  }

  private void displayPortfolioImportOptions() {
    mainFrame.getContentPane().removeAll();
    //mainFrame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    gbc.anchor = GridBagConstraints.WEST;
    JPanel importOptionsPanel = new JPanel(new GridBagLayout());
    importOptionsPanel.setBorder(BorderFactory.createTitledBorder("Select Portfolio Type"));

    JRadioButton immutableButton = new JRadioButton("Immutable Portfolio", true);
    JRadioButton flexibleButton = new JRadioButton("Flexible Portfolio");
    JRadioButton flexibleWithStrategyButton = new
        JRadioButton("Flexible Portfolio with Strategy");

    ButtonGroup group = new ButtonGroup();
    group.add(immutableButton);
    group.add(flexibleButton);
    group.add(flexibleWithStrategyButton);

    gbc.gridx = 0; // Align components to the left
    gbc.gridy = 0;

    importOptionsPanel.add(immutableButton, gbc);
    gbc.gridy++;
    importOptionsPanel.add(flexibleButton, gbc);
    gbc.gridy++;
    importOptionsPanel.add(flexibleWithStrategyButton, gbc);
    gbc.gridy++;

    JButton importButton = new JButton("Import");
    importButton.addActionListener(e -> {
      int selectedOption =
          immutableButton.isSelected() ? 1 : flexibleButton.isSelected() ? 2 : 3;
      proceedWithFileSelection(selectedOption);
    });

    gbc.gridy++;
    importOptionsPanel.add(importButton, gbc);
    gbc.gridy++;

    JButton mainMenuButton = new JButton("Main Menu");
    importOptionsPanel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    mainFrame.getContentPane().removeAll();
    mainFrame.getContentPane().add(importOptionsPanel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private String getFileExtension(File file) {
    String name = file.getName();
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // empty extension
    }
    return name.substring(lastIndexOf + 1).toLowerCase();
  }

  private void exportPortfolio() {
    mainFrame.getContentPane().removeAll();
    mainFrame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JPanel panel = new JPanel(new GridBagLayout());
    JTextField portfolioNameField = new JTextField(20);
    JTextField dateField = new JTextField(10); // For date input in format YYYY-MM-DD
    panel.add(new JLabel("Portfolio Name:"), gbc);
    panel.add(portfolioNameField, gbc);
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    panel.add(dateField, gbc);
    JButton exportButton = new JButton("Export");
    panel.add(exportButton, gbc);
    exportButton.setVisible(false);
    JLabel messageLabel = new JLabel();
    panel.add(messageLabel, gbc);

    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    DocumentListener documentListener = exportPortfolioDocumentListener(portfolioNameField
        , dateField, exportButton, messageLabel);
    portfolioNameField.getDocument().addDocumentListener(documentListener);
    dateField.getDocument().addDocumentListener(documentListener);
    exportButtonActionListener(portfolioNameField, dateField, exportButton);

    mainFrame.add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void exportButtonActionListener(JTextField portfolioNameField
      , JTextField dateField, JButton exportButton) {
    exportButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String dateString = dateField.getText().trim();
      LocalDate date;
      try {
        date = LocalDate.parse(dateString);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame, "The date format is invalid."
                + " Please use YYYY-MM-DD.",
            "Date Format Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      launchDirectoryChooser(portfolioName, date);
      showMainMenu();
    });
  }

  private DocumentListener exportPortfolioDocumentListener(JTextField portfolioNameField
      , JTextField dateField, JButton exportButton, JLabel messageLabel) {
    return new DocumentListener() {
      void update() {
        boolean hasInput = !portfolioNameField.getText().trim().isEmpty() &&
            !dateField.getText().trim().isEmpty();
        exportButton.setVisible(hasInput);
        if (!hasInput) {
          messageLabel.setText("Please fill in all fields.");
        } else {
          messageLabel.setText("");
        }
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        update();
      }
    };
  }

  private void launchDirectoryChooser(String portfolioName, LocalDate date) {
    JFileChooser dirChooser = new JFileChooser();
    dirChooser.setDialogTitle("Select Directory to Save Portfolio CSV");
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int userSelection = dirChooser.showSaveDialog(mainFrame);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File selectedDirectory = dirChooser.getSelectedFile();
      try {
        String directoryPath = selectedDirectory.getAbsolutePath();
        String resultMessage = guiControllerInterface.exportPortfolioWorkflow(portfolioName,
            directoryPath, date);
        JOptionPane.showMessageDialog(mainFrame, "Export successful! File saved to: "
            + resultMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(mainFrame, "Failed to export portfolio: "
            + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }


  private void portfolioPerformance() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate portfolio performance:");
    JTextField portfolioNameField = new JTextField(20);
    JTextField startDateField = new JTextField(20); // For the start date input
    startDateField.setToolTipText("Enter start date in YYYY-MM-DD format");
    JTextField endDateField = new JTextField(20); // For the end date input
    endDateField.setToolTipText("Enter end date in YYYY-MM-DD format");
    JButton submitButton = new JButton("Submit");

    panel.add(instructionLabel, gbc);
    panel.add(new JLabel("Portfolio Name:"), gbc);
    panel.add(portfolioNameField, gbc);
    panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
    panel.add(startDateField, gbc);
    panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
    panel.add(endDateField, gbc);
    panel.add(submitButton, gbc);
    submitButton.setEnabled(false);

    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    DocumentListener documentListener = performanceDocumentListener(portfolioNameField
        , startDateField, endDateField, submitButton);
    portfolioNameField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    endDateField.getDocument().addDocumentListener(documentListener);

    performanceSubmitButtonListener(portfolioNameField, startDateField, endDateField, submitButton);
    mainFrame.getContentPane().removeAll();
    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void performanceSubmitButtonListener(JTextField portfolioNameField
      , JTextField startDateField, JTextField endDateField, JButton submitButton) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String startText = startDateField.getText().trim();
      String endText = endDateField.getText().trim();
      try {
        LocalDate startDate = LocalDate.parse(startText, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(endText, DateTimeFormatter.ISO_LOCAL_DATE);
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        String result = guiControllerInterface.portfolioPerformanceWorkflow(portfolioName, dates);
        JOptionPane.showMessageDialog(mainFrame, result, "Portfolio Performance Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format."
                + " Please ensure both dates are in YYYY-MM-DD format and try again.",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }

  private DocumentListener performanceDocumentListener(JTextField portfolioNameField
      , JTextField startDateField, JTextField endDateField, JButton submitButton) {
    return new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String portfolioName = portfolioNameField.getText().trim();
        String startText = startDateField.getText().trim();
        String endText = endDateField.getText().trim();
        boolean isValidStartDate = startText.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean isValidEndDate = endText.matches("\\d{4}-\\d{2}-\\d{2}");
        submitButton.setEnabled(!portfolioName.isEmpty() && isValidStartDate && isValidEndDate);
      }
    };
  }


  private void buyOrSell(String cause) {
    mainFrame.getContentPane().removeAll();
    GridBagConstraints gbc = createGridBagConstraints();
    JPanel buyPanel = new JPanel(new GridBagLayout());

    JLabel instructionLabel = createLabel(
        "Please enter the following details to buy portfolio stocks:");
    buyPanel.add(instructionLabel, gbc);
    JLabel portfolioNameLabel = new JLabel("Portfolio Name:");
    JTextField portfolioNameField = createTextField(20);
    buyPanel.add(portfolioNameLabel, gbc);
    buyPanel.add(portfolioNameField, gbc);

    JButton submitButton = createButton("Submit");
    submitButton.setEnabled(false);
    buyPanel.add(submitButton, gbc);

    JButton mainMenuButton = new JButton("Main Menu");
    buyPanel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    buyOrSellListeners(portfolioNameField, cause, submitButton);
    mainFrame.getContentPane().add(buyPanel);
    mainFrame.revalidate();
    mainFrame.repaint();

  }

  private void buyOrSellListeners(JTextField portfolioNameField
      , String cause, JButton submitButton) {
    DocumentListener documentListener = new DocumentListener() {
      void update() {
        boolean enabled = !portfolioNameField.getText().trim().isEmpty();
        submitButton.setEnabled(enabled);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        update();
      }
    };

    portfolioNameField.getDocument().addDocumentListener(documentListener);
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      try {
        takeStockInput(portfolioName, 1, 1, cause);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(mainFrame,
            ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
        showMainMenu();
      }
    });
  }


  private void costBasis() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;

    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate portfolio cost basis:");
    panel.add(instructionLabel, gbc);

    gbc.gridy++;
    panel.add(new JLabel("Portfolio Name:"), gbc);

    gbc.gridy++;
    JTextField portfolioNameField = new JTextField(20);
    panel.add(portfolioNameField, gbc);

    gbc.gridy++;
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);

    gbc.gridy++;
    JTextField dateField = new JTextField(20);
    dateField.setToolTipText("Enter date in YYYY-MM-DD format");
    panel.add(dateField, gbc);

    gbc.gridy++;
    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    panel.add(errorLabel, gbc);

    gbc.gridy++;
    JButton submitButton = new JButton("Submit");
    panel.add(submitButton, gbc);
    gbc.gridy++;

    submitButton.setEnabled(false);
    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    costBasisListeners(portfolioNameField, dateField, submitButton,
        errorLabel);
    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void costBasisListeners(JTextField portfolioNameField
      , JTextField dateField, JButton submitButton, JLabel errorLabel) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String portfolioName = portfolioNameField.getText().trim();
        String dateText = dateField.getText().trim();
        boolean isValidDate = dateText.matches("\\d{4}-\\d{2}-\\d{2}");

        if (portfolioName.isEmpty()) {
          errorLabel.setText("Portfolio name cannot be empty.");
          submitButton.setEnabled(false);
        } else if (!isValidDate) {
          errorLabel.setText("Invalid date format. Use YYYY-MM-DD.");
          submitButton.setEnabled(false);
        } else {
          errorLabel.setText("");
          submitButton.setEnabled(true);
        }
      }
    };

    portfolioNameField.getDocument().addDocumentListener(documentListener);
    dateField.getDocument().addDocumentListener(documentListener);
    costBasisSubmitListener(submitButton, portfolioNameField, dateField);
  }

  private void costBasisSubmitListener(JButton submitButton, JTextField portfolioNameField,
      JTextField dateField) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String dateString = dateField.getText().trim();
      try {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        String result = guiControllerInterface.portfolioCostBasisWorkflow(portfolioName, date);
        JOptionPane.showMessageDialog(mainFrame, result, "Cost Basis Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format. Please use YYYY-MM-DD. Try again", "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }


  private void portfolioCompositionOrTotalValue(String s) {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel(
        "Please enter the following details to check portfolio " + s);
    JTextField portfolioNameField = new JTextField(20);
    JTextField dateField = new JTextField(20);
    JButton submitButton = new JButton("Submit");

    panel.add(instructionLabel, gbc);
    panel.add(new JLabel("Portfolio Name:"), gbc);
    panel.add(portfolioNameField, gbc);
    panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
    panel.add(dateField, gbc);
    panel.add(submitButton, gbc);

    submitButton.setEnabled(false);
    JButton mainMenuButton = new JButton("Main Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    compOrTotalValueDocumentListener(portfolioNameField, dateField, submitButton);
    compOrTotalValueSubmitButtonListener(portfolioNameField, dateField, submitButton, s);

    mainFrame.getContentPane().removeAll();
    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void compOrTotalValueDocumentListener(JTextField portfolioNameField
      , JTextField dateField, JButton submitButton) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String portfolioName = portfolioNameField.getText().trim();
        String dateText = dateField.getText().trim();
        submitButton.setEnabled(!portfolioName.isEmpty() && !dateText.isEmpty());
      }
    };

    portfolioNameField.getDocument().addDocumentListener(documentListener);
    dateField.getDocument().addDocumentListener(documentListener);
  }

  private void compOrTotalValueSubmitButtonListener(JTextField portfolioNameField
      , JTextField dateField, JButton submitButton, String s) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim();
      String dateString = dateField.getText().trim();
      try {
        String value;
        if (s.equalsIgnoreCase("composition")) {
          LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
          value = guiControllerInterface.viewPortfolioCompositionWorkflow(
              portfolioName, date);
        } else {
          LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
          value = guiControllerInterface.totalValueOfPortfolioWorkflow(portfolioName, date);
        }
        JOptionPane.showMessageDialog(mainFrame, value, "Portfolio " + value,
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format. Please use YYYY-MM-DD. Try again", "Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(mainFrame, ex.toString(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }

  private void createPortfolio() {
    mainFrame.getContentPane().removeAll();
    mainFrame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JPanel portfolioPanel = new JPanel(new GridBagLayout());
    portfolioPanel.add(createLabel("Create Portfolio"), gbc);
    portfolioPanel.add(new JLabel("Please enter the portfolio name:"), gbc);
    JTextField portfolioNameField = createTextField(20);
    portfolioPanel.add(portfolioNameField, gbc);
    JButton submitButton = createButton("Submit");
    portfolioPanel.add(submitButton, gbc);

    JButton mainMenuButton = new JButton("Main Menu");
    portfolioPanel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    createPortfolioActionListener(submitButton, portfolioNameField);
    mainFrame.add(portfolioPanel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void createPortfolioActionListener(JButton submitButton, JTextField portfolioNameField) {
    submitButton.addActionListener(e -> {
      String portfolioName = portfolioNameField.getText().trim().toLowerCase();
      String s;
      s = guiControllerInterface.createPortfolioWorkflow(portfolioName);
      if (s.equalsIgnoreCase(
          "Cannot create portfolio because this portfolio already"
              + " exists. Taking you back to main menu.")) {
        JOptionPane.showMessageDialog(mainFrame, s);
        showMainMenu();
      } else {
        displayPortfolioTypeOptions(portfolioName);
      }
    });
  }

  private void displayPortfolioTypeOptions(String portfolioName) {
    mainFrame.getContentPane().removeAll();
    mainFrame.setLayout(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.insets = new Insets(10, 50, 10, 50);

    JPanel portfolioTypePanel = new JPanel(new GridBagLayout());
    portfolioTypePanel.add(createLabel("What kind of portfolio do you want to create?"), gbc);

    JButton immutableButton = createButton("Immutable Portfolio");
    JButton flexibleButton = createButton("Flexible Portfolio");

    portfolioTypePanel.add(immutableButton, gbc);
    portfolioTypePanel.add(flexibleButton, gbc);

    immutableButton.addActionListener(e -> createImmutablePortfolio(portfolioName));
    flexibleButton.addActionListener(e -> {
      String s = guiControllerInterface.createFlexiblePortfolioWorkflow(portfolioName);
      JOptionPane.showMessageDialog(mainFrame, s);
      showMainMenu();
    });

    mainFrame.add(portfolioTypePanel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void createImmutablePortfolio(String portfolioName) {
    this.stockData = new HashMap<>();
    mainFrame.getContentPane().removeAll();
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.gridy = 0;

    JPanel formPanel = new JPanel(new GridBagLayout());
    JLabel welcomeLabel = createLabel("Please provide portfolio details for " + portfolioName);
    formPanel.add(welcomeLabel);
    gbc.gridy++;
    formPanel.add(new JLabel(), gbc);
    gbc.gridy++;
    formPanel.add(createLabel("Enter the number of stocks you want to add"), gbc);
    JTextField noOfStocksText = createTextField(20);
    gbc.gridy++;
    formPanel.add(noOfStocksText, gbc);
    gbc.gridy++;
    JButton submitButton = createButton("Submit");
    formPanel.add(submitButton, gbc);
    JButton mainMenuButton = new JButton("Main Menu");
    formPanel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    submitButton.addActionListener(e -> {
      int noOfStocks = Integer.parseInt(noOfStocksText.getText().trim());
      takeStockInput(portfolioName, 1, noOfStocks, "create portfolio");
    });

    mainFrame.add(formPanel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void createImmutablePortfolioWithController(String portfolioName) {
    String s;
    try {
      s = guiControllerInterface.immutablePortfolio(portfolioName, this.stockData);
      if (s.equalsIgnoreCase("An immutable portfolio with name " + portfolioName
          + " has been created and saved. Taking you back to main  menu.")) {
        JOptionPane.showMessageDialog(mainFrame, s);
      }
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(mainFrame, ex.getMessage()
          + " Taking you back to main menu.");
    }
    showMainMenu();
  }


  private void processStockDataInput(ArrayList<String> data) {
    String ticker = data.get(0);
    data.remove(0);
    if (stockData.containsKey(ticker)) {
      ArrayList<String> newdata = stockData.get(ticker);
      newdata.add(0,
          "" + Integer.parseInt(newdata.get(0)) + Integer.parseInt(data.get(0)));
      stockData.put(ticker, newdata);
    } else {
      stockData.put(ticker, data);
    }
  }

  private void takeStockInput(String portfolioName, int i, int noOfStocks, String cause) {
    mainFrame.getContentPane().removeAll();
    GridBagConstraints gbc = createGridBagConstraints();
    gbc.gridy = 0;
    JPanel formPanel = new JPanel(new GridBagLayout());
    JLabel welcomeLabel = createLabel(
        "Please enter stock information for stock " + i + " to " + cause);
    formPanel.add(welcomeLabel);
    gbc.gridy++;
    formPanel.add(new JLabel(), gbc);
    gbc.gridy++;
    formPanel.add(new JLabel("Enter the ticker of the stock"), gbc);
    JTextField ticker = createTextField(20);
    gbc.gridy++;
    formPanel.add(ticker, gbc);
    gbc.gridy++;
    formPanel.add(new JLabel("Please enter the quantity of this stock"), gbc);
    JTextField quantity = createTextField(20);
    gbc.gridy++;
    formPanel.add(quantity, gbc);
    gbc.gridy++;
    formPanel.add(
        new JLabel("Please enter the date in YYYY-MM-DD format"), gbc);
    JTextField date = createTextField(20);
    gbc.gridy++;
    formPanel.add(date, gbc);
    gbc.gridy++;

    JLabel quantityErrorLabel = createErrorLabel();
    gbc.gridy++;
    formPanel.add(quantityErrorLabel, gbc);

    JLabel dateErrorLabel = createErrorLabel();
    gbc.gridy += 2;
    formPanel.add(dateErrorLabel, gbc);

    JButton submitButton = createButton("SUBMIT");
    submitButton.setPreferredSize(new Dimension(70, 40));
    submitButton.setEnabled(false);
    gbc.gridy += 2;
    formPanel.add(submitButton, gbc);
    gbc.gridy++;
    JButton mainMenuButton = new JButton("Main Menu");
    formPanel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showMainMenu());

    addRealTimeValidation(ticker, quantity, date, submitButton, quantityErrorLabel, dateErrorLabel);

    stockInputSubmitButtonListener(submitButton, ticker
        , quantity, date, i, noOfStocks, cause, portfolioName);
    mainFrame.add(formPanel);
    mainFrame.revalidate();
    mainFrame.repaint();

  }

  private JLabel createErrorLabel() {
    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    return errorLabel;
  }

  private void addRealTimeValidation(JTextField ticker, JTextField quantity, JTextField date,
      JButton submitButton,
      JLabel quantityErrorLabel, JLabel dateErrorLabel) {

    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        boolean isValidQuantity = validateQuantity(quantity.getText().trim(), quantityErrorLabel);
        boolean isValidDate = validateDate(date.getText().trim(), dateErrorLabel);
        submitButton.setEnabled(isValidQuantity && isValidDate);
      }
    };

    quantity.getDocument().addDocumentListener(documentListener);
    date.getDocument().addDocumentListener(documentListener);
  }


  private boolean validateQuantity(String quantityText, JLabel errorLabel) {
    try {
      int quantity = Integer.parseInt(quantityText);
      if (quantity > 0) {
        errorLabel.setText("");
        return true;
      }
    } catch (NumberFormatException e) {
      errorLabel.setText("Quantity must be an integer greater than 0.");
    }
    errorLabel.setText("Quantity must be an integer greater than 0.");
    return false;
  }

  private boolean validateDate(String dateText, JLabel errorLabel) {
    try {
      LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      if (date.isAfter(LocalDate.now())) {
        errorLabel.setText("Date cannot be in the future.");
        return false;
      }
      errorLabel.setText("");
      return true;
    } catch (DateTimeParseException e) {
      errorLabel.setText("Invalid date. Use YYYY-MM-DD format.");
      return false;
    }
  }

  private void stockInputSubmitButtonListener(JButton submitButton
      , JTextField ticker, JTextField quantity, JTextField date, int i, int noOfStocks
      , String cause, String portfolioName) {
    submitButton.addActionListener(e -> {
      ArrayList<String> data = new ArrayList<>();
      data.add(ticker.getText().trim().toUpperCase());
      data.add(quantity.getText().trim());
      data.add(date.getText().trim());

      processStockDataInput(data);
      if (i == noOfStocks) {
        if (cause.equalsIgnoreCase("create portfolio")) {
          createImmutablePortfolioWithController(portfolioName);
        } else if (cause.equalsIgnoreCase("buy") ||
            cause.equalsIgnoreCase("sell")) {
          DateTimeFormatter newformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate dateObject = LocalDate.parse(date.getText().trim(), newformatter);
          String message;
          message = guiControllerInterface.buyOrSellStocksNonZeroPortfoliosWorkflow(
              portfolioName, ticker.getText().trim().toUpperCase()
              , Integer.parseInt(data.get(0)), dateObject, cause);

          JOptionPane.showMessageDialog(mainFrame, message);
          showMainMenu();
        }
      } else if (i < noOfStocks) {
        takeStockInput(portfolioName, i + 1, noOfStocks, cause);
      }
    });
  }


  private void showStockAnalysisMenu() {
    mainFrame.getContentPane().removeAll();

    JPanel menuPanel = new JPanel(new GridBagLayout());
    menuPanel.setBorder(BorderFactory.createTitledBorder("Stock Analysis Menu"));

    GridBagConstraints gbc = createGridBagConstraints();
    gbc.insets = new Insets(10, 50, 10, 50);

    JButton stockPerformanceButton = new JButton("Display stock performance");
    menuPanel.add(stockPerformanceButton, gbc);

    JButton stockGainOrLossButton = new JButton("Display stock gain/loss");
    menuPanel.add(stockGainOrLossButton, gbc);

    JButton stockCrossoverButton = new JButton("Display stock crossover days");
    menuPanel.add(stockCrossoverButton, gbc);

    JButton stockMovingCrossoverButton = new JButton("Check stock moving crossover days");
    menuPanel.add(stockMovingCrossoverButton, gbc);

    JButton stockMovingAverageButton = new JButton("Check stock moving average");
    menuPanel.add(stockMovingAverageButton, gbc);

    JButton mainMenuButton = new JButton("Back to Main Menu");
    menuPanel.add(mainMenuButton, gbc);

    try {
      stockAnalysisMenuActionListeners(stockPerformanceButton
          , stockGainOrLossButton, stockCrossoverButton
          , stockMovingCrossoverButton, stockMovingAverageButton
          , mainMenuButton);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainFrame, e.getMessage()
          + ". Taking you back to main menu.");
      showMainMenu();
    }
    stockAnalysisMenuFormat(mainFrame, menuPanel);
  }

  private void stockAnalysisMenuActionListeners(JButton stockPerformanceButton
      , JButton stockGainOrLossButton, JButton stockCrossoverButton
      , JButton stockMovingCrossoverButton, JButton stockMovingAverageButton
      , JButton mainMenuButton
  ) {
    stockPerformanceButton.addActionListener(e -> stockPerformance());
    stockGainOrLossButton.addActionListener(e -> stockGainOrLoss());
    stockCrossoverButton.addActionListener(e -> stockCrossover());
    stockMovingCrossoverButton.addActionListener(e -> stockMovingCrossover());
    stockMovingAverageButton.addActionListener(e -> movingAverage());
    mainMenuButton.addActionListener(e -> showMainMenu());
  }

  private void stockAnalysisMenuFormat(JFrame mainFrame, JPanel menuPanel) {
    mainFrame.getContentPane().setLayout(new GridBagLayout());
    GridBagConstraints frameGbc = new GridBagConstraints();
    frameGbc.gridwidth = GridBagConstraints.REMAINDER;
    frameGbc.fill = GridBagConstraints.BOTH;
    frameGbc.weightx = 1.0;
    frameGbc.weighty = 1.0;

    mainFrame.getContentPane().add(menuPanel, frameGbc);
    mainFrame.pack();
    mainFrame.setSize(800, 650);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }

  private void stockPerformance() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate stock performance:");
    JTextField tickerField = new JTextField(20);
    JTextField startDateField = new JTextField(20);
    startDateField.setToolTipText("Enter start date in YYYY-MM-DD format");
    JTextField endDateField = new JTextField(20);
    endDateField.setToolTipText("Enter end date in YYYY-MM-DD format");
    JButton submitButton = new JButton("Submit");
    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);

    panel.add(instructionLabel, gbc);
    panel.add(new JLabel("Stock Ticker:"), gbc);
    panel.add(tickerField, gbc);
    panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
    panel.add(startDateField, gbc);
    panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
    panel.add(endDateField, gbc);
    panel.add(errorLabel, gbc);
    panel.add(submitButton, gbc);

    submitButton.setEnabled(false);
    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());
    stockPerformanceDocumentListener(tickerField, startDateField, endDateField, submitButton,
        errorLabel);
    stockPerformanceSubmitListener(tickerField, startDateField, endDateField, submitButton);

    mainFrame.setLayout(new GridBagLayout());
    GridBagConstraints frameGbc = new GridBagConstraints();
    frameGbc.gridwidth = GridBagConstraints.REMAINDER;
    frameGbc.fill = GridBagConstraints.NONE;
    frameGbc.anchor = GridBagConstraints.CENTER;

    mainFrame.getContentPane().add(panel, frameGbc);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void stockPerformanceDocumentListener(JTextField tickerField, JTextField startDateField,
      JTextField endDateField, JButton submitButton,
      JLabel errorLabel) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String ticker = tickerField.getText().trim();
        String startText = startDateField.getText().trim();
        String endText = endDateField.getText().trim();
        boolean isValidStartDate = startText.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean isValidEndDate = endText.matches("\\d{4}-\\d{2}-\\d{2}");

        if (!isValidStartDate || !isValidEndDate) {
          errorLabel.setText("Invalid date format. Use YYYY-MM-DD.");
          submitButton.setEnabled(false);
        } else if (isValidStartDate && isValidEndDate) {
          try {
            LocalDate startDate = LocalDate.parse(startText, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(endText, DateTimeFormatter.ISO_LOCAL_DATE);
            if (endDate.isBefore(startDate)) {
              errorLabel.setText("End date cannot be before start date.");
              submitButton.setEnabled(false);
            } else {
              errorLabel.setText("");
              submitButton.setEnabled(!ticker.isEmpty());
            }
          } catch (DateTimeParseException e) {
            errorLabel.setText("Invalid date. Ensure format is YYYY-MM-DD.");
            submitButton.setEnabled(false);
          }
        } else {
          submitButton.setEnabled(!ticker.isEmpty());
          errorLabel.setText("");
        }
      }
    };

    tickerField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    endDateField.getDocument().addDocumentListener(documentListener);
  }


  private void stockPerformanceSubmitListener(JTextField tickerField
      , JTextField startDateField, JTextField endDateField, JButton submitButton) {
    submitButton.addActionListener(e -> {
      String ticker = tickerField.getText().trim();
      String startText = startDateField.getText().trim();
      String endText = endDateField.getText().trim();
      try {
        LocalDate startDate = LocalDate.parse(startText, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(endText, DateTimeFormatter.ISO_LOCAL_DATE);
        String result = guiControllerInterface.computeStockPerformance(ticker, startDate, endDate);
        JOptionPane.showMessageDialog(mainFrame, result, "Stock Performance Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format."
                + " Please ensure both dates are in YYYY-MM-DD format and try again.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(mainFrame, "An error occurred: "
                + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }

  private void stockGainOrLoss() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel("Do you want to compute gains or loss for:");
    JButton singleDayButton = new JButton("A Single Day");
    JButton periodButton = new JButton("A Period of Time");

    panel.add(instructionLabel, gbc);
    panel.add(singleDayButton, gbc);
    panel.add(periodButton, gbc);
    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());

    singleDayButton.addActionListener(e -> setupSingleOrPeriodInputs(true));
    periodButton.addActionListener(e -> setupSingleOrPeriodInputs(false));

    mainFrame.getContentPane().removeAll();
    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void setupSingleOrPeriodInputs(boolean isSingleDay) {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = createLabel(
        "Please enter the following inputs to compute stock gains/loss:");
    panel.add(instructionLabel, gbc);

    JTextField tickerField = new JTextField(20);
    JTextField startDateField = new JTextField(20); // For single day or start date
    startDateField.setToolTipText(
        isSingleDay ? "Enter date in YYYY-MM-DD format"
            : "Enter start date in YYYY-MM-DD format");

    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);

    panel.add(new JLabel("Stock Ticker:"), gbc);
    panel.add(tickerField, gbc);

    panel.add(new JLabel(isSingleDay ? "Date (YYYY-MM-DD):" : "Start Date (YYYY-MM-DD):"), gbc);
    panel.add(startDateField, gbc);

    JTextField endDateField = new JTextField(20); // Only for period
    endDateField.setToolTipText("Enter end date in YYYY-MM-DD format");

    if (!isSingleDay) {
      panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
      panel.add(endDateField, gbc);
    }

    panel.add(errorLabel, gbc);

    JButton submitButton = new JButton("Submit");
    panel.add(submitButton, gbc);
    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());

    singlePeriodInputsDocumentListener(startDateField, endDateField, tickerField, isSingleDay,
        submitButton, errorLabel);
    singlePeriodInputsSubmitListener(startDateField, endDateField, tickerField, isSingleDay,
        submitButton);

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void singlePeriodInputsDocumentListener(JTextField startDateField,
      JTextField endDateField, JTextField tickerField,
      boolean isSingleDay, JButton submitButton,
      JLabel errorLabel) {
    DocumentListener documentListener = new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        validate();
      }

      public void removeUpdate(DocumentEvent e) {
        validate();
      }

      public void changedUpdate(DocumentEvent e) {
        validate();
      }

      private void validate() {
        String ticker = tickerField.getText().trim();
        String startDate = startDateField.getText().trim();
        String endDate = isSingleDay ? "" : endDateField.getText().trim();
        boolean startDateValid = startDate.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean endDateValid = isSingleDay || endDate.matches("\\d{4}-\\d{2}-\\d{2}");
        if (ticker.isEmpty()) {
          errorLabel.setText("Ticker is required.");
          submitButton.setEnabled(false);
        } else if (!startDateValid) {
          errorLabel.setText("Invalid start date format.");
          submitButton.setEnabled(false);
        } else if (!endDateValid) {
          errorLabel.setText("Invalid end date format.");
          submitButton.setEnabled(false);
        } else if (!isSingleDay && LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate))) {
          errorLabel.setText("End date cannot be before start date.");
          submitButton.setEnabled(false);
        } else {
          errorLabel.setText("");
          submitButton.setEnabled(true);
        }
      }
    };

    tickerField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    if (!isSingleDay && endDateField != null) {
      endDateField.getDocument().addDocumentListener(documentListener);
    }
  }


  private void singlePeriodInputsSubmitListener(JTextField startDateField
      , JTextField endDateField, JTextField tickerField, boolean isSingleDay
      , JButton submitButton) {
    submitButton.addActionListener(e -> {
      try {
        int userInput = isSingleDay ? 1 : 2;
        String ticker = tickerField.getText().trim().toUpperCase();
        LocalDate startDate = LocalDate.parse(startDateField.getText().trim(),
            DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.now();
        if (!isSingleDay) {
          endDate = LocalDate.parse(endDateField.getText().trim(),
              DateTimeFormatter.ISO_LOCAL_DATE);
        }
        String resultMessage = guiControllerInterface.checkStockGainsOrLoss(ticker, userInput,
            startDate, endDate);
        JOptionPane.showMessageDialog(mainFrame, resultMessage, "Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(mainFrame, "An error occurred: "
                + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });

  }


  private void stockCrossover() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();

    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate stock crossover days:");
    JTextField tickerField = new JTextField(20);
    tickerField.setToolTipText("Enter the stock ticker");
    JTextField startDateField = new JTextField(20);
    startDateField.setToolTipText("Enter start date in YYYY-MM-DD format");
    JTextField endDateField = new JTextField(20);
    endDateField.setToolTipText("Enter end date in YYYY-MM-DD format");
    JButton submitButton = new JButton("Submit");

    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED); // Set error message color

    // Add components to panel
    panel.add(instructionLabel, gbc);
    panel.add(new JLabel("Ticker:"), gbc);
    panel.add(tickerField, gbc);
    panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
    panel.add(startDateField, gbc);
    panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
    panel.add(endDateField, gbc);
    gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch the error label horizontally
    panel.add(errorLabel, gbc); // Add the error label above the submit button
    panel.add(submitButton, gbc);

    submitButton.setEnabled(false);
    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());

    stockCrossoverDocumentListener(tickerField, startDateField, endDateField, submitButton,
        errorLabel);
    stockCrossoverSubmitListener(tickerField, startDateField, endDateField, submitButton);

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void stockCrossoverDocumentListener(JTextField tickerField, JTextField startDateField,
      JTextField endDateField,
      JButton submitButton, JLabel errorLabel) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String ticker = tickerField.getText().trim().toUpperCase();
        String startText = startDateField.getText().trim();
        String endText = endDateField.getText().trim();
        boolean isValidStartDate = startText.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean isValidEndDate = endText.matches("\\d{4}-\\d{2}-\\d{2}");

        if (ticker.isEmpty()) {
          errorLabel.setText("Ticker is required.");
          submitButton.setEnabled(false);
        } else if (!isValidStartDate) {
          errorLabel.setText("Invalid start date format. Use YYYY-MM-DD.");
          submitButton.setEnabled(false);
        } else if (!isValidEndDate) {
          errorLabel.setText("Invalid end date format. Use YYYY-MM-DD.");
          submitButton.setEnabled(false);
        } else if (!startText.isEmpty() && !endText.isEmpty() && LocalDate.parse(endText)
            .isBefore(LocalDate.parse(startText))) {
          errorLabel.setText("End date cannot be before start date.");
          submitButton.setEnabled(false);
        } else {
          errorLabel.setText(""); // Clear the error message
          submitButton.setEnabled(true);
        }
      }
    };

    tickerField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    endDateField.getDocument().addDocumentListener(documentListener);
  }


  private void stockCrossoverSubmitListener(JTextField tickerField
      , JTextField startDateField, JTextField endDateField
      , JButton submitButton) {
    submitButton.addActionListener(e -> {
      String ticker = tickerField.getText().trim().toUpperCase(); // Convert ticker to upper case
      String startText = startDateField.getText().trim();
      String endText = endDateField.getText().trim();
      try {
        LocalDate startDate = LocalDate.parse(startText, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(endText, DateTimeFormatter.ISO_LOCAL_DATE);
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        String result = guiControllerInterface.computeCrossoverDays(ticker, dates);
        JOptionPane.showMessageDialog(mainFrame, result, "Stock Crossover Days Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format."
                + " Please ensure both dates are in YYYY-MM-DD format and try again.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "An error occurred while processing your request: "
                + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }


  private void stockMovingCrossover() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = createGridBagConstraints();
    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate stock crossover days:");
    JTextField tickerField = new JTextField(20);
    JTextField startDateField = new JTextField(20); // For the start date input
    startDateField.setToolTipText("Enter start date in YYYY-MM-DD format");
    JTextField endDateField = new JTextField(20); // For the end date input
    endDateField.setToolTipText("Enter end date in YYYY-MM-DD format");
    JTextField shortMAField = new JTextField(20); // For short moving average days input
    JTextField longMAField = new JTextField(20); // For long moving average days input
    JButton submitButton = new JButton("Submit");
    panel.add(instructionLabel, gbc);
    panel.add(new JLabel("Ticker:"), gbc);
    panel.add(tickerField, gbc);
    panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
    panel.add(startDateField, gbc);
    panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
    panel.add(endDateField, gbc);
    panel.add(new JLabel("Short Moving Average Days:"), gbc);
    panel.add(shortMAField, gbc);
    panel.add(new JLabel("Long Moving Average Days:"), gbc);
    panel.add(longMAField, gbc);
    panel.add(submitButton, gbc);
    JLabel errorLabel = new JLabel("");
    errorLabel.setForeground(Color.RED);
    errorLabel.setVisible(false);
    panel.add(errorLabel, gbc);
    submitButton.setEnabled(false);
    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());
    stockMovingCrossoverDocumentListener(tickerField, startDateField, endDateField
        , shortMAField, longMAField, errorLabel, submitButton);
    stockMovingCrossoverSubmitListener(tickerField, startDateField, endDateField
        , shortMAField, longMAField, submitButton);
    mainFrame.getContentPane().removeAll();
    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private void stockMovingCrossoverDocumentListener(JTextField tickerField
      , JTextField startDateField, JTextField endDateField
      , JTextField shortMAField, JTextField longMAField
      , JLabel errorLabel, JButton submitButton) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String ticker = tickerField.getText().trim().toUpperCase();
        String startText = startDateField.getText().trim();
        String endText = endDateField.getText().trim();
        boolean isValidStartDate = startText.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean isValidEndDate = endText.matches("\\d{4}-\\d{2}-\\d{2}");
        boolean isNumericShortMA = shortMAField.getText().trim().matches("\\d+");
        boolean isNumericLongMA = longMAField.getText().trim().matches("\\d+");
        boolean isShortMAGreater = false;
        LocalDate startDate = null;
        LocalDate endDate = null;

        // Attempt to parse the dates if valid to compare them
        if (isValidStartDate && isValidEndDate) {
          startDate = LocalDate.parse(startText);
          endDate = LocalDate.parse(endText);
        }

        // Reset error label on each validation check
        errorLabel.setText("");
        errorLabel.setVisible(false);

        if (ticker.isEmpty()) {
          errorLabel.setText("Ticker is required.");
          errorLabel.setVisible(true);
        } else if (!isValidStartDate) {
          errorLabel.setText("Invalid start date format. Use YYYY-MM-DD.");
          errorLabel.setVisible(true);
        } else if (!isValidEndDate) {
          errorLabel.setText("Invalid end date format. Use YYYY-MM-DD.");
          errorLabel.setVisible(true);
        } else if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
          errorLabel.setText("End date cannot be before start date.");
          errorLabel.setVisible(true);
        } else if (!isNumericShortMA || !isNumericLongMA) {
          errorLabel.setText("Moving average days must be integers greater then zero.");
          errorLabel.setVisible(true);
        } else if (isNumericShortMA && isNumericLongMA) {
          try {
            int shortMA = Integer.parseInt(shortMAField.getText().trim());
            int longMA = Integer.parseInt(longMAField.getText().trim());

            isShortMAGreater = shortMA > longMA;

            if (isShortMAGreater) {
              errorLabel.setText("Short Moving average days must be smaller "
                  + "than Long Moving average days.");
              errorLabel.setVisible(true);
            }
          } catch (Exception e) {
            errorLabel.setText("Moving average days must be integers greater then zero.");
            errorLabel.setVisible(true);
          }
        }

        // Enable submit button only if all validations pass
        boolean enableSubmit = !ticker.isEmpty() && isValidStartDate && isValidEndDate &&
            isNumericShortMA && isNumericLongMA && !isShortMAGreater &&
            (startDate == null || endDate == null || !endDate.isBefore(startDate));

        submitButton.setEnabled(enableSubmit);
      }
    };

    tickerField.getDocument().addDocumentListener(documentListener);
    startDateField.getDocument().addDocumentListener(documentListener);
    endDateField.getDocument().addDocumentListener(documentListener);
    shortMAField.getDocument().addDocumentListener(documentListener);
    longMAField.getDocument().addDocumentListener(documentListener);
  }

  private void stockMovingCrossoverSubmitListener(JTextField tickerField
      , JTextField startDateField, JTextField endDateField
      , JTextField shortMAField, JTextField longMAField
      , JButton submitButton) {
    submitButton.addActionListener(e -> {
      String ticker = tickerField.getText().trim();
      String startText = startDateField.getText().trim();
      String endText = endDateField.getText().trim();
      int shortMA = Integer.parseInt(shortMAField.getText().trim());
      int longMA = Integer.parseInt(longMAField.getText().trim());

      try {
        LocalDate startDate = LocalDate.parse(startText, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(endText, DateTimeFormatter.ISO_LOCAL_DATE);
        ArrayList<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        String result = guiControllerInterface.computeMovingCrossoverDays(ticker, dates, shortMA,
            longMA);
        JOptionPane.showMessageDialog(mainFrame, result, "Stock Moving Crossover Days Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(mainFrame,
            "Invalid date format. Please ensure both dates are in YYYY-MM-DD format"
                + " and try again.",
            "Error", JOptionPane.ERROR_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(mainFrame, "An error occurred: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });

  }

  private void movingAverage() {
    mainFrame.getContentPane().removeAll();
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints(); // Direct initialization

    // Configure the initial constraints
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5); // Provide some padding
    gbc.gridx = 0; // Start from the first column
    gbc.gridy = 0; // Start from the first row

    JLabel instructionLabel = new JLabel(
        "Please provide the following data to calculate stock moving average:");
    panel.add(instructionLabel, gbc);

    // Increment gbc.gridy for each new component to ensure vertical layout
    gbc.gridy++;
    panel.add(new JLabel("Stock Ticker:"), gbc);

    gbc.gridy++;
    JTextField tickerField = new JTextField(20);
    panel.add(tickerField, gbc);

    gbc.gridy++;
    panel.add(new JLabel("Number of Days:"), gbc);

    gbc.gridy++;
    JTextField daysField = new JTextField(20);
    panel.add(daysField, gbc);

    // Prepare the error label
    gbc.gridy++;
    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    panel.add(errorLabel, gbc); // Now incrementing gbc.gridy before adding
    errorLabel.setVisible(false); // Initially invisible

    gbc.gridy++;
    JButton submitButton = new JButton("Submit");
    panel.add(submitButton, gbc);
    submitButton.setEnabled(false);
    gbc.gridy++;

    JButton mainMenuButton = new JButton("Stock Analysis Menu");
    panel.add(mainMenuButton, gbc);
    mainMenuButton.addActionListener(e -> showStockAnalysisMenu());

    // Document listeners and action listener for submit button
    movingAverageDocumentListener(tickerField, daysField, submitButton, errorLabel);
    movingAverageSubmitListener(tickerField, daysField, submitButton);

    mainFrame.getContentPane().add(panel);
    mainFrame.revalidate();
    mainFrame.repaint();
  }


  private void movingAverageDocumentListener(JTextField tickerField, JTextField daysField,
      JButton submitButton, JLabel errorLabel) {
    DocumentListener documentListener = new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void removeUpdate(DocumentEvent e) {
        checkInputs();
      }

      public void insertUpdate(DocumentEvent e) {
        checkInputs();
      }

      private void checkInputs() {
        String ticker = tickerField.getText().trim();
        String daysText = daysField.getText().trim();
        boolean isNumericDays = daysText.matches("\\d+");
        int days = 0;
        boolean isDaysPositive = false;
        if (isNumericDays) {
          days = Integer.parseInt(daysText);
          isDaysPositive = days > 0; // Check if days are greater than zero
        }

        if (ticker.isEmpty()) {
          errorLabel.setText("Ticker is required.");
          errorLabel.setVisible(true);
        } else if (!isNumericDays || days <= 0) {
          errorLabel.setText("Number of days must be a positive integer.");
          errorLabel.setVisible(true);
        } else {
          errorLabel.setText(""); // Clear the error message
          errorLabel.setVisible(false);
        }

        submitButton.setEnabled(!ticker.isEmpty() && isNumericDays && isDaysPositive);
      }
    };
    tickerField.getDocument().addDocumentListener(documentListener);
    daysField.getDocument().addDocumentListener(documentListener);
  }


  private void movingAverageSubmitListener(JTextField tickerField, JTextField daysField,
      JButton submitButton) {
    submitButton.addActionListener(e -> {
      String ticker = tickerField.getText().trim().toUpperCase();
      int days = Integer.parseInt(daysField.getText().trim());

      try {
        String result = guiControllerInterface.computeMovingAverage(ticker, days);
        JOptionPane.showMessageDialog(mainFrame, result, "Stock Moving Average Result",
            JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(mainFrame, "An error occurred: "
                + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      showMainMenu();
    });
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  @Override
  public void printValue(String value) {
    mainFrame.add(new JLabel(value));
  }

  @Override
  public void printValue(HashMap<String, String> hashMap) {
    mainFrame.add(new JLabel("" + hashMap));
  }

  @Override
  public void printError(String value) {
    JOptionPane.showMessageDialog(mainFrame,
        "An error occurred: " + value + "\nTaking you to main menu", "Error",
        JOptionPane.ERROR_MESSAGE);
    showMainMenu();
  }

  @Override
  public void printStockData(HashMap<String, ArrayList<String>> hashMap) {
    mainFrame.add(new JLabel("" + hashMap));
  }
}
