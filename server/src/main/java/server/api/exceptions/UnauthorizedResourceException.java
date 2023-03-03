package server.api.exceptions;

public class UnauthorizedResourceException extends RuntimeException {

    /**
     * Constructor for the {@link UnauthorizedResourceException}
     * @param resource the resource that the client is unauthorized to access
     * @param id the id of the unauthorized resource
     */
    public UnauthorizedResourceException(final Class resource, final String id) {
        super("Client is unauthorized to access resource " +
            resource.getSimpleName() +
            " with id " +
            id
        );
    }
}
