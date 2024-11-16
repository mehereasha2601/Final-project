import controller.Controller;
import controller.ControllerInterface;
import controller.GuiController;
import model.ModelInterface;
import model.ModelService;
import view.GuiView;
import view.View;
import view.ViewInterface;

/**
 * The entry point of the application.
 * <p>
 * This class is responsible for initializing the MVC components of the program: the Model, View,
 * and Controller, and starting the application's main flow. It creates instances of the
 * {@link View}, {@link ModelService}, and {@link Controller}, then invokes the {@code go} method of
 * {@link ControllerInterface} to start the application.
 * </p>
 */
public class Main {

  /**
   * Main method to start the application.
   * <p>
   * This method sets up the application by creating and linking the necessary MVC components. It
   * creates instances of the {@link View} and {@link ModelService} to serve as the View and Model
   * layers. A {@link Controller} is instantiated with references to both the Model and View, along
   * with the standard input stream, to handle user interactions. Finally, the application's control
   * flow is initiated by calling the {@code go} method on the controller.
   * </p>
   *
   * @param args Command line arguments passed to the application.
   */
  public static void main(String[] args) {
    ModelInterface modelInterface = new ModelService();

    boolean useConsole = false;

    if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
      useConsole = true;
    }

    if (useConsole) {
      System.out.println("Console mode selected.");
      ViewInterface viewInterface = new View();
      ControllerInterface controllerInterface = new Controller(modelInterface, viewInterface,
              System.in);
      controllerInterface.start();
    } else {
      System.out.println("Starting GUI...");
      GuiView view = new GuiView();
      ControllerInterface guiControllerInterface = new GuiController(modelInterface, view);
      guiControllerInterface.start();
    }
  }
}

