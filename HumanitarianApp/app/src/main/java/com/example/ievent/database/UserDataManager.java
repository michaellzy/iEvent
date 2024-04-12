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
     * check the duplicate key(userName) in database. The user can not register
     * if the user name is already exists
     * @param userName the user name that the user filled in sign up page.
     * @param listener a callback interface to handle the result of firestore query
     */
    public synchronized void isValidUserName(String userName, UserDataListener listener) {
        DocumentReference docRef = userRef.document(userName);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                listener.onUserNameValid(!document.exists());
            } else {
                Log.d(TAG, "Query failed with: " + task.getException());
            }
        });
    }

    /***
     * store new user to the database during sign up phase
     * @param user the new user to add to
     */
    public synchronized void addNewUser(User user) {
        userRef.document(user.getUserName()).set(user);
    }




}
