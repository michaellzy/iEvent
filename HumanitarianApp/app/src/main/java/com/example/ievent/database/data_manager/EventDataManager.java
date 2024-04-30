package com.example.ievent.database.data_manager;

import androidx.annotation.Nullable;

import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * use Firestore database to load, store and update event information
 * This is a Singleton class with thread-safe implementation
 */
public class EventDataManager {
    private static EventDataManager instance;


    /** reference of events collection in firestore*/
    private final CollectionReference eventRef;

    private DocumentSnapshot lastVisible;

    private EventDataManager(){
        eventRef = FirebaseFirestore.getInstance().collection("testevents");
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
        eventRef.add(e);
    }

    // ----------------------------------- SEARCH SECTION START------------------------------------ //
    public synchronized void HandleQuery(Query query, EventDataListener listener) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if(snapshots != null) {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        Event event = document.toObject(Event.class);
                        events.add(event);
                    }
                    if (!snapshots.getDocuments().isEmpty()) {
                        lastVisible = snapshots.getDocuments().get(snapshots.size() - 1);
                    } else {
                        // No documents were returned, so we've loaded all available data
                        listener.isAllData(true);
                    }
                    listener.onSuccess(events);
                } else {
                    listener.onFailure("No such document");
                }
            } else {
                listener.onFailure("Error getting documents: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    /**
     * get all events from the database based on the type and return them
     * @param type the type of the event
     */
    public synchronized void getAllEventsByType(String type, EventDataListener listener) {
        Query q = eventRef.whereEqualTo("type", type);

        HandleQuery(q, listener);
    }

    /**
     * get all events from the database based on the name of events and return them
     * @param name the name of the event
     * @param listener the listener to handle the result
     */
    public synchronized void getAllEventByFuzzyName(String name, EventDataListener listener) {
       Query q =  eventRef.whereGreaterThanOrEqualTo("title", name)
               .whereLessThanOrEqualTo("title", "\\uf8ff" + name + "\\uf8ff");

       HandleQuery(q, listener);
    }

    // ----------------------------------- SEARCH SECTION END ------------------------------------ //

    /**
     * get events from the database and return them by pages
     * @param pageSize the number of events to load
     * @param listener the listener to handle the result
     */
    public synchronized void loadEvents(int pageSize, EventDataListener listener) {
        Query query = eventRef.limit(pageSize);

        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }

        HandleQuery(query, listener);
    }

    public synchronized void updateEvents(EventDataListener listener) {
        eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    listener.onFailure("Listen failed " + error);
                }

                ArrayList<Event> newEvents = new ArrayList<>();
                for (DocumentChange dc: value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        newEvents.add(dc.getDocument().toObject(Event.class));
                    }
                }
                listener.onSuccess(newEvents);
            }
        });
    }
}
