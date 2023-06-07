package eggHunt.engine;

import java.util.Scanner;

public class TextUI {
    private GameEngine gameEngine;
    private Scanner scanner;

    public TextUI() {
        gameEngine = new GameEngine(5); // Initialize game with difficulty 5
        scanner = new Scanner(System.in);
    }

    public void run() {
        String command;
        while (true) {
            if (gameEngine.getPlayer().getSteps() >= 10) {
                System.out.println("Max Number of steps exceeded");
                System.out.println("You Lose");
                break;
            }
            gameEngine.getGameState();
            System.out.print("Enter command: ");
            command = scanner.nextLine();

            // Handle command
            if (command.equals("quit")) {
                break;
            } else if (command.startsWith("move ")) {
                String direction = command.substring(5);
                gameEngine.movePlayer(direction);
                if (gameEngine.checkWinCondition()) {
                    System.out.println("You've collected all the eggs and reached the exit! You win!");
                    break;
                }
            }
            // Add additional commands as needed
        }
    }

    public static void main(String[] args) {
        TextUI textUI = new TextUI();
        textUI.run();
    }
}

