package game;

/**
 * Represents the possible outcomes of a game.
 * The {@code GameResult} enum defines three possible states for a game: a win, a draw, or that the game is still ongoing.
 */
public enum GameResult {
    /**
     * Indicates that the game has been won by one of the players.
     * This result is set when a player successfully meets the win condition of the game.
     */
    WIN,
    /**
     * Indicates that the game has ended in a draw.
     * This result is set when neither player wins, and no more valid moves are possible.
     */
    DRAW,
    /**
     * Indicates that the game is still ongoing.
     * This result is set when the game is not yet finished, and more moves can be made.
     */
    CONTINUE
}
