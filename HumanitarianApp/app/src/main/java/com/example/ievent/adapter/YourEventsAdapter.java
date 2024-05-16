package com.example.ievent.adapter;

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
import com.example.ievent.activity.EventDetailActivity;
import com.example.ievent.entity.Event;

import java.util.List;

/**
 * Adapter for your events
 * This class is used to handle the your events adapter
 * @author Haolin Li
 * @author Qianwen Shen
 */
public class YourEventsAdapter extends RecyclerView.Adapter<YourEventsAdapter.ViewHolder> {

    private List<Event> eventList;

    public YourEventsAdapter(List<Event> eventList) {this.eventList = eventList;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_base_your_event, parent, false);
        return new ViewHolder(view);
    }
    public void setEvents(List<Event> events) {
        this.eventList = events;
    }



    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getTitle());
        holder.organizer.setText(event.getOrganizer());

        Glide.with(holder.itemView.getContext()).
                load(event.getImg()).
                into(holder.imageView);

        // When the user clicks on the item, the EventDetailActivity will be opened.
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
            intent.putExtra("event", event);
            v.getContext().startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,organizer;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.base_event_imageView);
            name = view.findViewById(R.id.base_event_activity_name);
            organizer = view.findViewById(R.id.base_event_organizer_name);
        }
    }
}