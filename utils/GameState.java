package utils;

import game.Player;

public class GameState {
    private char[][] board;
    private Player currentPlayer;

    public GameState(char[][] board, Player currentPlayer) {
        this.board=new char[board.length][];
        // deep copy board preventing tampering
        for(int i=0; i<board.length; i++){
            this.board[i]=board[i].clone();
        }
        this.currentPlayer=currentPlayer;
    }

    public char[][] getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
