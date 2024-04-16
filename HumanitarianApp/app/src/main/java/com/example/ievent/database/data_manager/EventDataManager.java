package com.example.ievent.database.data_manager;

import com.example.ievent.entity.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * use Firestore database to load, store and update event information
 * This is a Singleton class with thread-safe implementation
 */
public class EventDataManager {
    private static EventDataManager instance;

    /** reference of events collection in firestore*/
    private CollectionReference eventRef;

    private EventDataManager(){
        eventRef = FirebaseFirestore.getInstance().collection("Events");
    }

    public synchronized static EventDataManager getInstance(){
        if (instance == null) {
            synchronized (EventDataManager.class) {
                if (instance == null) {
                    instance = new EventDataManager();
                }
            }
        }
        return instance;
    }

    /***
     * store new event to the database
     * @param e the new event to add to
     */
    public synchronized void addNewEvent(Event e) {
        eventRef.add(e).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Event added successfully");
            } else {
                System.out.println("Event addition failed");
            }
        });
    }


    /**
     * get all events from the database based on the type and return them
     * @param type the type of the event
     * @return ArrayList<Event> the list of events
     */
    public synchronized ArrayList<Event> getAllEventsByType(String type) {
        ArrayList<Event> events = new ArrayList<>();
        eventRef.whereEqualTo("type", type).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Events retrieval successful");
                events.addAll(task.getResult().toObjects(Event.class));
            } else {
                System.out.println("Events retrieval failed");
            }
        });
        return events;
    }


}
