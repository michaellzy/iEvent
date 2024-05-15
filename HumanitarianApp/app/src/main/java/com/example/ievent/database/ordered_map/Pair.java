package com.example.ievent.database.ordered_map;

import java.util.LinkedList;

/**
 * Pair implementation.
 * @param <K> key
 * @param <V> value
 * @author Zhiyuan Lu
 */
public class Pair<K extends Comparable<K>, V> {
    private K key;
    // private List<V> value = new LinkedList<>();
    private LinkedList<V> value;


    public Pair(K key, LinkedList<V> value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public LinkedList<V> getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
