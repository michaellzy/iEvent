package com.example.ievent.database.data_manager;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ievent.activity.P2PChatActivity;
import com.example.ievent.database.listener.BlockListener;
import com.example.ievent.database.listener.BlockstateListener;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.entity.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * use Realtime database to load, store and update chat information
 */
public class ChatDataManager {

    private static ChatDataManager instance;


    private DatabaseReference chatRef;

    private DatabaseReference blockRef;

    private ChatDataManager(){
        chatRef = FirebaseDatabase.getInstance().getReference("chats");
        blockRef = FirebaseDatabase.getInstance().getReference("block");
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


    // ----------------------------------- Chat Operations ----------------------------------- //
    public synchronized void blockMessage(String senderId, String receiverId, BlockListener listener){
        blockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = "00"; // 默认结果，表示没有阻止或阻止未生效
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey(); // 获取键，例如 "a-b"
                    Boolean isBlocked = childSnapshot.getValue(Boolean.class); // 获取阻止状态的值
                    if (isBlocked != null && isBlocked) { // 确保值为true
                        String[] parts = key.split("-");
                        if (parts.length == 2) {
                            if (parts[0].equals(senderId) && parts[1].equals(receiverId)) {
                                result = "11"; // 第一个是senderId
                                break;
                            } else if (parts[0].equals(receiverId) && parts[1].equals(senderId)) {
                                result = "10"; // 第一个是receiverId
                                break;
                            }
                        }
                    }
                }
                listener.onSuccess(result); // 使用回调返回结果
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage()); // 处理错误
            }
        });
    }


    public synchronized void CheckBlockStatus(String senderId, String receiverId, BlockstateListener listener) {
        DatabaseReference blockRef = FirebaseDatabase.getInstance().getReference("block");
        String key = senderId + "-" + receiverId;
        blockRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean isBlocked = dataSnapshot.getValue(Boolean.class);
                    listener.onSuccess(isBlocked);
                    Log.d("isBlocked", "isBlocked: " + isBlocked);
                } else {
                    listener.onSuccess(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError.getMessage());
                Log.e("AddBlockMessage", "Database error: " + databaseError.getMessage());
            }
        });
    }

    public synchronized void AddBlockMessage(String senderId, String receiverId) {
        DatabaseReference blockRef = FirebaseDatabase.getInstance().getReference("block");
        String key = senderId + "-" + receiverId;
        blockRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean isBlocked = dataSnapshot.getValue(Boolean.class);
                    if (isBlocked != null) {
                        dataSnapshot.getRef().setValue(!isBlocked);
                        Log.d("AddBlockMessage", "Block status changed for: " + key + " to: " + isBlocked);
                    }
                } else {
                    dataSnapshot.getRef().setValue(true);
                    Log.d("AddBlockMessage", "New block added: " + key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AddBlockMessage", "Database error: " + databaseError.getMessage());
            }
        });
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
