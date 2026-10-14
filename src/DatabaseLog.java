/*
 * Name: Rachit Jain
 * SID: 200344721
 */

/**
 * Stores and parses database activity logs using MyStack.
 *
 * @author Rachit Jain
 * @since 2026-10-12
 */
public class DatabaseLog {
    private static final String FIELD_SEPARATOR = " - ";
    private static final int ID_INDEX = 0;
    private static final int ACTION_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int TIMESTAMP_INDEX = 3;

    private MyStack log;

    /**
     * Creates an empty database log with the specified capacity.
     * @param capacity maximum number of log entries
     * @throws IllegalArgumentException if capacity is less than one
     */
    public DatabaseLog(int capacity) {
        log = new MyStack(capacity);
    }

    /** Creates an empty database log with the default stack capacity. */
    public DatabaseLog() {
        log = new MyStack();
    }

    /**
     * Adds a log as the most recent entry.
     * @param logEntry formatted entry to store
     * @throws IllegalArgumentException if logEntry is null
     * @throws utilities.FullStackException if the stack is full
     */
    public void addLog(String logEntry) {
        log.push(logEntry);
    }

    /**
     * @return the most recent entry without removing it
     * @throws java.util.EmptyStackException if no entries exist
     */
    public String viewLog() {
        return log.peek();
    }

    /**
     * @return the removed most recent entry
     * @throws java.util.EmptyStackException if no entries exist
     */
    public String removeLog() {
        return log.pop();
    }

    /**
     * @param logEntry correctly formatted entry
     * @return the log ID
     */
    public String getLogID(String logEntry) {
        return logEntry.split(FIELD_SEPARATOR)[ID_INDEX];
    }

    /**
     * @param logEntry correctly formatted entry
     * @return the action
     */
    public String getAction(String logEntry) {
        return logEntry.split(FIELD_SEPARATOR)[ACTION_INDEX];
    }

    /**
     * @param logEntry correctly formatted entry
     * @return the name
     */
    public String getName(String logEntry) {
        return logEntry.split(FIELD_SEPARATOR)[NAME_INDEX];
    }

    /**
     * @param logEntry correctly formatted entry
     * @return month, date, year, hour, and minute, in that order
     */
    public String[] getTimestamp(String logEntry) {
        String timestamp = logEntry.split(FIELD_SEPARATOR)[TIMESTAMP_INDEX];
        return timestamp.split("[-_:]");
    }

    /**
     * @param logEntry correctly formatted entry
     * @return a four-line summary with ID, ACTION, NAME, and TIMESTAMP labels
     */
    public String summarizeLog(String logEntry) {
        String[] fields = logEntry.split(FIELD_SEPARATOR);
        return "ID: " + fields[ID_INDEX]
                + "\nACTION: " + fields[ACTION_INDEX]
                + "\nNAME: " + fields[NAME_INDEX]
                + "\nTIMESTAMP: " + fields[TIMESTAMP_INDEX];
    }

    /**
     * Removes up to count entries, newest first.
     * @param count positive requested number of entries
     * @return the removed entries
     * @throws IllegalArgumentException if count is not positive
     */
    public String[] getRecentLogs(int count) {
        return log.multiPop(count);
    }

    /**
     * Finds entries for an exact name in newest-first order.
     * This operation consumes the stored logs, as permitted by the assignment.
     * @param name name to match
     * @return all matching entries
     */
    public String[] getLogsForStudent(String name) {
        String[] matches = new String[log.size()];
        int matchCount = 0;
        while (!log.isEmpty()) {
            String entry = log.pop();
            if (getName(entry).equals(name)) {
                matches[matchCount] = entry;
                matchCount++;
            }
        }
        String[] result = new String[matchCount];
        for (int i = 0; i < matchCount; i++) {
            result[i] = matches[i];
        }
        return result;
    }
}
