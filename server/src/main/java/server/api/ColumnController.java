package server.api;

import commons.Board;
import commons.Column;
import commons.DTOs.ColumnDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;

import java.util.TreeSet;

@Controller
public class ColumnController {

    private final BoardService boardService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger;

    /**
     * Constructor for the Column Controller
     *
     * @param boardService      Dependency injection for the board service
     * @param messagingTemplate Template to send updates over socket
     */
    public ColumnController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
        logger = LogManager.getLogger(ColumnController.class);
    }


    /**
     * Add a Column
     * @param joinKey Key used to identify board
     * @param columnHeading Heading of column to be added
     * @param password Password to board
     * @param columnId Id for column
     * @param index Index of column to be added
     * @return The Column added to the ColumnRepository
     */
    @PostMapping("/columns/create/{joinKey}/{columnHeading}/{columnId}")
    public ResponseEntity<Column> addColumn(@PathVariable final String joinKey, @PathVariable final String columnHeading,
                                            @PathVariable final String columnId, @RequestBody(required = false) final String password,
                                            @RequestParam final int index)
    {
        try {
            final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);


            final Column column = new Column(Long.valueOf(columnId), columnHeading, index, new TreeSet<>());

            if (!board.addColumn(column)) {
                throw new Exception();
            }
            boardService.saveBoard(board);

            updateColumnAdded(joinKey, column);

            return ResponseEntity.ok(column);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Remove a Column
     *
     * @param joinKey  Key used to identify board
     * @param columnId Heading of column to be removed
     * @param password Password to board
     * @return The Column removed from the ColumnRepository
     */
    @PostMapping("/columns/remove/{joinKey}/{columnId}")
    public ResponseEntity<Column> removeColumn(@PathVariable final String joinKey, @PathVariable final long columnId,
                                               @RequestBody(required = false) final String password)
    {
        try {
            final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);

            final Column toBeRemoved = board.getColumnById(columnId);

            if (!board.removeColumn(toBeRemoved)) {
                throw new RuntimeException();
            }
            boardService.saveBoard(board);

            board.refreshIndices(toBeRemoved.getIndex());
            boardService.saveBoard(board);

            updateColumnRemoved(joinKey, columnId);

            return ResponseEntity.ok(toBeRemoved);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Sends the renamed Column that was added to all clients subscribed to that board and renames in repo
     *
     * @param password   String password for board
     * @param joinKey    String for board
     * @param columnId   Long Id of column to be renamed
     * @param newHeading String for new name of column
     * @return Column the renamed column
     */
    @MessageMapping("/columns/rename/{joinKey}/{columnId}/{newHeading}")
    public Column renameColumn(@DestinationVariable final String joinKey, @DestinationVariable final long columnId,
                               @DestinationVariable final String newHeading, @Payload(required = false) final String password)
    {
        try {
            final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
            final Column toBeRenamed = board.getColumnById(columnId);

            toBeRenamed.setHeading(newHeading);
            boardService.saveBoard(board);

            updateColumnRenamed(joinKey, columnId, newHeading);

            return toBeRenamed;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Sends the Column that was added to all clients subscribed to that board
     * @param joinKey String for board
     * @param column Column that was added
     */
    public void updateColumnAdded(final String joinKey, final Column column) {
        logger.info("Propagating column added for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/columns/" + joinKey + "/add", column);
    }

    /**
     * Sends the old and new heading of the column to all subscribed clients
     * @param joinKey String for board
     * @param columnId Long ID of column
     * @param newHeading  String for new name of column
     */
    public void updateColumnRenamed(final String joinKey, final Long columnId, final String newHeading) {
        logger.info("Propagating column renamed for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/columns/" + joinKey + "/rename", new ColumnDTO(columnId, newHeading));
    }

    /**
     * Sends the columnHeading that was removed to all clients subscribed to that board
     *
     * @param joinKey  String for board
     * @param columnId Column that was removed
     */
    public void updateColumnRemoved(final String joinKey, final long columnId) {
        logger.info("Propagating column removed to: " + joinKey);
        messagingTemplate.convertAndSend("/topic/columns/" + joinKey + "/remove", columnId);
    }
}
