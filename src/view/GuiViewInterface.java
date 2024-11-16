package view;

import controller.GuiControllerInterface;

/**
 * Interface defining the GUI view component of an application managing stock portfolios. Specifies
 * the methods for displaying various screens and interfacing with the controller.
 */
public interface GuiViewInterface extends ViewInterface {

  /**
   * Displays the login screen to the user. This screen typically includes fields for username and
   * password input and options to either log in or register a new account.
   */
  public void showLoginScreen();

  /**
   * Displays the main menu of the application. The main menu offers the user various options for
   * interacting with the application, such as creating or viewing portfolios, or logging out.
   */
  public void showMainMenu();

  /**
   * Sets the controller for this view. The controller handles the logic behind user actions on the
   * GUI, facilitating communication between the view and the model.
   *
   * @param guiControllerInterface The controller to be used with this view.
   */
  public void setController(GuiControllerInterface guiControllerInterface);

}
