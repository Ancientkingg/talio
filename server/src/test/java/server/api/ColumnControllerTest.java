package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Column;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import server.services.BoardService;

import java.sql.Timestamp;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    public void contextLoads() {
        assertNotNull(columnController);
    }

    @Test
    public void addColumnToBoardWithPasswordTest() throws Exception {

        Column addedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        finalBoard.addColumn(addedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/create/Column 1").param("index", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addedColumn)));

        assertEquals(addedColumn, initialBoard.getColumns().first());
        assertEquals(initialBoard, finalBoard);
    }

    @Test
    public void addColumnToBoardWithoutPasswordTest() throws Exception {

        Column addedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        finalBoard.addColumn(addedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/create/Column 1").param("index", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addedColumn)));

        assertEquals(addedColumn, initialBoard.getColumns().first());
        assertEquals(initialBoard, finalBoard);
    }

    @Test
    public void removeColumnFromBoardWithPasswordTest() throws Exception {

        Column toBeRemovedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        initialBoard.addColumn(toBeRemovedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/remove/Column 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toBeRemovedColumn)));

        assertEquals(0, initialBoard.getColumns().size());
        assertEquals(initialBoard, finalBoard);
    }

    @Test
    public void removeColumnFromBoardWithoutPasswordTest() throws Exception {

        Column toBeRemovedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", null, new TreeSet<>(), new Timestamp(12345L));
        initialBoard.addColumn(toBeRemovedColumn);

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/remove/Column 1")
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

        Column toBeRemovedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(initialBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/remove/Column 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toBeRemovedColumn)));

        assertEquals(0, initialBoard.getColumns().size());
        assertEquals(initialBoard, finalBoard);
    }
}
