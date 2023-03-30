package commons;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void testConstructor() {
        Tag t = new Tag("Important", new ColorScheme(Color.black, Color.white));
        assertEquals("Important", t.getTitle());
        assertEquals(new ColorScheme(Color.black, Color.white), t.getColorScheme());
    }

    @Test
    void setTitle() {
        Tag t = new Tag("Important", new ColorScheme(Color.black, Color.white));
        assertEquals("Important", t.getTitle());
        t.setTitle("Priority");
        assertEquals("Priority", t.getTitle());
    }

    @Test
    void setHexColor() {
        Tag t = new Tag("Important", new ColorScheme(Color.black, Color.white));
        assertEquals("Important", t.getTitle());
        t.setColorScheme(new ColorScheme(Color.black, Color.red));
        assertEquals(new ColorScheme(Color.black, Color.red), t.getColorScheme());
    }

    @Test
    void testEquals() {
        Tag t = new Tag("Important", new ColorScheme(Color.black, Color.white));
        Tag t2 = new Tag("Important", new ColorScheme(Color.black, Color.white));
        assertEquals(t, t2);
        Tag t3 = new Tag("Priority", new ColorScheme(Color.black, Color.white));
        Tag t4 = new Tag("Important", new ColorScheme(Color.red, Color.white));
        assertNotEquals(t, t3);
        assertNotEquals(t, t4);
    }

    @Test
    void testHashCode() {
        Tag t = new Tag("Important", new ColorScheme(Color.black, Color.white));
        Tag t2 = new Tag("Important", new ColorScheme(Color.black, Color.white));
        assertEquals(t.hashCode(), t2.hashCode());
        Tag t3 = new Tag("Priority", new ColorScheme(Color.black, Color.white));
        Tag t4 = new Tag("Important", new ColorScheme(Color.red, Color.white));
        assertNotEquals(t.hashCode(), t3.hashCode());
        assertNotEquals(t.hashCode(), t4.hashCode());
    }
}