package com.example.ievent.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.adapter.P2PChatAdapter;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.databinding.ActivityP2pChatBinding;
import com.example.ievent.entity.ChatMessage;
import java.util.ArrayList;


public class P2PChatActivity extends BaseActivity {

    private ActivityP2pChatBinding binding;

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    private String receiverId;

    // when get the new messages, use this to upload the old messages end at this time: base
    private long timeEnd = 0;

    // number of messages to get: offset
    private int n = 20;

    /** the adapter of the current recycleView */
    P2PChatAdapter P2pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the binding
        binding = ActivityP2pChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set the variables
        setVariables();
    }

    /** set Variables */
    private void setVariables() {
        // initialize the data
        String senderId = mAuth.getUid();
        if(senderId == null || senderId.isEmpty()) {
            senderId = "3";
        }
        String finalSenderId = senderId;

        receiverId = getIntent().getStringExtra("receiverId");
        if(receiverId == null || receiverId.isEmpty()) {
            receiverId = "4";
        }

        // bind listeners
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





        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        // get the last n messages and set the adapter: the first round of fetching data after user enters the chat room
        db.getTheLastChatMessage(senderId, receiverId, n, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Toast.makeText(P2PChatActivity.this, "success to get the message", Toast.LENGTH_SHORT).show();
                Log.i("MESSAGESSSS", "onSuccess: " + data.size());
                messages = data;

                // update the timeEnd
                timeEnd = messages.get(0).getTime() - 1;

                P2pAdapter = new P2PChatAdapter(messages, finalSenderId);
                binding.chatRecyclerView.setAdapter(P2pAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set the adapter
    private void loadMoreMessages() {
        String senderId = mAuth.getUid();
        if(senderId == null || senderId.isEmpty()) {
            senderId = "3";
        }

        db.getChatMessages(n, senderId, receiverId, timeEnd, new DataListener<ChatMessage>() {

            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                if(data.size() == 0) {
                    Toast.makeText(P2PChatActivity.this, "No more messages", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(P2PChatActivity.this, "get new" + data.size(), Toast.LENGTH_SHORT).show();

                // record the position of the first new message
                int startInsertPosition = messages.size();

                messages.addAll(0, data);

                // update the timeEnd
                timeEnd = messages.get(0).getTime() - 1;

                // notify the adapter that the data has been changed
                P2pAdapter.notifyItemRangeInserted(0, data.size());

                // recyclerView.scrollToPosition(startInsertPosition);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
