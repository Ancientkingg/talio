package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Column;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.web.servlet.MockMvc;
import server.services.BoardService;

import java.sql.Timestamp;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ColumnController.class)
public class ColumnControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ColumnController columnController;
    @MockBean
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimpMessageSendingOperations messagingTemplate;

    @Test
    public void contextLoads() {
        assertNotNull(columnController);
    }

    @Test
    public void addColumnToBoardWithPasswordTest() throws Exception {

        Column addedColumn = new Column(0, 1, "Column 1", new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        finalBoard.addColumn(addedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/create/joinkey/Column 1").param("index", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk());

        assertEquals(addedColumn.getIndex(), initialBoard.getColumns().first().getIndex());
        assertEquals(addedColumn.getHeading(), initialBoard.getColumns().first().getHeading());
        assertEquals(addedColumn.getCards(), initialBoard.getColumns().first().getCards());
    }

    @Test
    public void addColumnToBoardWithoutPasswordTest() throws Exception {

        Column addedColumn = new Column(1, 1, "Column 1", new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        finalBoard.addColumn(addedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/create/joinkey/Column 1").param("index", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isOk());

        assertEquals(addedColumn.getIndex(), initialBoard.getColumns().first().getIndex());
        assertEquals(addedColumn.getHeading(), initialBoard.getColumns().first().getHeading());
        assertEquals(addedColumn.getCards(), initialBoard.getColumns().first().getCards());
    }

    @Test
    public void removeColumnFromBoardWithPasswordTest() throws Exception {

        Column toBeRemovedColumn = new Column(1, 1, "Column 1", new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        initialBoard.addColumn(toBeRemovedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/remove/joinkey/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toBeRemovedColumn)));

        assertEquals(0, initialBoard.getColumns().size());
        assertEquals(initialBoard, finalBoard);
    }

    @Test
    public void removeColumnFromBoardWithoutPasswordTest() throws Exception {

        Column toBeRemovedColumn = new Column(1, 1, "Column 1", new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        initialBoard.addColumn(toBeRemovedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/remove/joinkey/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toBeRemovedColumn)));

        assertEquals(0, initialBoard.getColumns().size());
        assertEquals(initialBoard, finalBoard);
    }

    /**
     * The removeColumn endpoint tries to remove the specified column, but if the column does not exist,
     * nothing should happen.
     * @throws Exception JsonProcessingException thrown by ObjectMapper.writeValueAsString and also Exception thrown by MockMvc.perform
     */
    @Test
    public void removeColumnFromEmptyBoardTest() throws Exception {

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/remove/joinkey/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk());
//                .andExpect(content().json(objectMapper.writeValueAsString(toBeRemovedColumn))); // "Unparsable JSON string: null"

        assertEquals(0, initialBoard.getColumns().size());
        assertEquals(initialBoard, finalBoard);
    }

//    @Test
//    public void addAndRemoveMultipleColumnsTest() throws Exception {
//
//        Column column1 = new Column(1, 1, "Column 1", new TreeSet<>());
//        Column column2 = new Column(2, 2, "Column 1", new TreeSet<>());
//        Column column3 = new Column(3, 3, "Column 1", new TreeSet<>());
//        Column column4 = new Column(4, 4, "Column 1", new TreeSet<>());
//
//        Board actualBoard = new Board("joinkey", "Board", "password", new TreeSet<>(), new Timestamp(12345L));
//        Board expectedBoard = new Board("joinkey", "Board", "password", new TreeSet<>(), new Timestamp(12345L));
//
//        // Mock boardService
//        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
//        when(boardService.saveBoard(any(Board.class))).thenReturn(expectedBoard);
//
//
//        // add first column
//        expectedBoard.addColumn(column3); // [3]
//
//        this.mockMvc.perform(post("/columns/create/joinkey/Column 3").param("index", "3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column3)));
//
//        assertEquals(column3, actualBoard.getColumns().last());
//        assertEquals(expectedBoard, actualBoard);
//
//
//        // add another column (to the beginning)
//        expectedBoard.addColumn(column1); // [1, 3]
//
//        this.mockMvc.perform(post("/columns/create/joinkey/Column 1").param("index", "1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column1)));
//
//        assertEquals(expectedBoard, actualBoard);
//
//
//        // add Column 2 (in the middle)
//        expectedBoard.addColumn(column2); // [1, 2, 3]
//
//        this.mockMvc.perform(post("/columns/create/joinkey/Column 2").param("index", "2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column2)));
//
//        assertTrue(actualBoard.getColumns().contains(column2));
//        assertEquals(column1, actualBoard.getColumns().first());
//        assertEquals(column3, actualBoard.getColumns().last());
//        assertEquals(expectedBoard, actualBoard);
//
//
//        // remove Column 2 (from the middle)
//        expectedBoard.removeColumn(column2); // [1, 3]
//
//        this.mockMvc.perform(post("/columns/remove/joinkey/Column 2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column2)));
//
//        assertEquals(2, actualBoard.getColumns().size());
//        assertEquals(column1, actualBoard.getColumns().first());
//        assertEquals(column3, actualBoard.getColumns().last());
//        assertEquals(expectedBoard, actualBoard);
//
//
//        expectedBoard.addColumn(column4); // [1, 3, 4]
//
//        // add Column 4 (to the end)
//        this.mockMvc.perform(post("/columns/create/joinkey/Column 4").param("index", "4")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column4)));
//
//        assertEquals(3, actualBoard.getColumns().size());
//        assertEquals(column4, actualBoard.getColumns().last());
//        assertEquals(expectedBoard, actualBoard);
//
//
//        // remove Column 1 (first column)
//        expectedBoard.removeColumn(column1); // [3, 4]
//
//        this.mockMvc.perform(post("/columns/remove/joinkey/Column 1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column1)));
//
//        assertEquals(2, actualBoard.getColumns().size());
//        assertEquals(column3, actualBoard.getColumns().first());
//        assertEquals(column4, actualBoard.getColumns().last());
//        assertEquals(expectedBoard, actualBoard);
//
//
//        // remove Column 4 (last column)
//        expectedBoard.removeColumn(column4); // [3]
//
//        this.mockMvc.perform(post("/columns/remove/joinkey/Column 4")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(column4)));
//
//        assertEquals(1, actualBoard.getColumns().size());
//        assertEquals(column3, actualBoard.getColumns().first());
//        assertEquals(expectedBoard, actualBoard);
//    }
}
