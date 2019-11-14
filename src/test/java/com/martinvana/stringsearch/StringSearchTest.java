package com.martinvana.stringsearch;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StringSearch} algorithm.
 */
class StringSearchTest {

    @Test
    void testSearch() {
        StringSearch ss = new StringSearch( "a", "aa", "aaa", "aaaa");

        Hits expectedSearchResults = new Hits();
        expectedSearchResults.add("a", 0);
        expectedSearchResults.add("a", 1);
        expectedSearchResults.add("a", 2);
        expectedSearchResults.add("a", 3);
        expectedSearchResults.add("aa", 0);
        expectedSearchResults.add("aa", 1);
        expectedSearchResults.add("aa", 2);
        expectedSearchResults.add("aaa", 0);
        expectedSearchResults.add("aaa", 1);
        expectedSearchResults.add("aaaa", 0);

        Hits searchResults = ss.search("aaaa");

        assertFalse(searchResults.isEmpty());
        assertEquals(10, searchResults.size());
        assertEquals(expectedSearchResults, searchResults);
    }


    @ParameterizedTest
    @MethodSource("provideSearchArguments")
    void testSearch(final String[] patterns, final String text, final Hits expectedResults) {
        StringSearch ss = new StringSearch(patterns);

        Hits searchResults = ss.search(text);

        assertNotNull(searchResults);
        assertFalse(searchResults.isEmpty());
        assertEquals(expectedResults, searchResults);
    }

    private static Stream<Arguments> provideSearchArguments() {
        return Stream.of(
                provideSearchArgument(new String[] {"a", "aa", "aaa", "aaaa"}, "aaaa"),
                provideSearchArgument(new String[] {"a", "aa", "aaa", "aaaa"}, "bc a cc"),
                provideSearchArgument(new String[] {"i", "in", "tin", "sting"}, "sting"),
                provideSearchArgument(new String[] {"call me"}, "call me darling on a number 123 456 789"),
                provideSearchArgument(new String[] {"123 456 789"}, "call me darling on a number 123 456 789")
        );
    }

    private static Arguments provideSearchArgument(final String[] patterns, final String text) {
        return Arguments.of(patterns, text, naiveStringSearch(patterns, text));
    }

    @ParameterizedTest
    @MethodSource("provideEmptySearchArguments")
    void testSearchEmpty(final String[] patterns, final String text) {
        StringSearch ss = new StringSearch(patterns);

        Hits searchResults = ss.search(text);

        assertTrue(searchResults.isEmpty());
        assertEquals(0, searchResults.size());
        assertTrue(searchResults.hits().isEmpty());
    }


    private static Stream<Arguments> provideEmptySearchArguments() {
        return Stream.of(
                Arguments.of(new String[] {"a", "aa", "aaa", "aaaa"}, "bbb"), // no hits
                Arguments.of(new String[] {"a", "aa", "aaa", "aaaa"}, null)   // null text
        );
    }

    @Test
    void testSearchStreamOfTexts() {
        String[] patterns = new String[]{"a", "aa", "aaa", "aaaa"};

        StringSearch ss = new StringSearch(patterns);

        String text1 = "aaaa";
        String text2 = "bc a cc";

        Stream<Hits> searchResultStream = ss.search(Stream.of(text1, text2));

        Stream<Hits> expectedSearchResultStream = Stream.of(
                naiveStringSearch(patterns, text1),
                naiveStringSearch(patterns, text2)
        );

        assertNotNull(searchResultStream);
        assertEquals(expectedSearchResultStream.collect(Collectors.toList()), searchResultStream.collect(Collectors.toList()));
    }

    /**
     * Naive search algorithm.
     * @param patterns An array of pattern which will be searched for in a text.
     * @param text A text in which to search for patterns
     * @return Search results for the text.
     */
    private static Hits naiveStringSearch(final String[] patterns, final String text) {
        Hits hits = new Hits();

        // For each position in text
        for (int i = 0; i < text.length(); i++) {
            // For each pattern
            nextPattern:
            for (String pattern : patterns) {
                // Check if pattern starts at position i
                for (int j = 0; j < pattern.length(); j++) {
                    // If pattern is not matched
                    if (i + j >= text.length() || text.charAt(i + j) != pattern.charAt(j)) {
                        // Go to next pattern
                        continue nextPattern;
                    }
                }

                // Pattern found
                hits.add(pattern, i);
            }
        }

        return hits;
    }
}