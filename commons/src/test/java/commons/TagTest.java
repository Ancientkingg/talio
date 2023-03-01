package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void testConstructor() {
        Tag t = new Tag("Important", "#ff0000");
        assertEquals("Important", t.getTitle());
        assertEquals("#ff0000", t.getHexColor());
    }

    @Test
    void setTitle() {
        Tag t = new Tag("Important", "#ff0000");
        assertEquals("Important", t.getTitle());
        t.setTitle("Priority");
        assertEquals("Priority", t.getTitle());
    }

    @Test
    void setHexColor() {
        Tag t = new Tag("Important", "#ff0000");
        assertEquals("Important", t.getTitle());
        t.setHexColor("#00ff00");
        assertEquals("#00ff00", t.getHexColor());
    }

    @Test
    void testEquals() {
        Tag t = new Tag("Important", "#ff0000");
        Tag t2 = new Tag("Important", "#ff0000");
        assertEquals(t, t2);
        Tag t3 = new Tag("Priority", "#ff0000");
        Tag t4 = new Tag("Important", "#ffff00");
        assertNotEquals(t, t3);
        assertNotEquals(t, t4);
    }

    @Test
    void testHashCode() {
        Tag t = new Tag("Important", "#ff0000");
        Tag t2 = new Tag("Important", "#ff0000");
        assertEquals(t.hashCode(), t2.hashCode());
        Tag t3 = new Tag("Priority", "#ff0000");
        Tag t4 = new Tag("Important", "#ffff00");
        assertNotEquals(t.hashCode(), t3.hashCode());
        assertNotEquals(t.hashCode(), t4.hashCode());
    }
}