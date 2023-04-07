package client.exceptions;

public class ServerException extends RuntimeException {
    /**
     * Exception for if a method adding something to the server returns a bad status code
     * @param errorMessage String errormessage
     */
    public ServerException(final String errorMessage) {
        super(errorMessage);
    }
}
