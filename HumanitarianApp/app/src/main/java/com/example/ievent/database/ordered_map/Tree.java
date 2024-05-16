package com.example.ievent.database.ordered_map;

/**
 * Abstract class for a tree.
 * @param <K> key
 * @param <V> value
 * @author Zhiyuan Lu
 * @author Tengkai Wang
 * adopt from comp2100: Lab 4 - AVL Trees, modified by Zhiyuan, Tengkai
 */
public abstract class Tree<K extends Comparable<K>, V> {
    /**
     * Here we store our class fields.
     */
    public final Pair<K, V> value;       // element stored in this node of the tree.
    public Tree<K, V> leftNode;    // less than the node.
    public Tree<K, V> rightNode;   // greater than the node.

    /**
     * Constructor to allow for empty trees
     */
    public Tree() {
        value = null;
    }

    /**
     * Constructor for creating a new child node.
     * Note that the left and right nodes must be set by the subclass.
     *
     * @param value to set as this node's value.
     */
    public Tree(Pair<K, V> value) {
        // Ensure input is not null.
        if (value == null)
            throw new IllegalArgumentException("Input cannot be null");

        this.value = value;
    }

    /**
     * Constructor for creating a new node.
     * Note that this is what allows our implementation to be immutable.
     *
     * @param value     to set as this node's value.
     * @param leftNode  left child of current node.
     * @param rightNode right child of current node.
     */
    public Tree(Pair<K, V> value, Tree<K, V> leftNode, Tree<K, V> rightNode) {
        // Ensure inputs are not null.
        if (value == null || leftNode == null || rightNode == null)
            throw new IllegalArgumentException("Inputs cannot be null");

        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public abstract Pair<K, V> min();                     // Finds the minimum.

    public abstract Pair<K, V> max();                     // Finds the maximum.

    public abstract Tree<K, V> find(K key);     // Finds the element and returns the node.

    public abstract Tree<K, V> insert(K key, V value);   // Inserts the element and returns a new instance of itself with the new node.

    public abstract Tree<K, V> delete(K key);   // Deletes the element and returns a new instance of itself without the node.

    /**
     * Height of current node.
     *
     * @return The maximum height of either children.
     */
    public int getHeight() {
        // Check whether leftNode or rightNode are EmptyTree
        int leftNodeHeight = leftNode instanceof EmptyTree ? 0 : 1 + leftNode.getHeight();
        int rightNodeHeight = rightNode instanceof EmptyTree ? 0 : 1 + rightNode.getHeight();
        return Math.max(leftNodeHeight, rightNodeHeight);
    }

    @Override
    public String toString() {
        return "{" +
                "value=" + value +
                ", leftNode=" + leftNode +
                ", rightNode=" + rightNode +
                '}';
    }
}
