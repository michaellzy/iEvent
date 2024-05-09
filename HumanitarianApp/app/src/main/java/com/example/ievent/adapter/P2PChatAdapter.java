package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.entity.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class P2PChatAdapter extends RecyclerView.Adapter<P2PChatAdapter.MessageViewHolder> {

    private List<ChatMessage> messages;

    private String senderId;



    public P2PChatAdapter(List<ChatMessage> messages, String senderId) {
        this.messages = messages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 1 ? R.layout.message_item : R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
//        uid = FirebaseAuth.getInstance().getUid();
        if(senderId == null || senderId.isEmpty()) return 3;
        if(senderId.equals(messages.get(position).getUserId())){
            return 1;
        }else
            return 0;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.chat_recycler_view);
        }
    }
}

