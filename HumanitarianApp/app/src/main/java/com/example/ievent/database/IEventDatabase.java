package com.example.ievent.database;

import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;


/**
 *  the database interface, all data operations will be processed inside the class
 */
public class IEventDatabase {

    private static IEventDatabase instance;


    private IEventDatabase(){
    }


    public static synchronized IEventDatabase getInstance() {
        if (instance == null) {
            synchronized (IEventDatabase.class) {
                if (instance == null) {
                    instance = new IEventDatabase();
                }
            }
        }
        return instance;
    }


    /***
     * store new user to the database during sign up phase
     * @param user the new user to add to
     */
    public void addNewUser(String uid, User user) {
        UserDataManager.getInstance().addNewUser(uid, user);
    }


    public void getLoggedInUser(String uid, UserDataListener listener) {
        UserDataManager.getInstance().getLoggedInUser(uid, listener);
    }
}
