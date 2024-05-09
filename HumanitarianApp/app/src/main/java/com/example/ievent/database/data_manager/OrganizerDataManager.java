package com.example.ievent.database.data_manager;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ievent.R;
import com.example.ievent.database.listener.DataListener;
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
    public void addFollower(Context context, String organizerId, String followerId, DataListener<Void> listener) {
        DocumentReference organizerRef = orgRef.document(organizerId);
        organizerRef.update("followersList", FieldValue.arrayUnion(followerId))
                .addOnSuccessListener(aVoid -> {
                    organizerRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                List<String> followersList = (List<String>) document.get("followersList");
                                if (followersList != null && followersList.size() == 3) {
                                    showCongratulationsNotification(context, organizerId);
                                }
                            }
                        }
                    });
                    listener.onSuccess(new ArrayList<Void>());
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));

    }
    private void showCongratulationsNotification(Context context, String organizerId) {
        DocumentReference organizerRef = FirebaseFirestore.getInstance().collection("Organizers").document(organizerId);
        organizerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Organizer organizer = documentSnapshot.toObject(Organizer.class);
                if (organizer != null) {
                    String organizerName = organizer.getUserName();  // 假设Organizer类有一个getUserName()方法

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification.Builder(context, "followers")
                            .setContentTitle("Congratulations " + organizerName)
                            .setContentText("You have reached 5 followers!")
                            .setSmallIcon(R.mipmap.ievent_logo)
                            .setAutoCancel(true)
                            .build();

                    notificationManager.notify(1, notification);
                } else {
                    Log.e("NotificationError", "Organizer data not found");
                }
            } else {
                Log.e("NotificationError", "Document does not exist");
            }
        }).addOnFailureListener(e -> Log.e("NotificationError", "Error fetching organizer", e));
    }



}
