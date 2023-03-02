package server.services;

import commons.Column;
import org.springframework.stereotype.Service;
import server.database.ColumnRepository;

@Service
public class ColumnService {
    private final ColumnRepository cr;
    private final BoardService boardService;

    /**
     * Getter for board service
     * @return BoardService for board
     */
    public BoardService getBoardService() {
        return boardService;
    }

    /**
     * Constructor for the Column Service
     * @param cr Dependency Injection for the column repository
     * @param boardService BoardService for board in which ColumnService will make/update columns
     */
    public ColumnService(final ColumnRepository cr, final BoardService boardService) {
        this.cr = cr;
        this.boardService = boardService;
    }

    /**
     * Returns a Column object with the given id
     * @param id id of column
     *
     * @return The column with the right id if exists, otherwise null
     */
    public Column getColumn(final long id) {
        return cr.existsById(id) ? cr.getById(id) : null;
    }

    /**
     * Saves a board to the database
     * @param column Column to save
     * @return The saved column
     */
    public Column saveColumn(final Column column) {
        return cr.save(column);
    }
}
