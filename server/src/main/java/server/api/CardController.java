package server.api;

import commons.Board;
import commons.Card;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("cards")
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
     * @param password Password to board
     * @return The card added to column in board
     */
    @PostMapping("/{joinKey}/add/card")
    public ResponseEntity<Card> addCard(@Valid @RequestBody final Card cardDTO, @PathVariable final String joinKey,
                                        @RequestBody final String columnName, @RequestBody final String password)
    { // checkstyle complained if I kept this bracket on the line above. Why ??
        final Board board =  boardService.getBoardWithKeyAndPassword(joinKey, password);

        final String title = cardDTO.getTitle();
        final String description = cardDTO.getDescription();
        final Set<Tag> tags = cardDTO.getTags();
        final int priority = cardDTO.getPriority();

        final Card card = new Card(title, priority, description, tags);

        board.addCardToColumn(card, columnName);
        boardService.saveBoard(board);

        return ResponseEntity.ok(card);
    }
}
