package server.api;

import commons.Board;
import commons.Tag;
import commons.DTOs.TagDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.services.BoardService;

import javax.validation.Valid;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final BoardService boardService;

    private final SimpMessageSendingOperations messagingTemplate;
    private final Logger logger = LogManager.getLogger(CardController.class);


    public TagController(final BoardService boardService, final SimpMessageSendingOperations messagingTemplate) {
        this.boardService = boardService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/add/{joinKey}/")
    public ResponseEntity<Tag> addCard(@Valid @RequestBody final TagDTO tagDTO,
                                       @PathVariable final String joinKey)
    {
        final String password = tagDTO.password();
        final Board board = boardService.getBoardWithKey(joinKey);
        final Tag tag = tagDTO.tag();
        board.addTag(tag);
    }
}
