package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ColumnService;

import java.util.SortedSet;

@RestController
@RequestMapping("/columns")
public class ColumnController {
    private final ColumnService columnService;

    /**
     * Constructor for the Column Controller
     * @param columnService Dependency Injection for the column service
     */
    public ColumnController(final ColumnService columnService) {
        this.columnService = columnService;
    }

    /**
     * Create a Column
     * @param columnPostBody Column to be created
     * @param joinKey Key used to identify board
     * @param password Password to board
     * @return The Column saved in the ColumnRepository
     */
    @PostMapping("/{joinKey}/add/column") // can be changed later
    public ResponseEntity<Column> addColumn(@RequestBody final Column columnPostBody, @PathVariable final String joinKey, @RequestBody final String password) {

        final Board board = columnService.getBoardService().getBoardWithKeyAndPassword(joinKey, password);

        final String heading = columnPostBody.getHeading();
        final int index = columnPostBody.getIndex();
        final SortedSet<Card> cards = columnPostBody.getCards();

        final Column column = new Column(heading, index, cards);

        final Column savedColumn = columnService.saveColumn(column);
        board.addList(savedColumn);

        return ResponseEntity.ok(savedColumn);
    }
}
