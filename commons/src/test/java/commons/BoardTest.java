package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    TreeSet<Column> cl1;
    TreeSet<Column> cl2;

    @BeforeEach
    void setUp() {
        cl1 = new TreeSet<>();
        cl2 = new TreeSet<>();
        for (int i = 0; i < 10; i += 2) {
            cl1.add(new Column(String.valueOf(i), 0, new TreeSet<>()));
            cl1.add(new Column(String.valueOf(i + 1), 0, new TreeSet<>()));
        }
    }

    @Test
    void constructorPasswordTest() {
        Board board = new Board("joinme", "title", "password1", cl1);
        Timestamp time = new Timestamp(System.currentTimeMillis());

        assertEquals("joinme", board.getJoinKey());
        assertEquals("title", board.getTitle());
        assertEquals("password1", board.getPassword());
        assertEquals(cl1, board.getColumns());
        assertTrue((board.getCreated().getTime() - time.getTime()) < 10);
    }

    @Test
    void constructorNoPasswordTest() {
        Board board = new Board("joinme", "title", cl1);
        Timestamp time = new Timestamp(System.currentTimeMillis());

        assertEquals("joinme", board.getJoinKey());
        assertEquals("title", board.getTitle());
        assertNull(board.getPassword());
        assertEquals(cl1, board.getColumns());
        assertTrue((board.getCreated().getTime() - time.getTime()) < 10);
    }

    @Test
    void setPassword() {
        Board board = new Board("joinme", "title", "password1", cl1);
        board.setPassword("goodPassword");
        assertEquals("goodPassword", board.getPassword());
    }

    @Test
    void setTitle() {
        Board board = new Board("joinme", "title", "password1", cl1);
        board.setTitle("better title");
        assertEquals("better title", board.getTitle());
    }

    @Test
    void setColumns() {
        Board board = new Board("joinme", "title", "password1", cl1);
        board.setColumns(cl2);
        assertEquals(cl2, board.getColumns());
    }

    @Test
    void addList() {
        Board board = new Board("joinme", "title", "password1", cl1);
        for (Column cl : cl2) {
            assertTrue(board.addColumn(cl));
            assertTrue(board.getColumns().contains(cl));
            assertFalse(board.addColumn(cl));
        }
    }

    @Test
    void removeList() {
        Board board = new Board("joinme", "title", "password1", cl1);
        for (Column cl : new HashSet<>(cl1)) {
            assertTrue(board.getColumns().contains(cl));
            assertTrue(board.removeColumn(cl));
            assertFalse(board.getColumns().contains(cl));
            assertFalse(board.removeColumn(cl));
        }
    }

    @Test
    void testEquals() {
        Board board = new Board("joinme", "title", "password1", cl1);
        Board board1 = new Board("joinme", "title", "password1", cl1);
        Board board2 = new Board("joinme", "title", "password1", cl2);
        Board board3 = new Board("joinme", "title", "password2", cl1);
        Board board4 = new Board("joinme", "title2", "password1", cl1);


        assertNotEquals(board, board2);
        assertNotEquals(board, board3);
        assertNotEquals(board, board4);
    }

    @Test
    void testHashCode() {
        Board board = new Board("joinme", "title", "password1", cl1);
        Board board1 = new Board("joinme", "title", "password1", cl1);
        Board board2 = new Board("joinme", "title", "password1", cl2);
        Board board3 = new Board("joinme", "title", "password2", cl1);
        Board board4 = new Board("joinme2", "title", "password1", cl1);
        Board board5 = new Board("joinme", "title2", "password1", cl1);

        assertEquals(board.hashCode(), board1.hashCode());
        assertNotEquals(board.hashCode(), board2.hashCode());
        assertNotEquals(board.hashCode(), board3.hashCode());
        assertNotEquals(board.hashCode(), board4.hashCode());
        assertNotEquals(board.hashCode(), board5.hashCode());
    }
}