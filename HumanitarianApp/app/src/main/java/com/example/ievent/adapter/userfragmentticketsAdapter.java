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
import com.example.ievent.entity.Event;

import java.util.ArrayList;
import java.util.List;

// 对于推荐活动的RecyclerView
public class userfragmentticketsAdapter extends RecyclerView.Adapter<userfragmentticketsAdapter.ViewHolder> {
    private List<Event> eventList;

    public userfragmentticketsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_fragment_tickets, parent, false);
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
        holder.location.setText(event.getLocation());
//        holder.price.setText("$" + event.getPrice());
//        holder.organizer.setText("Organized by iEvent");  // Assuming you have an organizer field or similar

        // Load event image using Glide
        Glide.with(holder.imageView.getContext())
                .load(event.getImg())  // Ensure 'getImg()' returns a valid URL or path to the image
                .placeholder(R.drawable.default_avatar)  // Default image if none found
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
//        return activitiesList.size();
        return eventList.size();
    }

    public void setEvents(ArrayList<Event> events) {
        eventList.addAll(events);   // Add all new events

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, time, price,location, organizer;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView_event);
            name = view.findViewById(R.id.textView_event_name);
            time = view.findViewById(R.id.textView_event_time);
            location = view.findViewById(R.id.textView_event_location);
            price = view.findViewById(R.id.textView_recommended_activities_price);
            organizer = view.findViewById(R.id.textView_recommended_activities_organizer);
        }
    }
}
