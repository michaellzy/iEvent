package com.example.ievent.database.data_manager;

import android.util.Log;

import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Participant;
import com.example.ievent.entity.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * use Firestore database to load, store and update user information
 * This is a Singleton class with thread-safe implementation
 */
public class UserDataManager {

    final String TAG = "userDatabase";

    private static UserDataManager instance;

    private CollectionReference userRef;
    // private CollectionReference orgRef;


    private UserDataManager() {

        userRef = FirebaseFirestore.getInstance().collection("Users");
        // orgRef = FirebaseFirestore.getInstance().collection("Organizers");
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




    /**
     *  get the current user
     * @param uid the user id
     * @param listener the listener to handle the data
     */
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

    // ——————————————————————————————————————----- update user information —————————————————————————————————————— //
    /**
     * update the user avatar
     * @param uid the user id
     * @param avatar the new avatar
     */
    public synchronized void updateUserAvatar(String uid, String avatar, UserDataListener listener) {
        userRef.document(uid).update("avatar", avatar)
                .addOnSuccessListener(aVoid ->{
                    listener.onSuccess(new ArrayList<>());
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }


    public synchronized void addEventToUser(String uid, String eventId, DataListener<Void> listener) {
        DocumentReference userDoc = userRef.document(uid);
        userDoc.update("participatedEventList", FieldValue.arrayUnion(eventId))
                .addOnSuccessListener(aVoid -> {
                    ArrayList<Void> result = new ArrayList<>();  // Create an empty ArrayList of type Void
                    listener.onSuccess(result);  // Pass the empty list as success does not need to return any data
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public synchronized void getParticipantEvents(String uid, EventDataListener listener) {
        DocumentReference userDoc = userRef.document(uid);
        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Participant participant = document.toObject(Participant.class);
                    ArrayList<String> eventIds = participant.getParticipatedEventList();
                    EventDataManager.getInstance().fetchDocuments(eventIds, listener);
                } else {
                    listener.onFailure("User not found");
                }
            } else {
                listener.onFailure("Failed to fetch user: " + task.getException().getMessage());
            }
        });
    }
    /**
     * Retrieves multiple users based on a list of user IDs.
     * @param ids List of user IDs.
     * @param listener Listener to handle the result or failure.
     */
    public synchronized void getAllUsersByIds(List<String> ids, UserDataListener listener) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : ids) {
            tasks.add(userRef.document(id).get());
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(documents -> {
            ArrayList<User> users = new ArrayList<>();
            for (Object document : documents) {
                DocumentSnapshot doc = (DocumentSnapshot) document;
                if (doc.exists()) {
                    users.add(doc.toObject(User.class));
                } else {
                    Log.e(TAG, "Document not found: " + doc.getId());
                }
            }
            if (!users.isEmpty()) {
                listener.onSuccess(users);
            } else {
                listener.onFailure("No users found.");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching user documents: ", e);
            listener.onFailure("Error fetching users: " + e.getMessage());
        });
    }

    /**
     * Adds a user to the current user's subscription list.
     * @param currentUserId The UID of the user who is subscribing.
     * @param targetUserId The UID of the user to be subscribed to.
     * @param listener Callback for handling the operation's result.
     */
    public synchronized void addSubscription(String currentUserId, String targetUserId, DataListener<Void> listener) {
        DocumentReference currentUserRef = userRef.document(currentUserId);
        currentUserRef.update("subscribedList", FieldValue.arrayUnion(targetUserId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Subscription added successfully!");
                    listener.onSuccess(new ArrayList<Void>()); // Passing an empty list as no data needed to be returned.
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding subscription", e);
                    listener.onFailure(e.getMessage());
                });
    }




}
