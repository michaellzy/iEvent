package com.example.ievent.database.data_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ievent.database.listener.DataListener;
import com.example.ievent.entity.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Objects;


/**
 * use Realtime database to load, store and update chat information
 */
public class ChatDataManager {

    private static ChatDataManager instance;


    private DatabaseReference chatRef;



    private ChatDataManager(){
        chatRef = FirebaseDatabase.getInstance().getReference("chats");
    }


    public synchronized static ChatDataManager getInstance(){
        if (instance == null) {
            synchronized (ChatDataManager.class) {
                if (instance == null) {
                    instance = new ChatDataManager();
                }
            }
        }
        return instance;
    }


    /**
     * store new message to the database
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param message the content of the message
     */
    public synchronized void SendMessage(String senderId, String receiverId, String message) {
        String chatKey = getChatKey(senderId, receiverId);
        ChatMessage chatMessage = new ChatMessage(message, senderId, System.currentTimeMillis());
        chatRef.child(chatKey).push().setValue(chatMessage);
    }


    /**
     * get the last message of the chat
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param n the number of messages to get
     * @param listener the listener to handle the result
     */
    public synchronized void  getTheLastChatMessage(String senderId, String receiverId, int n, DataListener<ChatMessage> listener){
        String chatKey = getChatKey(senderId, receiverId);
        chatRef.child(chatKey).limitToLast(n).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot snapshot : task.getResult().getChildren()) {
                    chatMessages.add(snapshot.getValue(ChatMessage.class));
                }
                listener.onSuccess(chatMessages);
            } else {
                listener.onFailure(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    /**
     * get the last n messages of the chat until a certain time
     * @param number the number of messages to get
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param time the time to end the search
     * @param listener the listener to handle the result
     */
    public synchronized void getMessagesEndCertainTime(int number, String senderId, String receiverId, long time, DataListener<ChatMessage> listener){
        String chatKey = getChatKey(senderId, receiverId);


        // TODO: Maybe can remove orderByChild("time") and use limitToLast(number) only
        chatRef.child(chatKey).orderByChild("time").endAt(time).limitToLast(number).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot snapshot : task.getResult().getChildren()) {
                    chatMessages.add(snapshot.getValue(ChatMessage.class));
                }
                listener.onSuccess(chatMessages);
            } else {
                listener.onFailure(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    /**
     * use this method to get the new messages sent by the sender and receiver
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     * @param time the time to start the search
     * @param listener the listener to handle the result
     */
    public synchronized void getNewMessages(String senderId, String receiverId, long time, DataListener<ChatMessage> listener){
        String chatKey = getChatKey(senderId, receiverId);
        chatRef.child(chatKey).orderByChild("time").startAfter(time + 1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                chatMessages.add(chatMessage);
                listener.onSuccess(chatMessages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String getChatKey(String userId1, String userId2) {
        return userId1.compareTo(userId2) < 0 ? userId1 + "-" + userId2 : userId2 + "-" + userId1;
    }


}
