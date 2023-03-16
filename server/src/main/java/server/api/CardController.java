package server.api;

import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import commons.DTOs.CardListDTO;
import commons.exceptions.ColumnNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;
import java.util.List;
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

        final Card card = cardDTO.card();
        final Column column = board.getColumnByName(columnName);

        column.removeCard(card);

        boardService.saveBoard(board);

        return ResponseEntity.ok(card);
    }

    /**
     * Change the order of two cards in the same column
     * @param cardsDTO Containing cards to be swapped and password to board for authentication
     * @param joinKey Key of board containing cards
     * @param columnName Name of column containing cards
     * (Note: cards must be in the same column)
     * @return Column containing swapped cards
     */
    @PostMapping("/swap/{joinKey}/{columnName}")
    public ResponseEntity<Column> swapCards (@Valid @RequestBody final CardListDTO cardsDTO,
                                             @PathVariable final String joinKey, @PathVariable final String columnName)
    {
        final String password = cardsDTO.password();
        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final List<Card> cards = cardsDTO.cards();

        if (cards.size() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The list of cards must contain exactly two cards");
        }

        final Column column = board.getColumnByName(columnName);
        column.swapCards(cards.get(0), cards.get(1));
        boardService.saveBoard(board);
        return ResponseEntity.ok(column);
    }
}
