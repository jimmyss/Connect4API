package game;

import utils.Code;
import utils.GameState;
import utils.Response;

import java.util.Random;

import exceptions.GameException;

/**
 * Connect4 class represents the logic for the Connect4 game. It manages the game state, players,
 * and interactions such as placing pieces, checking if the game is won or drawn, and switching
 * between players.
 */
public class Connect4 {
    // Constants for the board size
    private static final int ROWS=6;
    private static final int COLS=7;
    // Constants for the game piece colors
    public static final char RED='@';
    public static final char BLUE='#';

    // Game related attributes
    private int mode=-1;
    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int[] lastDrop;
    private boolean isFinished;

    /**** Constructors ****/

    /**
     * Initializes a new Connect4 game with the given mode.
     *
     * @param mode  1. mode==1 human vs human
     *              2. mode==2 human vs computer
     *              3. mode==3 computer vs computer
     * @throws GameException if the game mode is invalid
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

    /**
     * Initializes a Connect4 game with two given players
     *
     * @param player1 The instance of the first player
     * @param player2 The instance of the second player
     */
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

    /**
     * Initializes a Connect4 game with given game mode and two players
     *
     * @param mode game mode 1, 2, or 3
     * @param player1 The instance of the first player
     * @param player2 The instance of the second player
     * @throws GameException if the game mode is invalid
     */
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

    /**
     * Updates players' type based on game mode
     *
     * @param mode The game mode
     * @throws GameException if the game mode is invalid
     */
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
        } else throw new GameException("Game not init");
    }

    /**
     * Updates the game board after a piece is dropped in specific column
     *
     * @param col The column where the piece is dropped
     * @throws GameException if the column is invalid or full
     */
    private void updateBoard(int col) throws GameException {
        if(!isFullCol(col)){ // if the column is not full
            char curColor=currentPlayer.getColor();
            for (int row = ROWS-1; row >= 0; row--) {
                if (board[row][col] == '\u0000') {
                    board[row][col] = curColor;
                    lastDrop[0] = row;
                    lastDrop[1] = col;
                    break;
                }
            }
            // for(int row=; row<5; row++){
            //     if(board[row][col] =='\u0000') {
            //         board[row][col] = curColor;
            //         lastDrop[0]=row;
            //         lastDrop[1]=col;
            //         break;
            //     }
            // }
        }
    }

    /**
     * Switches the current player to the other player.
     */
    private void updateCurPlayer(){
        if(currentPlayer==player1) currentPlayer=player2;
        else currentPlayer=player1;
    }

    /**
     * Checks if current player has won the game
     * @return true if current player has won, vise versa.
     */
    private boolean isWon() {
        char curColor=currentPlayer.getColor();
        int curRow=lastDrop[0];
        int curCol=lastDrop[1];
        // check horizontal direction
        int startCol=Math.max(curCol-3, 0);
        int endCol=Math.min(curCol+3, COLS - 1);
        for(int col=startCol; col<=endCol - 3; col++){
            if(board[curRow][col]==curColor &&
               board[curRow][col+1]==curColor &&
               board[curRow][col+2]==curColor &&
               board[curRow][col+3]==curColor){
                return true;
            }
        }

        // check vertical direction
        int startRow=Math.max(curRow-3, 0);
        int endRow = Math.min(curRow + 3, ROWS - 1);
        for(int row = startRow; row <= endRow - 3; row++){
            if(board[row][curCol]==curColor &&
               board[row+1][curCol]==curColor &&
               board[row+2][curCol]==curColor &&
               board[row+3][curCol]==curColor){
                return true;
            }
        }

        // check up-left to down-right direction
        startRow = curRow + Math.min(3, ROWS - 1 - curRow);
        startCol = curCol + Math.min(3, COLS - 1 - curCol);
        int count = 0;
        while (startRow >= 0 && startCol >= 0) {
            if (board[startRow][startCol] == curColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
            startRow--;
            startCol--;
        }

        // check up-right to down-left direction
        startRow = curRow + Math.min(3, ROWS - 1 - curRow);
        startCol = curCol - Math.min(3, curCol);
        count = 0;
        while (startRow >= 0 && startCol < COLS) {
            if (board[startRow][startCol] == curColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
            startRow--;
            startCol++;
        }

        return false;
    }

    /**
     * Checks if the game is a draw.
     *
     * @return true if the game is a draw, vise versa.
     */
    private boolean isDraw() {
        // check if there is a winner
        if(isWon()) return false;
        // check if the board is full
        for(int row=0; row<6; row++){
            for(int col=0; col<7; col++){
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the game has been won or drawn and set isFinished flag.
     */
    private void judgeGame(){
        if(isWon() || isDraw()) isFinished=true;
    }

    /**
     * Checks if the specific column is full
     *
     * @param column The column to check
     * @return true if the column is full, vise versa.
     * @throws GameException if the colum is out of bound
     */
    private boolean isFullCol(int column) throws GameException {
        if(column<0 || column>6) throw new GameException("Invalid move");
        if(this.board[0][column]=='\0'){
            return false;
        }else return true;
    }


    /************************/
    /**** Public Methods ****/
    /************************/

    /**
     * Returns current game state and status code information.
     *
     * @return A response object containing current game state and code
     */
    public Response<GameState> startGame(){
        if(mode!=-1){
            // This game has not initialize yet
            return Response.error(Code.START_GAME_ERR);
        }else{
            GameState state=new GameState(board, currentPlayer);
            return Response.success(Code.START_GAME, state);
        }
    }

    /**
     * Sets a player's name
     *
     * @param player The player whose name should be changed
     * @param name The new name for this player
     * @return A response object containing status code and message
     */
    public Response<String> setPlayerName(Player player, String name){
        player.setPlayerName(name);
        return Response.success(Code.SET_NAME,"new name:"+name);
    }

    /**
     * Drops a checker piece in the specific column and returns the updated game state.
     *
     * @param column The column where the piece should be dropped
     * @return A response object containing updated board and player information
     * @throws GameException if the move is invalid
     */
    public Response<GameState> dropChecker(int column) throws GameException {
        // judge if the column is valid
        if(column<0 || column>6) {
            return Response.error(Code.INVALID_MOVE_ERR);
        }else if(isFullCol(column)){
            return Response.error(Code.FULL_COL_ERR);
        }
        // judge if the game is over
        if(isFinished) return Response.error(Code.GAME_FIN_ERR);

        // drop piece according to current player
        if(currentPlayer.isComputer()){
            Random random=new Random();
            int randomNum=random.nextInt(7);
            updateBoard(randomNum);
        }else{
            updateBoard(column);
        }
        judgeGame();
        if(!isFinished) updateCurPlayer();
        GameState state=new GameState(board, currentPlayer);
        return Response.success(Code.DROP_PIECE, state);
    }

    /**
     * Returns information about current game state, including if there is a winner
     *
     * @return A response object containing status code and current winner(if the game has a winner)
     */
    public Response<Player> getWinningInfo() {
        if(isFinished){
            if(isWon()){
                if(currentPlayer==player1)
                    return Response.success(Code.P1_WIN, player1);
                else
                    return Response.success(Code.P2_WIN, player2);
            }
            else
                return Response.success(Code.DRAW_GAME, null);
        }else{
            return Response.success(Code.CONT_GAME, null);
        }
    }

    /**
     * Gets game board and current player
     *
     * @return A response object containing status code and game status
     */
    public Response<GameState> getGameState(){
        return Response.success(Code.GET_GAME_STATUS_SUC, new GameState(board, currentPlayer));
    }

    /**
     * Resets current game to its initial state
     *
     * @return A response object indicating success or failure
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
            return Response.success(Code.START_GAME, true);
        }catch (Exception e){
            throw e;
        }
    }
}
