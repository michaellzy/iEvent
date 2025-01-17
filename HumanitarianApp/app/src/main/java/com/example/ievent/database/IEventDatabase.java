package com.example.ievent.database;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.example.ievent.database.data_manager.ChatDataManager;
import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.MediaManager;
import com.example.ievent.database.data_manager.OrganizerDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.BlockListener;
import com.example.ievent.database.listener.BlockstateListener;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.FollowerNumListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.ChatMessage;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *  the database interface, all data operations will be processed inside the class, and
 *  @author team
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

    // ----------------------------------- USER Operation ----------------------------------- //
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


    /**
     * Retrieves a simgle user based on their ID.
     * @param uid The user's ID.
     * @param listener Listener to handle the result or failure.
     */
    public void getUserById(String uid, UserDataListener listener) {
        UserDataManager.getInstance().getUserById(uid, listener);
    }

    // // -----------------------------------organizer Operation ----------------------------------- //

    /**
     * store new organizer to the database during sign up phase
     * @param uid the user id
     * @param organizer the new organizer to add to
     */
    public void addNewOrganizer(String uid, Organizer organizer) {
        OrganizerDataManager.getInstance().addOrganizer(uid, organizer);
    }

    /**
     * get the user object by the organizer id
     * @param uid the user id
     * @param listener the listener to handle the data
     */
    public void getOrganizer(String uid, OrgDataListener listener) {
        OrganizerDataManager.getInstance().getOrganizer(uid, listener);
    }



    // ----------------------------------- EVENTS ----------------------------------- //

    /**
     * store new event to the database
     * @param e the new event to add to
     */
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

    /**
     * get all events by the page size
     * @param pageSize the number of events to get
     * @param listener the listener to handle the data
     */
    public void getEvents(int pageSize, EventDataListener listener) {
        EventDataManager.getInstance().loadEvents(pageSize, listener);
    }

    public void getGreaterThan(double price, EventDataListener listener){
        EventDataManager.getInstance().getGreaterThan(price,listener);
    }
    public void getLessThan(double price, EventDataListener listener){
        EventDataManager.getInstance().getLessThan(price,listener);
    }

    public void getAllEventsByPrice(double price, EventDataListener listener){
        EventDataManager.getInstance().getAllEventsByPrice(price,listener);

    }

    public void getAllEventsByDate(long timestamp, EventDataListener listener){
        EventDataManager.getInstance().getAllEventsByDate(timestamp,listener);
    }
    // ----------------------------------- Media Operations ----------------------------------- //

    /**
     * upload the avatar of the user
     * @param uid the user id
     * @param file the file to upload
     * @param listener the listener to handle the data
     */
    public void uploadAvatar(String uid, Uri file, DataListener<String> listener){
        MediaManager.getInstance().uploadAvatar(uid, file, listener);
    }

    /**
     * download the avatar of the user
     * @param imageView the image view to display the avatar
     * @param uid the user id
     * @param activity the activity to display the image
     */
    public void downloadAvatar(ImageView imageView, String uid, Activity activity) {
        MediaManager.getInstance().loadAvatarIntoView(imageView, uid, activity);
    }

    /**
     * upload the event image
     * @param file the file to upload
     * @param listener the listener to handle the data
     */
    public void uploadEventImage(Uri file, DataListener<String> listener) {
        MediaManager.getInstance().uploadEventImg(file, listener);
    }


    public void fetchOrganizedData(String uid, OrganizedEventListener listener){
        OrganizerDataManager.getInstance().fetchOragnizedData(uid, listener);
    }

    /**
     * fetch the documents of the events by the event ids
     * @param EventIds the list of event ids
     * @param listener the listener to handle the data
     */
    public void fetchDocuments(ArrayList<String> EventIds , EventDataListener listener){
        EventDataManager.getInstance().fetchDocuments(EventIds,listener);
    }


    // ----------------------------------- Chat Operations ----------------------------------- //
    public void blockMessage(String senderId, String receiverId, BlockListener listener){
        ChatDataManager.getInstance().blockMessage(senderId, receiverId, listener);
    }

    public void CheckBlockStatus(String senderId, String receiverId, BlockstateListener listener){
        ChatDataManager.getInstance().CheckBlockStatus(senderId, receiverId, listener);
    }
    public void AddBlockMessage(String senderId, String receiverId){
        ChatDataManager.getInstance().AddBlockMessage(senderId, receiverId);
    }

    /**
     * store new message to the database
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param message the content of the message
     */
    public void SendMessage(String senderId, String receiverId, String message) {
        ChatDataManager.getInstance().SendMessage(senderId, receiverId, message);
    }


    /**
     * get the last message of the chat
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param n the number of messages to get
     * @param listener the listener to handle the result
     */
    public void getTheLastChatMessage(String senderId, String receiverId, int n,  DataListener<ChatMessage> listener){
        ChatDataManager.getInstance().getTheLastChatMessage(senderId, receiverId,n, listener);
    }


    /**
     * get the last n messages of the chat until a certain time
     * @param number the number of messages to get
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param time the time to end the search
     * @param listener the listener to handle the result
     */
    public void getChatMessages(int number, String senderId, String receiverId, long time, DataListener<ChatMessage> listener){
        ChatDataManager.getInstance().getMessagesEndCertainTime(number, senderId, receiverId, time, listener);
    }



    /**
     * use this method to get the new messages sent by the sender and receiver
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param time the time to start the search
     * @param listener the listener to handle the result
     */
    public void getNewMessages(String senderId, String receiverId, long time, DataListener<ChatMessage> listener){
        ChatDataManager.getInstance().getNewMessages(senderId, receiverId, time, listener);
    }


    /**
     * set the chatlog of two users
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param listener the listener to handle the result
     */
    public synchronized void setChatLog(String senderId, String receiverId, DataListener<Boolean> listener) {
        ChatDataManager.getInstance().setChatLog(senderId, receiverId, listener);
    }

    /**
     * get the chatlog of a user
     * @param userId the id of the user
     * @param listener the listener to handle the result
     */
    public synchronized void getChatLog(String userId, DataListener<String> listener) {
        ChatDataManager.getInstance().getChatLog(userId, listener);
    }


    // ----------------------------------- Other added Operations ----------------------------------- //

    //followers and subscriptions
    /**
     * Adds an event to a user's list of events.
     * @param uid The user ID to which the event will be added.
     * @param eventId The ID of the event to add.
     * @param listener A callback to handle the outcome of the add operation.
     */

    public synchronized void addEventToUser(String uid, String eventId, DataListener<Void> listener){
        UserDataManager.getInstance().addEventToUser(uid,eventId,listener);
    }


    /**
     * Sets up a listener to monitor changes in the number of followers an organizer has. Notification
     * @param organizerId The organizer's user ID whose follower count is to be monitored.
     * @param listener The listener that handles the logic for when the follower count changes.
     */
    public void setupFollowerListener(String organizerId, FollowerNumListener listener){
        OrganizerDataManager.getInstance().setupFollowerListener(organizerId,listener);
    }

    /**
     * Adds a follower to an organizer.
     * @param organizerId The ID of the organizer who will gain a new follower.
     * @param followerId The ID of the user who will follow the organizer.
     * @param listener A callback to handle the result of the operation.
     */

    public void addFollower(String organizerId, String followerId, DataListener<Void> listener){
        OrganizerDataManager.getInstance().addFollower(organizerId,followerId,listener);
    }

    public void removeFollower(String organizerId, String followerId, DataListener<Void> listener) {
        OrganizerDataManager.getInstance().removeFollower(organizerId,followerId,listener);
    }

    /**
     * Adds a user to the current user's subscription list.
     * @param currentUserId The UID of the user who is subscribing.
     * @param targetUserId The UID of the user to be subscribed to.
     * @param listener Callback for handling the operation's result.
     */
    public synchronized void addSubscription(String currentUserId, String targetUserId, DataListener<Void> listener){
        UserDataManager.getInstance().addSubscription(currentUserId,targetUserId,listener);
    }

    /**
     * Removes a user from the current user's subscription list.
     * @param currentUserId The UID of the user who is unsubscribing.
     * @param targetUserId The UID of the user to be unsubscribed from.
     * @param listener Callback for handling the operation's result.
     */
    public synchronized void removeSubscription(String currentUserId, String targetUserId, DataListener<Void> listener){
        UserDataManager.getInstance().removeSubscription(currentUserId,targetUserId,listener);
    }

    /**
     * Retrieves multiple users based on a list of user IDs.
     * @param ids List of user IDs.
     * @param listener Listener to handle the result or failure.
     */
    public synchronized void getAllUsersByIds(List<String> ids, UserDataListener listener){
        UserDataManager.getInstance().getAllUsersByIds(ids,listener);
    }

    /**
     * Retrieves the events that a participant is involved in.
     * @param uid The user ID of the participant whose events are to be retrieved.
     * @param listener Listener to handle the results or failures of the data retrieval.
     */
    public synchronized void getParticipantEvents(String uid, EventDataListener listener){
        UserDataManager.getInstance().getParticipantEvents(uid,listener);
    }

    public synchronized void getEventsByFilters(String type, String titlePrefix, Date startDate, Date endDate, double minPrice, double maxPrice, EventDataListener listener){
        EventDataManager.getInstance().getEventsByFilters(type,titlePrefix, startDate, endDate,minPrice,maxPrice,listener);
    }


    public synchronized void fetchEventsByOrganizerIds(List<String> organizerIds, EventDataListener listener) {
        EventDataManager.getInstance().fetchEventsByOrganizerIds(organizerIds,listener);
    }

    public synchronized void loadLatestEvent(long timestamp, EventDataListener listener) {
        EventDataManager.getInstance().loadLatestEvent(timestamp, listener);
    }
}
