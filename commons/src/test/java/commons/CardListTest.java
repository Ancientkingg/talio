package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    HashSet<Card> c1;
    HashSet<Card> c2;

    @BeforeEach
    void setUp() {
        c1 = new HashSet<>();
        c2 = new HashSet<>();
        for (int i = 0; i < 10; i += 2) {
            c1.add(new Card(String.valueOf(i), String.valueOf(i), new HashSet<>()));
            c2.add(new Card(String.valueOf(i + 1), String.valueOf(i + 1), new HashSet<>()));
        }
    }

    @Test
    void testConstructor() {
        CardList cl = new CardList("To-Do", c1);
        assertEquals("To-Do", cl.getHeading());
        assertEquals(c1, cl.getCards());
    }

    @Test
    void setHeading() {
        CardList cl = new CardList("To-Do", c1);
        cl.setHeading("Done");
        assertEquals("Done", cl.getHeading());
    }

    @Test
    void setCards() {
        CardList cl = new CardList("To-Do", c1);
        cl.setCards(c2);
        assertEquals(c2, cl.getCards());
    }

    @Test
    void addCard() {
        CardList cl = new CardList("To-Do", c1);
        for (Card c : c2) {
            assertTrue(cl.addCard(c));
            assertTrue(cl.getCards().contains(c));
            assertFalse(cl.addCard(c));
        }
    }

    @Test
    void removeCard() {
        CardList cl = new CardList("To-Do", c1);
        for (Card c : new HashSet<>(c1)) {
            assertTrue(cl.getCards().contains(c));
            assertTrue(cl.removeCard(c));
            assertFalse(cl.getCards().contains(c));
            assertFalse(cl.removeCard(c));
        }
    }

    @Test
    void testEquals() {
        CardList cl = new CardList("To-Do", c1);
        CardList cl2 = new CardList("To-Do", c1);
        CardList cl3 = new CardList("Done", c1);
        CardList cl4 = new CardList("To-Do", c2);

        assertEquals(cl, cl2);
        assertNotEquals(cl, cl3);
        assertNotEquals(cl, cl4);
    }

    @Test
    void testHashCode() {
        CardList cl = new CardList("To-Do", c1);
        CardList cl2 = new CardList("To-Do", c1);
        CardList cl3 = new CardList("Done", c1);
        CardList cl4 = new CardList("To-Do", c2);

        assertEquals(cl.hashCode(), cl2.hashCode());
        assertNotEquals(cl.hashCode(), cl3.hashCode());
        assertNotEquals(cl.hashCode(), cl4.hashCode());
    }
}