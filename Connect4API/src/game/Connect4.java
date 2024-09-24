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
 *
 * <p>This class handles different modes of play including human vs human, human vs computer, and
 * computer vs computer. It also manages the game board, handles player actions, and determines the
 * game outcome.</p>
 *
 * @author Shimao Du, Bohan Liu
 * @version 1.0
 * @since 2024-09-23
 */
public class Connect4 {
    // Constants for the board size
    private static final int ROWS=6;
    private static final int COLS=7;
    // Constants for the game piece colors
    /**
     * '@' is represented as RED
     */
    public static final char RED='@';
    /**
     * '#' is represented as BLUE
     */
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
     * @param mode The game mode
     *             <ul>
     *                 <li>1: human vs human</li>
     *                 <li>2: human vs computer</li>
     *                 <li>3: computer vs computer</li>
     *             </ul>
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
     *
     * @return {@code true} if current player has won, {@code false} otherwise.
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
     * @return {@code true} if the game is a draw, {@code false} otherwise.
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
     * @return {@code true} if the column is full, {@code false} otherwise.
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
     * Starts the game by returning the current game state and status code information.
     *
     * This function is called to initiate the game after it has been properly set up. 
     * It checks whether the game is already initialized and ready to start. If the game has not been 
     * initialized correctly (i.e., the mode is not set), an error is returned. Otherwise, it returns 
     * the current state of the game including the board and the current player.
     *
     * @return A {@code Response<GameState>} object that contains the current game state and status code.
     *         Possible responses include:
     *         - {@code Response.success(Code.START_GAME, GameState)}: The game is successfully started, 
     *           and the current game state is returned.
     *         - {@code Response.error(Code.START_GAME_ERR)}: The game has not been properly initialized (the mode is not set).
     *
     * ### Game State Modifications:
     * - **No modifications**: This function does not modify any internal state of the game. 
     *   It only returns the current state if the game is properly initialized.
     *
     * ### Detailed Behavior:
     * 1. **Game Initialization Check**:
     *    - The function checks whether the `mode` is valid. If `mode == -1`, it indicates that the game is not initialized properly.
     *    - If the game is not initialized (i.e., `mode == -1`), a `Code.START_GAME_ERR` error response is returned.
     *
     * 2. **Returning Game State**:
     *    - If the game has been initialized (i.e., `mode != -1`), the function constructs a `GameState` object containing the current board and the current player.
     *    - The function then returns a `Response.success()` with the `Code.START_GAME` status and the current `GameState`.
     *
     * ### Related Class Members:
     * - **mode**: This integer field indicates the current game mode. If it is `-1`, the game is considered uninitialized. 
     * - **board**: The current state of the game board, which is part of the returned `GameState` if the game is properly initialized.
     * - **currentPlayer**: The player who is currently taking a turn, which is also part of the returned `GameState`.
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
     * @return A {@code Response} object containing status code and message
     */
    public Response<String> setPlayerName(Player player, String name){
        player.setPlayerName(name);
        return Response.success(Code.SET_NAME,"new name:"+name);
    }

    /**
     * Drops a checker piece in the specified column and returns the updated game state.
     *
     * The function handles both human and computer players, validating the input column
     * and managing the game flow. If the game is finished or if the column is invalid or full, 
     * an error response is returned. If the move is valid, the board is updated, and the function 
     * checks whether the game has been won, drawn, or should continue.
     *
     * @param column The column where the human player intends to drop their piece.
     *               For computer players, a random valid column will be selected.
     * @return A {@code Response} object that contains the game state (including the board and current player information) 
     *         and any relevant status codes.
     *         Possible responses include:
     *         - {@code Response.success(Code.DROP_PIECE, GameState)}: The piece was successfully dropped.
     *         - {@code Response.error(Code.INVALID_MOVE_ERR)}: The specified column is out of bounds (not between 0 and 6).
     *         - {@code Response.error(Code.FULL_COL_ERR)}: The specified column is already full.
     *         - {@code Response.error(Code.GAME_FIN_ERR)}: The game is already finished and no more moves are allowed.
     *
     * @throws GameException if any unexpected issue occurs during the move.
     *
     * ### Game State Modifications:
     * - **Game board (`board`)**: The function modifies the 6x7 game board by placing a checker piece in the specified column (or a random column for the computer).
     * - **Current player (`currentPlayer`)**: If the move is successful, the function will update the current player to the next one unless the game is finished.
     * - **Last dropped position (`lastDrop`)**: This array is updated to store the row and column where the latest piece was dropped.
     * - **Game finished flag (`isFinished`)**: This boolean flag is updated to `true` if the move results in a win or draw.
     *
     * ### Detailed Behavior:
     * 1. **Column Validation**:
     *    - If the column is outside the valid range (0 to 6), a `Code.INVALID_MOVE_ERR` is returned.
     *    - If the column is full (all rows in that column are occupied), a `Code.FULL_COL_ERR` is returned.
     *  
     * 2. **Game Finished Check**:
     *    - If the game has already finished (`isFinished == true`), a `Code.GAME_FIN_ERR` is returned, and no moves are allowed.
     *  
     * 3. **Piece Dropping**:
     *    - For a human player: The function attempts to drop the checker in the specified `column` if it passes validation.
     *    - For a computer player: The function randomly selects a valid column between 0 and 6 using `Random.nextInt(7)`.
     *    - After a piece is successfully dropped, the function updates the game board by calling `updateBoard()`, which modifies the `board` array and the `lastDrop[]` array.
     *  
     * 4. **Game Status Update**:
     *    - The function then calls `judgeGame()` to check if the move results in a win, draw, or if the game should continue.
     *    - If the game is not finished after the move, the function updates the `currentPlayer` to the next player by calling `updateCurPlayer()`.
     *  
     * 5. **Returning Game State**:
     *    - If the move is valid and the game is not finished, a successful `Response` with `Code.DROP_PIECE` and the updated `GameState` (including the current board and player information) is returned.
     *  
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
     * Returns information about the current game state, including whether there is a winner or if the game is still ongoing.
     *
     * This function checks the game’s current status to determine if there is a winner, a draw, or if the game is still in progress.
     * If the game is finished, it returns the winner (if any) or indicates a draw. If the game is ongoing, it returns the appropriate status code without a winner.
     *
     * @return A {@code Response<Player>} object containing:
     *         - {@code Response.success(Code.P1_WIN, player1)}: If player 1 (RED) has won the game.
     *         - {@code Response.success(Code.P2_WIN, player2)}: If player 2 (BLUE) has won the game.
     *         - {@code Response.success(Code.DRAW_GAME, null)}: If the game has ended in a draw.
     *         - {@code Response.success(Code.CONT_GAME, null)}: If the game is still ongoing with no winner yet.
     *
     * ### Game State Modifications:
     * - **No modifications**: This function only checks the current state of the game and returns the appropriate response. 
     *   It does not alter the game’s internal state.
     *
     * ### Detailed Behavior:
     * 1. **Game Finished Check**:
     *    - The function first checks the `isFinished` flag to determine if the game has concluded.
     *    - If `isFinished == true`, it proceeds to check whether there is a winner.
     *
     * 2. **Winner Determination**:
     *    - If the game has been won (`isWon()` returns `true`), the function determines the winner based on the `currentPlayer`.
     *      - If `currentPlayer` is `player1` (RED), it returns a success response with `Code.P1_WIN` and `player1` as the winner.
     *      - If `currentPlayer` is `player2` (BLUE), it returns a success response with `Code.P2_WIN` and `player2` as the winner.
     *
     * 3. **Draw Check**:
     *    - If the game is finished but there is no winner (`isWon()` returns `false`), the function returns `Response.success(Code.DRAW_GAME, null)` to indicate that the game ended in a draw.
     *
     * 4. **Game Ongoing**:
     *    - If the game is still in progress (`isFinished == false`), the function returns `Response.success(Code.CONT_GAME, null)` to indicate that the game is ongoing and no winner has been determined yet.
     *
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
     * @return A {@code Response} object containing status code and game status
     */
    public Response<GameState> getGameState(){
        return Response.success(Code.GET_GAME_STATUS_SUC, new GameState(board, currentPlayer));
    }

    /**
     * Resets the current game to its initial state.
     *
     * This method clears the board, resets players, and reinitializes the game to its starting state.
     * For details on the initial state, refer to the {@code startGame()} documentation.
     *
     * @return A {@code Response<Boolean>} object indicating success (with {@code true}) or failure (with {@code false}).
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
