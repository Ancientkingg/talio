package server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.exceptions.BoardNotFoundException;

@RestControllerAdvice
public class BoardNotFoundAdvice {

	@ExceptionHandler(BoardNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String boardNotFoundHandler(BoardNotFoundException err) {
		return err.getMessage();
	}
}
