package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.entity.ChatMessage;
import com.example.ievent.entity.User;

import java.util.List;


public class P2PChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messages;

    private User receiver;

    private User sender;


    private static final int VIEW_TYPE_MESSAGE_SENT = 1;

    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public P2PChatAdapter(List<ChatMessage> messages,  User receiver, User sender) {
        this.messages = messages;
        this.receiver = receiver;
        this.sender = sender;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder instanceof SentMessageHolder) {
            SentMessageHolder sentHolder = (SentMessageHolder) holder;
            sentHolder.messageText.setText(message.getMessage());
            if(sender != null && sender.getAvatar() != null){
                Glide.with(sentHolder.avatar.getContext())
                        .load(sender.getAvatar())
                        .into(sentHolder.avatar);
            }else{
                Glide.with(sentHolder.avatar.getContext())
                        .load(R.drawable.default_avatar)
                        .into(sentHolder.avatar);
            }
        } else if (holder instanceof ReceivedMessageHolder) {
            ReceivedMessageHolder receivedHolder = (ReceivedMessageHolder) holder;
            receivedHolder.messageText.setText(message.getMessage());
            if(receiver != null && receiver.getAvatar() != null){
                Glide.with(receivedHolder.avatar.getContext())
                        .load(receiver.getAvatar())
                        .into(receivedHolder.avatar);
            }else{
                Glide.with(receivedHolder.avatar.getContext())
                        .load(R.drawable.default_avatar)
                        .into(receivedHolder.avatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(sender.getUid() == null || sender.getUid().isEmpty()) return 3;
        if(sender.getUid().equals(messages.get(position).getUserId())){
            return VIEW_TYPE_MESSAGE_SENT;
        }else
            return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView avatar;

        public SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body_sent);
            avatar = itemView.findViewById(R.id.image_avatar);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ImageView avatar;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body_received);
            avatar = itemView.findViewById(R.id.image_avatar);
        }
    }

}

