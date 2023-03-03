package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.SortedSet;

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
     * Create a Column
     * @param columnPostBody Column to be created
     * @param joinKey Key used to identify board
     * @param password Password to board
     * @return The Column saved in the ColumnRepository
     */
    @PostMapping("/{joinKey}/add/column") // can be changed later
    public ResponseEntity<Column> addColumn(@RequestBody final Column columnPostBody, @PathVariable final String joinKey, @RequestBody final String password) {

        // One way to do get a board - downside - repeating code already in BoardController
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);

        if (board == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String heading = columnPostBody.getHeading();
        final int index = columnPostBody.getIndex();
        final SortedSet<Card> cards = columnPostBody.getCards();

        final Column column = new Column(heading, index, cards);

        board.addList(column);
        boardService.saveBoard(board);

        return ResponseEntity.ok(column);
    }
}
