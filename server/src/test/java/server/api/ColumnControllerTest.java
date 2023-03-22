package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Column;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import server.TestConfig;
import server.database.BoardRepository;
import server.services.BoardService;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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


    /*
    * Problem I was running into - Board is null
    *
    *
    * What needs to be done -
    * getBoardWithKeyAndPassword of BoardService is called by addColumn of ColumnController.
    */

    // This ones a mess I'll clean it up later
    // Move to the tests below
    @Test
    public void addColumnToBoardWithPasswordTest() throws Exception {

        Column addedColumn = new Column("Column 1", 1, new TreeSet<>());

        Board initialBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        Board finalBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        finalBoard.addColumn(addedColumn);

        when(boardService.getBoardWithKeyAndPassword("joinkey", "password")).thenReturn(initialBoard);
        when(boardService.saveBoard(finalBoard)).thenReturn(finalBoard);

        // Perform the request
        this.mockMvc.perform(post("/columns/joinkey/create/Column 1").param("index", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("password")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addedColumn)));
    }

//    @Test
//    public void addColumnToBoardWithoutPasswordTest() throws Exception {
//
//        when(boardService.getBoardWithKeyAndPassword("joinkey", "password")).thenReturn(board);
//        when(boardService.saveBoard(board)).thenReturn(board);
//
//        Column columnClientOutput = new Column("Colunn1", 1, new TreeSet<>());
//
//        this.mockMvc.perform(post("/columns/joinkey/create/Column1").param("index", "1")
//                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString("password")))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(columnClientOutput)));
//
//    }
//
//    @Test
//    public void removeColumnFromBoardWithPasswordTest() throws Exception {}
//
//    @Test
//    public void removeColumnFromBoardWithoutPasswordTest() throws Exception {}
}
