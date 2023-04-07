package client.exceptions;

public class BoardChangeException extends RuntimeException {
    /**
     * Exception for if a method adding something to the board model returns false
     * @param errorMessage String errormessage
     */
    public BoardChangeException (final String errorMessage) {
        super(errorMessage);
    }
}
