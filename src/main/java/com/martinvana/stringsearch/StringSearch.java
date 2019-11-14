package com.martinvana.stringsearch;

import java.util.*;
import java.util.stream.Stream;

/**
 * Aho–Corasick string-searching algorithm.
 *
 * @see <a href="http://web.stanford.edu/class/archive/cs/cs166/cs166.1166/lectures/02/Slides02.pdf">CS166: Lecture 2 - Aho-Corasick Automata</a>
 */
public final class StringSearch {

    /**
     * A root node of trie.
     */
    private Node root;

    /**
     * Init string searching algorithm with a dictionary of patterns, which will be searched for in text(s).
     * @param pattern A pattern which will be searched for in a text.
     */
    public StringSearch(String... pattern) {
        constructTrie(pattern);
        fillInSuffixAndOutputLinks();
    }

    /**
     * O(n) trie construction.
     * @param pattern A pattern which will be searched in a text.
     */
    private void constructTrie(String... pattern) {
        root = new Node();

        // For each pattern
        for (String p : pattern) {
            Node parent = root;

            // Iterate over its characters
            for (int i = 0; i < p.length(); i++) {
                char c = p.charAt(i);
                Node child = parent.getChildren().get(c);

                // Create node if not exists
                if (child == null) {
                    child = new Node();
                    child.setC(c);
                    child.setOutput(i + 1 == p.length());
                    child.setParent(parent);

                    parent.getChildren().put(c, child);
                }

                // Move to child node
                parent = child;
            }
        }
    }

    /**
     * O(n) suffix link construction. (p. 389)
     * O(n) output link construction. (p. 440)
     */
    private void fillInSuffixAndOutputLinks() {
        // Breadth-first search of the trie
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node u = queue.remove();
            // The root has no suffix link
            if (!u.isRoot()) {
                if (u.getParent().isRoot()) {
                    // The node one hop away from root has suffix link pointing to the root
                    u.setSuffixLink(root);
                } else {
                    // The node corresponds to some string wa
                    // Try to find xa. If not set root.
                    u.setSuffixLink(root); // Default suffix link if nothing is found
                    Node w = u.getParent(); // w
                    char c = u.getC(); // a

                    Node x = w.getSuffixLink(); // x

                    while (x != null) {
                        // Found xa
                        if (x.getChildren().containsKey(c)) {
                            Node xa = x.getChildren().get(c);
                            u.setSuffixLink(xa);

                            // Fill in output links
                            if (xa.isOutput()) {
                                // It is a pattern
                                u.setOutputLink(xa);
                            } else {
                                // Copy output link
                                u.setOutputLink(xa.getOutputLink());
                            }
                            break;
                        } else {
                            // Follow already built suffix links
                            x = x.getSuffixLink();
                        }
                    }
                }
            }

            // Add all u's children to a queue
            queue.addAll(u.getChildren().values());
        }
    }

    /**
     * @param texts A stream of texts in which to search for patterns.
     * @return A stream of search results for each text.
     */
    public final Stream<Hits> search(Stream<String> texts) {
        return texts.map(this::search);
    }

    /**
     * @param text A text in which to search for patterns.
     * @return Search results for the text.
     */
    public final Hits search(String text) {
        Hits hits = new Hits();

        if (text == null) {
            return hits;
        }

        // Start at the root node (empty string match)
        Node node = root;

        // Iterate over its characters
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // While there is no edge labeled c
            while (!node.getChildren().containsKey(c)) {
                if (node.isRoot()) {
                    // Do nothing
                    break;
                } else {
                    // Follow a suffix link
                    node = node.getSuffixLink();
                }
            }

            // If edge exists
            if (node.getChildren().containsKey(c)) {

                // Follow edge labeled c
                node = node.getChildren().get(c);

                // If current node corresponds to a pattern
                if (node.isOutput()) {
                    // Output node
                    addHit(hits, i, node);
                }

                // Find overlapping patterns
                if (node.getOutputLink() != null) {
                    Node outputNode = node.getOutputLink();
                    while (outputNode != null) {
                        addHit(hits, i, outputNode);
                        outputNode = outputNode.getOutputLink();
                    }
                }
            }
        }

        return hits;
    }

    /**
     * Add pattern to results.
     * @param endOfWordPosition A position of last character in pattern which was found.
     * @param endNode The last node of a pattern in trie.
     */
    private void addHit(Hits hits, int endOfWordPosition, Node endNode) {
        StringBuilder sb = new StringBuilder();

        Node node = endNode;

        // Materialize pattern found
        while (!node.isRoot()) {
            sb.insert(0, node.getC());
            node = node.getParent();
        }


        hits.add(sb.toString(), endOfWordPosition - sb.length() + 1);
    }
}
