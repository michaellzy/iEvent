package com.example.ievent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.database.IEventDatabase;

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

    // ChatLog class
    public static class ChatLog {
        private String sender;
        private String message;
        private long timestamp;

        public ChatLog(String sender, String message, long timestamp) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
