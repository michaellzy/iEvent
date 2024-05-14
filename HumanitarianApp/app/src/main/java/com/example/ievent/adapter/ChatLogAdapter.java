package com.example.ievent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.database.IEventDatabase;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.ChatMessage;
import com.example.ievent.entity.User;
import com.example.ievent.global.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChatLogAdapter extends RecyclerView.Adapter<ChatLogAdapter.ChatLogViewHolder> {

    private List<String> receiverIds;

    private String senderId;

    private Context context;

    private IEventDatabase database;


    private static final String TAG = "ChatLogAdapter";

    // Constructor
    public ChatLogAdapter(Context context, ArrayList<String> receiverIds, String senderId) {
        this.context = context;
        this.receiverIds = receiverIds;
        this.senderId = senderId;
        this.database = IEventDatabase.getInstance();
    }

    @NonNull
    @Override
    public ChatLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_sub, parent, false);
        return new ChatLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatLogViewHolder holder, int position) {
        String receiverId = receiverIds.get(position);

        database.getTheLastChatMessage(senderId, receiverId, 1, new DataListener<ChatMessage>() {
            @Override
            public void onSuccess(ArrayList<ChatMessage> data) {
                ChatMessage chatMessage = data.get(0);
                holder.messageTextView.setText(chatMessage.getMessage());
                holder.timestampTextView.setText(Utility.TimeFormatter.formatTimestamp(chatMessage.getTime()));
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i(TAG, "onFailure: " + errorMessage);
            }
        });

        database.getUserById(receiverId, new UserDataListener() {


            @Override
            public void onSuccess(ArrayList<User> data) {
                User user = data.get(0);
                holder.receiverTextView.setText(user.getUserName());
                Glide.with(holder.itemView.getContext())
                        .load(user.getAvatar())
                        .placeholder(R.drawable.default_avatar)
                        .into(holder.avatarImageView);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.i(TAG, "onFailure: " + errorMessage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return receiverIds.size();
    }

    // View Holder
    public static class ChatLogViewHolder extends RecyclerView.ViewHolder {

        TextView receiverTextView;
        TextView messageTextView;
        TextView timestampTextView;
        ImageView avatarImageView;

        public ChatLogViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverTextView = itemView.findViewById(R.id.chatlog_name);
            messageTextView = itemView.findViewById(R.id.chatlog_message);
            timestampTextView = itemView.findViewById(R.id.chatlog_time);
            avatarImageView = itemView.findViewById(R.id.chatlog_avatar);
        }
    }
}
