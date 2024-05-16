package com.example.ievent.entity;

import com.example.ievent.database.data_manager.OrganizerDataManager;

import java.util.ArrayList;

/**
 * Organizer class that extends the User class. It represents an organizer in the system.
 * @author Zhiyuan Lu
 * @author Qianwen Shen
 */
public class Organizer extends User implements java.io.Serializable {

    private ArrayList<String> organizedEventList = new ArrayList<>();

    private ArrayList<String> followersList = new ArrayList<>();

    public Organizer() {
        super();
    }

    public Organizer(String uid, String email, String userName) {
        super(uid, email, userName);
    }

    public void organizeEvent(String eventId) {
        // Adds an event to the list of events the participant has joined
        // organizedEventList.add(event);
        OrganizerDataManager.getInstance().addOrganizedEvent(super.getUid(), eventId);

    }


    public ArrayList<String> getOrganizedEventList() {
        // Overridden to return the list of events this participant has joined
        return organizedEventList;
    }

    public ArrayList<String> getFollowersList() {
        // Returns the list of followers for this organizer
        return followersList;
    }


    public boolean isFollower(String userId) {
        // Checks if a specific user is following this organizer
        return followersList.contains(userId);
    }

}


