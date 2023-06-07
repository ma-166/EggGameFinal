package eggHunt.engine;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (isValid(x))
            this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (isValid(y))
            this.y = y;
    }

    public boolean isValid(int num){
        if (num < 0 || num >= 10) {
            return false;
        }
        return true;
    }
}

