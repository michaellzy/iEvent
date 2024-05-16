package com.example.ievent.database.ordered_map;
import java.util.LinkedList;

/**
 * Binary Search Tree implementation.
 * @param <K> key
 * @param <V> value
 * @author Zhiyuan Lu
 * @author Tengkai Wang
 * adopt from comp2100: Lab 4 - AVL Trees, modified by Zhiyuan, Tengkai
 */
public class BinarySearchTree<K extends Comparable<K>, V> extends Tree<K, V> {
    public BinarySearchTree() {
        super();
    }

    public BinarySearchTree(Pair<K, V> value) {
        super(value);
        this.leftNode = new EmptyBST<>();
        this.rightNode = new EmptyBST<>();

    }
    public BinarySearchTree(K key, V value) {
        super(new Pair<>(key, new LinkedList<>()));
        this.value.getValue().add(value);
        this.leftNode = new EmptyBST<>();
        this.rightNode = new EmptyBST<>();
    }

    public BinarySearchTree(Pair<K, V> pair, Tree<K, V> leftNode, Tree<K, V> rightNode) {
        super(pair, leftNode, rightNode);
    }

    @Override
    public Pair<K, V> min() {
        return (leftNode instanceof EmptyTree) ? value : leftNode.min();
    }

    @Override
    public Pair<K, V> max() {
        return (rightNode instanceof EmptyTree) ? value : rightNode.max();
    }

    @Override
    public Tree<K, V> find(K key) {
        // Ensure input is not null.
        if (key == null) throw new IllegalArgumentException("Input cannot be null");

        int cmp = key.compareTo(value.getKey());
        if (cmp == 0) {
            return this;
        } else if (cmp < 0) {
            return leftNode.find(key);
        } else {
            return rightNode.find(key);
        }
    }

    @Override
    public BinarySearchTree<K, V> insert(K key, V value) {
        // Ensure input is not null.
        if (key == null) throw new IllegalArgumentException("Input cannot be null");

        int cmp = key.compareTo(this.value.getKey());
        if (cmp < 0) {
            if (this.leftNode == null) {
                this.leftNode = new BinarySearchTree<>();
            }
            this.leftNode = this.leftNode.insert(key, value);
        } else if (cmp > 0) {
            if (this.rightNode == null) {
                this.rightNode = new BinarySearchTree<>();
            }
            this.rightNode = this.rightNode.insert(key, value);
        } else {
            this.value.getValue().add(value);
        }
        return this;

    }


    @Override
    public Tree<K, V> delete(K key) {
        return this; // Placeholder
    }

    /**
     * EmptyBST class modified for handling Pair<K, List<V>>.
     */
    public static class EmptyBST<K extends Comparable<K>, V> extends EmptyTree<K, V> {

        @Override
        public Tree<K, V> insert(K element, V value) {
            return new BinarySearchTree<>(element, value);
        }

        @Override
        public Tree<K, V> delete(K key) {
            return null;
        }
    }
}

