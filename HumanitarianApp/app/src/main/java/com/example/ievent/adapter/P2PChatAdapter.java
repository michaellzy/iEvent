package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ievent.R;
import com.example.ievent.entity.ChatMessage;
import java.util.List;


public class P2PChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messages;

    private String senderId;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;

    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public P2PChatAdapter(List<ChatMessage> messages, String senderId) {
        this.messages = messages;
        this.senderId = senderId;
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
        } else if (holder instanceof ReceivedMessageHolder) {
            ReceivedMessageHolder receivedHolder = (ReceivedMessageHolder) holder;
            receivedHolder.messageText.setText(message.getMessage());
        }
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
            return VIEW_TYPE_MESSAGE_SENT;
        }else
            return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body_sent);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body_received);
        }
    }

}

