package com.martinvana.stringsearch;

import java.util.HashMap;
import java.util.Map;

/**
 * A node in a trie.
 */
class Node {

    /**
     * Value of the node. {@code null} if root (empty string).
     */
    private Character c;

    /**
     * Pointers to following characters.
     */
    private Map<Character, Node> children;

    /**
     * Parent node. {@code null} if root.
     */
    private Node parent;

    /**
     * Suffix link (aka failure link). {@code null} if root.
     *
     * By following a suffix link we want to preserve as much context as possible.
     * It points to the longest suffix which is still in the trie.
     */
    private Node suffixLink;

    /**
     * {@code true} if it is the end of the pattern, {@code false} otherwise.
     */
    private boolean output;

    /**
     * Node corresponding to the longest proper suffix of w that is pattern or {@code null} if no such suffix exists.
     */
    private Node outputLink;

    /**
     *
     */
    Node() {
        children = new HashMap<>();
    }

    boolean isRoot() {
        return c == null;
    }

    Character getC() {
        return c;
    }

    void setC(Character c) {
        this.c = c;
    }

    Map<Character, Node> getChildren() {
        return children;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    Node getSuffixLink() {
        return suffixLink;
    }

    void setSuffixLink(Node suffixLink) {
        this.suffixLink = suffixLink;
    }

    boolean isOutput() {
        return output;
    }

    void setOutput(boolean output) {
        this.output = output;
    }

    Node getOutputLink() {
        return outputLink;
    }

    void setOutputLink(Node outputLink) {
        this.outputLink = outputLink;
    }
}
