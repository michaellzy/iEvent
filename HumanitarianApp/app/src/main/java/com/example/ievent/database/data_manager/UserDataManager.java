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

}
