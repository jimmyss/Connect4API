import java.util.Random;
import java.util.Scanner;

import game.Connect4;
import game.Player;
import utils.GameState;
import utils.Message;
import utils.Response;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Connect4!");
        System.out.println("Please select a mode: ");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs Computer");
        System.out.println("3. Computer vs Computer");

        int mode = scanner.nextInt();

        Player player1, player2;
        scanner.nextLine();

        if (mode == 1) {
            // Human vs Human
            System.out.println("Enter Player 1 name: ");
            String player1Name = scanner.next();
            player1 = new Player(player1Name, false);

            System.out.println("Enter Player 2 name: ");
            String player2Name = scanner.next();
            player2 = new Player(player2Name, false);
        } else if (mode == 2) {
            // Human vs Computer
            System.out.println("Enter Player name: ");
            String player1Name = scanner.next();
            player1 = new Player(player1Name, false);
            player2 = new Player("Computer", true);
        } else {
            // Computer vs Computer
            player1 = new Player("Computer 1", true);
            player2 = new Player("Computer 2", true);
        }

        Connect4 game = new Connect4(mode, player1, player2);
        GameState gameState = game.getGameState().getData();
        printBoard(gameState.getBoard());

        while (!game.getWinningInfo().getMsg().equals(Message.GAME_OVER)) {

            Player currentPlayer = gameState.getCurrentPlayer();
            int column;

            if (currentPlayer.isComputer()) {
                System.out.println("Computer is thinking...");
                column = new Random().nextInt(7);
                System.out.println(currentPlayer.getPlayerName() + " chooses column " + column);
            } else {
                System.out.println(currentPlayer.getPlayerName() + ", please choose a column (0-6): ");
                column = scanner.nextInt();
            }

            Response<GameState> response = game.dropChecker(column);
            gameState = response.getData();

            if (response.getStatusCode() != 200) {
                if (response.getMsg().equals(Message.COLUMN_FULL)) {
                    System.out.println("Column " + column + " is full. Please choose another column.");
                    continue;
                }
                if (response.getMsg().equals(Message.INVALID_MOVE)) {
                    System.out.println("Invalid move. Please choose a column between 0 and 6.");
                    continue;
                }
            }

            printBoard(gameState.getBoard());
        }

        String gameOverMsg = game.getWinningInfo().getMsg();
        System.out.println("Game over!");
        if (gameOverMsg.equals(Message.DRAW_INFO)) {
            System.out.println("It's a draw!");
        } else if (gameOverMsg.equals(Message.P1_WIN_INFO)) {
            System.out.println(player1.getPlayerName() + " wins!");
        } else if (gameOverMsg.equals(Message.P2_WIN_INFO)) {
            System.out.println(player2.getPlayerName() + " wins!");
        }

        scanner.close();
    }

    private static void printBoard(char[][] board) {
        System.out.println("Current board:");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\u0000' ? '.' : cell) + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
        System.out.println();        
    }
}
