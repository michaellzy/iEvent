package com.example.ievent.entity;

import java.util.ArrayList;

/**
 * Participant class that extends the User class. It represents a participant in the system.
 * @author Zhiyuan Lu
 */
public class Participant extends User {


    private ArrayList<String> participatedEventList = new ArrayList<>();
    public Participant() {
        super();
    }


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
