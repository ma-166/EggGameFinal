package eggHunt.gui;

import eggHunt.engine.Cell;
import eggHunt.engine.GameEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

// Class to manage the Game User Interface
public class GameUI {
    @FXML
    private GridPane gridPane;
    private final GameEngine gameEngine;
    private final int cellSize = 65;
    private final Label stepsLabel;
    private final Label keysLabel;
    private final Label eggsLabel;
    private ComboBox<Integer> difficultyComboBox;
    private final Image playerImage;
    private final Image exitImage;
    private final Image eggImage;
    private final Image lockImage;
    private final Image keyImage;

    private static final String PLAYER_IMG_PATH = "src/main/resources/sprites/player.png";
    private static final String EXIT_IMG_PATH = "src/main/resources/sprites/exit.png";
    private static final String EGG_IMG_PATH = "src/main/resources/sprites/egg_icon.png";
    private static final String LOCK_IMG_PATH = "src/main/resources/sprites/lock_icon.png";
    private static final String KEY_IMG_PATH = "src/main/resources/sprites/key_icon.png";

    private Stage stage;

    private Timeline timeline;
    private Duration time = Duration.minutes(0);
    private Label timerLabel;

    public GameUI(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        gameEngine.initGame();
        stepsLabel = new Label("Steps: 0");
        keysLabel = new Label("Keys: 0");
        eggsLabel = new Label("Eggs: 0");
        timerLabel = new Label();


        playerImage = ImageLoader.loadImage(PLAYER_IMG_PATH);
        exitImage = ImageLoader.loadImage(EXIT_IMG_PATH);
        eggImage = ImageLoader.loadImage(EGG_IMG_PATH);
        lockImage = ImageLoader.loadImage(LOCK_IMG_PATH);
        keyImage = ImageLoader.loadImage(KEY_IMG_PATH);

    }

    public void setupGameBoard(Stage primaryStage) {

        this.stage = primaryStage;
        gridPane = new GridPane();
        Cell[][] grid = gameEngine.getGrid();

        // Create your grid
        for (int i = 0; i < GameEngine.GRID_SIZE; i++) {
            for (int j = 0; j < GameEngine.GRID_SIZE; j++) {
                Button button = new Button();
                Cell cell = grid[i][j];
                ImageView imageView = new ImageView();

                // Set the preferred size of the button
                button.setPrefWidth(cellSize);
                button.setPrefHeight(cellSize);

                String cellStyle = "-fx-border-color: black; -fx-border-width: 1px;"; // Add black border to each cell

                switch (cell.getType()) {
                    case PLAYER:
                        imageView.setImage(playerImage);
                        break;
                    case ENTRANCE:
                        imageView.setImage(playerImage);
                        break;
                    case FINISH:
                        imageView.setImage(exitImage);
                        break;
                    case EGG:
                        imageView.setImage(eggImage);
                        break;
                    case LOCK:
                        imageView.setImage(lockImage);
                        break;
                    case KEY:
                        imageView.setImage(keyImage);
                        break;
                    default:
                        button.setStyle(cellStyle + "-fx-background-color: white;");
                        break;
                }

                // Set the size of the ImageView based on the cell size
                imageView.setFitWidth(cellSize);
                imageView.setFitHeight(cellSize);

                // Set the ImageView as the graphic for the button
                button.setGraphic(imageView);

                // Add the button to the grid pane
                gridPane.add(button, j, i);
            }
        }

        handleEvents();
        createScene(primaryStage);
    }

    public void handleEvents(){
        // Handle key events for player movement
        gridPane.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            switch (keyCode) {
                case UP:
                    gameEngine.movePlayer("up");
                    break;
                case DOWN:
                    gameEngine.movePlayer("down");
                    break;
                case LEFT:
                    gameEngine.movePlayer("left");
                    break;
                case RIGHT:
                    gameEngine.movePlayer("right");
                    break;
                default:
                    break;
            }
        });
    }

    private Button createArrowButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(50, 50);
        return button;
    }

    public void createTimer(){
        time = Duration.minutes(0);
        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
            time = time.add(javafx.util.Duration.seconds(0.95));
            long totalSeconds = (long) time.toSeconds();
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            timerLabel.setText("Time Elapsed: " + minutes + ":" + String.format("%02d", seconds));

            // Stop the timer when it reaches 0
            if (totalSeconds <= 0) {
                timeline.stop();
            }
        });

        // Create and start the timeline
        timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public Scene createScene(Stage primaryStage) {
        // Create and return your Scene here
        // ...
        // Create a VBox to hold the key, egg, and step labels
        MenuBar menuBar = new MenuBar();
        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().add(helpMenu);

        MenuItem howToPlay = new MenuItem("How to Play");
        MenuItem about = new MenuItem("About");

        helpMenu.getItems().addAll(howToPlay, about);

        howToPlay.setOnAction(e -> {
            Alert howToPlayAlert = new Alert(Alert.AlertType.INFORMATION);
            howToPlayAlert.setTitle("How to Play");
            howToPlayAlert.setHeaderText(null);
            howToPlayAlert.setContentText("1. Collect all the eggs and reach teh exit to win.\n2. You need to have a key to pass through the locked cells");
            howToPlayAlert.showAndWait();
        });

        about.setOnAction(e -> {
            Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
            aboutAlert.setTitle("About");
            aboutAlert.setHeaderText(null);
            aboutAlert.setContentText("OOP Project");
            aboutAlert.showAndWait();
        });

        VBox vbox_menu = new VBox(menuBar);
        vbox_menu.setAlignment(Pos.TOP_RIGHT);
        VBox.setMargin(vbox_menu, new Insets(0, 0, 30, 0));


        HBox timer = new HBox(10);
        timerLabel.setStyle("-fx-font-size: 18px;");
        timer.getChildren().add(timerLabel);
        timer.setAlignment(Pos.CENTER);

        createTimer();

        HBox infoBox = new HBox(10);
        infoBox.setAlignment(Pos.CENTER);

        // Create a label to display the key count
        keysLabel.setStyle("-fx-font-size: 18px;");
        infoBox.getChildren().add(keysLabel);

        // Create a label to display the egg count
        eggsLabel.setStyle("-fx-font-size: 18px;");
        infoBox.getChildren().add(eggsLabel);

        // Create a label to display the step count
        stepsLabel.setStyle("-fx-font-size: 18px;");
        infoBox.getChildren().add(stepsLabel);

        // Create a ComboBox for selecting the difficulty
        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll(1, 2, 3, 4, 5);
        difficultyComboBox.setValue(1); // Set default difficulty

        // Handle the difficulty selection change
        difficultyComboBox.setOnAction(event -> {
            int difficulty = difficultyComboBox.getValue();
            gameEngine.setDifficulty(difficulty);
            gameEngine.initGame();

            // Update the UI grid after changing the difficulty
            updateGrid(gameEngine.getGrid(), gridPane, true);
            stepsLabel.setText("Steps: " + gameEngine.getPlayer().getSteps());
            keysLabel.setText("Keys: " + gameEngine.getKeysCollected());
            eggsLabel.setText("Eggs: " + gameEngine.getEggsCollected());
        });

        // Create arrow buttons for movement
        Button upButton = createArrowButton("↑");
        Button downButton = createArrowButton("↓");
        Button leftButton = createArrowButton("←");
        Button rightButton = createArrowButton("→");
//
        // Handle button actions for movement
        upButton.setOnAction(event -> movePlayer("up"));
        downButton.setOnAction(event -> movePlayer("down"));
        leftButton.setOnAction(event -> movePlayer("left"));
        rightButton.setOnAction(event -> movePlayer("right"));

        // Create an HBox to hold the arrow buttons
        HBox arrowButtonBox = new HBox(10, upButton, downButton, leftButton, rightButton);
        arrowButtonBox.setAlignment(Pos.CENTER);

        // Create an HBox to hold the difficulty label and ComboBox
        HBox difficultyBox = new HBox(10, new Label("Difficulty:"), difficultyComboBox);
        difficultyBox.setAlignment(Pos.CENTER);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Game Saved!");
            alert.setOnHidden(event_alert -> saveGame("src/main/resources/savedGame.txt"));
            alert.showAndWait();
        });

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadGame("src/main/resources/savedGame.txt"));

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(event -> restartGame());

        HBox saving = new HBox(10, saveButton, loadButton, restartButton);
        saving.setAlignment(Pos.CENTER);

        VBox infoPanel = new VBox(30, vbox_menu, timer, arrowButtonBox, difficultyBox, infoBox, saving);
        infoPanel.setAlignment(Pos.CENTER);

        infoPanel.setFillWidth(true);
        infoPanel.setAlignment(Pos.CENTER);



        HBox hbox = new HBox(10, gridPane, infoPanel);
        hbox.setAlignment(Pos.CENTER);



        Scene scene = new Scene(hbox, 1100, 772);

        gridPane.prefWidthProperty().bind(scene.widthProperty());
        gridPane.prefHeightProperty().bind(scene.heightProperty().subtract(infoPanel.heightProperty()));
        infoPanel.prefWidthProperty().bind(scene.widthProperty());
        infoBox.prefWidthProperty().bind(scene.widthProperty());


        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        return scene; // Placeholder return, replace with your own implementation
    }

    private void movePlayer(String direction) {

        boolean won = gameEngine.movePlayer(direction);

        if (gameEngine.getPlayer().getSteps() > 100){
            gameEngine.setGameOver(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You Lose!");
            alert.setHeaderText(null);
            alert.setContentText("Number of steps Exceeded the limit. Try Again!");
            alert.setOnHidden(event -> restartGame());
            alert.showAndWait();

            return;
        }

        if (won){
            gameEngine.getPlayer().setWin();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText("You Win!");
            alert.setOnHidden(event -> restartGame());
            alert.showAndWait();
            return;
        }
        // Update the UI grid after each movement
        updateGrid(gameEngine.getGrid(), gridPane, false);

        stepsLabel.setText("Steps: " + gameEngine.getPlayer().getSteps());
        keysLabel.setText("Keys: " + gameEngine.getKeysCollected());
        eggsLabel.setText("Eggs: " + gameEngine.getEggsCollected());
    }

    private void restartGame() {
        // Reset the game state to its initial state
        gameEngine.reset();

        gameEngine.setGameEngine(2);

        this.time = Duration.minutes(5);

        // Update the UI grid

        updateGrid(gameEngine.getGrid(), gridPane, true);
        createTimer();


        // Reset labels
        stepsLabel.setText("Steps: 0");
        keysLabel.setText("Keys: 0");
        eggsLabel.setText("Eggs: 0");
    }

    private void updateGrid(Cell[][] grid, GridPane gridPane, boolean update_flag) {

        for (int i = 0; i < GameEngine.GRID_SIZE; i++) {
            for (int j = 0; j < GameEngine.GRID_SIZE; j++) {
                Button button = (Button) gridPane.getChildren().get(i * GameEngine.GRID_SIZE + j);
                Cell cell = grid[i][j];
                ImageView imageView = new ImageView();

                // Set the preferred size of the button
                button.setPrefWidth(cellSize);
                button.setPrefHeight(cellSize);

                String cellStyle = "-fx-border-color: black; -fx-border-width: 1px;"; // Add black border to each cell

                switch (cell.getType()) {
                    case PLAYER:
                        imageView.setImage(playerImage);
                        break;
                    case ENTRANCE:
                        if (update_flag) {
                            imageView.setImage(playerImage);
                        }
                        break;
                    case FINISH:
                        imageView.setImage(exitImage);
                        break;
                    case EGG:
                        imageView.setImage(eggImage);
                        break;
                    case LOCK:
                        imageView.setImage(lockImage);
                        break;
                    case KEY:
                        imageView.setImage(keyImage);
                        break;
                    default:
                        button.setStyle(cellStyle + "-fx-background-color: white;");
                        break;
                }

                // Set the size of the ImageView based on the cell size
                imageView.setFitWidth(cellSize);
                imageView.setFitHeight(cellSize);

                // Set the ImageView as the graphic for the button
                button.setGraphic(imageView);
            }
        }
    }

    public void saveGame(String filePath) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameEngine);
            gameEngine.getGameState();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filePath) {
        try {
//            gameEngine.reset();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            gameEngine.setGameEngine((GameEngine) objectInputStream.readObject());



            objectInputStream.close();
            fileInputStream.close();

//            Stage primaryStage = new Stage();
            gameEngine.getGameState();
            updateGrid(gameEngine.getGrid(), gridPane, true); // You need to redraw the board after loading a new game state

            stepsLabel.setText("Steps: " + gameEngine.getPlayer().getSteps());
            keysLabel.setText("Keys: " + gameEngine.getKeysCollected());
            eggsLabel.setText("Eggs: " + gameEngine.getEggsCollected());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
