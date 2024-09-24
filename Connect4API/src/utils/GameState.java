package utils;

import game.Player;

/**
 * The GameState class represents the current state of the Connect4 game.
 * It holds the game board and the current player, allowing for the
 * retrieval of the game's status at any point.
 *
 * <p>This class is useful for encapsulating the game's state, making
 * it easier to manage and pass around during game play. The board is
 * stored as a deep copy to prevent unintended modifications.</p>
 *
 * @author Shimao Du
 * @version 1.0
 * @since 2024-09-23
 */
public class GameState {
    // The game board represented as a 2D character array
    private char[][] board;
    // The current player whose turn it is
    private Player currentPlayer;

    /**
     * Constructs a new GameState with the specified board and current player.
     *
     * @param board the current state of the game board
     * @param currentPlayer the player whose turn it is currently
     */
    public GameState(char[][] board, Player currentPlayer) {
        this.board=new char[board.length][];
        // deep copy board preventing tampering
        for(int i=0; i<board.length; i++){
            this.board[i]=board[i].clone();
        }
        this.currentPlayer=currentPlayer;
    }

    /**
     * Gets the current game board.
     *
     * @return a 2D array representing the game board
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Gets the current player.
     *
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
