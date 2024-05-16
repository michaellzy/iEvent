package com.example.ievent;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.ievent.database.ordered_map.Iterator;
import com.example.ievent.database.ordered_map.OrderedEvent;

import java.util.LinkedList;

public class OrderedMapTest {
    private OrderedEvent<Integer, String> orderedEvent;

    @Before
    public void setUp() {
        orderedEvent = new OrderedEvent<>();
    }

    @Test
    public void testInsertion() {
        orderedEvent.insert(10, "Ten");
        orderedEvent.insert(20, "Twenty");
        orderedEvent.insert(5, "Five");

        Iterator it = orderedEvent.getIterator();
        assertTrue(it.hasNext());
        assertEquals("Five", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Ten", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Twenty", ((LinkedList<String>) it.next()).getFirst());
        assertFalse(it.hasNext());
    }

    @Test
    public void testEmptyTreeIteration() {
        Iterator it = orderedEvent.getIterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void testTreeBalance() {
        // Insert elements to potentially unbalance the tree
        orderedEvent.insert(30, "Thirty");
        orderedEvent.insert(20, "Twenty");
        orderedEvent.insert(40, "Forty");
        orderedEvent.insert(10, "Ten");
        orderedEvent.insert(5, "Five");

        Iterator it = orderedEvent.getIterator();
        assertNotEquals("Thirty", ((LinkedList<String>) it.next()).getFirst());
        assertNotEquals("Five", ((LinkedList<String>) it.next()).getFirst());
    }

    @Test
    public void testIterativeOrder() {
        orderedEvent.insert(15, "Fifteen");
        orderedEvent.insert(6, "Six");
        orderedEvent.insert(23, "Twenty-three");
        orderedEvent.insert(4, "Four");
        orderedEvent.insert(7, "Seven");

        Iterator it = orderedEvent.getIterator();
        assertEquals("Four", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Six", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Seven", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Fifteen", ((LinkedList<String>) it.next()).getFirst());
        assertEquals("Twenty-three", ((LinkedList<String>) it.next()).getFirst());
    }

    @Test
    public void testDuplicateKeys() {
        orderedEvent.insert(10, "Ten");
        orderedEvent.insert(10, "Ten Plus");

        Iterator it = orderedEvent.getIterator();
        assertTrue(it.hasNext());
        LinkedList<String> values = (LinkedList<String>) it.next();

        assertEquals(2, values.size());
        assertTrue(values.contains("Ten"));
        assertTrue(values.contains("Ten Plus"));
        assertFalse(it.hasNext());
    }

    @Test
    public void testIterativeWithDuplicate() {
        orderedEvent.insert(10, "Ten");
        orderedEvent.insert(20, "Twenty");
        orderedEvent.insert(10, "Ten Plus");
        orderedEvent.insert(5, "Five");
        orderedEvent.insert(20, "Twenty Plus");

        Iterator it = orderedEvent.getIterator();
        assertTrue(it.hasNext());
        assertEquals("Five", ((LinkedList<String>) it.next()).getFirst());

        LinkedList<String> tenValues = (LinkedList<String>) it.next();
        assertEquals(2, tenValues.size());
        assertTrue(tenValues.contains("Ten"));
        assertTrue(tenValues.contains("Ten Plus"));

        LinkedList<String> twentyValues = (LinkedList<String>) it.next();
        assertEquals(2, twentyValues.size()); // Check both values stored under key 20
        assertTrue(twentyValues.contains("Twenty"));
        assertTrue(twentyValues.contains("Twenty Plus"));

        assertFalse(it.hasNext()); // No more elements should be there
    }
}
