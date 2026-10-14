/*
 * Name: Rachit Jain
 * PID: 200344721
 */

import java.util.EmptyStackException;
import utilities.FullStackException;

/**
 * A fixed-capacity stack of non-null strings, stored in LIFO order.
 *
 * @author Rachit Jain
 * @since 2026-10-12
 */
public class MyStack {
    private static final int DEFAULT_CAPACITY = 10;
    private static final String FULL_MESSAGE = "Your stack is full.";

    private String[] data;
    private int size;

    /**
     * Creates an empty stack with the specified capacity.
     * @param capacity maximum number of elements
     * @throws IllegalArgumentException if capacity is less than one
     */
    public MyStack(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        data = new String[capacity];
        size = 0;
    }

    /** Creates an empty stack with capacity ten. */
    public MyStack() {
        this(DEFAULT_CAPACITY);
    }

    /** @return whether the stack contains no elements */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all logical elements in constant time. Old array entries are
     * inaccessible and are overwritten by subsequent pushes.
     */
    public void clear() {
        size = 0;
    }

    /** @return the number of elements currently stored */
    public int size() {
        return size;
    }

    /** @return the maximum number of elements that can be stored */
    public int capacity() {
        return data.length;
    }

    /**
     * Returns the most recently pushed element without removing it.
     * @return the top element
     * @throws EmptyStackException if the stack is empty
     */
    public String peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return data[size - 1];
    }

    /**
     * Adds an element to the top of the stack.
     * @param element string to add
     * @throws IllegalArgumentException if element is null
     * @throws FullStackException if the stack is full
     */
    public void push(String element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        if (size == capacity()) {
            throw new FullStackException(FULL_MESSAGE);
        }
        data[size] = element;
        size++;
    }

    /**
     * Removes and returns the most recently pushed element.
     * @return the removed top element
     * @throws EmptyStackException if the stack is empty
     */
    public String pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        size--;
        String element = data[size];
        data[size] = null;
        return element;
    }

    /**
     * Pushes elements in array order, retaining successful pushes on failure.
     * @param elements strings to add, starting at index zero
     * @throws IllegalArgumentException if the array or an element is null
     * @throws FullStackException if an element cannot fit
     */
    public void multiPush(String[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements cannot be null.");
        }
        for (String element : elements) {
            push(element);
        }
    }

    /**
     * Removes up to amount elements, returning them in removal order.
     * @param amount requested number of elements to remove
     * @return removed elements, most recent first
     * @throws IllegalArgumentException if amount is not positive
     */
    public String[] multiPop(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        int count = Math.min(amount, size);
        String[] removed = new String[count];
        for (int i = 0; i < count; i++) {
            removed[i] = pop();
        }
        return removed;
    }
}
