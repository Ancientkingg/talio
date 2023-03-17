package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import commons.exceptions.ColumnNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;
import javax.validation.Valid;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final BoardService boardService;

    /**
     * Constructor for the Card Controller
     * @param boardService Dependency injection for the board service
     */
    public CardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     *
     * @param cardDTO Card to be created
     * @param joinKey Key used to identify board to which card is to be added
     * @param columnName Used to identify column to which card is to be added
     * @return The card added to column in board
     */
    @PostMapping("/add/{joinKey}/{columnName}")
    public ResponseEntity<Card> addCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                        @PathVariable final String columnName)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.card();

        try {
            board.addCardToColumn(card, columnName);
        } catch (ColumnNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The column " + columnName + " was not found in the board with join key " + joinKey);
        }
        boardService.saveBoard(board);

        return ResponseEntity.ok(card);
    }

    /**
     * Remove a card
     * @param cardDTO Containing card to be removed and password to board for authentication
     * @param joinKey Key of board from which card is to be deleted
     * @param columnName Name of column from which card is to be deleted
     * @return The card deleted from CardRepository
     */
    @PostMapping("/remove/{joinKey}/{columnName}")
    public ResponseEntity<Card> removeCard(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                           @PathVariable final String columnName)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column = board.getColumnByName(columnName);

        column.removeCard(card);

        boardService.saveBoard(board);

        return ResponseEntity.ok(card);
    }

    /**
     * Update the position of a card in a column according to the new position
     * @param cardDTO Card to be updated and password to board for authentication
     * @param joinKey Key of board to which card belongs
     * @param columnName Name of column to which card belongs
     * @param newPosition New position of card in column
     *
     * @return The column in which the card was updated
     */
    @PostMapping("/updatePosition/{joinKey}/{columnName}/{newPosition}")
    public ResponseEntity<Column> updatePosition(@Valid @RequestBody final CardDTO cardDTO, @PathVariable final String joinKey,
                                                 @PathVariable final String columnName, @PathVariable final int newPosition)
    {
        final String password = cardDTO.password();

        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final Card card = cardDTO.getCard();
        final Column column = board.getColumnByName(columnName);

        column.updateCardPosition(card, newPosition);

        boardService.saveBoard(board);

        return ResponseEntity.ok(column);
    }

}
