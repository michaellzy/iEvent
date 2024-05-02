package com.example.ievent.entity;

import com.example.ievent.database.data_manager.OrgnizerDataManager;
import com.example.ievent.database.data_manager.UserDataManager;

import java.util.ArrayList;

public class Organizer extends User implements java.io.Serializable{

    private ArrayList<Event> organizedEventList = new ArrayList<>();

    public Organizer() {
        super();
    }
    public Organizer(String uid, String email, String userName) {
        super(uid, email, userName);
    }
    public void organizeEvent(String eventId) {
        // Adds an event to the list of events the participant has joined
        // organizedEventList.add(event);
        OrgnizerDataManager.getInstance().addOrganizedEvent(super.getUid(), eventId);
    }

    public ArrayList<Event> getOrganizedEventList() {
        // Overridden to return the list of events this participant has joined
        return organizedEventList;
    }
}


