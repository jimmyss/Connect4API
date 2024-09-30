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
     * Starts the game and returns the current game context, which includes the game board, current player, and game result.
     *
     * This method is used to begin the game after it has been properly initialized. It checks whether the game has been set up correctly.
     * If the game has not been initialized (i.e., `mode == -1`), it throws a {@code GameException}. If the game is initialized,
     * it returns the current {@code GameContext}, which provides the state of the game, including the board, the current player, 
     * and the game result.
     *
     * @return A {@code GameContext} object that contains:
     *         - The current game board (`char[][] board`), representing the 6x7 Connect4 grid.
     *         - The current player (`Player currentPlayer`), indicating whose turn it is.
     *         - The current game result (`GameResult result`), providing details on the game's progress (win, draw, or ongoing).
     *
     * @throws GameException if the game has not been initialized (i.e., when `mode == -1`).
     *
     * ### Game State Modifications:
     * - **No modifications**: This method does not modify any game state. It simply returns the current game context or throws an exception if the game is not properly initialized.
     *
     * ### Detailed Behavior:
     * 1. **Game Initialization Check**:
     *    - The method checks the `mode` variable to determine if the game has been initialized. If `mode == -1`, it throws a {@code GameException} with the message "The game is not initialized".
     *
     * 2. **Returning Game Context**:
     *    - If the game is properly initialized (`mode != -1`), the method returns the current {@code GameContext}, which includes:
     *      - The current state of the game board (`board`), showing the positions of all pieces on the 6x7 grid.
     *      - The player whose turn it is (`currentPlayer`).
     *      - The current game result (`result`), which may indicate an ongoing game, a win, or a draw.
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
     * Sets the name for the specified player and returns a confirmation message.
     *
     * This method allows setting or updating the name of a specific {@code Player}. It validates that both the player 
     * and the name are not null before setting the name. If the validation fails, it throws a {@code GameException}.
     *
     * @param player The {@code Player} object whose name is to be set. Must not be null.
     * @param name The new name to assign to the player. Must not be null.
     * @return A {@code String} message indicating the new name that has been set in the format "new name: [name]".
     *
     * @throws GameException if the provided player is null or the provided name is null.
     *
     * ### Game State Modifications:
     * - **Player Name**: The function updates the name of the specified {@code Player} by calling {@code player.setPlayerName(name)}.
     *
     * ### Detailed Behavior:
     * 1. **Null Checks**:
     *    - The function first checks if the provided `player` object is null. If it is, a {@code GameException} is thrown with the message "Player is null".
     *    - It then checks if the provided `name` is null. If it is, a {@code GameException} is thrown with the message "name is null".
     *
     * 2. **Updating Player Name**:
     *    - If both the player and name are valid, the function calls `player.setPlayerName(name)` to set the new name for the player.
     *
     * 3. **Returning Confirmation**:
     *    - After successfully setting the player's name, the function returns a confirmation message in the format: `"new name: [name]"`, where `[name]` is the newly set name.
     *
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
     * Drops a checker into the specified column and returns the updated game context.
     *
     * This method handles the dropping of a checker piece into the game board for the current player. 
     * It validates the column input, ensures that the column is not full, and checks if the game is already over. 
     * If the move is valid, the method updates the board and game state accordingly, switches players if the game is ongoing, 
     * and returns the updated {@code GameContext}.
     *
     * @param column The column (0-6) where the checker should be dropped.
     * @return A {@code GameContext} object containing the updated game state, including the board, current player, and result.
     *
     * @throws GameException if the column is invalid (not between 0 and 6), the column is already full, or the game has already finished.
     *
     * ### Game State Modifications:
     * - **Game Board**: The board is updated by placing the current player's checker in the specified column. If the current player is a computer, the column is chosen randomly.
     * - **Current Player**: After a valid move, the current player may be switched if the game has not finished.
     * - **Game Finished Flag**: The `isFinished` flag is updated if the game has been won or ends in a draw.
     *
     * ### Detailed Behavior:
     * 1. **Column Validation**:
     *    - The method checks if the provided `column` is valid (between 0 and 6). If the column is out of bounds, a {@code GameException} with the message "Column is invalid" is thrown.
     *    - It then checks if the column is full using `isFullCol(column)`. If the column is full, a {@code GameException} with the message "Column: [column] is full" is thrown.
     *
     * 2. **Game Finished Check**:
     *    - If the game has already finished (`isFinished == true`), a {@code GameException} is thrown with the message "game has finished", indicating that no more moves can be made.
     *
     * 3. **Dropping the Checker**:
     *    - If the current player is a human, the checker is dropped in the specified column by calling `updateBoard(column)`.
     *    - If the current player is a computer, a random valid column is selected using `Random.nextInt(7)`, and the checker is dropped into that column.
     *
     * 4. **Game Status Update**:
     *    - After dropping the checker, the method calls `judgeGame()` to evaluate whether the move has resulted in a win or draw.
     *    - If the game is not finished (`isFinished == false`), the current player is switched using `switchCurPlayer()`.
     *
     * 5. **Returning Game Context**:
     *    - The method returns the current {@code GameContext}, which includes:
     *      - The updated game board (`board`).
     *      - The current player (`currentPlayer`), who will make the next move if the game is not over.
     *      - The game result (`result`), indicating whether the game is still ongoing, has been won, or has ended in a draw.
     *
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
     * Returns the player who won the game, or {@code null} if the game ended in a draw or is still ongoing.
     *
     * This method checks the current game result from the {@code GameContext}. If the game has been won, 
     * it returns the winning {@code Player}. If the game has ended in a draw or is still in progress, 
     * it returns {@code null}.
     *
     * @return The {@code Player} who won the game, or {@code null} if the game ended in a draw or is still in progress.
     *
     * ### Game State Modifications:
     * - **No modifications**: This method does not modify the state of the game; it only checks and returns the current game result.
     *
     * ### Detailed Behavior:
     * 1. **Game Result Check**:
     *    - The method retrieves the current game result from the {@code GameContext} by calling `gameContext.getResult()`.
     *    - If the result is {@code GameResult.CONTINUE}, indicating that the game is still ongoing, the method returns {@code null}.
     *
     * 2. **Winner Determination**:
     *    - If the game result is {@code GameResult.WIN}, the method checks which player won:
     *      - If the current player at the time of winning is `player1`, it returns `player1`.
     *      - If the current player at the time of winning is `player2`, it returns `player2`.
     *
     * 3. **Draw Check**:
     *    - If the game result is not {@code WIN} and the game has finished, the method returns {@code null}, indicating the game ended in a draw.
     *
     */
    public Player getWinner() {
        if(gameContext.getResult()!=GameResult.CONTINUE){
            if(gameContext.getResult()==GameResult.WIN){
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
     * Gets game context that contains game board, current player and game result
     *
     * @return current game context
     */
    public GameContext getGameContext(){
        return gameContext;
    }

    /**
     * Prints the current game board in a text-based format.
     *
     * This method displays the 6x7 game board, where empty cells are represented by dots ('.') and 
     * filled cells are shown using their respective characters. It also prints the column numbers (0-6) at the bottom.
     *
     * @param board A 2D character array representing the game board.
     *
     * ### Output Format:
     * - Empty cells are displayed as '.'.
     * - Filled cells display their corresponding player markers.
     * - Column numbers (0-6) are printed at the bottom for reference.
     */
    public static void printTextBasedBoard(char[][] board) {
        System.out.println("Current board:");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\u0000' ? '.' : cell) + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
        System.out.println();        
    }
}
