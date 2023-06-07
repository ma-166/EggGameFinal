package eggHunt.engine;

import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine implements Serializable {


    public void setGameEngine(int difficulty) {
        this.gameEngine = new GameEngine(difficulty);
        setDifficulty(difficulty);
        updateInstance();
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
//        System.out.println("HEREHEREHERE");
//        gameEngine.getGameState();
//        System.out.println("HEREHEREHERE");
        updateInstance();
    }

    private void updateInstance(){
        this.grid = this.gameEngine.getGrid();
        this.difficulty = this.gameEngine.difficulty;
        this.player = this.gameEngine.player;
        this.keysCollected = this.gameEngine.keysCollected;
        this.eggsCollected = this.gameEngine.eggsCollected;
        this.oldPosition = this.gameEngine.oldPosition;
        this.gameOver = this.gameEngine.gameOver;
    }
    private GameEngine gameEngine;


    public static final int GRID_SIZE = 10;

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private boolean gameOver = false;

    private Position oldPosition = new Position(GRID_SIZE - 1, 0);
    private Cell[][] grid;

    private Cell[][] originalGrid;

    public Player getPlayer() {
        return player;
    }

    private Player player;

    public int getDifficulty() {
        return difficulty;
    }

    private int difficulty;

    private int keysCollected = 0;
    private int eggsCollected = 0;

    public int getKeysCollected() {
        return keysCollected;
    }

    public void setKeysCollected(int keysCollected) {
        this.keysCollected = keysCollected;
    }

    public int getEggsCollected() {
        return eggsCollected;
    }

    public void setEggsCollected(int eggsCollected) {
        this.eggsCollected = eggsCollected;
    }



    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine(5);
        gameEngine.printGrid();
    }

    public void printGrid() {
        Position currPos = player.getPosition();

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {

                if (i == currPos.getX() && j == currPos.getY())
                    System.out.print("P ");
                else if (grid[i][j].getType().name() == "EGG")
                    System.out.print("O ");
                else if (grid[i][j].getType().name() == "EMPTY")
                    System.out.print("_ ");
                else
                    System.out.print(grid[i][j].getType().name().charAt(0) + " ");
            }
            System.out.println();
        }
    }
    public Cell[][] getGrid() {
        return grid;
    }
    public GameEngine(int difficulty) {
        this.difficulty = difficulty;
        this.grid = new Cell[GRID_SIZE][GRID_SIZE];
        initGame();
    }

    private Cell[][] copyGrid(Cell[][] grid) {
        Cell[][] copy = new Cell[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                copy[i][j] = new Cell(grid[i][j].getType(), grid[i][j].getPosition());
            }
        }
        return copy;
    }

    public void initGame() {
        // Initialize all cells to empty
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Cell(CellType.EMPTY, new Position(i, j));
            }
        }

        // Add entrance and exit
        grid[GRID_SIZE - 1][0] = new Cell(CellType.ENTRANCE, new Position(0, 0));
        grid[0][GRID_SIZE - 1] = new Cell(CellType.FINISH, new Position(GRID_SIZE - 1, GRID_SIZE - 1));

        // Add player at the entrance
        player = new Player(new Position(GRID_SIZE - 1, 0));

        // Add random elements
        addRandomElements(CellType.EGG, 5, difficulty);
        addRandomElements(CellType.LOCK, 5, difficulty);
        addRandomElements(CellType.KEY, 5, difficulty);

        originalGrid = copyGrid(grid);
    }

    private void addRandomElements(CellType type, int quantity, int difficulty) {
        Random random = new Random();
        Position lockPosition = null;
        List<Position> keyPositions = new ArrayList<>();
        Position exitPosition = findCellByType(CellType.FINISH);

        for (int i = 0; i < quantity; i++) {
            int x, y;
            boolean isValidPosition = false;

            while (!isValidPosition) {
                x = random.nextInt(GRID_SIZE);
                y = random.nextInt(GRID_SIZE);

                // Check if the cell is empty
                if (grid[x][y].getType() == CellType.EMPTY) {
                    // Initialize the lock position if it's not set
                    if (lockPosition == null && type == CellType.LOCK) {
                        lockPosition = new Position(x, y);
                    }

                    // Check the distance to the lock
                    double distanceToLock = (lockPosition != null) ? calculateDistance(x, y, lockPosition.getX(), lockPosition.getY()) : Double.MAX_VALUE;

                    // Check the distance to each existing key
                    boolean isValidDistance = true;
                    for (Position keyPosition : keyPositions) {
                        double distanceToKey = calculateDistance(x, y, keyPosition.getX(), keyPosition.getY());
                        if (distanceToKey < (difficulty + 1) && distanceToKey > (2*difficulty+1)) {
                            isValidDistance = false;
                            break;
                        }
                    }

                    // Adjust lock placement based on difficulty and distance to exit
                    if (type == CellType.LOCK) {
                        double distanceToExit = calculateDistance(x, y, exitPosition.getX(), exitPosition.getY());
                        if (distanceToExit < (5 - difficulty)) {
                            isValidDistance = false;
                        }
                    }

                    if (distanceToLock >= (difficulty + 2) && isValidDistance) {
                        isValidPosition = true;
                        grid[x][y] = new Cell(type, new Position(x, y));

                        if (type == CellType.KEY) {
                            keyPositions.add(new Position(x, y));
                        }
                    }
                }
            }
        }
    }

    public Position findCellByType(CellType type) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j].getType() == type) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }


    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private Position getLockPosition() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j].getType() == CellType.LOCK) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }


    public void updateGrid(Position oldPos, Position newPos) {
        int oldX = oldPos.getX();
        int oldY = oldPos.getY();
        int newX = newPos.getX();
        int newY = newPos.getY();

        CellType oldCellType = grid[oldX][oldY].getType();
        CellType newCellType = grid[newX][newY].getType();

        // Update the old position cell type based on its original value
        if (oldCellType == CellType.PLAYER) {
            if (newCellType == CellType.KEY) {
                // If the player moved onto a key cell, pick up the key
                pickUpKey();
            } else if (newCellType == CellType.EGG) {
                // If the player moved onto an egg cell, pick up the egg
                pickUpEgg();
            } else if (newCellType == CellType.LOCK) {
                // If the player moved onto a lock cell, try to unlock it
                if (unlockLock()) {
                    grid[newX][newY].setType(CellType.UNLOCK);
                }
            }
            grid[oldX][oldY].setType(CellType.EMPTY);
        }

        if (oldCellType == CellType.ENTRANCE){
            grid[oldX][oldY].setType(CellType.EMPTY);
        }
        // Update the new position cell type to player
        grid[newX][newY].setType(CellType.PLAYER);

        oldPosition = newPos;

//        printGrid();
    }


    public boolean movePlayer(String direction) {

        System.out.println("HERE" + direction);
        Position newPosition = new Position(player.getPosition().getX(), player.getPosition().getY());
        switch (direction.toLowerCase()) {
            case "left":
                newPosition.setY(newPosition.getY() - 1);
                break;
            case "right":
                newPosition.setY(newPosition.getY() + 1);
                break;
            case "up":
                newPosition.setX(newPosition.getX() - 1);
                break;
            case "down":
                newPosition.setX(newPosition.getX() + 1);
                break;
            default:
                // Invalid direction
                return false;
        }

        if (!isValidMove(newPosition)){
            return false;
        }

        if (checkWinCondition(newPosition)){
            return true;
        }
        else {
            if (originalGrid[newPosition.getX()][newPosition.getY()].getType() == CellType.FINISH) {
                Alert howToPlayAlert = new Alert(Alert.AlertType.INFORMATION);
                howToPlayAlert.setTitle("!!!");
                howToPlayAlert.setHeaderText(null);
                howToPlayAlert.setContentText("Collect all the Eggs");
                howToPlayAlert.showAndWait();
                return false;
            }
        }
        updateGrid(oldPosition, newPosition);


        // If the move is valid, update the player's position and increment the step count
        player.setPosition(newPosition);
        player.incrementSteps();

        return false;

    }

    private boolean isValidMove(Position newPosition) {
        int x = newPosition.getX();
        int y = newPosition.getY();

        // Check if the new position is within the grid bounds
        if (x < 0 || x >= GameEngine.GRID_SIZE || y < 0 || y >= GameEngine.GRID_SIZE) {
            return false; // Outside the grid bounds
        }

        // Check if the new position is an empty cell or an unlocked cell
        CellType cellType = grid[x][y].getType();
        if (cellType == CellType.LOCK){
            if (getKeysCollected() > 0){
//                setKeysCollected(getKeysCollected() - 1);
                return true;
            }
            else{
                Alert howToPlayAlert = new Alert(Alert.AlertType.INFORMATION);
                howToPlayAlert.setTitle("!!!");
                howToPlayAlert.setHeaderText(null);
                howToPlayAlert.setContentText("You need a key to unlock this cell");
                howToPlayAlert.showAndWait();
                return false;
            }
        }
        return true;
    }


    public void pickUpKey() {
//        setKeysCollected(getKeysCollected() + 1);
        this.keysCollected++;
        System.out.println(getKeysCollected());

    }

    public void pickUpEgg() {
        setEggsCollected(getEggsCollected() + 1);
    }

    public boolean unlockLock() {

        if (getKeysCollected() != 0) {
//            int curr_keys = getKeysCollected();
//            this.setKeysCollected( curr_keys - 1);
            this.keysCollected--;
            System.out.println(getKeysCollected());
            return true;
        }

        return false;

    }

    public boolean checkWinCondition() {
        if (player.getPosition().equals(grid[GRID_SIZE - 1][GRID_SIZE - 1].getPosition()) // Player is at the exit
                && eggsCollected == 5) { // All eggs have been collected
            return true;
        }
        return false;
    }

    public void getGameState() {
        this.printGrid();
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void reset() {


        this.keysCollected = 0;
        this.eggsCollected = 0;
        this.getPlayer().setSteps(0);

        System.out.println("Reset Successful");
    }

    private boolean checkWinCondition(Position nextPos){

        int x = nextPos.getX();
        int y = nextPos.getY();

        if ((originalGrid[x][y].getType() == CellType.FINISH) && (this.eggsCollected == 5)){
            return true;
        }

        return false;

    }

}

