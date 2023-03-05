package commons.exceptions;

public class ColumnNotFoundException extends Exception {
    /**
     * Constructs a {@link ColumnNotFoundException}.
     * This exception is thrown when a column in a board cannot be found
     *
     * @param errorMessage the error message that the exception should contain
     */
    public ColumnNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
