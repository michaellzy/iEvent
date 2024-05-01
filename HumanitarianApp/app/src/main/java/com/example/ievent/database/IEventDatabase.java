package com.example.ievent.database;

import android.net.Uri;
import android.widget.ImageView;

import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.MediaManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;


/**
 *  the database interface, all data operations will be processed inside the class
 */
public class IEventDatabase{

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

    /**
     * update the user avatar
     * @param uid the user id
     * @param avatar the new avatar
     * @param listener the listener to handle the data
     */
    public void updateUserAvatar(String uid, String avatar, UserDataListener listener) {
        UserDataManager.getInstance().updateUserAvatar(uid, avatar, listener);
    }


    // ----------------------------------- EVENTS ----------------------------------- //

    public void addNewEvent(Event e) {
        EventDataManager.getInstance().addNewEvent(e);
    }

    /**
     * get all events by type
     * @param type the type of the event
     * @param listener the listener to handle the data
     */
    public void getAllEventsByType(String type, EventDataListener listener) {
        EventDataManager.getInstance().getAllEventsByType(type, listener);
    }


    /**
     * get all events by name of the event
     * @param name the name of the event
     * @param listener the listener to handle the data
     */
    public void getAllEventsByFuzzyName(String name, EventDataListener listener) {
        EventDataManager.getInstance().getAllEventByFuzzyName(name, listener);
    }

    public void getEvents(int pageSize, EventDataListener listener) {
        EventDataManager.getInstance().loadEvents(pageSize, listener);
    }

    // ----------------------------------- Media Operations ----------------------------------- //
    public void uploadAvatar(String uid, Uri file, DataListener<String> listener){
        MediaManager.getInstance().uploadAvatar(uid, file, listener);
    }

    public void downloadAvatar(ImageView imageView, String uid){
        MediaManager.getInstance().loadAvatarIntoView(imageView, uid);
    }
}
