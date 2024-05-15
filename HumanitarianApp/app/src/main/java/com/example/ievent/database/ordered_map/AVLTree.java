package com.example.ievent.database.ordered_map;
import java.util.LinkedList;

/**
 * AVL Tree implementation.
 * @param <K> key
 * @param <V> value
 * @author Zhiyuan Lu
 * @author Tengkai Wang
 */
public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {

    public AVLTree(K key, V value) {
        super(new Pair<>(key, new LinkedList<>()));
        super.value.getValue().add(value);
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(Pair<K, V> pair, Tree<K, V> leftNode, Tree<K, V> rightNode) {
        super(pair, leftNode, rightNode);
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        return leftNode.getHeight() - rightNode.getHeight();
    }

    @Override
    public AVLTree<K, V> insert(K key, V value) {

        // Ensure input is not null.
        if (key == null)
            throw new IllegalArgumentException("Input cannot be null");
        int cmp = key.compareTo(this.value.getKey());
        AVLTree<K, V> newNode;
        if (cmp > 0) {
            // COMPLETE
            newNode = new AVLTree<>(this.value, leftNode, rightNode.insert(key, value));
        } else if (cmp < 0) {
            // COMPLETE
            newNode = new AVLTree<>(this.value, leftNode.insert(key, value), rightNode);
        } else {
            // COMPLETE
            this.value.getValue().add(value);
            return this;
        }

        if (newNode.getBalanceFactor() > 1) {
            // left heavy
            if (key.compareTo(newNode.leftNode.value.getKey()) < 0) { // LL case
                return newNode.rightRotate();
            } else {
                if (newNode.leftNode instanceof AVLTree) { // LR case
                    AVLTree<K, V> temp = ((AVLTree<K, V>) newNode.leftNode).leftRotate();
                    newNode.leftNode = temp;
                }
                return newNode.rightRotate();
            }
        }

        if (newNode.getBalanceFactor() < -1) {
            // right heavy
            if (key.compareTo(newNode.rightNode.value.getKey()) > 0) {
                // RR rotation
                return newNode.leftRotate();
            } else {
                // RL rotation
                if (newNode.rightNode instanceof AVLTree) {
                    AVLTree<K, V> temp = ((AVLTree<K, V>) newNode.rightNode).rightRotate();
                    newNode.rightNode = temp;
                }
                return newNode.leftRotate();
            }
        }

        return newNode; // Change to return something different
    }

    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<K, V> leftRotate() {

        Tree<K, V> newParent = this.rightNode;
        Tree<K, V> newRightOfCurrent = newParent.leftNode;
        // COMPLETE
        this.rightNode = newRightOfCurrent;
        newParent.leftNode = this;

        return (AVLTree<K, V>) newParent; // Change to return something different
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<K, V> rightRotate() {

        Tree<K, V> newParent = this.leftNode;
        Tree<K, V> newLeftOfCurrent = newParent.rightNode;

        this.leftNode = newLeftOfCurrent;
        newParent.rightNode = this;

        return (AVLTree<K, V>) newParent; // Change to return something different
    }

    /**
     * Note that this is not within a file of its own... WHY?
     * The answer is: this is just a design decision. 'insert' here will return something specific
     * to the parent class inheriting Tree from BinarySearchTree. In this case an AVL tree.
     */
    public static class EmptyAVL<K extends Comparable<K>, V> extends EmptyTree<K, V> {

        @Override
        public Tree<K, V> insert(K element, V value) {
            return new AVLTree<>(element, value);
        }

        @Override
        public Tree<K, V> delete(K key) {
            return null;
        }
    }
}
