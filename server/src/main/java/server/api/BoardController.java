package server.api;


import commons.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Controller
public class BoardController {
    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(CardController.class);

    private final Clock clock;

    /**
     * Constructor for the Board Controller
     *
     * @param boardService      Dependency Injection for the board service
     * @param messagingTemplate Template to send updates over socket
     * @param clock             Dependency Injection for the clock
     */
    public BoardController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate, final Clock clock) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
        this.clock = clock;
    }

    /**
     * Returns a Board object with the given join key
     * @param joinKey Join key of the board
     * @param password Optional password of the board
     * @return The board with the right joinKey if the board has the correct password provided, otherwise a
     */
    @GetMapping("/boards/get/{joinKey}")
    public ResponseEntity<Board> getBoard(@PathVariable final String joinKey, @RequestBody(required = false) final String password) {

        final Board board = password == null ?
                boardService.getBoardWithKey(joinKey) :
                boardService.getBoardWithKeyAndPassword(joinKey, password);

        return ResponseEntity.ok(board);
    }

    /**
     * Returns a list of boards with the given join keys
     * @param joinKeys List of join keys
     * @return List of boards
     */
    @PostMapping("/boards/getAll")
    public ResponseEntity<List<Board>> getAllBoards(@RequestBody final List<String> joinKeys) {
        final List<Board> boards = new ArrayList<>();

        for (final String joinKey : joinKeys) {
            boards.add(boardService.getBoardWithKeyUnsafe(joinKey));
        }

        return ResponseEntity.ok(boards);
    }

    /**
     * Creates a {@link Board}
     * @param boardDTO {@link Board} to create
     * @return The {@link Board} that was saved in the {@code BoardRepository}, so the client can ensure data integrity.
     */
    @PostMapping("/boards/create")
    public ResponseEntity<Board> createBoard(@Valid @RequestBody final Board boardDTO) {

        final String boardJoinKey = boardService.generateJoinKey();

        final Board board = new Board(boardJoinKey, boardDTO.getTitle(), boardDTO.getPassword(), new TreeSet<>(), Timestamp.from(Instant.now(clock)));

        final Board savedBoard = boardService.saveBoard(board);
        return ResponseEntity.ok(savedBoard);
    }

    /**
     * Renames board in repo and sends update to subscribed clients
     * @param joinKey String for board
     * @param newHeading  String for new name of board
     * @param password String password for board
     * @return Board the renamed board
     */
    @MessageMapping("/boards/rename/{joinKey}/{newHeading}")
    public Board renameBoard(final String password, @DestinationVariable final String joinKey,
                             @DestinationVariable final String newHeading)
    {
        final Board toBeRenamed = boardService.getBoardWithKeyAndPassword(joinKey, password);

        toBeRenamed.setTitle(newHeading);
        boardService.saveBoard(toBeRenamed);

        updateBoardRenamed(joinKey, newHeading);

        return toBeRenamed;
    }

    /**
     * Sends the old and new heading of the board to all subscribed clients
     * @param joinKey String for board
     * @param newHeading  String for new name of board
     */
    public void updateBoardRenamed(final String joinKey, final String newHeading) {
        logger.info("Propagating column renamed for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/boards/" + joinKey + "/rename", newHeading);
    }

}
