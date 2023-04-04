package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import commons.exceptions.ColumnNotFoundException;
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
//@RequestMapping("/cards")
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
    @PostMapping("/cards/add/{joinKey}/{columnId}")
    public ResponseEntity<Card> addCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                        @PathVariable final long columnId)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.card();

        try {
            board.addCardToColumn(card, columnId);
        } catch (ColumnNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The column with id" + columnId + " was not found in the board with join key " + joinKey);
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
    @PostMapping("/cards/remove/{joinKey}/{columnId}")
    public ResponseEntity<Card> removeCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                           @PathVariable final long columnId)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column;
        try { column = board.getColumnById(columnId); }
        catch (ColumnNotFoundException e) { throw new RuntimeException(e); }

        if (!column.removeCard(card)) throw new RuntimeException();
        boardService.saveBoard(board);

        updateCardRemoved(joinKey, columnId, card);

        return ResponseEntity.ok(card);
    }

    /**
     * Update the position of a card in a column according to the new position
     * @param cardDTO Card to be updated and password to board for authentication
     * @param joinKey Key of board to which card belongs
     * @param sourceColumnId ID of column to which card belongs
     * @param destinationColumnId ID of column to which card is to be moved
     * @param newPosition New position of card in column
     *
     * @return The column in which the card was updated
     */
//    @Transactional
    @MessageMapping("/cards/reposition/{joinKey}/{sourceColumnId}/{destinationColumnId}/{newPosition}")
    public Column repositionCard(@RequestBody final CardDTO cardDTO, @DestinationVariable final String joinKey,
                                 @DestinationVariable final long sourceColumnId, @DestinationVariable final int newPosition,
                                 @PathVariable @DestinationVariable final long destinationColumnId)
    {
        if (newPosition < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new position must be a positive integer");
        }

        try {
            final String password = cardDTO.password();

            final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);

            final Card card = cardDTO.getCard();
            final Column sourceColumn = board.getColumnById(sourceColumnId);
            final Column destinationColumn = board.getColumnById(destinationColumnId);

            if (sourceColumnId == destinationColumnId && newPosition != card.getPriority()) {
                sourceColumn.updateCardPosition(card, newPosition);
            } else {
                sourceColumn.getCards().remove(card);
                card.setPriority(newPosition);
                destinationColumn.addCard(card);
            }

            boardService.saveBoard(board);

            updateCardRepositioned(joinKey, sourceColumnId, destinationColumnId, card, newPosition);

            return sourceColumn;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Change the title, description, or tags of a card
     *
     * @param cardDTO  Containing card to be updated and password to board for authentication
     * @param joinKey  Key of board from which card is to be updated
     * @param columnId Name of column from which card is to be updated
     * @return The card updated in CardRepository
     */
    @MessageMapping("/cards/edit/{joinKey}/{columnId}")
    public Card editCard(@RequestBody final CardDTO cardDTO, @DestinationVariable final String joinKey,
                                         @DestinationVariable final long columnId)
    {
        try {
            final String password = cardDTO.getPassword();

            final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

            final Card card = cardDTO.getCard();
            final Column column = board.getColumnById(columnId);

            column.updateCard(card);

            boardService.saveBoard(board);

            updateCardEdited(joinKey, columnId, card);

            return card;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Notifies all subscribed clients to alter the position of the card
     *
     * @param joinKey             String of board
     * @param columnId            ID of column card is in
     * @param destinationColumnId ID of column to which card is to be moved
     * @param card                Card to be repositioned
     * @param newPosition         int the index to move to
     */
    public void updateCardRepositioned(final String joinKey, final long columnId, final long destinationColumnId, final Card card, final int newPosition) {
        logger.info("Propagating card repositioned for: " + joinKey);
        messagingTemplate.convertAndSend("/topic/cards/" + joinKey + "/reposition,", new CardDTO(card, columnId, destinationColumnId, newPosition));
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
        messagingTemplate.convertAndSend("/topic/cards/" + joinKey + "/edit/", new CardDTO(card, columnId));
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
        messagingTemplate.convertAndSend("/topic/cards/" + joinKey + "/add", new CardDTO(card, columnId));
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
        messagingTemplate.convertAndSend("/topic/cards/" + joinKey + "/remove", new CardDTO(card, columnId));
    }

}
