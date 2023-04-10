package server.api;

import commons.Board;
import commons.Card;
import commons.DTOs.SubTaskDTO;
import commons.SubTask;
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

@Controller
public class SubTaskController {

    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(SubTaskController.class);

    /**
     * Constructor for SubTaskController
     *
     * @param boardService      dependency injection for boardService
     * @param messagingTemplate Template to send updates over socket
     */
    public SubTaskController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Adds a subtask to a card
     *
     * @param joinKey    join key of board
     * @param subTaskDTO subtask to be added
     *
     * @return subtask added
     */
    @PostMapping("/subtasks/add/{joinKey}")
    public ResponseEntity<SubTask> addSubTask(@PathVariable final String joinKey,
                                              @RequestBody final SubTaskDTO subTaskDTO)
    {

        final SubTask subTask = subTaskDTO.subTask();

        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, subTaskDTO.password());

        final Card card;
        try {
            card = board.getCard(subTaskDTO.cardId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id"
                    + subTaskDTO.cardId()
                    + " was not found in the board with join key " + joinKey);
        }

        subTask.setPriority(card.getSubtasks().size());

        if (!card.addSubTask(subTask)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The subtask with id "
                    + subTask.getId()
                    + " already exists in the card with id " + subTaskDTO.cardId());
        }

        boardService.saveBoard(board);

        updateAddSubTask(subTask, subTaskDTO.cardId(), joinKey);

        return ResponseEntity.ok(subTask);
    }

    private void updateAddSubTask(final SubTask subTask, final long cardId, final String joinKey) {
        logger.info("Subtask added to card, propagating - board joinKey: " + joinKey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/"
                + joinKey + "/add", new SubTaskDTO(subTask, cardId));
    }

    @MessageMapping("/subtasks/edit/{joinKey}")
    private void editSubTask(final SubTaskDTO subTaskDTO, final @DestinationVariable String joinKey) {
        final Board board = boardService.getBoardWithKeyAndPassword(joinKey, subTaskDTO.password());
        final Card card;
        try {
            card = board.getCard(subTaskDTO.cardId());
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }
        card.updateSubTask(subTaskDTO.subTask());
        boardService.saveBoard(board);
        updateSubTaskEdited(subTaskDTO.subTask(), subTaskDTO.cardId(), joinKey);
    }

    private void updateSubTaskEdited(final SubTask subTask, final long cardId, final String joinKey) {
        logger.info("Subtask updated, propagating - board join key: " + joinKey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/" + joinKey + "/edit", new SubTaskDTO(subTask, cardId));
    }

    /**
     * Remove subtask from card
     *
     * @param joinkey    joinkey for board
     * @param subTaskDTO DTO containing subtask and id of card containing it and password
     *
     * @return Response entity built around subtask removed from card
     */
    @PostMapping("/subtasks/remove/{joinkey}")
    public ResponseEntity<SubTask> removeSubTask(@PathVariable final String joinkey,
                                                 @RequestBody final SubTaskDTO subTaskDTO)
    {

        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, subTaskDTO.password());

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
     *
     * @param joinkey    joinkey for board
     * @param subTaskDTO DTO containing subtask and id of card containing it
     *
     * @return Response entity built around subtask whose state is toggled
     */
    @PostMapping("/subtasks/toggle/{joinkey}")
    public ResponseEntity<SubTask> toggleSubTask(@PathVariable final String joinkey,
                                                 @RequestBody final SubTaskDTO subTaskDTO)
    {
        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, subTaskDTO.password());
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

    /**
     * moves subtask within the card
     *
     * @param joinkey    joinkey for board
     * @param subTaskDTO DTO containing subtask and id of card containing it
     */
    @MessageMapping("/subtasks/move/{joinkey}")
    public void moveSubTask(final SubTaskDTO subTaskDTO,
                                               @DestinationVariable final String joinkey)
    {
        final Board board = boardService.getBoardWithKeyAndPassword(joinkey, subTaskDTO.password());
        final SubTask subTask;
        try {
            subTask = board.getCard(subTaskDTO.cardId()).getSubtasks()
                    .stream().filter(x -> x.getId() == subTaskDTO.subTask().getId()).toList().get(0);
        } catch (CardNotFoundException e) {
            throw new RuntimeException(e);
        }

        final Card card;
        try {
            card = board.getCard(subTaskDTO.cardId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id " + subTaskDTO.cardId() +
                    " was not found in the board with join key " + joinkey);
        }

        if (!card.getSubtasks().contains(subTask))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The card with id " + card.getId() + " does not contain the subtask being moved");

        if (subTaskDTO.index() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subtask cannot be moved to negative index");

        final int newIndex = Math.min(card.getSubtasks().size() - 1, subTaskDTO.index());

        card.moveSubTask(subTask, newIndex);

        boardService.saveBoard(board);

        updateMoveSubTask(subTask, card.getId(), joinkey, newIndex);
    }

    private void updateMoveSubTask(final SubTask subTask, final long cardId, final String joinkey, final int index) {
        logger.info("Subtask state changed to " + subTask.isDone() + ", propogating - board joinkey: " + joinkey + ", cardId: " + cardId +
                ", subTask description: " + subTask.getDescription());
        messagingTemplate.convertAndSend("/topic/subtasks/" + joinkey + "/move", new SubTaskDTO(subTask, cardId, index));
    }
}
