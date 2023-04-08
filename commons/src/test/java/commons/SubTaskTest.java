package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    SubTask subtask1, subtask2, subtask3, subtask4;

    @BeforeEach
    public void setup() {
        subtask1 = new SubTask("do something", false, 123L);
        subtask2 = new SubTask("do something", true, 123L);
        subtask3 = new SubTask("do nothing", false, 123L);
        subtask4 = new SubTask("do something", false, 123L);
    }

    @Test
    void testEquals() {
        assertEquals(subtask1, subtask4);
        assertNotEquals(subtask1, subtask2);
        assertNotEquals(subtask1, subtask3);
    }

    @Test
    void testHashCode() {
        assertEquals(subtask1.hashCode(), subtask4.hashCode());
    }
}