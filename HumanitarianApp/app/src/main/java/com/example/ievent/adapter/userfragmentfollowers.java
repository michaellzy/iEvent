package com.example.ievent.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.activity.P2PChatActivity;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for user fragment followers
 * This class is used to handle the user fragment followers adapter
 * @author Xuan Li
 * @author Qianwen Shen
 * @author Tengkai Wang
 */
public class userfragmentfollowers extends RecyclerView.Adapter<userfragmentfollowers.ViewHolder> {
    private List<User> UserList;

    public userfragmentfollowers() {
        this.UserList = new ArrayList<>();
    }

    public userfragmentfollowers(ArrayList<User> users) {
        this.UserList = users;
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
        holder.name.setText(user.getUserName());
        Glide.with(holder.imageView.getContext())
                .load(user.getAvatar())  // Ensure 'getImg()' returns a valid URL or path to the image
                .placeholder(R.drawable.default_avatar)  // Default image if none found
                .into(holder.imageView);

        holder.chatImage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), P2PChatActivity.class);
            intent.putExtra("receiver", UserList.get(position));
            startActivity(v.getContext(), intent, null);
        });
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public void setUsers(ArrayList<User> users) {
        clearUsers();
        UserList.addAll(users);
    }

    public void clearUsers() {
        UserList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;

        ImageView chatImage;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.subscriber_name);
            imageView = view.findViewById(R.id.subscriber_image);
            chatImage = view.findViewById(R.id.chat_icon);
        }
    }
}
