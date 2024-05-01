package com.example.ievent.database.data_manager;

import android.util.Log;

import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * use Firestore database to load, store and update user information
 * This is a Singleton class with thread-safe implementation
 */
public class UserDataManager {

    final String TAG = "userDatabase";

    private static UserDataManager instance;

    private CollectionReference userRef;
    private CollectionReference orgRef;


    private UserDataManager() {

        userRef = FirebaseFirestore.getInstance().collection("Users");
        orgRef = FirebaseFirestore.getInstance().collection("Organizers");
    }

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            synchronized (UserDataManager.class) {
                if (instance == null) {
                    instance = new UserDataManager();
                }
            }
        }
        return instance;
    }


    /***
     * store new user to the database during sign up phase
     * @param user the new user to add to
     */
    public synchronized void addNewUser(String uid, User user) {
        userRef.document(uid).set(user);
    }
    public synchronized void addOrganizer(String uid, Organizer org) {
        orgRef.document(uid).set(org);
    }


    public synchronized void getLoggedInUser(String uid, UserDataListener listener) {
        DocumentReference docRef = userRef.document(uid);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
           if (task.isSuccessful()) {
               if (document.exists()) {
                   User user = document.toObject(User.class);

                   ArrayList<User> users = new ArrayList<>();
                   users.add(user);

                   listener.onSuccess(users);
               } else {
                   listener.onFailure("No such document");
               }
           } else {
               listener.onFailure("get failed with " + Objects.requireNonNull(task.getException()).getMessage());
           }
        });
    }

    public synchronized void getOrganizer(String uid, OrgDataListener listener) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (task.isSuccessful()) {
                if (document.exists()) {
                    // User user = document.toObject(User.class);
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

    public synchronized void addOrganizedEvent(String uid, Event event) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.update("organizedEventList", FieldValue.arrayUnion(event)).
                addOnSuccessListener(aVoid -> Log.d("UserDataManager", "Event added successfully!")).
                addOnFailureListener(e -> Log.e("UserDataManager", "Error adding event", e));
    }

    public synchronized void fetchOrganizedEvent(String uid, DataListener<Event> listener) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                ArrayList<Event> orgEvents = new ArrayList<>();
                ArrayList<Map<String, Object>> eventsMap = (ArrayList<Map<String,java.lang.Object>>) document.get("organizedEventList");

                // Process the fetched events
                for (Map<String, Object> event : eventsMap) {
                    for (Map.Entry<String, Object> entry : event.entrySet()) {
                        Object value = entry.getValue();  // This is the value associated with the key
                        orgEvents.add((Event) value);
                    }
                }
                listener.onSuccess(orgEvents);
            } else {
                listener.onFailure("No such document found in organizer");
            }
        });
    }
}
