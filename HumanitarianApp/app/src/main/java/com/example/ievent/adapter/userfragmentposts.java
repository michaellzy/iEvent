package com.example.ievent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.entity.Event;

import java.util.ArrayList;
import java.util.List;

// 对于推荐活动的RecyclerView
public class userfragmentposts extends RecyclerView.Adapter<userfragmentposts.ViewHolder> {
    private List<Event> eventList;

    public userfragmentposts(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_fragment_posts, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ActivityModel activity = activitiesList.get(position);
        // 在这里设置你的ImageView和TextViews
        Event event = eventList.get(position);
        holder.name.setText(event.getTitle());
        holder.time.setText(event.getDateTime());
        holder.price.setText("$" + event.getPrice());
        holder.organizer.setText("iEvent");
    }

    @Override
    public int getItemCount() {
//        return activitiesList.size();
        return eventList.size();
    }

    public void setEvents(ArrayList<Event> events) {
        eventList.addAll(events);
        // notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, time, price, organizer;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView_recommended_activities);
            name = view.findViewById(R.id.textView_recommended_activities_name);
            time = view.findViewById(R.id.textView_recommended_activities_time);
            price = view.findViewById(R.id.textView_recommended_activities_price);
            organizer = view.findViewById(R.id.textView_recommended_activities_organizer);
        }
    }
}
