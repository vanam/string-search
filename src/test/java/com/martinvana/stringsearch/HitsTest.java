package com.martinvana.stringsearch;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests {@link Hits} class.
 */
class HitsTest {

    @Test
    void testEquals() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 1);

        Hits h2 = new Hits();
        h2.add("a", 1);
        h2.add("a", 0);

        assertEquals(h1, h2);
        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void testEquals2() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 1);

        assertEquals(h1, h1);
        assertEquals(h1.hashCode(), h1.hashCode());
    }

    @Test
    void testNotEquals() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("b", 1);

        Hits h2 = new Hits();
        h2.add("a", 1);
        h2.add("a", 0);

        assertNotEquals(h1, h2);
        assertNotEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void testNotEquals2() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 2);

        Hits h2 = new Hits();
        h2.add("a", 1);
        h2.add("a", 0);

        assertNotEquals(h1, h2);
        assertNotEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void testNotEquals3() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 2);

        assertNotEquals(h1, 1);
        assertNotEquals(h1.hashCode(), Integer.valueOf(1).hashCode());
    }

    @Test
    void testNotEquals4() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 2);

        assertNotEquals(h1, null);
    }

    @Test
    void testNotEquals5() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 1);
        h1.add("a", 2);

        Hits h2 = new Hits();
        h2.add("a", 1);
        h2.add("a", 0);

        assertNotEquals(h1, h2);
        assertNotEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void testHashCode() {
        Hits h1 = new Hits();
        h1.add("a", 0);
        h1.add("a", 1);

        Hits h2 = new Hits();
        h2.add("a", 1);
        h2.add("a", 0);

        assertEquals(h1.hashCode(), h2.hashCode());
    }

}
