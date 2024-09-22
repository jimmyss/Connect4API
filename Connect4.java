
public class Connect4 {
    private int mode;
    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Connect4(int mode) {
        
    }

    public Connect4(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new char[6][7];
        this.currentPlayer = player1;
        initializeBoard();
    }

    private void initializeBoard() {
        
    }

    private boolean isWon() {
        return true;
    }

    private boolean isDraw() {
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void dropChecker(int column) {

    }

    public int getGameStatus() {
        if (isWon()) {
            return 1;
        }
        if (isDraw()) {
            return 2;
        }
        return 0;
    }


    


}
