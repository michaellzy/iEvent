package com.example.ievent.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ievent.adapter.P2PChatAdapter;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.databinding.ActivityP2pChatBinding;
import com.example.ievent.entity.ChatMessage;

import java.util.ArrayList;


public class P2PChatActivity extends BaseActivity {

    private ActivityP2pChatBinding binding;

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    private String receiverId;

    // when get the new messages, use this to upload the old messages end at this time
    private long timeEnd = 0;

    // number of messages to get
    private int n = 1;

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
        // get the data
        String senderId = mAuth.getUid();
        if(senderId == null || senderId.isEmpty()) {
            senderId = "3";
        }

        receiverId = getIntent().getStringExtra("receiverId");
        if(receiverId == null || receiverId.isEmpty()) {
            receiverId = "4";
        }


//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        layoutManager.setReverseLayout(true);
////        layoutManager.setStackFromEnd(true);
////        binding.chatRecyclerView.setLayoutManager(layoutManager);


        String finalSenderId = senderId;
        db.getTheLastChatMessage(senderId, receiverId, n, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Toast.makeText(P2PChatActivity.this, "success to get the message", Toast.LENGTH_SHORT).show();
                Log.i("MESSAGESSSS", "onSuccess: " + data.size());
                messages = data;
                timeEnd = messages.get(0).getTime() - 1;
                P2PChatAdapter adapter = new P2PChatAdapter(messages, finalSenderId);
                binding.chatRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
            }
        });





        // Set the adapter

    }
}
