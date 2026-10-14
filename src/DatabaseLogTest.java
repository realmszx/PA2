import org.junit.jupiter.api.Test;
import java.util.EmptyStackException;
import utilities.FullStackException;
import static org.junit.jupiter.api.Assertions.*;

/** Tests storage, parsing, and filtering through DatabaseLog's public API. */
class DatabaseLogTest {
    private static final String FIRST =
            "1 - LOGIN - Marina - 09-28-26_11:00";
    private static final String SECOND =
            "2 - EDIT - Walter - 09-28-26_11:30";
    private static final String THIRD =
            "3 - LOGOUT - Marina - 09-28-26_12:00";

    private DatabaseLog populated() {
        DatabaseLog database = new DatabaseLog();
        database.addLog(FIRST);
        database.addLog(SECOND);
        database.addLog(THIRD);
        return database;
    }

    @Test
    void testDefaultConstructor() {
        DatabaseLog database = new DatabaseLog();
        assertThrows(EmptyStackException.class, database::viewLog);
        assertThrows(EmptyStackException.class, database::removeLog);
        for (int i = 0; i < 10; i++) {
            database.addLog(FIRST);
        }
        assertThrows(FullStackException.class, () -> database.addLog(SECOND));
        assertEquals(10, database.getRecentLogs(20).length);
    }

    @Test
    void testCustomConstructor() {
        for (int capacity : new int[] {1, 3, 12}) {
            DatabaseLog database = new DatabaseLog(capacity);
            assertThrows(EmptyStackException.class, database::viewLog);
            for (int i = 0; i < capacity; i++) {
                database.addLog(FIRST);
            }
            assertThrows(FullStackException.class, () -> database.addLog(SECOND));
            assertEquals(capacity, database.getRecentLogs(capacity + 1).length);
        }
        assertThrows(IllegalArgumentException.class, () -> new DatabaseLog(0));
        assertThrows(IllegalArgumentException.class, () -> new DatabaseLog(-1));
    }

    @Test
    void testAddLog() {
        DatabaseLog database = new DatabaseLog(3);
        assertThrows(IllegalArgumentException.class, () -> database.addLog(null));
        database.addLog(FIRST);
        assertEquals(FIRST, database.viewLog());
        database.addLog(SECOND);
        assertEquals(SECOND, database.viewLog());
        database.addLog(THIRD);
        assertEquals(THIRD, database.viewLog());
        FullStackException error = assertThrows(FullStackException.class,
                () -> database.addLog(FIRST));
        assertEquals("Your stack is full.", error.getMessage());
        assertEquals(THIRD, database.viewLog());
    }

    @Test
    void testViewLog() {
        DatabaseLog database = populated();
        assertEquals(THIRD, database.viewLog());
        assertEquals(THIRD, database.viewLog());
        database.removeLog();
        assertEquals(SECOND, database.viewLog());
        database.removeLog();
        assertEquals(FIRST, database.viewLog());
        database.removeLog();
        assertThrows(EmptyStackException.class, database::viewLog);
    }

    @Test
    void testRemoveLog() {
        DatabaseLog database = populated();
        assertEquals(THIRD, database.removeLog());
        assertEquals(SECOND, database.removeLog());
        assertEquals(FIRST, database.removeLog());
        assertThrows(EmptyStackException.class, database::removeLog);
        database.addLog(SECOND);
        assertEquals(SECOND, database.removeLog());
    }

    @Test
    void testGetLogID() {
        DatabaseLog database = new DatabaseLog();
        assertEquals("1", database.getLogID(FIRST));
        assertEquals("2", database.getLogID(SECOND));
        assertEquals("3", database.getLogID(THIRD));
        assertEquals("100", database.getLogID(
                "100 - EDIT - A - 01-01-27_00:00"));
    }

    @Test
    void testGetAction() {
        DatabaseLog database = new DatabaseLog();
        assertEquals("LOGIN", database.getAction(FIRST));
        assertEquals("EDIT", database.getAction(SECOND));
        assertEquals("LOGOUT", database.getAction(THIRD));
    }

    @Test
    void testGetName() {
        DatabaseLog database = new DatabaseLog();
        assertEquals("Marina", database.getName(FIRST));
        assertEquals("Walter", database.getName(SECOND));
        assertEquals("Marina", database.getName(THIRD));
        assertEquals("Mary Ann", database.getName(
                "4 - LOGIN - Mary Ann - 01-01-27_00:00"));
    }

    @Test
    void testGetTimestamp() {
        DatabaseLog database = new DatabaseLog();
        assertArrayEquals(new String[] {"09", "28", "26", "11", "00"},
                database.getTimestamp(FIRST));
        assertArrayEquals(new String[] {"09", "28", "26", "11", "30"},
                database.getTimestamp(SECOND));
        assertArrayEquals(new String[] {"09", "28", "26", "12", "00"},
                database.getTimestamp(THIRD));
        assertArrayEquals(new String[] {"01", "02", "27", "00", "05"},
                database.getTimestamp("4 - EDIT - A - 01-02-27_00:05"));
    }

    @Test
    void testSummarizeLog() {
        DatabaseLog database = new DatabaseLog();
        assertEquals("ID: 1\nACTION: LOGIN\nNAME: Marina"
                + "\nTIMESTAMP: 09-28-26_11:00", database.summarizeLog(FIRST));
        assertEquals("ID: 2\nACTION: EDIT\nNAME: Walter"
                + "\nTIMESTAMP: 09-28-26_11:30", database.summarizeLog(SECOND));
        assertEquals("ID: 3\nACTION: LOGOUT\nNAME: Marina"
                + "\nTIMESTAMP: 09-28-26_12:00", database.summarizeLog(THIRD));
    }

    @Test
    void testGetRecentLogs() {
        DatabaseLog database = populated();
        assertThrows(IllegalArgumentException.class,
                () -> database.getRecentLogs(0));
        assertThrows(IllegalArgumentException.class,
                () -> database.getRecentLogs(-1));
        assertArrayEquals(new String[] {THIRD, SECOND},
                database.getRecentLogs(2));
        assertEquals(FIRST, database.viewLog());
        assertArrayEquals(new String[] {FIRST}, database.getRecentLogs(20));
        assertArrayEquals(new String[0], database.getRecentLogs(1));
        assertThrows(EmptyStackException.class, database::viewLog);
        assertArrayEquals(new String[] {THIRD, SECOND, FIRST},
                populated().getRecentLogs(3));
    }

    @Test
    void testGetLogsForStudent() {
        assertArrayEquals(new String[] {THIRD, FIRST},
                populated().getLogsForStudent("Marina"));
        assertArrayEquals(new String[] {SECOND},
                populated().getLogsForStudent("Walter"));
        assertArrayEquals(new String[0],
                populated().getLogsForStudent("Unknown"));
        assertArrayEquals(new String[0],
                new DatabaseLog().getLogsForStudent("Marina"));
        assertArrayEquals(new String[0],
                populated().getLogsForStudent("marina"));
        DatabaseLog database = new DatabaseLog();
        String partialName = "4 - EDIT - Marina2 - 09-29-26_10:00";
        database.addLog(FIRST);
        database.addLog(partialName);
        database.addLog(FIRST);
        assertArrayEquals(new String[] {FIRST, FIRST},
                database.getLogsForStudent("Marina"));
    }
}
