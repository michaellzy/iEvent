package com.example.ievent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.entity.Participant;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.List;

// 对于推荐活动的RecyclerView
public class userfragmentfollowers extends RecyclerView.Adapter<userfragmentfollowers.ViewHolder> {
    private List<User> UserList;

    public userfragmentfollowers() {
        this.UserList = new ArrayList<>();
        this.UserList.add(new Participant("XXX", "XXXX", "XXXX"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_fragment_followers, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = UserList.get(position);
        holder.name.setText("XXXX");
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public void setEvents(ArrayList<User> events) {
        UserList.addAll(events);
        // notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.subscriber_name);
        }
    }
}
