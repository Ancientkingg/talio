package server.services;

import commons.Board;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.api.exceptions.ResourceNotFoundException;
import server.api.exceptions.UnauthorizedResourceException;

import java.util.Objects;
import java.util.Optional;

@Service
public class BoardService {
    private final BoardRepository br;


    /**
     * Constructor for the Board Service
     * @param br Dependency Injection for the board repository
     */
    public BoardService(final BoardRepository br) {
        this.br = br;
    }

    /**
     * Returns a Board object with the given join key
     * @param joinKey Join key of the board
     *
     * @return The board with the right joinKey if exists, otherwise null
     * @throws ResourceNotFoundException if the board does not exist
     */
    public Board getBoardWithKey(final String joinKey) {
        if (br.existsById(joinKey))
            return br.findById(joinKey).get();
        throw new ResourceNotFoundException(Board.class, joinKey);
    }

    /**
     * Returns a Board object with the given join key
     * @param joinKey Join key of the board
     * @return The board with the right joinKey if exists, otherwise null
     */
    public Board getBoardWithKeyUnsafe(final String joinKey) {
        final Optional<Board> board = br.findById(joinKey);
        return board.isEmpty() ? null : board.get();
    }

    /**
     * Returns a Board object with the given join key and password
     * @param joinKey Join key of the board
     * @param password Password of the board
     * @return The board with the right joinKey and password if exists, otherwise null
     */
    public Board getBoardWithKeyAndPassword(final String joinKey, final String password) {
        if (br.existsById(joinKey)) {
            final Board board = br.findById(joinKey).get();
            if (Objects.equals(board.getPassword(), password)) // null safe - Board.getPassword could return null
                return board;
            throw new UnauthorizedResourceException(Board.class, joinKey);
        }
        throw new ResourceNotFoundException(Board.class, joinKey);
    }

    /**
     * Saves a board to the database
     * @param board Board to save
     * @return The saved board
     */
    public Board saveBoard(final Board board) {
        return br.save(board);
    }

    /**
     * Generates a join key for a board
     * @return The join key
     */
    public String generateJoinKey() {
        String joinKey = RandomStringUtils.random(6, "0123456789abcdef");
        while (br.existsById(joinKey)) {
            joinKey = RandomStringUtils.random(6, "0123456789abcdef");
        }
        return joinKey;
    }

    /**
     * Deletes a board from the database
     * @param board Board to delete
     */
    public void deleteBoard(final Board board) {
        this.br.delete(board);
    }
}
