package com.example.ievent.database;

import android.nfc.Tag;
import android.util.Log;

import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * use Firestore database to load, store and update user information
 * This is a Singleton class with thread-safe implementation
 */
public class UserDataManager {

    final String TAG = "userDatabase";
    private static UserDataManager instance;
    private CollectionReference userRef;

    private UserDataManager() {
        userRef = FirebaseFirestore.getInstance().collection("Users");
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


    public synchronized void getLoggedInUser(String uid, UserDataListener listener) {
        DocumentReference docRef = userRef.document(uid);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
           if (task.isSuccessful()) {
               if (document.exists()) {
                   User user = document.toObject(User.class);
                   listener.onCurrentUser(user);
               } else {
                   listener.onFailure("No such document");
               }
           } else {
               listener.onFailure("get failed with " + task.getException().getMessage());
           }
        });

    }
}
