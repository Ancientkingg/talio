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
    * Tried to ways of creating boards - directly and using createBoard method in BoardController
    * Input that will come to controller from client - Request to add column with
    * Board joinKey as path variable
    * new Column's heading as path variable
    * password to board in request body
    * index of new Column as request parameter
    *
    * What needs to be done -
    *
    */

    // This ones a mess I'll clean it up later
    // Move to the tests below
    @Test
    public void addColumnToBoardWithPasswordTest() throws Exception {

        Column addedColumn = new Column("Column 1", 1, new TreeSet<>());


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
