package server.services;

import commons.Board;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.ResourceNotFoundException;

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
            return br.getById(joinKey);
        throw new ResourceNotFoundException(Board.class, joinKey);
    }

    public Board getBoardWithKeyAndPassword(final String joinKey, final String password) {

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
     * @param partialJoinKey The partial join key
     * @return The full join key
     */
    public String generateJoinKey(final String partialJoinKey) {
        String joinKey = partialJoinKey + "-" + RandomStringUtils.random(6, "0123456789abcdef");
        while (br.existsById(joinKey)) {
            joinKey = partialJoinKey + "-" + RandomStringUtils.random(6, "0123456789abcdef");
        }
        return joinKey;
    }

}
