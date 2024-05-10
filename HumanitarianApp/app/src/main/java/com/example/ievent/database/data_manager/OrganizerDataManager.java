package com.example.ievent.database.data_manager;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;


import com.example.ievent.R;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.FollowerNumListener;
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

//    public void addFollower(String organizerId, String followerId, DataListener<Void> listener) {
//        DocumentReference organizerRef = orgRef.document(organizerId);
//        organizerRef.update("followersList", FieldValue.arrayUnion(followerId))
//                .addOnSuccessListener(aVoid -> {
//                    Log.d("OrganizerDataManager", "Follower added successfully!");
//                    listener.onSuccess(new ArrayList<Void>()); // Pass an empty ArrayList
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("OrganizerDataManager", "Error adding follower", e);
//                    listener.onFailure(e.getMessage());
//                });
//    }
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


    public void setupFollowerListener(String organizerId, FollowerNumListener listener) {
        DocumentReference organizerRef = orgRef.document(organizerId);
        // 记录上次的粉丝数量，初始值为 0
        AtomicInteger previousFollowerCount = new AtomicInteger(0);
        organizerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("MainActivity", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    List<String> followersList = (List<String>) snapshot.get("followersList");
                    if (followersList != null) {
                        int currentFollowerCount = followersList.size();
                        // 检查粉丝数量是否从四变为五
                        if (previousFollowerCount.get() == 4 && currentFollowerCount == 5) {
                            // 达到5个粉丝，触发通知.
                            listener.reached(true);
                            Log.i("OrganizerDataManager", "reached num of followers");
                        }
                        // 更新上次的粉丝数量
                        previousFollowerCount.set(currentFollowerCount);
                    }
                } else {
                    Log.d("MainActivity", "Current data: null");
                }
            }
        });
    }


}
