package commons.exceptions;

public class CardNotFoundException extends Exception {
    /**
     * Constructs a {@link CardNotFoundException}.
     * This exception is thrown when a card in a board cannot be found
     *
     * @param errorMessage the error message that the exception should contain
     */
    public CardNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
