package com.example.ievent.database.data_manager;

import android.util.Log;

import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.FollowerNumListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.entity.Organizer;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used to manage the data of the organizer
 * @author Zhiyuan Lu
 * @author Qianwen Shen
 * @author Xuan Li
 */
public class OrganizerDataManager {
    private static OrganizerDataManager instance;

    private CollectionReference orgRef;

    private List<OrganizedEventListener> listeners = new ArrayList<>();

    private Map<String, List<String>> lastKnownEventLists = new HashMap<>();

    private OrganizerDataManager() {
        orgRef = FirebaseFirestore.getInstance().collection("Organizers");
    }

    public static synchronized OrganizerDataManager getInstance() {
        if (instance == null) {
            synchronized (OrganizerDataManager.class) {
                if (instance == null) {
                    instance = new OrganizerDataManager();
                }
            }
        }
        return instance;
    }

    /**
     * Add an organizer to the database
     * @param uid the user id
     * @param org the organizer object
     */
    public synchronized void addOrganizer(String uid, Organizer org) {
        orgRef.document(uid).set(org);
    }


    /**
     * Get the organizer from the database
     * @param uid the user id
     * @param listener the listener to handle the result
     */
    public synchronized void getOrganizer(String uid, OrgDataListener listener) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (task.isSuccessful()) {
                if (document.exists()) {
                    Organizer org = document.toObject(Organizer.class);
                    ArrayList<Organizer> organizers = new ArrayList<>();
                    organizers.add(org);
                    listener.onSuccess(organizers);
                } else {
                    listener.onFailure("No such organizer exist, should register one");
                }
            } else {
                listener.onFailure("get failed with " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    /**
     * Add an event to the organized event list
     * @param uid the user id
     * @param eventId the event id
     */
    public synchronized void addOrganizedEvent(String uid, String eventId) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.update("organizedEventList", FieldValue.arrayUnion(eventId)).
                addOnSuccessListener(aVoid -> Log.d("UserDataManager", "Event added successfully!")).
                addOnFailureListener(e -> Log.e("UserDataManager", "Error adding event", e));
    }

    /**
     * Remove an event from the organized event list
     * @param uid the user id
     * @param listener the listener to handle the result
     */
    public synchronized void fetchOragnizedData(String uid, OrganizedEventListener listener) {
        DocumentReference docRef = orgRef.document(uid);


        docRef.addSnapshotListener((snapshot, e) -> {


            if (e != null) {
                Log.e("UserDataManager", "Listen failed.", e);
                listener.onError(e.toString());
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                ArrayList<String> orgnizedEventIds = new ArrayList<>();
                List<String> organizedEvents = (List<String>) snapshot.get("organizedEventList");
                Log.i("FETCH ORGANIZED DATA", "fetchOragnizedData: " + organizedEvents.size());
                if (organizedEvents != null) {
                    orgnizedEventIds.addAll(organizedEvents);
                }
                listener.onEventsUpdated(orgnizedEventIds);
            } else {
                listener.onError("null");
                Log.d("UserDataManager", "Current data: null");
            }
        });
    }

    /**
     * Add a follower to the organizer
     * @param organizerId the organizer id
     * @param followerId the follower id
     * @param listener the listener to handle the result
     */
    public void addFollower(String organizerId, String followerId, DataListener<Void> listener) {
        DocumentReference organizerRef = orgRef.document(organizerId);
        organizerRef.update("followersList", FieldValue.arrayUnion(followerId))
                .addOnSuccessListener(aVoid -> {
                    Log.d("OrganizerDataManager", "Follower added successfully!");
                    listener.onSuccess(new ArrayList<Void>()); // Pass an empty ArrayList
                })
                .addOnFailureListener(e -> {
                    Log.e("OrganizerDataManager", "Error adding follower", e);
                    listener.onFailure(e.getMessage());
                });
    }

    public void removeFollower(String organizerId, String followerId, DataListener<Void> listener) {
        DocumentReference organizerRef = orgRef.document(organizerId);
        organizerRef.update("followersList", FieldValue.arrayRemove(followerId))
                .addOnSuccessListener(aVoid -> {
                    Log.d("OrganizerDataManager", "Follower removed successfully!");
                    listener.onSuccess(new ArrayList<Void>()); // Pass an empty ArrayList
                })
                .addOnFailureListener(e -> {
                    Log.e("OrganizerDataManager", "Error removing follower", e);
                    listener.onFailure(e.getMessage());
                });
    }





    /**
     * setup a listener to listen to the number of followers of an organizer and notify the listener when the number of followers reaches 5
     * @param organizerId the organizer id
     * @param listener the listener to handle the result
     */
    public void setupFollowerListener(String organizerId, FollowerNumListener listener) {
        DocumentReference organizerRef = orgRef.document(organizerId);
        // record the previous number of followers, initial value is 0
        AtomicInteger previousFollowerCount = new AtomicInteger(0);
        organizerRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.e("MainActivity", "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                List<String> followersList = (List<String>) snapshot.get("followersList");
                if (followersList != null) {
                    int currentFollowerCount = followersList.size();
                    // check if the number of followers has changed from 4 to 5
                    if (previousFollowerCount.get() == 4 && currentFollowerCount == 5) {
                        // notify the listener that the number of followers has reached 5
                        listener.reached(true);
                        Log.i("OrganizerDataManager", "reached num of followers");
                    }
                    // update the previous number of followers
                    previousFollowerCount.set(currentFollowerCount);
                }
            } else {
                Log.d("MainActivity", "Current data: null");
            }
        });
    }
}
