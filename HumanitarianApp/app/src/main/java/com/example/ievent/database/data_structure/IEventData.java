package com.example.ievent.database.data_structure;

import java.util.LinkedList;

public class IEventData implements Comparable<IEventData>{
    /**
     *  it can be used to compare two objects.
     */
    private double key;

    /**
     *  it stores the id reference of the event.
     */
    private LinkedList<String> ids;


    public IEventData(int key, LinkedList<String> ids) {
        this.key = key;
        this.ids = ids;
    }

    @Override
    public int compareTo(IEventData o) {
        // compare the double value of the key.
        return Double.compare(key, o.key);
    }

    public double getKey() {
        return key;
    }

    public LinkedList<String> getIds() {
        return ids;
    }

    public void setKey(double key) {
        this.key = key;
    }

    public void setIds(LinkedList<String> ids) {
        this.ids = ids;
    }

    public void addId(String id) {
        ids.add(id);
    }
}
