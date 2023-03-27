package server.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.web.servlet.MockMvc;
import server.services.BoardService;

import java.sql.Timestamp;
import java.time.Clock;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardController boardController;
    @MockBean
    private BoardService boardService;

    @MockBean
    private Clock clock;

    @MockBean
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertNotNull(boardController);
    }

    @Test
    public void createBoardWithoutPasswordTest() throws Exception {
        // Input to the controller coming from the client
        Board boardClientInput = new Board("joinkey", "title", null, null, null);
        // Output from the controller going to the client
        Board boardServerOutput = new Board("joinkey", "title", null, new TreeSet<>(), new Timestamp(12345L));

        // Mock the boardService
        when(boardService.saveBoard(boardServerOutput)).thenReturn(boardServerOutput);
        when(boardService.generateJoinKey()).thenReturn("joinkey");

        // Mock the clock
        when(clock.instant()).thenReturn(new Timestamp(12345L).toInstant());

        // Perform the request
        this.mockMvc.perform(post("/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardClientInput)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardServerOutput)));
    }

    @Test
    public void createBoardWithPasswordTest() throws Exception {
        // Input to the controller coming from the client
        Board boardClientInput = new Board("joinkey", "title", "password", null, null);
        // Output from the controller going to the client
        Board boardServerOutput = new Board("joinkey", "title", "password", new TreeSet<>(), new Timestamp(12345L));

        // Mock the boardService
        when(boardService.saveBoard(boardServerOutput)).thenReturn(boardServerOutput);
        when(boardService.generateJoinKey()).thenReturn("joinkey");

        // Mock the clock
        when(clock.instant()).thenReturn(new Timestamp(12345L).toInstant());

        // Perform the request
        this.mockMvc.perform(post("/boards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardClientInput)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardServerOutput)));
    }

}
