package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import commons.exceptions.ColumnNotFoundException;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;
import javax.validation.Valid;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(CardController.class);

    /**
     * Constructor for the Card Controller
     *
     * @param boardService      Dependency injection for the board service
     * @param messagingTemplate Template to send updates over socket
     */
    public CardController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     *
     * @param cardDTO Card to be created
     * @param joinKey Key used to identify board to which card is to be added
     * @param columnId Used to identify column to which card is to be added
     * @return The card added to column in board
     */
    @PostMapping("/add/{joinKey}/{columnId}")
    public ResponseEntity<Card> addCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                        @PathVariable final long columnId)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.card();

        try {
            board.addCardToColumn(card, columnId);
        } catch (ColumnNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The column " + columnId + " was not found in the board with join key " + joinKey);
        }
        boardService.saveBoard(board);

        updateCardAdded(joinKey, columnId, card);

        return ResponseEntity.ok(card);
    }

    /**
     * Remove a card
     *
     * @param cardDTO  Containing card to be removed and password to board for authentication
     * @param joinKey  Key of board from which card is to be removed
     * @param columnId Name of column from which card is to be removed
     * @return The card removed from CardRepository
     */
    @PostMapping("/remove/{joinKey}/{columnId}")
    public ResponseEntity<Card> removeCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                           @PathVariable final long columnId)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column = board.getColumnById(columnId);

        column.removeCard(card);
        boardService.saveBoard(board);

        updateCardRemoved(joinKey, columnId, card);

        return ResponseEntity.ok(card);
    }

    /**
     * Update the position of a card in a column according to the new position
     * @param cardDTO Card to be updated and password to board for authentication
     * @param joinKey Key of board to which card belongs
     * @param columnId Name of column to which card belongs
     * @param newPosition New position of card in column
     *
     * @return The column in which the card was updated
     */
    @MessageMapping("/reposition/{joinKey}/{columnId}/{newPosition}")
    public Column repositionCard(final CardDTO cardDTO, @DestinationVariable final String joinKey,
                                 @DestinationVariable final long columnId, @DestinationVariable final int newPosition)
    {
        final String password = cardDTO.password();

        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column = board.getColumnById(columnId);

        if (newPosition < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new position must be a positive integer");

        if (newPosition != card.getPriority())
            column.updateCardPosition(card, newPosition);

        boardService.saveBoard(board);

        updateCardRepositioned(joinKey, columnId, card, newPosition);

        return column;
    }

    /**
     * Change the title, description, or tags of a card
     *
     * @param cardDTO  Containing card to be updated and password to board for authentication
     * @param joinKey  Key of board from which card is to be updated
     * @param columnId Name of column from which card is to be updated
     * @return The card updated in CardRepository
     */
    @MessageMapping("/edit/{joinKey}/{columnId}")
    public ResponseEntity<Card> editCard(final CardDTO cardDTO, @DestinationVariable final String joinKey,
                                         @DestinationVariable final long columnId)
    {
        final String password = cardDTO.getPassword();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column = board.getColumnById(columnId);

        column.updateCard(card);

        boardService.saveBoard(board);

        updateCardEdited(joinKey, columnId, card);

        return ResponseEntity.ok(card);
    }

    /**
     * Notifies all subscribed clients to alter the position of the card
     *
     * @param joinKey     String of board
     * @param columnId    String of column card is in
     * @param card        Card to be repositioned
     * @param newPosition int the index to move to
     */
    public void updateCardRepositioned(final String joinKey, final long columnId, final Card card, final int newPosition) {
        logger.info("Propagating card repositioned for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/cards" + joinKey + "/reposition,", new ImmutableTriple<>(columnId, newPosition, card));
    }

    /**
     * Notifies all subscribed client to alter contents of card
     *
     * @param joinKey  String of board
     * @param columnId String column card is in
     * @param card     Card to be edited
     */
    public void updateCardEdited(final String joinKey, final long columnId, final Card card) {
        logger.info("Propagating card edited for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/cards" + joinKey + "/edit/", new Pair<>(columnId, card));
    }

    /**
     * Sends the Card that was added to all clients subscribed to that board
     *
     * @param joinKey  String of the board
     * @param columnId String the name of column
     * @param card     Card that was added
     */
    public void updateCardAdded(final String joinKey, final long columnId, final Card card) {
        logger.info("Propagating card added for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/cards" + joinKey + "/add", new Pair<>(columnId, card));
    }

    /**
     * Sends the Card that was removed to all clients subscribed to that board
     *
     * @param joinKey  String of the board
     * @param columnId String the name of column
     * @param card     Card that was removed
     */
    public void updateCardRemoved(final String joinKey, final long columnId, final Card card) {
        logger.info("Propagating card removed for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/cards" + joinKey + "/remove", new Pair<>(columnId, card));
    }

}
