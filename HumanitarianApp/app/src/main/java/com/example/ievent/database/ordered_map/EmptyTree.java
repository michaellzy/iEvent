package com.example.ievent.database.ordered_map;

/**
 * To avoid null pointer errors (and because this implementation is immutable)
 * we have a class that represents an 'empty' tree.
 */
public abstract class EmptyTree<K extends Comparable<K>, V> extends Tree<K, V> {
    // Will need to be implemented by the subclass inheriting this class.
    public abstract Tree<K, V> insert(K element, V value);

    @Override
    public Pair<K, V> min() {
        // No minimum.
        return null;
    }

    @Override
    public Pair<K, V> max() {
        // No maximum.
        return null;
    }

    @Override
    public Tree<K, V> find(K element) {
        // Was unable to find the item. Hence, return null.
        return null;
    }

    @Override
    public int getHeight() {

        return -1;
    }

    @Override
    public String toString() {
        return "{}";
    }

}
