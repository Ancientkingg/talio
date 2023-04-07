package server.services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.List;

@Service
public class AdminService {

    private final BoardRepository boardRepository;


    /**
     * Constructor for the Board Service
     * @param boardRepository Dependency Injection for the board repository
     */
    public AdminService(final BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * method to get all the boards in the database
     * @return all boards in database
     */
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
