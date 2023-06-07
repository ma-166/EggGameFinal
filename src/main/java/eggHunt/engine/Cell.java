package eggHunt.engine;
import java.io.Serializable;
public class Cell implements Serializable{
    private CellType type;
    private Position position;

    public Cell(CellType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
