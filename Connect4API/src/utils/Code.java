package utils;

/**
 * The {@code Code} class defines constants for various status codes used in a Connect4 game API.
 * These codes represent the results of different actions taken during the game (both success
 * and error scenarios).
 *
 * <p>These constants are used to standardize the response codes for different operations
 * like starting the game, dropping a piece, checking the game status, and handling errors.</p>
 *
 * <p><strong>Success Codes:</strong> These codes range from 200 to 207 and indicate successful
 * operations such as starting the game, setting player names, dropping pieces, and determining
 * the outcome of the game (win, draw, or continue).</p>
 *
 * <p><strong>Error Codes:</strong> These codes range from 300 to 304 and indicate various errors,
 * including starting game errors, invalid moves, full column errors, and actions on a finished game.</p>
 *
 * @author Shimao Du
 * @version 1.0
 * @since 2024-09-23
 */
public class Code {

    // Successful Code

    /**
     * Status code indicating the game has successfully started.
     */
    public static final int START_GAME=200;
    /**
     * Status code indicating the player name has been successfully set.
     */
    public static final int SET_NAME=201;
    /**
     * Status code indicating a piece has been successfully dropped into the game board.
     */
    public static final int DROP_PIECE=202;
    /**
     * Status code indicating Player 1 has won the game.
     */
    public static final int P1_WIN=203;
    /**
     * Status code indicating Player 2 has won the game.
     */
    public static final int P2_WIN=204;
    /**
     * Status code indicating the game has ended in a draw.
     */
    public static final int DRAW_GAME=205;
    /**
     * Status code indicating the game is still ongoing and no player has won yet.
     */
    public static final int CONT_GAME=206;
    /**
     * Status code indicating that the current game status was successfully retrieved.
     */
    public static final int GET_GAME_STATUS_SUC=207;


    // Error Code
    /**
     * Error code indicating an error occurred while trying to start the game.
     */
    public static final int START_GAME_ERR=300;
    /**
     * Error code indicating an invalid move was attempted (e.g., dropping a piece in a non-existing column).
     */
    public static final int INVALID_MOVE_ERR=302;
    /**
     * Error code indicating the column is full, and a piece cannot be dropped in that column.
     */
    public static final int FULL_COL_ERR=303;
    /**
     * Error code indicating an action was attempted on a finished game (e.g., trying to drop a piece after the game is over).
     */
    public static final int GAME_FIN_ERR=304;
}
