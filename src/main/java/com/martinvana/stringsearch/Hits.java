package com.martinvana.stringsearch;

import java.util.*;

/**
 * Search hit representation.
 */
public final class Hits {

    /**
     * A total number of hits.
     */
    private int size;

    /**
     * Map {@literal <pattern, set-of-positions>}.
     */
    private Map<String, SortedSet<Integer>> hits;

    /**
     * Init search hit representation.
     */
    Hits() {
        this.hits = new HashMap<>();
    }

    /**
     * Add pattern to results.
     * @param pattern A pattern found.
     * @param patternPosition A position of the first character in pattern which was found.
     */
    final void add(final String pattern, final int patternPosition) {
        SortedSet<Integer> positions = hits.getOrDefault(pattern, new TreeSet<>());
        positions.add(patternPosition);
        hits.put(pattern, positions);
        size++;
    }

    /**
     * @return {@code true} if no patterns were found, {@code false} otherwise.
     */
    public final boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return A total number of hits.
     */
    public final int size() {
        return size;
    }

    /**
     * @return Map {@literal <pattern, set-of-positions>}.
     */
    public final Map<String, SortedSet<Integer>> hits() {
        return hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hits hits1 = (Hits) o;
        return size == hits1.size
                && hits.equals(hits1.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, hits);
    }

    @Override
    public String toString() {
        return "Hits{"
                + "size=" + size
                + ", hits=" + hits
                + '}';
    }
}
