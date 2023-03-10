package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Set<Tag> t1;
    Set<Tag> t2;


    @BeforeEach
    void setUp() {
        t1 = new HashSet<>();
        t2 = new HashSet<>();
        for (int i = 0; i < 10; i+=2) {
            t1.add(new Tag(String.valueOf(i), "#000000"));
            t2.add(new Tag(String.valueOf(i+1), "#000000"));
        }
    }

    @Test
    void testConstructor() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        assertEquals("Do all my cards", card.getTitle());
        assertEquals("1. card 1; 2. card 2", card.getDescription());
        assertEquals(t1, card.getTags());
    }

    @Test
    void setTitle() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        card.setTitle("Test Title");
        assertEquals("Test Title", card.getTitle());
    }

    @Test
    void setIndex() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        card.setPriority(1);
        assertEquals(1, card.getPriority());
    }

    @Test
    void setDescription() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        card.setDescription("No Description");
        assertEquals("No Description", card.getDescription());
    }

    @Test
    void getTags() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        assertEquals(t1, card.getTags());
        assertNotEquals(t2, card.getTags());
    }

    @Test
    void setTags() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        card.setTags(t2);
        assertEquals(t2, card.getTags());
        assertNotEquals(t1, card.getTags());
    }

    @Test
    void addTag() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        for (Tag t : t2) {
            assertTrue(card.addTag(t));
            assertTrue(card.getTags().contains(t));
            assertFalse(card.addTag(t));
        }
    }

    @Test
    void removeTag() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        for (Tag t : new HashSet<>(t1)) {
            assertTrue(card.getTags().contains(t));
            assertTrue(card.removeTag(t));
            assertFalse(card.getTags().contains(t));
            assertFalse(card.removeTag(t));
        }
    }

    @Test
    void comparisonTest() {
        Card card = new Card("Do all my cards", 1, "1. card 1; 2. card 2", t1);
        Card card2 = new Card("Do all my cards", 1, "1. card 1; 2. card 2", t1);
        Card card3 = new Card("Do all my cards", 2, "1. card 1; 2. card 2", t1);
        Card card4 = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);

        assertEquals(0, card.compareTo(card2));
        assertEquals(1, card.compareTo(card4));
        assertEquals(-1, card.compareTo(card3));
    }

    @Test
    void testEquals() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        Card card2 = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        Card card3 = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t2);
        Card card4 = new Card("Not all my cards", 0, "1. card 1; 2. card 2", t2);
        Card card5 = new Card("Do all my cards", 0, "test", t2);
        Card card6 = new Card("Do all my cards", 1, "1. card 1; 2. card 2", t1);


        assertEquals(card, card2);
        assertNotEquals(card, card3);
        assertNotEquals(card, card4);
        assertNotEquals(card, card5);
        assertNotEquals(card, card6);
    }

    @Test
    void testHashCode() {
        Card card = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        Card card2 = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t1);
        Card card3 = new Card("Do all my cards", 0, "1. card 1; 2. card 2", t2);
        Card card4 = new Card("Not all my cards", 0, "1. card 1; 2. card 2", t2);
        Card card5 = new Card("Do all my cards", 0, "test", t2);
        Card card6 = new Card("Do all my cards", 1, "1. card 1; 2. card 2", t1);


        assertEquals(card.hashCode(), card2.hashCode());
        assertNotEquals(card.hashCode(), card3.hashCode());
        assertNotEquals(card.hashCode(), card4.hashCode());
        assertNotEquals(card.hashCode(), card5.hashCode());
        assertNotEquals(card.hashCode(), card6.hashCode());
    }
}