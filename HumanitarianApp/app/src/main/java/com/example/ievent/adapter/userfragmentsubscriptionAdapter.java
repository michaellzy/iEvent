package com.example.ievent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.List;

// 对于推荐活动的RecyclerView
public class userfragmentsubscriptionAdapter extends RecyclerView.Adapter<userfragmentsubscriptionAdapter.ViewHolder> {
    private List<User> UserList;

    public userfragmentsubscriptionAdapter() {
        this.UserList = new ArrayList<User>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_fragment_subscriptions, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ActivityModel activity = activitiesList.get(position);
        // 在这里设置你的ImageView和TextViews
        User user = UserList.get(position);
        holder.name.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
//        return activitiesList.size();
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
