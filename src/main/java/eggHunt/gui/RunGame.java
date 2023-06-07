package eggHunt.gui;

import eggHunt.engine.GameEngine;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// The main class which extends Application to run the JavaFX application
public class RunGame extends Application {
    // Constants
    private static final int GRID_SIZE = GameEngine.GRID_SIZE;

    private GameEngine gameEngine;
    private GameUI gameUI;
    private ComboBox<Integer> difficultyComboBox;

    @Override
    public void start(Stage primaryStage) {
        gameEngine = new GameEngine(1); // Initialize game with default difficulty

        // Create game UI components and setup the game board
        gameUI = new GameUI(gameEngine);
        gameUI.setupGameBoard(primaryStage);



        // Initialize the game stage
        primaryStage.setScene(gameUI.createScene(primaryStage));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
