package com.example.ievent.database.data_manager;

import com.example.ievent.entity.Event;

import java.util.ArrayList;

/**
 * EventCache
 * @author Zhiyuan Lu
 */
public class EventCache {
    private static EventCache instance;
    private ArrayList<Event> events;

    private EventCache() {
        events = new ArrayList<>();
    }

    public static synchronized EventCache getInstance() {
        if (instance == null) {
            instance = new EventCache();
        }
        return instance;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> data) {
        // this.events = events;
        events.addAll(data);
    }
}

