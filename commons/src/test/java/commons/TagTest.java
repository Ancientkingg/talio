package commons;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void testConstructor() {
        Tag t = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        assertEquals("Important", t.getTitle());
        assertEquals(new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)), t.getColorScheme());
    }

    @Test
    void setTitle() {
        Tag t = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        assertEquals("Important", t.getTitle());
        t.setTitle("Priority");
        assertEquals("Priority", t.getTitle());
    }

    @Test
    void setHexColor() {
        Tag t = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        assertEquals("Important", t.getTitle());
        t.setColorScheme(new ColorScheme(new Color(0,0,0,255), new Color(255,0,0,255)));
        assertEquals(new ColorScheme(new Color(0,0,0,255), new Color(255,0,0,255)), t.getColorScheme());
    }

    @Test
    void testEquals() {
        Tag t = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        Tag t2 = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        assertEquals(t, t2);
        Tag t3 = new Tag("Priority", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        Tag t4 = new Tag("Important", new ColorScheme(new Color(255,0,0,255), new Color(255,255,255,255)));
        assertNotEquals(t, t3);
        assertNotEquals(t, t4);
    }

    @Test
    void testHashCode() {
        Tag t = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        Tag t2 = new Tag("Important", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        assertEquals(t.hashCode(), t2.hashCode());
        Tag t3 = new Tag("Priority", new ColorScheme(new Color(0,0,0,255), new Color(255,255,255,255)));
        Tag t4 = new Tag("Important", new ColorScheme(new Color(255,0,0,255), new Color(255,255,255,255)));
        assertNotEquals(t.hashCode(), t3.hashCode());
        assertNotEquals(t.hashCode(), t4.hashCode());
    }
}