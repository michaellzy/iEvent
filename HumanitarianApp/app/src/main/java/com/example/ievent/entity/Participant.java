package com.example.ievent.entity;

import java.util.ArrayList;

public class Participant extends User {
    private ArrayList<Event> participatedEventList = new ArrayList<>();
    public Participant(String uid) {
        super(uid);
    }

    public void publishNewEvent(Event event) {
        participatedEventList.add(event);
    }

    public ArrayList<Event> getOrganizedEventList() {
        return participatedEventList;
    }
}
