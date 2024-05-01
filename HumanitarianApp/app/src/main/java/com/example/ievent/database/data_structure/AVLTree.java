package com.example.ievent.database.data_structure;

/**
 * An AVL tree is actually an extension of a Binary Search Tree
 * with self balancing properties. Hence, our AVL trees will 'extend'
 * this Binary Search tree data structure.
 */
public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    /*
        As a result of inheritance by using 'extends BinarySearchTree<T>,
        all class fields within BinarySearchTree are also present here.
        So while not explicitly written here, this class has:
            - value
            - leftNode
            - rightNode
     */

    public AVLTree(T value) {
        super(value);
        // Set left and right children to be of EmptyAVL as opposed to EmptyBST.
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(value, leftNode, rightNode);
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        /*
             Note:
             Calculating the balance factor and height each time they are needed is less efficient than
             simply storing the height and balance factor as fields within each tree node (as some
             implementations of the AVLTree do). However, although it is inefficient, it is easier to implement.
         */
        return leftNode.getHeight() - rightNode.getHeight();
    }

    @Override
    public AVLTree<T> insert(T element) {
        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");

        AVLTree<T> newRoot;
        if (element.compareTo(value) > 0) {
            newRoot = new AVLTree<T>(value, leftNode, rightNode.insert(element));
            if(newRoot.getBalanceFactor() == -2) {
                AVLTree<T> rightN = ((AVLTree<T>)newRoot.rightNode);
                if(rightN.getBalanceFactor() == 1)  newRoot.rightNode = rightN.rightRotate();
                newRoot = newRoot.leftRotate();
            }
        } else if (element.compareTo(value) < 0) {
            newRoot = new AVLTree<T>(value, leftNode.insert(element), rightNode);
            if(newRoot.getBalanceFactor() == 2){
                AVLTree<T> leftN = ((AVLTree<T>)newRoot.leftNode);
                if(leftN.getBalanceFactor() == -1) newRoot.leftNode = leftN.leftRotate();
                newRoot = newRoot.rightRotate();
            }
        } else {
            newRoot = this;
        }

        return newRoot;
    }

    @Override
    public Tree<T> delete(T element) {
        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");


        AVLTree<T> newRoot;
        if (element.compareTo(value) > 0) {
            newRoot = new AVLTree<T>(value, leftNode, rightNode.delete(element));
            if(newRoot.getBalanceFactor() == 2){
                AVLTree<T> leftN = ((AVLTree<T>)newRoot.leftNode);
                if(leftN.getBalanceFactor() == -1) newRoot.leftNode = leftN.leftRotate();
                newRoot = newRoot.rightRotate();
            }
        } else if (element.compareTo(value) < 0) {
            newRoot = new AVLTree<T>(value, leftNode.delete(element), rightNode);
            if(newRoot.getBalanceFactor() == -2) {
                AVLTree<T> rightN = ((AVLTree<T>)newRoot.rightNode);
                if(rightN.getBalanceFactor() == 1)  newRoot.rightNode = rightN.rightRotate();
                newRoot = newRoot.leftRotate();
            }
        } else {
            if (leftNode instanceof EmptyAVL) {
                return rightNode;
            } else if (rightNode instanceof EmptyAVL) {
                return leftNode;
            } else {
                T minValue = rightNode.min();
                newRoot = new AVLTree<T>(minValue, leftNode, rightNode.delete(minValue));
                if(newRoot.getBalanceFactor() == 2){
                    AVLTree<T> leftN = ((AVLTree<T>)newRoot.leftNode);
                    if(leftN.getBalanceFactor() == -1) newRoot.leftNode = leftN.leftRotate();
                    newRoot = newRoot.rightRotate();
                }
            }
        }

        return newRoot;
    }







    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> leftRotate() {

        Tree<T> newParent = this.rightNode;
        Tree<T> newRightOfCurrent = newParent.leftNode;

        // COMPLETE
        newParent.leftNode = this;
        this.rightNode = newRightOfCurrent;

        return (AVLTree<T>) newParent; // Change to return something different
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> rightRotate() {
        Tree<T> newParent = this.leftNode;
        Tree<T> newLeftOfCurrent = newParent.rightNode;

        newParent.rightNode = this;
        this.leftNode = newLeftOfCurrent;

        return (AVLTree<T>) newParent; // Change to return something different
    }

    /**
     * Note that this is not within a file of its own... WHY?
     * The answer is: this is just a design decision. 'insert' here will return something specific
     * to the parent class inheriting Tree from BinarySearchTree. In this case an AVL tree.
     */
    public static class EmptyAVL<T extends Comparable<T>> extends EmptyTree<T> {
        @Override
        public Tree<T> insert(T element) {
            // The creation of a new Tree, hence, return tree.
            return new AVLTree<T>(element);
        }

        @Override
        public Tree<T> delete(T element) {
            return null;
        }
    }
}
