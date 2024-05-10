package com.example.ievent.database;

import com.example.ievent.entity.Event;

import java.util.ArrayList;

public class DataHolder {
    private static ArrayList<Event> events = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
        return events;
    }

    public static void setEvents(ArrayList<Event> newEvents) {
        events = newEvents;
    }
}
