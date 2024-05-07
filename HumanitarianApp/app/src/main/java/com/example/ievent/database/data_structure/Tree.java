package com.example.ievent.database.data_structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * The following interface defines required methods of any Tree.
 * Note that this is simplified for this lab (no delete).
 *
 * @param <T> the generic type this Tree uses. It extends comparable
 *            which allows us to order two of the same type.
 */
public abstract class Tree<T extends Comparable<T>> {
    /**
     * Here we store our class fields.
     */
    public final T value;       // element stored in this node of the tree.
    public Tree<T> leftNode;    // less than the node.
    public Tree<T> rightNode;   // greater than the node.

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
    public Tree(T value) {
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
    public Tree(T value, Tree<T> leftNode, Tree<T> rightNode) {
        // Ensure inputs are not null.
        if (value == null || leftNode == null || rightNode == null)
            throw new IllegalArgumentException("Inputs cannot be null");

        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public abstract T min();                     // Finds the minimum.

    public abstract T max();                     // Finds the maximum.

    public abstract Tree<T> find(T element);     // Finds the element and returns the node.

    public abstract Tree<T> insert(T element);   // Inserts the element and returns a new instance of itself with the new node.

    public abstract Tree<T> delete(T element);   // Deletes the element and returns a new instance of itself without the node.

    /**
     * Height of current node.
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

    /**
     * Graphically visualises the tree for human readability.
     * Feel free to edit this display methods
     *
     * @return graph of the tree.
     */
    public String display() {
        return display(0);
    }

    /**
     * Graphically visualises the tree for human readability.
     * Feel free to edit this display methods
     *
     * @param tabs from the left side of the screen.
     * @return graph of the tree.
     */
    public String display(int tabs) {
        // StringBuilder is faster than using string concatenation (which in java makes a new object per concatenation).
//        assert value != null;
//        StringBuilder sb = new StringBuilder(value.toString());
//        sb.append("\n").append("\t".repeat(tabs)).append("├─").append(leftNode.display(tabs + 1));
//        sb.append("\n").append("\t".repeat(tabs)).append("├─").append(rightNode.display(tabs + 1));
//        return sb.toString();
        return null;
    }

    /**
      * List the elements of the tree with in-order
      */
    public List<T> inOrder() {
		return this.treeToListInOrder(this);
	}

    /**
     * Converts tree to list in-order. Helper method of inOrder.
     * @param tree to convert to list.
     * @return in-order list of tree values.
     */
	private List<T> treeToListInOrder(Tree<T> tree) {
		List<T> list = new LinkedList<>();

		// Recurse through left subtree.
        if (tree.leftNode != null) {
            if (tree.leftNode.value != null) {
                list.addAll(treeToListInOrder(tree.leftNode));
            }
        }


		// Add current node's value
        if (tree.value != null) {
            list.add(tree.value);
        }

        // Recurse through left subtree.
        if (tree.rightNode != null) {
            if (tree.rightNode.value != null) {
                list.addAll(treeToListInOrder(tree.rightNode));
            }
        }
		return list;
	}

    /**
     * Encodes a tree to a single string.
     *
     * @return The serialized string representation of the tree.
     */
    public String serialize() {
        return serialize(this);
    }

    /**
     * Static method to serialize a tree to a single string.
     *
     * @param root The root of the tree to be serialized.
     * @return The serialized string representation of the tree.
     */
    public static <T extends Comparable<T>> String serialize(Tree<T> root) {
        if (root == null) {
            return null;
        }
        Stack<Tree<T>> stack = new Stack<>();
        stack.push(root);

        List<String> list = new ArrayList<>();
        while (!stack.isEmpty()) {
            Tree<T> currentNode = stack.pop();

            // If current node is null, store marker
            if (currentNode == null) {
                list.add("#");
            } else {
                // Else, store current node
                // and recur for its children
//                list.add();
                stack.push(currentNode.rightNode);
                stack.push(currentNode.leftNode);
            }
        }
        return String.join(",", list);
    }

    /**
     * Decodes your encoded data to a tree.
     *
     * @param data The serialized string representation of the tree.
     * @return The root of the deserialized tree.
     */
    public static <T extends Comparable<T>> Tree<T> deserialize(String data) {
        if (data == null) {
            return null;
        }
        String[] arr = data.split(",");
        return deserializeHelper(arr);
    }

    private static <T extends Comparable<T>> Tree<T> deserializeHelper(String[] arr) {
//        if (arr[t].equals("#")) {
//            t++;
//            return null;
//        }
//
//        // Create node with this item
//        // and recur for children
//        T value = (T) arr[t];
//        t++;
//        Tree<T> root = new TreeNode<>(value);
//        root.leftNode = deserializeHelper(arr);
//        root.rightNode = deserializeHelper(arr);
//        return root;
        return null;
    }
}
