package server.api;

import commons.Board;
import commons.ColorScheme;
import commons.DTOs.ColorSchemeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * Sets the default color preset for the card
     * @param colorSchemeDTO The color scheme to set
     * @param joinKey The join key of the board
     * @return The color scheme that was set
     */
    @MessageMapping("/color-presets/set-card/{joinKey}")
    public ResponseEntity<ColorScheme> setCardColorPreset(@Valid final ColorSchemeDTO colorSchemeDTO,
                                                          @DestinationVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        final ColorScheme existingColorScheme = board.getCardColorScheme();

        existingColorScheme.setBackgroundColor(colorScheme.getBackgroundColor());
        existingColorScheme.setTextColor(colorScheme.getTextColor());
        existingColorScheme.setName(colorScheme.getName());

        boardService.saveBoard(board);

        updateColorPresetCard(colorScheme, board);
        return ResponseEntity.ok(colorScheme);
    }

    /**
     * Sets the default color preset for the column
     * @param colorSchemeDTO The color scheme to set
     * @param joinKey The join key of the board
     * @return The color scheme that was set
     */
    @MessageMapping("/color-presets/set-column/{joinKey}")
    public ResponseEntity<ColorScheme> setColumnColorPreset(@Valid final ColorSchemeDTO colorSchemeDTO,
                                                            @DestinationVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        final ColorScheme existingColorScheme = board.getColumnColorScheme();

        existingColorScheme.setBackgroundColor(colorScheme.getBackgroundColor());
        existingColorScheme.setTextColor(colorScheme.getTextColor());
        existingColorScheme.setName(colorScheme.getName());

        boardService.saveBoard(board);

        updateColorPresetColumn(colorScheme, board);
        return ResponseEntity.ok(colorScheme);
    }

    /**
     * Sets the default color preset for the board
     * @param colorSchemeDTO The color scheme to set
     * @param joinKey The join key of the board
     * @return The color scheme that was set
     */
    @MessageMapping("/color-presets/set-board/{joinKey}")
    public ResponseEntity<ColorScheme> setBoardColorPreset(@Valid final ColorSchemeDTO colorSchemeDTO,
                                                           @DestinationVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        final ColorScheme existingColorScheme = board.getBoardColorScheme();

        existingColorScheme.setBackgroundColor(colorScheme.getBackgroundColor());
        existingColorScheme.setTextColor(colorScheme.getTextColor());
        existingColorScheme.setName(colorScheme.getName());

        boardService.saveBoard(board);

        updateColorPresetBoard(colorScheme, board);
        return ResponseEntity.ok(colorScheme);
    }

    /**
     * Adds a color scheme to a board
     * @param colorSchemeDTO Tag to be added
     * @param joinKey Key used to identify board to which tag is to be added
     *
     * @return The tag added to board
     */
    @PostMapping("/color-presets/add/{joinKey}")
    public ResponseEntity<ColorScheme> addColorPreset(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                       @PathVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        board.addColorPreset(colorScheme);
        boardService.saveBoard(board);

        updateColorPresetAdded(colorScheme, board);
        return ResponseEntity.ok(colorScheme);
    }

    /**
     * Removes a color scheme from a board
     * @param colorSchemeDTO Tag to be removed
     * @param joinKey Key used to identify board from which tag is to be removed
     *
     * @return The tag removed from board
     */
    @PostMapping("/color-presets/remove/{joinKey}")
    public ResponseEntity<ColorScheme> removeColorPreset(@Valid @RequestBody final ColorSchemeDTO colorSchemeDTO,
                                         @PathVariable final String joinKey)
    {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        board.deleteColorPreset(colorScheme);
        boardService.saveBoard(board);

        updateColorPresetRemoved(colorScheme, board);
        return ResponseEntity.ok(colorScheme);
    }

    /**
     * Updates a color scheme on a board
     * @param colorSchemeDTO Tag to be updated
     * @param joinKey Key used to identify board on which tag is to be updated
     *
     * @return The tag updated on board
     */
    @MessageMapping("/color-presets/edit/{joinKey}")
    public ColorScheme editColorPreset(@Valid final ColorSchemeDTO colorSchemeDTO, @DestinationVariable final String joinKey) {
        final String password = colorSchemeDTO.getPassword();
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, password);
        final ColorScheme colorScheme = colorSchemeDTO.getColorScheme();

        board.updateColorScheme(colorScheme);

        boardService.saveBoard(board);

        editColorPresetUpdated(colorScheme, board);
        return colorScheme;
    }

    private void editColorPresetUpdated(final ColorScheme colorScheme, final Board board) {
        logger.info("ColorPreset updated in board, propagating: " + board.getJoinKey() + " with name: " + colorScheme.getName());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/edit", colorScheme);
    }

    private void updateColorPresetRemoved(final ColorScheme colorScheme, final Board board) {
        logger.info("ColorPreset removed from board, propagating: " + board.getJoinKey() + " with name: " + colorScheme.getName());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/remove", colorScheme);
    }

    private void updateColorPresetAdded(final ColorScheme colorScheme, final Board board) {
        logger.info("ColorPreset added to board, propagating: " + board.getJoinKey() + " with name: " + colorScheme.getName());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/add", colorScheme);
    }

    private void updateColorPresetBoard(final ColorScheme colorScheme, final Board board) {
        logger.info("Default ColorPreset set to board, propagating: " + board.getJoinKey());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/set-board", colorScheme);

    }

    private void updateColorPresetColumn(final ColorScheme colorScheme, final Board board) {
        logger.info("Default ColorPreset set to column of board, propagating: " + board.getJoinKey());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/set-column", colorScheme);
    }

    private void updateColorPresetCard(final ColorScheme colorScheme, final Board board) {
        logger.info("Default ColorPreset set to card of board, propagating: " + board.getJoinKey());
        messagingTemplate.convertAndSend("/topic/color-presets/" + board.getJoinKey() + "/set-card", colorScheme);
    }
}
