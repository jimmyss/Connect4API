package game;

import utils.GameState;
import utils.Message;
import utils.Response;

import java.util.Random;

import exceptions.GameException;

public class Connect4 {
    public static final char RED='@';
    public static final char BLUE='#';
    private int mode=-1;
    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int[] lastDrop;
    private boolean isFinished;

    /**** Constructors ****/
    /**
     * This constructor takes in the game mode and initialize a new game
     *
     * @param mode  1. mode==1 human vs human
     *              2. mode==2 human vs computer
     *              3. mode==3 computer vs computer
     */
    public Connect4(int mode) throws GameException {
        this.mode=mode;
        this.board=new char[6][7];
        this.lastDrop=new int[2];
        this.isFinished=false;
        this.player1=new Player();
        this.player2=new Player();
        this.currentPlayer=null;
        this.player1.setColor(RED);
        this.player2.setColor(BLUE);
        updatePlayers(this.mode);
    }

    public Connect4(Player player1, Player player2) {
        this.board = new char[6][7];
        this.lastDrop=new int[2];
        this.isFinished=false;
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setColor(RED);
        this.player2.setColor(BLUE);
        this.currentPlayer = player1;
    }

    public Connect4(int mode, Player player1, Player player2) throws GameException {
        this.mode = mode;
        this.board=new char[6][7];
        this.lastDrop=new int[2];
        this.isFinished=false;
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setColor(RED);
        this.player2.setColor(BLUE);
        this.currentPlayer=player1;
        updatePlayers(this.mode);
    }

    /*************************/
    /**** Private Methods ****/
    /*************************/

    private void updatePlayers(int mode) throws GameException {
        if(mode==1){ // user vs user
            player1.setComputer(false);
            player2.setComputer(false);
        }else if(mode==2){ // user vs computer
            player1.setComputer(false);
            player2.setComputer(true);
        }else if (mode==3){ // com vs com
            player1.setComputer(true);
            player2.setComputer(true);
        } else throw new GameException(Message.GAME_NOT_INIT);
    }

    private void updateBoard(int col) throws GameException {
        if(!isFullCol(col)){ // if the column is not full
            char curColor=currentPlayer.getColor();
            for(int row=0; row<5; row++){
                if(board[row][col]!='\0') {
                    board[row][col] = curColor;
                    lastDrop[0]=row;
                    lastDrop[1]=col;
                }
            }
        }
    }

    private void updateCurPlayer(){
        if(currentPlayer==player1) currentPlayer=player2;
        else currentPlayer=player1;
    }

    private boolean isWon() {
        char curColor=currentPlayer.getColor();
        int curRow=lastDrop[0];
        int curCol=lastDrop[1];
        // check horizontal direction
        int startCol=Math.max(curCol-3, 0);
        for(int col=startCol; col<=curCol; col++){
            if(board[curRow][col]==curColor &&
               board[curRow][col+1]==curColor &&
               board[curRow][col+2]==curColor &&
               board[curRow][col+3]==curColor){
                return true;
            }
        }

        // check vertical direction
        int startRow=Math.max(curRow-3, 0);
        for(int row=startRow; row<=curRow; row++){
            if(board[row][curCol]==curColor &&
               board[row+1][curCol]==curColor &&
               board[row+2][curCol]==curColor &&
               board[row+3][curCol]==curColor){
                return true;
            }
        }

        // check up-left to down-right direction
        for(int i=-3; i<=0; i++){
            int row=curRow+i;
            int col=curCol+i;
            if (row >= 0 && row + 3 <= 5 && col >= 0 && col + 3 <= 6) {
                if (board[row][col] == curColor &&
                        board[row + 1][col + 1] == curColor &&
                        board[row + 2][col + 2] == curColor &&
                        board[row + 3][col + 3] == curColor) {
                    return true;
                }
            }
        }

        // check up-right to down-left direction
        for (int i = -3; i <= 0; i++) {
            int row = curRow + i;
            int col = curCol - i;
            if (row >= 0 && row + 3 <= 5 && col >= 0 && col <= 6) {
                if (board[row][col] == curColor &&
                        board[row + 1][col - 1] == curColor &&
                        board[row + 2][col - 2] == curColor &&
                        board[row + 3][col - 3] == curColor) {
                    return true;
                }
            }
        }

        return true;
    }

    private boolean isDraw() {
        // check if the board is full
        for(int row=0; row<6; row++){
            for(int col=0; col<7; col++){
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }

        // check if current player win the game
        if (!isWon()) {
            return true;
        }
        return false;
    }

    private void judgeGame(){
        if(isWon() || isDraw()) isFinished=true;
    }

    private boolean isFullCol(int column) throws GameException {
        if(column<0 || column>6) throw new GameException(Message.INVALID_MOVE);
        if(this.board[0][column]=='\0'){
            return false;
        }else return true;
    }


    /************************/
    /**** Public Methods ****/
    /************************/

    /**
     * Return information of current player, color, and board info
     *
     */
    public Response<GameState> startGame(){
        if(mode!=-1){
            // This game has not initialize yet
            return Response.error(301, Message.GAME_NOT_INIT);
        }else{
            GameState state=new GameState(board, currentPlayer);
            return Response.success(state);
        }
    }

    /**
     * Allow changing of human player's name
     *
     * @param player
     * @param name
     * @return
     */
    public Response<String> setPlayerName(Player player, String name){
        player.setPlayerName(name);
        return Response.success("new name:"+name);
    }

    /**
     * This method is used to drop one piece to specific column and give response
     *
     * @param column
     */
    public Response<GameState> dropChecker(int column) throws GameException {
        // judge if the column is valid
        if(column<0 || column>6) {
            return Response.error(302, Message.INVALID_MOVE);
        }else if(isFullCol(column)){
            return Response.error(303, Message.COLUMN_FULL);
        }
        // judge if the game is over
        if(isFinished) return Response.error(304, Message.GAME_OVER);

        // drop piece according to current player
        if(currentPlayer.isComputer()){
            Random random=new Random(222);
            int randomNum=random.nextInt(7);
            updateBoard(randomNum);
        }else{
            updateBoard(column);
        }
        judgeGame();
        if(!isFinished) updateCurPlayer();
        GameState state=new GameState(board, currentPlayer);
        return Response.success(state);
    }

    /**
     * This method returns current winning information
     *
     * @return
     */
    public Response<String> getWinningInfo() {
        if(isFinished){
            if(isWon()){
                return Response.success(
                        currentPlayer==player1 ?
                                (Message.P1_WIN_INFO):(Message.P2_WIN_INFO)
                );
            }
            else{
                return Response.success(Message.DRAW_INFO);
            }
        }else{
            return Response.success(Message.GAME_CONTINUE);
        }
    }

    public Response<GameState> getGameState(){
        return Response.success(new GameState(board, currentPlayer));
    }

    /**
     * Reset the game to original status.
     *
     */
    public Response<Boolean> resetGame() {
        try{
            this.board=new char[6][7];
            this.mode=-1;
            this.isFinished=false;
            this.lastDrop=new int[2];
            this.player1=new Player();
            this.player2=new Player();
            this.currentPlayer=player1;
            return Response.success(true);
        }catch (Exception e){
            throw e;
        }
    }
}
