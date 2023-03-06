package client.exceptions;

public class BoardChangeException extends Exception {

    /**
     * Exception for if a method adding something to the DB returns false
     * @param errorMessage String errormessage
     */
    public BoardChangeException (final String errorMessage) {
        super(errorMessage);
    }
}
