package eggHunt.engine;

import javafx.scene.control.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private Position position;

    private boolean playerWon = false;
    private int steps;
    private List<Cell> collectedKeys;

    public Player(Position position) {
        this.position = position;
        this.steps = 0;
        this.collectedKeys = new ArrayList<>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getSteps() {
        return steps;
    }

    public void incrementSteps() {
        this.steps++;
    }

    public List<Cell> getCollectedKeys() {
        return collectedKeys;
    }

    public void collectKey(Cell keyCell) {
        this.collectedKeys.add(keyCell);
    }

    public void setWin(){
        this.playerWon = true;
    }

    public boolean hasWon(){
        return this.playerWon;
    }
    public void setSteps(int steps){
        this.steps = steps;
    }
}
