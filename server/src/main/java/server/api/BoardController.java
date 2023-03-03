package server.api;



import commons.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import javax.validation.Valid;
import java.util.TreeSet;

@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    /**
     * Constructor for the Board Controller
     * @param boardService Dependency Injection for the board service
     */
    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Returns a Board object with the given join key
     * @param joinKey Join key of the board
     * @param password Optional password of the board
     * @return The board with the right joinKey if the board has the correct password provided, otherwise a
     */
    @GetMapping("/get/{joinKey}")
    public ResponseEntity<Board> getBoard(@PathVariable final String joinKey, @RequestBody final String password) {

        final Board board = password == null ?
                boardService.getBoardWithKey(joinKey) :
                boardService.getBoardWithKeyAndPassword(joinKey, password);

        return ResponseEntity.ok(board);
    }

    /**
     * Creates a {@link Board}
     * @param boardPostBody {@link Board} to create
     * @return The {@link Board} that was saved in the {@code BoardRepository}, so the client can ensure data integrity.
     */
    @PostMapping("/create")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody final Board boardPostBody) {

        final String boardPartialJoinKey = boardPostBody.getJoinKey();
        final String boardJoinKey = boardService.generateJoinKey(boardPartialJoinKey);

        final Board board = new Board(boardJoinKey, boardPostBody.getTitle(), boardPostBody.getPassword(), new TreeSet<>());

        final Board savedBoard = boardService.saveBoard(board);
        return ResponseEntity.ok(savedBoard);
    }

}
