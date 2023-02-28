package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    HashSet<CardList> cl1;
    HashSet<CardList> cl2;

    @BeforeEach
    void setUp() {
        cl1 = new HashSet<>();
        cl2 = new HashSet<>();
        for (int i = 0; i < 10; i += 2) {
            cl1.add(new CardList(String.valueOf(i), new HashSet<>()));
            cl1.add(new CardList(String.valueOf(i + 1), new HashSet<>()));
        }
    }

    @Test
    void constructorTest() {
        Board board = new Board("joinme", "password1", cl1);
        Timestamp time = new Timestamp(System.currentTimeMillis());

        assertEquals("joinme", board.getJoinKey());
        assertEquals("password1", board.getPassword());
        assertEquals(cl1, board.getColumns());
        assertTrue((board.getCreated().getTime() - time.getTime()) < 10);
    }

    @Test
    void setPassword() {
        Board board = new Board("joinme", "password1", cl1);
        board.setPassword("goodPassword");
        assertEquals("goodPassword", board.getPassword());
    }

    @Test
    void setColumns() {
        Board board = new Board("joinme", "password1", cl1);
        board.setColumns(cl2);
        assertEquals(cl2, board.getColumns());
    }

    @Test
    void addList() {
        Board board = new Board("joinme", "password1", cl1);
        for (CardList cl : cl2) {
            assertTrue(board.addList(cl));
            assertTrue(board.getColumns().contains(cl));
            assertFalse(board.addList(cl));
        }
    }

    @Test
    void removeList() {
        Board board = new Board("joinme", "password1", cl1);
        for (CardList cl : new HashSet<>(cl1)) {
            assertTrue(board.getColumns().contains(cl));
            assertTrue(board.removeList(cl));
            assertFalse(board.getColumns().contains(cl));
            assertFalse(board.removeList(cl));
        }
    }

    @Test
    void testEquals() {
        Board board = new Board("joinme", "password1", cl1);
        Board board1 = new Board("joinme", "password1", cl1);
        Board board2 = new Board("joinme", "password1", cl2);
        Board board3 = new Board("joinme", "password2", cl1);
        Board board4 = new Board("joinme2", "password1", cl1);

        assertEquals(board, board1);
        assertNotEquals(board, board2);
        assertNotEquals(board, board3);
        assertNotEquals(board, board4);
    }

    @Test
    void testHashCode() {
        Board board = new Board("joinme", "password1", cl1);
        Board board1 = new Board("joinme", "password1", cl1);
        Board board2 = new Board("joinme", "password1", cl2);
        Board board3 = new Board("joinme", "password2", cl1);
        Board board4 = new Board("joinme2", "password1", cl1);

        assertEquals(board.hashCode(), board1.hashCode());
        assertNotEquals(board.hashCode(), board2.hashCode());
        assertNotEquals(board.hashCode(), board3.hashCode());
        assertNotEquals(board.hashCode(), board4.hashCode());
    }
}