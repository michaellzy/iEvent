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

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for recommended activities
 * @author Haolin Li
 * @author Zhiyuan Lu
 * @author Tengkai Wang
 */
public class RecommendedActivitiesAdapter extends RecyclerView.Adapter<RecommendedActivitiesAdapter.ViewHolder> {
    private List<Event> eventList;

    public RecommendedActivitiesAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_base_recommended, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getTitle());
        holder.time.setText(event.getDateTime());
        holder.price.setText("$" + event.getPrice());
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

    public void setEvents(ArrayList<Event> events) {
        eventList.addAll(events);
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
