package com.example.ievent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for user subscription
 * @author Xuan Li
 */
public class userfragmentsubscriptionAdapter extends RecyclerView.Adapter<userfragmentsubscriptionAdapter.ViewHolder> {
    private List<User> UserList;

    public userfragmentsubscriptionAdapter() {
        this.UserList = new ArrayList<User>();
    }
    public userfragmentsubscriptionAdapter(ArrayList<User> users) {
        this.UserList = users;
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
        Glide.with(holder.imageView.getContext())
                .load(user.getAvatar())  // Ensure 'getImg()' returns a valid URL or path to the image
                .placeholder(R.drawable.default_avatar)  // Default image if none found
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
//        return activitiesList.size();
        return UserList.size();
    }

    public void setUsers(ArrayList<User> users) {
        clearUsers();
        UserList.addAll(users);
        // notifyDataSetChanged();
    }

    public void clearUsers() {
        UserList.clear();
//        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.subscriber_name);
            imageView = view.findViewById(R.id.subscriber_image);
        }
    }
}
