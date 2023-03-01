package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.MapKeyEnumerated;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

    TreeSet<Card> c1;
    TreeSet<Card> c2;

    @BeforeEach
    void setUp() {
        c1 = new TreeSet<>();
        c2 = new TreeSet<>();
        for (int i = 0; i < 10; i += 2) {
            c1.add(new Card(String.valueOf(i), i, String.valueOf(i), new HashSet<>()));
            c2.add(new Card(String.valueOf(i + 1), i+1, String.valueOf(i + 1), new HashSet<>()));
        }
    }

    @Test
    void testConstructor() {
        Column cl = new Column("To-Do", 0, c1);
        assertEquals("To-Do", cl.getHeading());
        assertEquals(c1, cl.getCards());
    }

    @Test
    void setOrder() {
        Column cl = new Column("To-Do", 0, c1);
        cl.setOrder(1);
        assertEquals(1, cl.getOrder());
    }

    @Test
    void setHeading() {
        Column cl = new Column("To-Do", 0, c1);
        cl.setHeading("Done");
        assertEquals("Done", cl.getHeading());
    }

    @Test
    void setCards() {
        Column cl = new Column("To-Do", 0, c1);
        cl.setCards(c2);
        assertEquals(c2, cl.getCards());
    }

    @Test
    void addCard() {
        Column cl = new Column("To-Do", 0, c1);
        for (Card c : c2) {
            assertTrue(cl.addCard(c));
            assertTrue(cl.getCards().contains(c));
            assertFalse(cl.addCard(c));
        }
    }

    @Test
    void removeCard() {
        Column cl = new Column("To-Do", 0, c1);
        for (Card c : new HashSet<>(c1)) {
            assertTrue(cl.getCards().contains(c));
            assertTrue(cl.removeCard(c));
            assertFalse(cl.getCards().contains(c));
            assertFalse(cl.removeCard(c));
        }
    }

    @Test
    void testComparison() {
        Column cl = new Column("To-Do", 1, c1);
        Column cl2 = new Column("To-Do", 1, c1);
        Column cl3 = new Column("To-Do", 0, c1);
        Column cl4 = new Column("To-Do", 2, c1);

        assertEquals(0, cl.compareTo(cl2));
        assertEquals(1, cl.compareTo(cl3));
        assertEquals(-1, cl.compareTo(cl4));
    }

    @Test
    void testEquals() {
        Column cl = new Column("To-Do", 0, c1);
        Column cl2 = new Column("To-Do", 0, c1);
        Column cl3 = new Column("Done", 0, c1);
        Column cl4 = new Column("To-Do", 0, c2);
        Column cl5 = new Column("To-Do", 1, c1);


        assertEquals(cl, cl2);
        assertNotEquals(cl, cl3);
        assertNotEquals(cl, cl4);
        assertNotEquals(cl, cl5);
    }

    @Test
    void testHashCode() {
        Column cl = new Column("To-Do", 0, c1);
        Column cl2 = new Column("To-Do", 0, c1);
        Column cl3 = new Column("Done", 0, c1);
        Column cl4 = new Column("To-Do", 0, c2);
        Column cl5 = new Column("To-Do", 1, c1);

        assertEquals(cl.hashCode(), cl2.hashCode());
        assertNotEquals(cl.hashCode(), cl3.hashCode());
        assertNotEquals(cl.hashCode(), cl4.hashCode());
        assertNotEquals(cl.hashCode(), cl5.hashCode());

    }
}