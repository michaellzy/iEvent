package com.example.ievent.database;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.MediaManager;
import com.example.ievent.database.data_manager.OrganizerDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;

import java.util.ArrayList;

import java.util.EventListener;


/**
 *  the database interface, all data operations will be processed inside the class
 */
public class IEventDatabase{

    private static IEventDatabase instance;


    private IEventDatabase(){
        EventDataManager.getInstance();
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

    public void addNewOrganizer(String uid, Organizer organizer) {
        OrganizerDataManager.getInstance().addOrganizer(uid, organizer);
    }

    public void getOrganizer(String uid, OrgDataListener listener) {
        OrganizerDataManager.getInstance().getOrganizer(uid, listener);
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
    public void getGreaterThan(double price, EventDataListener listener){
        EventDataManager.getInstance().getGreaterThan(price,listener);
    }
    public void getLessThan(double price, EventDataListener listener){
        EventDataManager.getInstance().getLessThan(price,listener);
    }
    public void getDateAfter(int timestamp, EventDataListener listener){
        EventDataManager.getInstance().getDateAfter(timestamp,listener);

    }
    public void getDateBefore(int timestamp, EventDataListener listener){
        EventDataManager.getInstance().getDateBefore(timestamp,listener);

    }
    public void getAllEventsByPrice(double price, EventDataListener listener){
        EventDataManager.getInstance().getAllEventsByPrice(price,listener);

    }
    public void getAllEventsByDate(long timestamp, EventDataListener listener){
        EventDataManager.getInstance().getAllEventsByDate(timestamp,listener);
    }
    // ----------------------------------- Media Operations ----------------------------------- //
    public void uploadAvatar(String uid, Uri file, DataListener<String> listener){
        MediaManager.getInstance().uploadAvatar(uid, file, listener);
    }

    public void downloadAvatar(ImageView imageView, String uid){
        MediaManager.getInstance().loadAvatarIntoView(imageView, uid);
    }

    public void uploadEventImage(Uri file, DataListener<String> listener) {
        MediaManager.getInstance().uploadEventImg(file, listener);
    }

//    public void fetchOrganizedEvent(String uid, DataListener<Event> listener) {
//        OrganizerDataManager.getInstance().fetchOrganizedEvent(uid, listener);
//    }

    public void fetchOrganizedData(String uid, OrganizedEventListener listener){
        OrganizerDataManager.getInstance().fetchOragnizedData(uid, listener);
    }

    public void fetchDocuments(ArrayList<String> EventIds , EventDataListener listener){
        EventDataManager.getInstance().fetchDocuments(EventIds,listener);
    }

    public synchronized void getAllEventsByIds(ArrayList<String> ids, EventDataListener listener){
        EventDataManager.getInstance().getAllEventsByIds(ids, listener);
    }
}
