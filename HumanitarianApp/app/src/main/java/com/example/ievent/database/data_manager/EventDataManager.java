package com.example.ievent.database.data_manager;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ievent.database.data_structure.IEventAVLTree;
import com.example.ievent.database.data_structure.IEventData;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.EventUpdateListener;
import com.example.ievent.entity.Event;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * use Firestore database to load, store and update event information
 * This is a Singleton class with thread-safe implementation
 */
public class EventDataManager {
    private static EventDataManager instance;


    /** reference of events collection in firestore*/
    private final CollectionReference eventRef;

    /** AVL tree to store events ordered by the key */
    private IEventAVLTree eventAVLTree = new IEventAVLTree(new IEventData(0, new LinkedList<>()));

    private DocumentSnapshot lastVisible;

    private ArrayList<EventUpdateListener> listeners = new ArrayList<>();

    private EventDataManager(){

//         eventRef = FirebaseFirestore.getInstance().collection("testevent-lzy");

        eventRef = FirebaseFirestore.getInstance().collection("events");
        eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    notifyError("Listen failed " + error);
                    return;
                }

                ArrayList<Event> newEvents = new ArrayList<>();
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        newEvents.add(dc.getDocument().toObject(Event.class));
                    }
                }
                notifyEventUpdate(newEvents);
            }
        });


//        getAllEventsByType("wellness", new EventDataListener() {
//            @Override
//            public void isAllData(boolean isALl) {
//                //;
//            }
//
//            @Override
//            public void onSuccess(ArrayList<Event> events) {
//                eventAVLTree = IEventAVLTree.insertEvents(events, IEventAVLTree.keyType.Price, eventAVLTree);
//        getAllEventsByType("wellness", new EventDataListener() {
//            @Override
//            public void onSuccess(ArrayList<Event> events) {
//
//                String s = "";
//                for (Event e : events) {
//                    s += e.getPrice() + " ";
//                }
//                Log.i("AVLTree", "onSuccess: " + s);
//
////                eventAVLTree = eventAVLTree.insert(new IEventData(0, new LinkedList<>()))
////                        .insert(new IEventData(3, new LinkedList<>()))
////                        .insert(new IEventData(2, new LinkedList<>()))
////                        .insert(new IEventData(2, new LinkedList<>()));
//
//
////                eventAVLTree = eventAVLTree.insertEvents(events, IEventAVLTree.KeyType.PRICE);
////                StringBuilder sb = new StringBuilder();
////                for (IEventData iEventData : eventAVLTree.inOrder()) {
////                    sb.append(iEventData.getKey()).append(" ");
////                }
////                Log.i("AVLTree", "onSuccess: " + sb);
////                Log.i("AVLTree", eventAVLTree.getEventIdsInRange(-1000, 1000).size() + "");
//
////                StringBuilder sb = new StringBuilder();
////                for (String id : eventAVLTree.getEventIdsInRange(0, 0)) {
////                    sb.append(id).append("\n");
////                }
////                Log.i("AVLTree", "onSuccess: " + sb);
//
//
//
////                Log.i("AVLTree", eventAVLTree.getEventIdsInRange(0, 0).size() + "");
////                eventAVLTree.deleteByIdRef("Carer Wellness Program - Thirroul - NSW", 0);
//////                Log.i("AVLTree", eventAVLTree.getEventIdsInRange(0, 0).size() + "");
////                eventAVLTree = eventAVLTree.insertEvents(events, IEventAVLTree.KeyType.PRICE);
////                StringBuilder sb = new StringBuilder();
//
//
////                ArrayList<String> ids = eventAVLTree.getEventIdsInRange(0, 0);
////                for (String string : ids) {
////                    eventAVLTree = eventAVLTree.deleteByIdRef(string, 0);
////                    Log.i("AVLTree", eventAVLTree.getEventIdsInRange(0, 0).size() + "");
////                }
////
////
////                sb = new StringBuilder();
////                for (IEventData iEventData : eventAVLTree.inOrder()) {
////                    sb.append(iEventData.getKey()).append(" ");
////                }
////                Log.i("AVLTree", "onSuccess: " + sb);
//
//
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.e("AVLTree", "onFailure: " + error);
//            }
//        });
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

    public void addEventListener(EventUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(EventUpdateListener listener) {
        listeners.remove(listener);
    }

    private void notifyEventUpdate(ArrayList<Event> events) {
        for (EventUpdateListener listener : listeners) {
            listener.onEventsUpdated(events);
        }
    }

    private void notifyError(String error) {
        for (EventUpdateListener listener : listeners) {
            listener.onError(error);
        }
    }


    /***
     * store new event to the database
     * @param e the new event to add to
     */
    public synchronized void addNewEvent(Event e) {

        String eventId = eventRef.document().getId();
        e.setEventId(eventId);
        eventRef.document(eventId).set(e);
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
    public synchronized void fetchDocuments(ArrayList<String> EventIds, EventDataListener listener){
        ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        Log.i("FETCHDOCUMENTS", "fetchDocuments: ");

        for (String docId : EventIds) {
            DocumentReference docRef = eventRef.document(docId);
            // Asynchronously retrieve each document and add the task to the list
            tasks.add(docRef.get());
        }

        // Wait for all tasks to complete
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(documents -> {
            ArrayList<Event> events = new ArrayList<>();
            for (Object document : documents) {
                DocumentSnapshot doc = (DocumentSnapshot) document;
                if (doc.exists()) {
                    Event event = doc.toObject(Event.class);
                    events.add(event);
                    listener.onSuccess(events);
                    Log.d("EventDataManager", "Document data: " + doc.getId() + " => " + doc.getData());
                } else {
                    listener.onFailure("Document not found: " + doc.getId());
                    Log.d("EventDataManager", "Document not found: " + doc.getId());
                }
            }
        }).addOnFailureListener(e -> {
            listener.onFailure("Error fetching documents" + e);
            Log.e("EventDataManager", "Error fetching documents", e);
        });
    }

    /**
     * get all events from the database based on the type and return them
     * @param type the type of the event
     */
    public synchronized void getAllEventsByType(String type, EventDataListener listener) {
        Query q = eventRef.whereEqualTo("type", type).limit(30);

        HandleQuery(q, listener);
    }

    /**
     * get all events from the database based on the name of events and return them
     * @param name the name of the event
     * @param listener the listener to handle the result
     */
    public synchronized void getAllEventByFuzzyName(String name, EventDataListener listener) {
       Query q =  eventRef.whereGreaterThanOrEqualTo("title", name)
               .whereLessThanOrEqualTo("title",  name + "\\uf8ff").limit(30);
       HandleQuery(q, listener);
    }

    /**
     * get all events from the database on price which is >= input price
     * @param price the input price of the event
     * @param listener the listener to handle the result
     */
    public synchronized void getGreaterThan(double price, EventDataListener listener) {
        Query q = eventRef.whereGreaterThanOrEqualTo("price",(double)(price)).limit(30);
        HandleQuery(q, listener);
    }

    /**
     * get all events from the database on price which is <= input price
     * @param price the input price of the event
     * @param listener the listener to handle the result
     */
    public synchronized void getLessThan(double price, EventDataListener listener) {
        Query q = eventRef.whereLessThanOrEqualTo("price",(double)(price)).limit(30);
        HandleQuery(q, listener);
    }
    public synchronized void getDateAfter(long timestamp, EventDataListener listener) {
        Query q = eventRef.whereGreaterThanOrEqualTo("timestamp", timestamp).limit(30);
        HandleQuery(q, listener);
    }
    public synchronized void getDateBefore(long timestamp, EventDataListener listener) {
        Query q = eventRef.whereLessThanOrEqualTo("timestamp", timestamp).limit(30);
        HandleQuery(q, listener);
    }
    public synchronized void getAllEventsByPrice(double price, EventDataListener listener){
        Query q = eventRef.whereEqualTo("price", price).limit(30);
        HandleQuery(q, listener);
    }
    public synchronized void getAllEventsByDate(long timestamp, EventDataListener listener){
        Query q = eventRef.whereEqualTo("timestamp", timestamp).limit(30);
        HandleQuery(q, listener);
    }

    public synchronized void getEventsByFilters(String type, String titlePrefix, Date startDate, Date endDate, double minPrice, double maxPrice, EventDataListener listener) {
        // Base query including type filter
        Query q = eventRef.whereEqualTo("type", type);

        // Add fuzzy search-like functionality for title
        if (titlePrefix != null && !titlePrefix.isEmpty()) {
            q = q.whereGreaterThanOrEqualTo("title", titlePrefix)
                    .whereLessThanOrEqualTo("title", titlePrefix + "\uf8ff");
        }

        // Add date range to query if both dates are provided
        if (startDate != null && endDate != null) {
            q = q.whereGreaterThanOrEqualTo("date", startDate)
                    .whereLessThanOrEqualTo("date", endDate);
        }

        // Add price range to the query
        q = q.whereGreaterThanOrEqualTo("price", minPrice)
                .whereLessThanOrEqualTo("price", maxPrice)
                .limit(30);

        // Execute the query
        HandleQuery(q, listener);
    }

    public synchronized void getAllEventsByIds(ArrayList<String> ids, EventDataListener listener) {
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        for (String id : ids) {
            DocumentReference docRef = eventRef.document(id);
            tasks.add(docRef.get()); // Add each document fetch task to the list
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean allFound = true;
                for (Task<DocumentSnapshot> documentTask : tasks) {
                    DocumentSnapshot document = documentTask.getResult();
                    if (document.exists()) {
                        events.add(document.toObject(Event.class));
                    } else {
                        allFound = false;
                        Log.e("EventDataManager", "Document not found: " + document.getId());
                    }
                }
                if (allFound) {
                    listener.onSuccess(events);
                } else {
                    listener.onFailure("One or more documents could not be found");
                }
            } else {
                listener.onFailure("Error getting documents: " + task.getException().getMessage());
            }
        });
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

    public synchronized void fetchEventsByOrganizerIds(List<String> organizerIds, EventDataListener listener) {

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (String orgId : organizerIds) {
            // Create a query for each organizer ID
            Query query = eventRef.whereEqualTo("orgId", orgId);

            // Asynchronously retrieve each document and add the task to the list
            tasks.add(query.get());
        }

        // Wait for all tasks to complete
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(documents -> {
            ArrayList<Event> events = new ArrayList<>();
            for (Object document : documents) {
                QuerySnapshot snapshot = (QuerySnapshot) document;
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    Event event = doc.toObject(Event.class);
                    if (event != null) {
                        events.add(event);
                    }
                }
            }
            if (!events.isEmpty()) {
                listener.onSuccess(events);
            } else {
                listener.isAllData(true);
            }
        }).addOnFailureListener(e -> {
            listener.onFailure("Error fetching documents: " + e.getMessage());
        });
    }


}
