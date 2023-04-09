package server.api;

import commons.Board;
import commons.ColorScheme;
import commons.DTOs.ColorSchemeDTO;
import commons.DTOs.TagDTO;
import commons.Tag;
import commons.exceptions.CardNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;

import javax.validation.Valid;

@Controller
public class ColorPresetController {

    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(CardController.class);


    /**
     * Constructor for the Card Controller
     * @param boardService Dependency injection for the board service
     * @param messagingTemplate Template to send updates over socket
     */
    public ColorPresetController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Adds a tag to a board
     * @param tagDTO Tag to be added
     * @param joinKey Key used to identify board to which tag is to be added
     *
     * @return The tag added to board
     */
    @PostMapping("/color-presets/add/{joinKey}")
    public ResponseEntity<Tag> addTag(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                       @PathVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();
        board.addTag(tag);
        boardService.saveBoard(board);

        updateTagAdded(tag, board);
        return ResponseEntity.ok(tag);
    }

    /**
     * Removes a tag from a board
     * @param tagDTO Tag to be removed
     * @param joinKey Key used to identify board from which tag is to be removed
     *
     * @return The tag removed from board
     */
    @PostMapping("/color-presets/remove/{joinKey}")
    public ResponseEntity<Tag> removeTag(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                         @PathVariable final String joinKey)
    {
        final String password = tagDTO.password();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        board.deleteTag(tag);
        boardService.saveBoard(board);

        updateTagRemoved(tag, board);
        return ResponseEntity.ok(tag);
    }

    /**
     * Adds a tag to a card
     * @param tagDTO Tag to be added
     * @param joinKey Key used to identify board to which tag is to be added
     * @param cardId Id of card to which tag is to be added
     *
     * @return The tag added to card
     */
    @PostMapping("/color-presets/addToCard/{joinKey}/{cardId}")
    public ResponseEntity<Tag> addTagToCard(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                            @PathVariable final String joinKey,
                                            @PathVariable final long cardId)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        try {
            board.addTagToCard(cardId, tag);
        } catch (CardNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id" + cardId + " was not found in the board with join key " + joinKey);
        }
        boardService.saveBoard(board);

        updateTagAddedToCard(tag, cardId, board);
        return ResponseEntity.ok(tag);
    }

    /**
     * Removes a tag from a card
     * @param tagDTO Tag to be removed
     * @param joinKey Key used to identify board from which tag is to be removed
     * @param cardId Id of card from which tag is to be removed
     *
     * @return The tag removed from card
     */
    @PostMapping("/color-presets/removeFromCard/{joinKey}/{cardId}")
    public ResponseEntity<Tag> removeTagFromCard(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                                 @PathVariable final String joinKey,
                                                 @PathVariable final long cardId)
    {
        final String password = colorSchemeDTO.getPassword()
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        try {
            board.removeTagFromCard(cardId, tag);
        } catch (CardNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id" + cardId + " was not found in the board with join key " + joinKey);
        }
        boardService.saveBoard(board);

        updateTagRemovedFromCard(tag, cardId, board);
        return ResponseEntity.ok(tag);
    }

    /**
     * Updates a tag on a board
     * @param tagDTO Tag to be updated
     * @param joinKey Key used to identify board on which tag is to be updated
     *
     * @return The tag updated on board
     */
    @MessageMapping("/color-presets/edit/{joinKey}")
    public Tag editTag(@Valid final ColorSchemeDTO colorSchemeDTO, @DestinationVariable final String joinKey) {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        board.updateTag(tag);

        boardService.saveBoard(board);

        editTagUpdated(tag, board);
        return tag;
    }

    private void updateTagRemovedFromCard(final Tag tag, final long cardId, final Board board) {
        logger.info("Tag removed from card, propagating: " + board.getJoinKey() + " with name: " + tag.getTitle());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/removeFromCard", new TagDTO(tag, cardId));
    }

    private void updateTagAddedToCard(final Tag tag, final long cardId, final Board board) {
        logger.info("Tag added to card, propagating: " + board.getJoinKey() + " with name: " + tag.getTitle());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/addToCard", new TagDTO(tag, cardId));
    }

    private void editTagUpdated(final Tag tag, final Board board) {
        logger.info("Tag updated in board, propagating: " + board.getJoinKey() + " with name: " + tag.getTitle());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/edit", tag);
    }

    private void updateTagRemoved(final Tag tag, final Board board) {
        logger.info("Tag removed from board, propagating: " + board.getJoinKey() + " with name: " + tag.getTitle());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/remove", tag);
    }

    private void updateTagAdded(final Tag tag, final Board board) {
        logger.info("Tag added to board, propagating: " + board.getJoinKey() + " with name: " + tag.getTitle());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/add", tag);
    }
}
