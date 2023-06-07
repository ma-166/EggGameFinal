import eggHunt.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() {
        gameEngine = new GameEngine(1);
    }

    @Test
    public void testInitGame() {
        gameEngine.initGame();
        Player player = gameEngine.getPlayer();
        assertNotNull(player, "Player should be initialized");

        int keysCollected = gameEngine.getKeysCollected();
        assertEquals(0, keysCollected, "Initial keys collected should be 0");

        int eggsCollected = gameEngine.getEggsCollected();
        assertEquals(0, eggsCollected, "Initial eggs collected should be 0");
    }

    @Test
    public void testMovePlayer() {
        gameEngine.initGame();
        // Assuming the player is at the initial position (GRID_SIZE-1, 0)
        boolean result = gameEngine.movePlayer("up");
        Player player = gameEngine.getPlayer();
        assertEquals(GameEngine.GRID_SIZE - 2, player.getPosition().getX(), "Player should move up");

        // Move to an invalid direction
        result = gameEngine.movePlayer("down");
        assertFalse(result, "Player should not be able to move down from the initial position");
    }

    @Test
    public void testUpdateGrid() {
        gameEngine.initGame();
        Position oldPos = gameEngine.getPlayer().getPosition();
        Position newPos = new Position(GameEngine.GRID_SIZE - 2, 0);

        gameEngine.updateGrid(oldPos, newPos);

        assertEquals(CellType.EMPTY, gameEngine.getGrid()[oldPos.getX()][oldPos.getY()].getType(), "Old position should be empty");
        assertEquals(CellType.PLAYER, gameEngine.getGrid()[newPos.getX()][newPos.getY()].getType(), "New position should be player");
    }

    @Test
    public void testSetDifficulty() {
        gameEngine.setDifficulty(3);
        assertEquals(3, gameEngine.getDifficulty(), "Difficulty should be set to 3");
    }


    @Test
    public void testResetGame() {
        gameEngine.initGame();

        // Move player and collect some items
        gameEngine.movePlayer("right");
        gameEngine.movePlayer("up");

        gameEngine.reset();

        assertEquals(0, gameEngine.getKeysCollected(), "After reset, keys collected should be 0");
        assertEquals(0, gameEngine.getEggsCollected(), "After reset, eggs collected should be 0");

        Player player = gameEngine.getPlayer();
        assertNotNull(player, "After reset, player should not be null");

        assertFalse(gameEngine.getPlayer().hasWon(), "After reset, player should not have won");
    }

}
