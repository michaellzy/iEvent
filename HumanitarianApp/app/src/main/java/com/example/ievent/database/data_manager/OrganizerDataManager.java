package com.example.ievent.database.data_manager;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.entity.Organizer;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public synchronized void addOrganizer(String uid, Organizer org) {
        orgRef.document(uid).set(org);
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

    public synchronized void addOrganizedEvent(String uid, String eventId) {
        DocumentReference docRef = orgRef.document(uid);
        docRef.update("organizedEventList", FieldValue.arrayUnion(eventId)).
                addOnSuccessListener(aVoid -> Log.d("UserDataManager", "Event added successfully!")).
                addOnFailureListener(e -> Log.e("UserDataManager", "Error adding event", e));
    }

    public synchronized DocumentReference getOrgRef(String uid) {
        return orgRef.document(uid);
    }

    public synchronized void fetchOragnizedData(String uid, OrganizedEventListener listener) {
        DocumentReference docRef = orgRef.document(uid);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("UserDataManager", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    ArrayList<String> orgnizedEventIds = new ArrayList<>();
                    List<String> organizedEvents = (List<String>) snapshot.get("organizedEventList");
                    if (organizedEvents != null) {
                        orgnizedEventIds.addAll(organizedEvents);
                    }
                } else {
                    Log.d("UserDataManager", "Current data: null");
                }
            }
        });
    }


}
