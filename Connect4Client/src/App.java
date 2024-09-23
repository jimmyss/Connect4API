import java.util.Scanner;

import game.Connect4;
import game.Player;
import utils.GameState;
import utils.Response;
import utils.Code;

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
        game.startGame();
        printBoard(gameState.getBoard());

        System.out.println(game.getWinningInfo().getStatusCode());

        while (game.getWinningInfo().getStatusCode() == Code.CONT_GAME) {

            Player currentPlayer = gameState.getCurrentPlayer();
            int column = 0;

            if (currentPlayer.isComputer()) {
                System.out.println("Computer is thinking...");
                // sleep for 1 second
                Thread.sleep(1000);
            } else {
                System.out.println(currentPlayer.getPlayerName() + ", please choose a column (0-6): ");
                // Check if the input is an integer
                while (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid integer.");
                    scanner.next();
                }
                column = scanner.nextInt();
            }

            Response<GameState> response = game.dropChecker(column);

            if (response.getStatusCode() != 200) {
                if (response.getStatusCode() == Code.FULL_COL_ERR) {
                    System.out.println("Column " + column + " is full. Please choose another column.");
                    continue;
                }
                if (response.getStatusCode() == Code.INVALID_MOVE_ERR) {
                    System.out.println("Invalid move. Please choose a column between 0 and 6.");
                    continue;
                }
            }

            gameState = response.getData();
            printBoard(gameState.getBoard());
        }

        Response<Player> gameStatus = game.getWinningInfo();
        System.out.println("Game over!");
        if (gameStatus.getStatusCode() == Code.DRAW_GAME) {
            System.out.println("It's a draw!");
        } else if (gameStatus.getStatusCode() == Code.P1_WIN) {
            System.out.println(player1.getPlayerName() + " wins!");
        } else if (gameStatus.getStatusCode() == Code.P2_WIN) {
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
