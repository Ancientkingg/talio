package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Card;
import commons.Column;
import commons.DTOs.CardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertNotNull(cardController);
    }

    @Test
    public void addCardTest() throws Exception {

        Card cardToBeAdded = new Card("Card 1", 1, "Description", new HashSet<>());

        CardDTO cardDTO = new CardDTO(cardToBeAdded, "password");

        Board actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        Column actualColumn = new Column("Column 1", 1, new TreeSet<>());
        Column expectedColumn = new Column("Column 1", 1, new TreeSet<>());

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(actualBoard);

        expectedColumn.addCard(cardToBeAdded);

        this.mockMvc.perform(post("/cards/add/joinkey/Column 1")
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

        Board actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        Column actualColumn = new Column("Column 1", 1, new TreeSet<>());
        Column expectedColumn = new Column("Column 1", 1, new TreeSet<>());

        actualColumn.addCard(cardToBeRemoved);

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(actualBoard);

        this.mockMvc.perform(post("/cards/remove/joinkey/Column 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cardToBeRemoved)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void updateCardTest() throws Exception {

        Card initialCard = new Card("Card 1", 1, "Description", new HashSet<>());
        Card finalCard = new Card("Card 1", 1, "Description", new HashSet<>());

        CardDTO cardDTO = new CardDTO(finalCard, "password");

        Board actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        Column actualColumn = new Column("Column 1", 1, new TreeSet<>());
        Column expectedColumn = new Column("Column 1", 1, new TreeSet<>());

        actualColumn.addCard(initialCard);
        expectedColumn.addCard(finalCard);

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(actualBoard);

        this.mockMvc.perform(post("/cards/update/joinkey/Column 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(initialCard)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void updatePositionOfCardTest() throws Exception {

        Card actualCard1 = new Card("Card 1", 0, "Description", new HashSet<>());
        Card actualCard2 = new Card("Card 2", 1, "Description", new HashSet<>());
        Card actualCard3 = new Card("Card 3", 2, "Description", new HashSet<>());
        Card actualCard4 = new Card("Card 4", 3, "Description", new HashSet<>());

        Card expectedCard1 = new Card("Card 1", 2, "Description", new HashSet<>());
        Card expectedCard2 = new Card("Card 2", 0, "Description", new HashSet<>());
        Card expectedCard3 = new Card("Card 3", 1, "Description", new HashSet<>());
        Card expectedCard4 = new Card("Card 4", 3, "Description", new HashSet<>());

        CardDTO cardDTO1 = new CardDTO(actualCard1, "password");
        CardDTO cardDTO4 = new CardDTO(actualCard4, "password");

        Board actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        Column actualColumn = new Column("Column 1", 1, new TreeSet<>());
        Column expectedColumn = new Column("Column 1", 1, new TreeSet<>());

        actualColumn.addCard(actualCard1);
        actualColumn.addCard(actualCard2);
        actualColumn.addCard(actualCard3);
        actualColumn.addCard(actualCard4);

        expectedColumn.addCard(expectedCard1);
        expectedColumn.addCard(expectedCard2);
        expectedColumn.addCard(expectedCard3);
        expectedColumn.addCard(expectedCard4);

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(actualBoard);

        // 1, 2, 3, 4 -> 2, 3, 1, 4

        // weird thing about Column.updateCardPostion - passing position 4 as new index results in card being placed at 4th position
        this.mockMvc.perform(post("/cards/updatePosition/joinkey/Column 1/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actualColumn)));

        assertEquals(expectedBoard, actualBoard);

        int[] expectedPriorities = {0, 2, 3, 1};

        int i = 0;

        for(Card c : expectedColumn.getCards()) {
            c.setPriority(expectedPriorities[i ++]);
        }

        // 2, 3, 1, 4 -> 2, 4, 3, 1
        this.mockMvc.perform(post("/cards/updatePosition/joinkey/Column 1/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO4)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actualColumn)));

        assertEquals(expectedBoard, actualBoard);

        expectedPriorities =  new int[] {1, 3, 0, 2};

        i = 0;

        for(Card c : expectedColumn.getCards()) {
            c.setPriority(expectedPriorities[i ++]);
        }

        // 2, 3, 1, 4 -> 1, 2, 4, 3
        this.mockMvc.perform(post("/cards/updatePosition/joinkey/Column 1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(actualColumn)));

        assertEquals(expectedBoard, actualBoard);
    }
}
