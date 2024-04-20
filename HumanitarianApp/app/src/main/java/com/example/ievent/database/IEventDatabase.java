package com.example.ievent.database;

import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;


/**
 *  the database interface, all data operations will be processed inside the class
 */
public class IEventDatabase{

    private User currentUser;

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

    /***
     * get the current user
     * @param uid the user id
     * @param listener the listener to handle the data
     */
    public void getLoggedInUser(String uid, UserDataListener listener) {
        UserDataManager.getInstance().getLoggedInUser(uid, listener);
    }


    public void addNewEvent(Event e) {
        EventDataManager.getInstance().addNewEvent(e);
    }

    public void getAllEventsByType(String type, EventDataListener listener) {
        EventDataManager.getInstance().getAllEventsByType(type, listener);
    }

    public void getEvents(int pageSize, EventDataListener listener) {
        EventDataManager.getInstance().loadEvents(pageSize, listener);
    }

}
