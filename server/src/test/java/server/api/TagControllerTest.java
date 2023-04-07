package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.*;
import commons.Color;
import commons.DTOs.TagDTO;
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

@WebMvcTest(TagController.class)
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagController tagController;
    @MockBean
    private BoardService boardService;
    @MockBean
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private Board actualBoard;
    private Board expectedBoard;
    private Card actualCard;
    private Card expectedCard;
    private Column actualColumn;
    private Column expectedColumn;

    @BeforeEach
    public void setup() { // executed before each test
        actualBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));
        expectedBoard = new Board("joinkey", "Board 1", "password", new TreeSet<>(), new Timestamp(12345L));

        actualCard = new Card("title", 1, "Tag 1", new HashSet<>());
        expectedCard = new Card("title", 1, "Tag 1", new HashSet<>());

        actualColumn = new Column(1, "Column 1", 1,  new TreeSet<>());
        expectedColumn = new Column(1, "Column 1", 1,  new TreeSet<>());

        when(boardService.getBoardWithKeyAndPassword(anyString(), anyString())).thenReturn(actualBoard);
        when(boardService.saveBoard(any(Board.class))).thenReturn(expectedBoard);
    }

    @Test
    public void contextLoads() {
        assertNotNull(tagController);
    }

    @Test
    public void addTag() throws Exception {
        Tag tag = new Tag("Tag 1", new ColorScheme(new Color(0, 0, 0,255), new Color(0,0,0, 255)));
        TagDTO tagDTO = new TagDTO(tag, "password");

        actualBoard.addTag(tag);

        mockMvc.perform(post("/tags/add/joinkey/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tag)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void addTagToCard() throws Exception {
        Tag tag = new Tag("Tag 1",  new ColorScheme(new Color(0, 0, 0, 255), new Color(0, 0, 0, 255)));
        TagDTO tagDTO = new TagDTO(tag, "password");

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        actualColumn.addCard(actualCard);
        expectedColumn.addCard(expectedCard);

        actualCard.addTag(tag);

        mockMvc.perform(post("/tags/addToCard/joinkey/" + actualCard.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tag)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void removeTag() throws Exception {
        Tag tag = new Tag("Tag 1",  new ColorScheme(new Color(0, 0, 0, 255), new Color(0, 0, 0, 255)));
        TagDTO tagDTO = new TagDTO(tag, "password");

        actualBoard.addTag(tag);

        mockMvc.perform(post("/tags/remove/joinkey/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tag)));

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void removeTagFromCard() throws Exception {
        Tag tag = new Tag("Tag 1",  new ColorScheme(new Color(0, 0, 0, 255), new Color(0, 0, 0, 255)));
        TagDTO tagDTO = new TagDTO(tag, "password");

        actualBoard.addColumn(actualColumn);
        expectedBoard.addColumn(expectedColumn);

        actualColumn.addCard(actualCard);
        expectedColumn.addCard(expectedCard);

        actualCard.addTag(tag);

        mockMvc.perform(post("/tags/removeFromCard/joinkey/" + actualCard.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(tag)));

        assertEquals(expectedBoard, actualBoard);
    }

//    @Test
//    public void updateTag() throws Exception {
//        Tag tag = new Tag("Tag 1",  new ColorScheme(new Color(0, 0, 0, 255), new Color(0, 0, 0, 255)));
//
//        actualBoard.addTag(tag);
//        expectedBoard.addTag(tag);
//
//        Tag updatedTag = new Tag("Tag 1",  new ColorScheme(new Color(1, 0,0, 255), new Color(1,0,0, 255)));
//        TagDTO tagDTO = new TagDTO(updatedTag, "password");
//
//
//        expectedBoard.updateTag(updatedTag);
//
//        mockMvc.perform(post("/tags/update/joinkey/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(tagDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(updatedTag)));
//
//        assertEquals(expectedBoard, actualBoard);
//    }
}
