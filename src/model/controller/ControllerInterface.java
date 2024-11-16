package controller;

/**
 * This interface outlines the responsibilities of a Controller in managing the flow of the
 * application based on user input and interactions. Implementing classes will orchestrate the
 * interaction between the Model and View components, handling user inputs, application logic, and
 * updates to the user interface.
 */
public interface ControllerInterface {

  /**
   * Starts the application's main workflow, handling user interactions and application flow.
   * <p>
   * This method orchestrates the application's initial actions and responds to user inputs. It
   * employs exception handling to manage various scenarios, including invalid input or operational
   * errors, by displaying appropriate messages to the user and prompting for re-entry of data or
   * actions as necessary.
   * </p>
   */
  void start();
}
