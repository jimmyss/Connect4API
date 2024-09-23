package game;

public class Player {
    private String playerName;
    private char color;
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
     * Gets player's name
     *
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
