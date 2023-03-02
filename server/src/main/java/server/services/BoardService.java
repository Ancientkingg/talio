package server.services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.BoardNotFoundException;

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
     */
    public Board getBoardWithKey(final String joinKey) {
        if (br.existsById(joinKey))
            return br.getById(joinKey);
        throw new BoardNotFoundException(joinKey);
    }

    /**
     * Saves a board to the database
     * @param board Board to save
     */
    public void saveBoard(final Board board) {
        br.save(board);
    }

}
