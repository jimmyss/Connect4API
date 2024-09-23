package game;

public class Player {
    private String playerName;
    private char color;
    private boolean isComputer;

    public Player() {
        playerName="";
    }

    public Player(String playerName, boolean isComputer) {
        this.playerName = playerName;
        this.isComputer = isComputer;
    }

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
