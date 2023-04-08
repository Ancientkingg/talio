package server.api;

import commons.Board;
import commons.Card;
import commons.DTOs.SubTaskDTO;
import commons.SubTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import server.services.BoardService;

@Controller
public class SubTaskController {

    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(SubTaskController.class);

    /**
     * Constructor for SubTaskController
     * @param boardService dependency injection for boardService
     * @param messagingTemplate Template to send updates over socket
     */
    public SubTaskController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Add subtask to card
     * @param joinkey joinkey for board
     * @param cardId id of card to which subtask is being added
     * @param description description of subtask
     * @param password password of board
     * @return Response entity built around subtask added to card
     */
    @PostMapping("/subtasks/add/{joinkey}")
    public ResponseEntity<SubTask> addSubTask(@PathVariable final String joinkey, @RequestParam final long cardId,
                                              @RequestParam final String description, @RequestBody final String password)
    {

        final SubTask subTask = new SubTask(description, false);

        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, password);

        final Card card;
        try {
            card = board.getCard(cardId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id" + cardId + " was not found in the board with join key " + joinkey);
        }

        card.addSubTask(subTask);

        boardService.saveBoard(board);

        updateAddSubTask(subTask, cardId, joinkey);

        return ResponseEntity.ok(subTask);
    }

    private void updateAddSubTask(final SubTask subTask, final long cardId, final String joinkey) {
        logger.info("Subtask added to card, propogating - board joinkey: " + joinkey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/" + joinkey + "/add", new SubTaskDTO(subTask, cardId));
    }

    /**
     * Remove subtask from card
     * @param joinkey joinkey for board
     * @param subTaskDTO subtask to be deleted
     * @param password password of board
     * @return Response entity built around subtask added to card
     */
    @PostMapping("/subtasks/remove/{joinkey}")
    public ResponseEntity<SubTask> removeSubTask(@PathVariable final String joinkey,
                                              @RequestParam final SubTaskDTO subTaskDTO, @RequestBody final String password)
    {

        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, password);

        final SubTask subTask = subTaskDTO.subTask();

        final Card card;
        try {
            card = board.getCard(subTaskDTO.cardId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id" + subTaskDTO.cardId() +
                    " was not found in the board with join key " + joinkey);
        }

        card.removeSubTask(subTask);

        boardService.saveBoard(board);

        updateRemoveSubTask(subTask, card.getId(), joinkey);

        return ResponseEntity.ok(subTask);
    }

    private void updateRemoveSubTask(final SubTask subTask, final long cardId, final String joinkey) {
        logger.info("Subtask removed from card, propogating - board joinkey: " + joinkey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/" + joinkey + "/remove", new SubTaskDTO(subTask, cardId));
    }

    /**
     * Toggle state of subtask (done/not done)
     * @param joinkey joinkey for board
     * @param subTaskDTO description of subtask
     * @param password password of board
     * @return Response entity built around subtask added to card
     */
    @PostMapping("/subtasks/toggle/{joinkey}")
    public ResponseEntity<SubTask> toggleSubTask(@PathVariable final String joinkey,
                                              @RequestParam final SubTaskDTO subTaskDTO, @RequestBody final String password)
    {

        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, password);
        final SubTask subTask = subTaskDTO.subTask();

        final Card card;
        try {
            card = board.getCard(subTaskDTO.cardId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id " + subTaskDTO.cardId() +
                    " was not found in the board with join key " + joinkey);
        }

        if (card.getSubtasks().contains(subTask))
            subTask.setDone(!subTask.isDone());
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id " + card.getId() + " does not contain the subtask being toggled");

        boardService.saveBoard(board);

        updateToggleSubTask(subTask, card.getId(), joinkey);

        return ResponseEntity.ok(subTask);
    }

    private void updateToggleSubTask(final SubTask subTask, final long cardId, final String joinkey) {
        logger.info("Subtask state changed to " + subTask.isDone() + ", propogating - board joinkey: " + joinkey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/" + joinkey + "/toggle", new SubTaskDTO(subTask, cardId));
    }
}
