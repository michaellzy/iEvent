package com.example.ievent.entity;

import java.util.ArrayList;

public class Organizer extends User {
    private ArrayList<Event> organizedEventList = new ArrayList<>();

    private String userName;
    public Organizer(String uid) {
        super(uid);
        this.userName = super.getUserName();
    }

    public void publishNewEvent(Event event) {
        organizedEventList.add(event);
    }

    public ArrayList<Event> getOrganizedEventList() {
        return organizedEventList;
    }
}
