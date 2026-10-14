import org.junit.jupiter.api.Test;
import java.util.EmptyStackException;
import utilities.FullStackException;
import static org.junit.jupiter.api.Assertions.*;

/** Tests the public behavior of MyStack. */
class MyStackTest {
    @Test
    void testDefaultConstructor() {
        MyStack stack = new MyStack();
        assertEquals(10, stack.capacity());
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testCustomConstructor() {
        for (int capacity : new int[] {1, 3, 17}) {
            MyStack stack = new MyStack(capacity);
            assertEquals(capacity, stack.capacity());
            assertEquals(0, stack.size());
            assertTrue(stack.isEmpty());
        }
        assertThrows(IllegalArgumentException.class, () -> new MyStack(0));
        assertThrows(IllegalArgumentException.class, () -> new MyStack(-1));
        assertThrows(IllegalArgumentException.class,
                () -> new MyStack(Integer.MIN_VALUE));
    }

    @Test
    void testIsEmpty() {
        MyStack stack = new MyStack();
        assertTrue(stack.isEmpty());
        stack.push("A");
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testClear() {
        MyStack stack = new MyStack(2);
        stack.clear();
        assertTrue(stack.isEmpty());
        stack.multiPush(new String[] {"A", "B"});
        stack.clear();
        assertEquals(0, stack.size());
        assertEquals(2, stack.capacity());
        assertThrows(EmptyStackException.class, stack::peek);
        assertThrows(EmptyStackException.class, stack::pop);
        stack.multiPush(new String[] {"C", "D"});
        assertArrayEquals(new String[] {"D", "C"}, stack.multiPop(2));
        stack.push("E");
        stack.clear();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testSize() {
        MyStack stack = new MyStack();
        assertEquals(0, stack.size());
        stack.push("A");
        assertEquals(1, stack.size());
        stack.push("B");
        assertEquals(2, stack.size());
        stack.pop();
        assertEquals(1, stack.size());
    }

    @Test
    void testCapacity() {
        MyStack stack = new MyStack(2);
        assertEquals(2, stack.capacity());
        stack.multiPush(new String[] {"A", "B"});
        assertEquals(2, stack.capacity());
        stack.clear();
        assertEquals(2, stack.capacity());
    }

    @Test
    void testPeek() {
        MyStack stack = new MyStack();
        assertThrows(EmptyStackException.class, stack::peek);
        stack.push("A");
        assertEquals("A", stack.peek());
        assertEquals("A", stack.peek());
        assertEquals(1, stack.size());
        stack.push("B");
        assertEquals("B", stack.peek());
        stack.pop();
        assertEquals("A", stack.peek());
    }

    @Test
    void testPush() {
        MyStack stack = new MyStack(2);
        assertThrows(IllegalArgumentException.class, () -> stack.push(null));
        assertEquals(0, stack.size());
        stack.push("");
        assertEquals("", stack.peek());
        stack.push("A");
        assertEquals("A", stack.peek());
        FullStackException error = assertThrows(FullStackException.class,
                () -> stack.push("B"));
        assertEquals("Your stack is full.", error.getMessage());
        assertEquals(2, stack.size());
        assertEquals("A", stack.pop());
        stack.push("C");
        assertEquals("C", stack.peek());
    }

    @Test
    void testPop() {
        MyStack stack = new MyStack();
        assertThrows(EmptyStackException.class, stack::pop);
        stack.multiPush(new String[] {"A", "B", "C"});
        assertEquals("C", stack.pop());
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
        assertThrows(EmptyStackException.class, stack::pop);
        assertEquals(0, stack.size());
    }

    @Test
    void testMultiPush() {
        MyStack stack = new MyStack(3);
        assertThrows(IllegalArgumentException.class, () -> stack.multiPush(null));
        stack.multiPush(new String[0]);
        assertEquals(0, stack.size());
        stack.multiPush(new String[] {"A", "B"});
        assertEquals("B", stack.peek());
        FullStackException error = assertThrows(FullStackException.class,
                () -> stack.multiPush(new String[] {"C", "D"}));
        assertEquals("Your stack is full.", error.getMessage());
        assertArrayEquals(new String[] {"C", "B", "A"}, stack.multiPop(3));
        stack.multiPush(new String[] {"X", "Y", "Z"});
        assertEquals(3, stack.size());
        stack.multiPush(new String[0]);
        assertEquals("Z", stack.peek());
        assertThrows(FullStackException.class,
                () -> stack.multiPush(new String[] {"extra"}));
    }

    @Test
    void testMultiPushNullMember() {
        MyStack stack = new MyStack();
        assertThrows(IllegalArgumentException.class,
                () -> stack.multiPush(new String[] {"A", null, "B"}));
        assertEquals(1, stack.size());
        assertEquals("A", stack.peek());
    }

    @Test
    void testMultiPop() {
        MyStack stack = new MyStack();
        assertThrows(IllegalArgumentException.class, () -> stack.multiPop(0));
        assertThrows(IllegalArgumentException.class, () -> stack.multiPop(-1));
        assertArrayEquals(new String[0], stack.multiPop(2));
        stack.multiPush(new String[] {"A", "B", "C", "D"});
        assertArrayEquals(new String[] {"D", "C"}, stack.multiPop(2));
        assertEquals("B", stack.peek());
        assertArrayEquals(new String[] {"B", "A"},
                stack.multiPop(Integer.MAX_VALUE));
        assertTrue(stack.isEmpty());
        stack.multiPush(new String[] {"X", "Y"});
        assertArrayEquals(new String[] {"Y", "X"}, stack.multiPop(2));
    }
}
