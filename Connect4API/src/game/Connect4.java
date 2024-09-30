package game;

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
    private GameContext gameContext;

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
        this.gameContext=new GameContext(board, currentPlayer, GameResult.CONTINUE);
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
        this.gameContext=new GameContext(board, currentPlayer, GameResult.CONTINUE);
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
        this.gameContext=new GameContext(board, currentPlayer, GameResult.CONTINUE);
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
                    gameContext.setBoard(board);
                    break;
                }
            }
        }
    }

    /**
     * Switches the current player to the other player.
     */
    private void switchCurPlayer(){
        if(currentPlayer==player1) currentPlayer=player2;
        else currentPlayer=player1;
        gameContext.setCurrentPlayer(currentPlayer);
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
                gameContext.setResult(GameResult.WIN);
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
                gameContext.setResult(GameResult.WIN);
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
                    gameContext.setResult(GameResult.WIN);
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
                    gameContext.setResult(GameResult.WIN);
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
                if (board[row][col] == '\u0000') {
                    return false;
                }
            }
        }
        gameContext.setResult(GameResult.DRAW);
        return true;
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
        if(this.board[0][column]=='\u0000'){
            return false;
        }else return true;
    }


    /************************/
    /**** Public Methods ****/
    /************************/

    /**
     * Starts the game by returning the current game context or throwing an exception if uninitialized.
     *
     * This method is called to initiate the game after setup. It verifies whether the game has been
     * properly initialized by checking the `mode`. If the game mode is not set (i.e., `mode == -1`),
     * it throws a {@code GameException} to indicate that the game setup is incomplete. Otherwise,
     * it returns the current {@code GameContext}, which contains details about the board and current player.
     *
     * @return A {@code GameContext} object representing the current game state, including the board and current player.
     *
     * @throws GameException if the game has not been properly initialized (i.e., the mode is not set).
     *
     * ### Game State Modifications:
     * - **No modifications**: This method does not modify the game state. It only returns the existing
     *   game state if the game has been initialized.
     *
     * ### Detailed Behavior:
     * 1. **Game Initialization Check**:
     *    - The method checks whether the `mode` is valid. If `mode == -1`, it means the game was not initialized properly.
     *    - If the game is uninitialized (i.e., `mode == -1`), a {@code GameException} is thrown with an appropriate error message.
     *
     * 2. **Returning Game Context**:
     *    - If the game has been initialized (i.e., `mode != -1`), the method returns the current {@code GameContext},
     *      which encapsulates the current state of the game including the board and the player who is about to take a turn.
     *
     * ### Related Class Members:
     * - **mode**: An integer field indicating the game mode. If it is `-1`, the game is considered uninitialized.
     * - **board**: The current state of the game board, which is included in the returned {@code GameContext} if the game is initialized.
     * - **currentPlayer**: The player currently taking a turn, also included in the returned {@code GameContext}.
     */
    public GameContext startGame() throws GameException {
        if(mode==-1){
            // This game has not initialize yet
            throw new GameException("The game is not initialized");
        }else{
            return gameContext;
        }
    }

    /**
     * Sets a player's name after validating input.
     *
     * This method updates the name of the specified player. It first checks if the provided player and
     * name are non-null. If either is null, a {@code GameException} is thrown with a relevant message.
     * If validation passes, the player's name is updated.
     *
     * @param player The player whose name should be updated. Must not be null.
     * @param name The new name to assign to the player. Must not be null.
     * @return A {@code String} message confirming the player's new name in the format "new name:{name}".
     *
     * @throws GameException if the {@code player} or {@code name} is null.
     *
     * ### Validations:
     * - **Player null check**: If {@code player} is null, a {@code GameException} is thrown with the message "Player is null".
     * - **Name null check**: If {@code name} is null, a {@code GameException} is thrown with the message "name is null".
     *
     * ### Behavior:
     * - Upon successful validation, the method sets the player's name to the provided {@code name}.
     * - The method returns a string confirming the player's new name.
     */
    public String setPlayerName(Player player, String name) throws GameException {
        if(player==null)
            throw new GameException("Player is null");
        if(name==null)
            throw new GameException("name is null");
        player.setPlayerName(name);
        return "new name:"+name;
    }

    /**
     * Drops a checker piece into the specified column and returns the updated game state.
     *
     * This method handles dropping a checker for both human and computer players. It validates the input column,
     * manages game flow, and updates the game board. If the game has ended or the column is invalid/full,
     * an error is returned. Otherwise, it drops the checker, updates the game state, and checks for win, draw,
     * or continuation.
     *
     * @param column The column where a human player intends to drop their checker. For computer players,
     *               a random valid column is selected.
     * @return A {@code GameContext} object containing the updated game state and relevant status.
     *
     * @throws GameException if an error occurs, including:
     *         - Invalid column (not between 0 and 6).
     *         - The specified column is full.
     *         - The game has already finished, and no more moves are allowed.
     *
     * ### Possible Exceptions:
     * - {@code GameException("Column is invalid")}: Thrown when the column number is out of range (less than 0 or greater than 6).
     * - {@code GameException("Column is full")}: Thrown when the column is already full and no more pieces can be placed.
     * - {@code GameException("game has finished")}: Thrown if the game is already over and no further moves are allowed.
     *
     * ### Game State Modifications:
     * - **Game board**: A checker is dropped into the specified column for human players, or a random column for computer players.
     * - **Current player**: After a valid move, the current player is switched unless the game ends with the move.
     * - **Last dropped position**: The row and column of the last dropped checker are stored for reference.
     * - **Game finish status**: If a win or draw occurs, the game is marked as finished.
     *
     * ### Detailed Behavior:
     * 1. **Column Validation**:
     *    - The method checks if the provided column is within the valid range (0 to 6).
     *    - If the column is full, an error is raised, and no piece is dropped.
     *
     * 2. **Game Finished Check**:
     *    - If the game has already concluded, no more moves are allowed, and an exception is thrown.
     *
     * 3. **Piece Dropping**:
     *    - For human players: The checker is dropped into the specified column after passing validation.
     *    - For computer players: A valid column is randomly selected, and the checker is dropped into it.
     *
     * 4. **Game Status Update**:
     *    - The method calls `judgeGame()` to check if the current move results in a win or draw.
     *    - If the game is still ongoing, the current player is switched to the next one.
     *
     * 5. **Returning Game State**:
     *    - A valid move results in the current game state being returned, including the updated board and player information.
     */
    public GameContext dropChecker(int column) throws GameException {
        // judge if the column is valid
        if(column<0 || column>6) {
            throw new GameException("Column is invalid.");
        }else if(isFullCol(column)){
            throw new GameException("Column: "+column+" is full.");
        }
        // judge if the game is over
        if(isFinished) throw new GameException("game has finished");

        // drop piece according to current player
        if(currentPlayer.isComputer()){
            Random random=new Random();
            int randomNum=random.nextInt(7);
            updateBoard(randomNum);
        }else{
            updateBoard(column);
        }
        judgeGame();
        if(!isFinished) switchCurPlayer();
        return gameContext;
    }

    /**
     * Returns information about the current game state, indicating whether there is a winner or if the game is still ongoing.
     *
     * This method checks the current game status to determine if there is a winner, a draw, or if the game is still in progress.
     * It returns the corresponding response based on the game outcome. If the game has concluded, it provides information
     * about the winner or if the game resulted in a draw. If the game is still ongoing, it indicates that the game has not yet finished.
     *
     * @return A {@code Player} object representing the winner of the game or {@code null} if there is no winner.
     *         - Returns {@code player1} if player 1 (RED) has won the game.
     *         - Returns {@code player2} if player 2 (BLUE) has won the game.
     *         - Returns {@code null} if the game is either a draw or still ongoing.
     *
     * ### Game State Modifications:
     * - **No modifications**: This method does not alter the internal state of the game. It only checks the current game status
     *   and returns the appropriate result based on the outcome.
     *
     * ### Detailed Behavior:
     * 1. **Game Finished Check**:
     *    - The method first checks the `isFinished` flag to determine if the game has concluded.
     *    - If `isFinished == true`, it proceeds to check whether the game has been won or drawn.
     *
     * 2. **Winner Determination**:
     *    - If the game has been won (based on the `isWon()` method returning `true`), the method identifies the winner based on the `currentPlayer`.
     *      - If `currentPlayer` is `player1` (RED), the method returns `player1` as the winner.
     *      - If `currentPlayer` is `player2` (BLUE), the method returns `player2` as the winner.
     *
     * 3. **Draw Check**:
     *    - If the game has finished but there is no winner (`isWon()` returns `false`), the method returns `null`, indicating a draw.
     *
     * 4. **Game Ongoing**:
     *    - If the game is still in progress (`isFinished == false`), the method returns `null` to indicate that the game is ongoing and no winner has been determined yet.
     *
     * @throws GameException If any unexpected issue occurs during the process of checking the game state.
     */
    public Player getWinningInfo() {
        if(isFinished){
            if(isWon()){
                if(currentPlayer==player1)
                    return player1;
                else
                    return player2;
            }
            else
                return null;
        }else{
            return null;
        }
    }

    /**
     * Gets game board and current player
     *
     * @return current gameState
     */
    public GameContext getGameState(){
        return gameContext;
    }
}
