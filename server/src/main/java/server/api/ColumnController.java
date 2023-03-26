package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.TreeSet;

@RestController
@RequestMapping("/columns")
public class ColumnController {

    private final BoardService boardService;


    /**
     * Constructor for the Column Controller
     * @param boardService Dependency injection for the board service
     */
    public ColumnController(final BoardService boardService) {
        this.boardService = boardService;
    }


    /**
     * Add a Column
     * @param joinKey Key used to identify board
     * @param columnHeading Heading of column to be added
     * @param password Password to board
     * @param index Index of column to be added
     * @return The Column added to the ColumnRepository
     */
    @PostMapping("/{joinKey}/create/{columnHeading}")
    public ResponseEntity<Column> addColumn(@PathVariable final String joinKey, @PathVariable final String columnHeading,
                                            @RequestBody(required = false) final String password, @RequestParam final int index)
    {

        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);


        final Column column = new Column(columnHeading, index, new TreeSet<Card>());

        board.addColumn(column);
        boardService.saveBoard(board);

        return ResponseEntity.ok(column);
    }

    /**
     * Remove a Column
     * @param joinKey Key used to identify board
     * @param columnHeading Heading of column to be removed
     * @param password Password to board
     * @return The Column removed from the ColumnRepository
     */
    @PostMapping("/{joinKey}/remove/{columnHeading}")
    public ResponseEntity<Column> removeColumn(@PathVariable final String joinKey, @PathVariable final String columnHeading,
                                               @RequestBody(required = false) final String password)
    {

        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Column toBeRemoved = board.getColumnByName(columnHeading);

        board.removeColumn(toBeRemoved);
        boardService.saveBoard(board);

        return ResponseEntity.ok(toBeRemoved);
    }
}
