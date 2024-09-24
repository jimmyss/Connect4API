package game;

/**
 * The Player class represents a player in the Connect4 game.
 * It holds information about the player's name, piece color, and
 * whether the player is a human or a computer.
 *
 * <p>This class provides methods to set and retrieve the player's name,
 * piece color, and type (human or computer). It is essential for managing
 * player-specific data and interactions within the game.</p>
 *
 * @author Shimao Du
 * @version 1.0
 * @since 2024-09-23
 */
public class Player {
    // Name of the player
    private String playerName;
    // Color of the player's game piece
    private char color;
    // Indicates if the player is a computer
    private boolean isComputer;

    /**
     * Default constructor setting player name to blank
     */
    public Player() {
        playerName="";
    }

    /**
     * Constructor including specific player name and computer information
     *
     * @param playerName the name of this player
     * @param isComputer indicate if current player is a computer
     */
    public Player(String playerName, boolean isComputer) {
        this.playerName = playerName;
        this.isComputer = isComputer;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player's name.
     *
     * @param playerName the new name for the player
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Checks if the player is a computer.
     *
     * @return true if the player is a computer, false if the player is human
     */
    public boolean isComputer() {
        return isComputer;
    }

    /**
     * Sets whether the player is a computer or a human.
     *
     * @param computer true to set the player as a computer, false for human
     */
    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    /**
     * Gets the player's piece color.
     *
     * @return the color of the player's piece
     */
    public char getColor() {
        return color;
    }

    /**
     * Sets the player's piece color.
     *
     * @param color the color to assign to the player's piece
     */
    public void setColor(char color) {
        this.color = color;
    }
}
