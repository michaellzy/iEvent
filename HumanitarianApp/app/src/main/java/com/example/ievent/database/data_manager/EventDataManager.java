package com.example.ievent.database.data_manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
        eventRef = FirebaseFirestore.getInstance().collection("events");
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


    /**
     * get all events from the database based on the type and return them
     * @param type the type of the event
     */
    public synchronized void getAllEventsByType(String type, EventDataListener listener) {
        ArrayList<Event> events = new ArrayList<>();
        eventRef.whereEqualTo("type", type).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if(snapshots != null) {
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        Event event = document.toObject(Event.class);
                        events.add(event);
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

    public synchronized void loadEvents(int pageSize, EventDataListener listener) {
        Query query = eventRef.limit(pageSize);

        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Event> eventList = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()) {
                        Event event = snapshot.toObject(Event.class);
                        eventList.add(event);
                    }
                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);

                    listener.onSuccess(eventList);
                } else {
                    // add empty list
                    listener.onSuccess(eventList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure("Error loading events: " + e.getMessage());
            }
        });
    }
}
