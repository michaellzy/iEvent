package com.example.ievent.entity;

import java.util.ArrayList;

import java.util.ArrayList;

public class Participant extends User {


    private ArrayList<String> participatedEventList = new ArrayList<>();

    public Participant(String uid, String email, String userName) {
        super(uid, email, userName);
    }


    public void participateEvent(String eventId) {
        // Adds an event to the list of events the participant has joined
        participatedEventList.add(eventId);
    }

    public ArrayList<String> getParticipatedEventList() {
        // Overridden to return the list of events this participant has joined
        return participatedEventList;
    }
}
