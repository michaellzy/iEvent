package com.example.ievent.entity;

import java.util.ArrayList;

public class Organizer extends User {

    private ArrayList<Event> organizedEventList = new ArrayList<>();

    public Organizer() {
        super();
    }
    public Organizer(String uid) {
        super(uid);
    }
    public void organizeEvent(Event event) {
        // Adds an event to the list of events the participant has joined
        organizedEventList.add(event);
    }

    public ArrayList<Event> getOrganizedEventList() {
        // Overridden to return the list of events this participant has joined
        return organizedEventList;
    }
}


