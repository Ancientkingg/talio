package server.api.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor for the {@link ResourceNotFoundException}
     * @param resource the resource that was not found
     * @param id the id of the resource that was not found
     */
    public ResourceNotFoundException(final Class resource, final String id) {
        super("Could not find resource " +
                resource.getSimpleName() +
                " with id " +
                id
        );
    }
}
