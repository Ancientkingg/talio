package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import javax.validation.Valid;
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
     * @param columnDTO Column to be created
     * @param joinKey Key used to identify board
     * @param password Password to board
     * @return The Column saved in the ColumnRepository
     */
    @PostMapping("/{joinKey}/add/column") // can be changed later
    public ResponseEntity<Column> addColumn(@Valid @RequestBody final Column columnDTO,
                                            @PathVariable final String joinKey,
                                            @RequestBody final String password)
    {

        // One way to do get a board - downside - repeating code already in BoardController
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);


        final String heading = columnDTO.getHeading();
        final int index = columnDTO.getIndex();
        final SortedSet<Card> cards = columnDTO.getCards();

        final Column column = new Column(heading, index, cards);

        board.addColumn(column);
        boardService.saveBoard(board);

        return ResponseEntity.ok(column);
    }
}
