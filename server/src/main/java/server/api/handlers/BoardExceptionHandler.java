package server.api.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.UnauthorizedResourceException;

@RestControllerAdvice
public class BoardExceptionHandler {

    /**
     * Exception handler for when a resource has not been found
     * @param err the {@link ResourceNotFoundException} that triggered the handler
     * @return a response to the client specifying what resource has not been found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String resourceNotFoundHandler(final ResourceNotFoundException err) {
        return err.getMessage();
    }

    /**
     * Exception handler for when a client is unauthorized to access a resource
     * @param err the {@link UnauthorizedResourceException} that triggered the handler
     * @return a response to the client specifying what resource is forbidden
     */
    @ExceptionHandler(UnauthorizedResourceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorizedResourceHandler(final UnauthorizedResourceException err) {
        return err.getMessage();
    }
}
