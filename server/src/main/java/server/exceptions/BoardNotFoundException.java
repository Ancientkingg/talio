package server.exceptions;

public class BoardNotFoundException extends RuntimeException {

	/**
	 * Constructs a {@link BoardNotFoundException}.
	 * @param joinKey the join key of the board that was not found
	 */
	public BoardNotFoundException(String joinKey) {
		super("Could not find board with join key: " + joinKey);
	}
}
