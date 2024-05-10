package com.example.ievent.activity;

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

        receiverId = getIntent().getStringExtra("receiverId");
        if(receiverId == null || receiverId.isEmpty()) {
            receiverId = "4";
        }



        // ---  bind listeners --- //
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
        String finalSenderId = senderId;
        binding.buttonSend.setOnClickListener(v -> {
            String message = binding.edittextChat.getText().toString();
            if(message.isEmpty()) {
                Toast.makeText(this, "Please enter the message", Toast.LENGTH_SHORT).show();
                return;
            }

            // send the message
            db.SendMessage(finalSenderId, receiverId, message);
            isSending = true;
            binding.edittextChat.setText("");
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        // get the last n messages and set the adapter: the first round of fetching data after user enters the chat room
        db.getTheLastChatMessage(senderId, receiverId, n, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Toast.makeText(P2PChatActivity.this, "success to get the message", Toast.LENGTH_SHORT).show();

                messages = data;

                // if message size is not only 1 update the timeEnd
                if(!messages.isEmpty()){
                    timeEnd = messages.get(0).getTime() - 1;
                }

                // update the layout of the recyclerView based on the number of messages
                updateRecyclerViewLayout(data.size());

                P2pAdapter = new P2PChatAdapter(messages, finalSenderId);
                binding.chatRecyclerView.setAdapter(P2pAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
            }
        });

        // search from when the user enters the chat room
        timeStart = System.currentTimeMillis();
        // if the data added in database, then update the adapter
        String finalSenderId1 = senderId;
        db.getNewMessages(senderId, receiverId, timeStart, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                Toast.makeText(P2PChatActivity.this, "success to get the message", Toast.LENGTH_SHORT).show();
                Log.i("MESSAGESSSS", "onSuccess: " + data.size());

                messages.addAll(data);
                // update the timeStart
                if (!data.isEmpty()) {
                    timeStart = messages.get(messages.size() - 1).getTime() + 1;
                }

                if (P2pAdapter != null) {
                    P2pAdapter.notifyItemInserted(messages.size() - data.size());
                } else {
                    // if P2pAdapter is null, then initialize it
                    P2pAdapter = new P2PChatAdapter(messages, finalSenderId1);
                    binding.chatRecyclerView.setAdapter(P2pAdapter);
                }

                // if the user is sending the message, then scroll to the bottom
                if (isSending && !messages.isEmpty()) {
                    binding.chatRecyclerView.scrollToPosition(messages.size() - 1);
                    isSending = false;
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(P2PChatActivity.this, "fail to get the message", Toast.LENGTH_SHORT).show();
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
