package eggHunt.engine;

import java.io.Serializable;

public enum CellType implements Serializable {
    EMPTY,
    ENTRANCE,
    FINISH,
    EGG,
    LOCK,
    PLAYER,
    KEY,
    UNLOCK
}

