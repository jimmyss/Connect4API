package exceptions;

/**
 * Represents an exception that occurs in the game logic.
 * This exception can be thrown to indicate specific errors
 * that arise during the game's execution.
 */
public class GameException extends Exception{

    /**
     * Constructs a new GameException with no detail message.
     */
    public GameException() {
        super();
    }

    /**
     * Constructs a new GameException with the specified detail message.
     *
     * @param message the detail message, which provides specific information
     *                about the exception.
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructs a new GameException with the specified detail message
     * and cause.
     *
     * @param message the detail message, which provides specific information
     *                about the exception.
     * @param cause the cause of the exception, which can be retrieved later
     *              using {@link Throwable#getCause()}.
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
