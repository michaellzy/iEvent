package com.example.ievent.database.ordered_map;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Ordered Event implementation.
 * @param <K> key
 * @param <V> value
 * @author Zhiyuan Lu
 */
public class OrderedEvent<K extends Comparable<K>, V> implements Container {
    private AVLTree<K, V> root;

    public OrderedEvent() {
        this.root = null; // Initialize the AVL tree root as null.
    }

    public void insert(K key, V value) {
        if (root == null) {
            root = new AVLTree<>(key, value);
        } else {
            root = root.insert(key, value);
        }
    }

    @Override
    public Iterator getIterator() {
        return new EventIterator<>(root);
    }

    private class EventIterator<K extends Comparable<K>, V> implements Iterator {
        private Stack<AVLTree<K, V>> stack;

        public EventIterator(AVLTree<K, V> root) {
            this.stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(AVLTree<K, V> node) {
            boolean isEmpty = false;
            while (node != null && !isEmpty) {
                stack.push(node);
                if (node.leftNode instanceof AVLTree)
                    node = (AVLTree<K, V>) node.leftNode;
                else
                    isEmpty = true;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                return null;
            }
            AVLTree<K, V> node = stack.pop();
            LinkedList<V> value = node.value.getValue();
            if (node.rightNode != null && !(node.rightNode instanceof AVLTree.EmptyAVL)) {
                pushLeft((AVLTree<K, V>) node.rightNode);
            }
            return value;
        }
    }
}
