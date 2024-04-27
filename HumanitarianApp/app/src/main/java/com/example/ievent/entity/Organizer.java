package com.example.ievent.entity;

import java.util.ArrayList;

public class Organizer extends User {
    private ArrayList<Event> organizedEventList = new ArrayList<>();
    public Organizer() {
        super();
    }

    public void publishNewEvent(Event event) {
        organizedEventList.add(event);
    }

    public ArrayList<Event> getOrganizedEventList() {
        return organizedEventList;
    }
}
