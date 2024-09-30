package game;

import game.Player;

/**
 * The GameState class represents the current state of the Connect4 game.
 * It holds the game board, the current player and the current result of the game, 
 * allowing for the retrieval of the game's status at any point.
 *
 * <p>This class is useful for encapsulating the game's state, making
 * it easier to manage and pass around during game play. The board is
 * stored as a deep copy to prevent unintended modifications.</p>
 *
 * @author Shimao Du
 * @version 1.0
 * @since 2024-09-23
 */
public class GameContext {
    // The game board represented as a 2D character array
    private char[][] board;
    // The current player whose turn it is
    private Player currentPlayer;

    private GameResult result;

    /**
     * Constructs a new {@code GameContext} with the given board, current player, and game result.
     *
     * This constructor performs a deep copy of the provided board to prevent external modification. 
     * The initial board is a 2D character array, where each element represents a cell on the game board. 
     * The board typically has 6 rows and 7 columns, with empty cells initialized to '\u0000'.
     *
     * @param board A 2D character array representing the game board. This array is deep-copied.
     * @param currentPlayer The player who is currently taking their turn.
     * @param result The current result of the game (e.g., WIN, DRAW, or CONTINUE).
     */
    public GameContext(char[][] board, Player currentPlayer, GameResult result) {
        this.board=new char[board.length][];
        // deep copy board preventing tampering
        for(int i=0; i<board.length; i++){
            this.board[i]=board[i].clone();
        }
        this.currentPlayer=currentPlayer;
        this.result=result;
    }

    /**
     * Returns the current game board.
     *
     * The board is a 6x7 grid of characters. Initially, the board contains empty cells 
     * represented by the null character ('\u0000'). During gameplay, these cells are replaced 
     * by player tokens, such as 'R' for Player 1 and 'B' for Player 2.
     *
     * @return A 2D character array representing the current state of the game board.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Returns the current player.
     *
     * @return The {@code Player} object representing the player whose turn it is.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the current game result.
     *
     * @return The {@code GameResult} object representing the current game status, 
     *         which can be {@code WIN}, {@code DRAW}, or {@code CONTINUE}.
     */
    public GameResult getResult() {
        return result;
    }

    /**
     * Sets the game board.
     *
     * @param board A 2D character array representing the new state of the game board.
     */
    public void setBoard(char[][] board) {
        this.board = board;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer The new current player.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets the current game result.
     *
     * @param result The new result of the game (e.g., WIN, DRAW, CONTINUE).
     */
    public void setResult(GameResult result) {
        this.result = result;
    }
}
