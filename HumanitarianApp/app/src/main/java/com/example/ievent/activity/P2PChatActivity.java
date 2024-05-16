package com.example.ievent.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.adapter.P2PChatAdapter;
import com.example.ievent.database.listener.BlockListener;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityP2pChatBinding;
import com.example.ievent.entity.ChatMessage;
import com.example.ievent.entity.User;
import android.app.AlertDialog;
import java.util.ArrayList;


/**
 * This class is used to display the chat room between two users.
 * @author Tengkai Wang
 * @author Xuan Li
 */
public class P2PChatActivity extends BaseActivity {


    final static String TAG = "P2PChatActivity1111111";

    private ActivityP2pChatBinding binding;

    // check whether the chat log is set
    private boolean isChatLogSet = false;


    private ArrayList<ChatMessage> messages = new ArrayList<>();


    private User receiver;


    // use this to download the old messages end before this time: base
    private long timeEnd = System.currentTimeMillis();


    // use this to download the new messages start after this time: base
    private long timeStart = System.currentTimeMillis();


    // number of messages to get: offset
    private int n = 20;

    // the number of messages which can cover the screen
    private static final int FILLNUMBER = 10;

    /** the adapter of the current recycleView */
    P2PChatAdapter P2pAdapter;


    /** check whether the user is sending the message */
    boolean isSending = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the binding
        binding = ActivityP2pChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set the variables
        setVariables();
    }


    /** set Variables such as the intent data, UI Listeners and layout of the */
    private void setVariables() {
        // --- initialize data --- //
        String senderId = mAuth.getUid();
        if(senderId == null || senderId.isEmpty()) {
            senderId = "3";
        }
        receiver = (User) getIntent().getSerializableExtra("receiver");
        String receiverId = receiver.getUid();


        // ---  bind listeners to recycleView and send button --- //
        setRecycleViewScrollListener();
        setSendButtonListener(senderId, receiverId);



        // --- set the layout manager --- //
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        // --- get the recent messages --- //
        getRecentMessages(senderId, receiver);



        // search from when the user enters the chat room
        // if the data added in database, then update the adapter
        getNewMessages(senderId, receiver);
    }

    /**
     * get the new messages, invocked when new messages are sent or received
     * @param senderId
     * @param receiver
     */
    private void getNewMessages(String senderId, User receiver){
        db.getNewMessages(senderId, receiver.getUid(), timeStart, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Log.i(TAG, "onSuccess: " + data.size());


                messages.addAll(data);
                // update the timeStart
                if (!data.isEmpty()) {
                    timeStart = messages.get(messages.size() - 1).getTime() + 1;
                    if(!isChatLogSet){
                        db.setChatLog(senderId, receiver.getUid(), new DataListener<Boolean>() {
                            @Override
                            public void onSuccess(ArrayList<Boolean> data) {
                                isChatLogSet = true;
                                Log.i(TAG, "onSuccess: " + "success to set the chat log");
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Log.i(TAG, "onFailure: " + "fail to set the chat log");
                            }
                        });
                    }
                }

                if (P2pAdapter != null) {
                    P2pAdapter.notifyItemInserted(messages.size() - data.size());
                }

                // if the user is sending the message, then scroll to the bottom
                if (isSending && !messages.isEmpty()) {
                    binding.chatRecyclerView.scrollToPosition(messages.size() - 1);
                    isSending = false;
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i(TAG, "onFailure: " + "fail to get the message");
            }
        });
    }

    /**
     * get the recent messages
     * @param senderId the id of the sender
     * @param receiver the receiver
     */
    private void getRecentMessages(String senderId, User receiver) {
        // get the last n messages and set the adapter: the first round of fetching data after user enters the chat room
        db.getTheLastChatMessage(senderId, receiver.getUid(), n, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Log.i(TAG, "onSuccess: " + "success to get the message");
                Log.i(TAG, "onSuccess: " + data.size());

                messages = data;


                // if message size is not only 1 update the timeEnd
                if(!messages.isEmpty()){
                    timeEnd = messages.get(0).getTime() - 1;
                }


                // update the layout of the recyclerView based on the number of messages
                updateRecyclerViewLayout(data.size());

                db.getLoggedInUser(senderId, new UserDataListener() {
                    @Override
                    public void onSuccess(ArrayList<User> data) {
                        if (!data.isEmpty()) {
                            User sender = data.get(0);
                            P2pAdapter = new P2PChatAdapter(messages, receiver, sender);
                            binding.chatRecyclerView.setAdapter(P2pAdapter);
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                    }
                });

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * set the send button listener
     * @param senderId the id of the sender
     * @param receiverId the id of the receiver
     */
    private void setSendButtonListener(String senderId, String receiverId) {
        binding.buttonSend.setOnClickListener(v -> {
            db.blockMessage(senderId, receiverId, new BlockListener() {
                @Override
                public void onSuccess(String result) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(P2PChatActivity.this);
                    builder.setTitle("message");
                    builder.setPositiveButton("verify", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    if ("11".equals(result)) {
                        builder.setMessage("Receiver is blocked by yourself. Please unblock first.");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if ("10".equals(result)) {
                        builder.setMessage("You are blocked by the receiver.");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else{
                        String message = binding.edittextChat.getText().toString();
                        if(message.isEmpty()) {
                            Toast.makeText(P2PChatActivity.this, "Please enter the message", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // send the message
                        db.SendMessage(senderId, receiver.getUid(), message);
                        isSending = true;
                        binding.edittextChat.setText("");
                    }
                }
                @Override
                public void onFailure(String error) {
                    // handle possible mistakes
                    Toast.makeText(P2PChatActivity.this, "Error checking block status: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    /**
     * set the recycleView scroll listener to load more messages when the user scrolls to the top
     */
    private void setRecycleViewScrollListener() {
        binding.chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // check whether the user scrolls to the top
                if (!recyclerView.canScrollVertically(-1)) {
                    loadMoreMessages();
                }
            }
        });
    }


    /**
     * load more messages when the user scrolls to the top
     */
    private void loadMoreMessages() {
        String senderId = mAuth.getUid();
        if(senderId == null || senderId.isEmpty()) {
            senderId = "3";
        }

        db.getChatMessages(n, senderId, receiver.getUid(), timeEnd, new DataListener<ChatMessage>() {

            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                if(data.isEmpty()) {
                    Log.i(TAG,  "no more messages");
                    return;
                }

                Log.i(TAG, "get new" + data.size());


                messages.addAll(0, data);

                // update the timeEnd
                timeEnd = messages.get(0).getTime() - 1;

                // notify the adapter that the data has been changed
                P2pAdapter.notifyItemRangeInserted(0, data.size());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i(TAG, "onFailure: " + "fail to get the message");
            }
        });
    }




    /**
     * update the layout of the recyclerView (messages stack from the bottom or top)
     * @param messageCount the number of messages
     */
    private void updateRecyclerViewLayout(int messageCount) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (messageCount < FILLNUMBER) {
            // if the message can't fill the screen, start from the top
            layoutManager.setStackFromEnd(false);
        } else {
            // if the message can fill the screen, start from the bottom
            layoutManager.setStackFromEnd(true);
        }
        binding.chatRecyclerView.setLayoutManager(layoutManager);
    }
}
