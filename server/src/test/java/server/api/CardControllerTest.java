package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.web.servlet.MockMvc;
import server.services.BoardService;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardController cardController;

    @MockBean
    private BoardService boardService;

    @MockBean
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    private Board actualBoard;
    private Board expectedBoard;
    private Column actualColumn;
    private Column expectedColumn;

    @BeforeEach
    public void setup() { // exectued before each test

        actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        actualColumn = new Column(1, 1, "Column 1", new TreeSet<>());
        expectedColumn = new Column(1, 1, "Column 1", new TreeSet<>());

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(actualBoard);
    }

    @Test
    public void contextLoads() {
        assertNotNull(cardController);
    }

    @Test
    public void addCardTest() throws Exception {

        Card cardToBeAdded = new Card("Card 1", 1, "Description", new HashSet<>());

        CardDTO cardDTO = new CardDTO(cardToBeAdded, "password");

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        expectedColumn.addCard(cardToBeAdded);

        this.mockMvc.perform(post("/cards/add/joinkey/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cardToBeAdded)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void removeCardTest() throws Exception {

        Card cardToBeRemoved = new Card("Card 1", 1, "Description", new HashSet<>());

        CardDTO cardDTO = new CardDTO(cardToBeRemoved, "password");

        actualColumn.addCard(cardToBeRemoved);

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        this.mockMvc.perform(post("/cards/remove/joinkey/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cardToBeRemoved)));

        assertEquals(expectedBoard, actualBoard);
    }

}
