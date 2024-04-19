package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;

public class YourEventsAdapter extends RecyclerView.Adapter<YourEventsAdapter.ViewHolder> {
//    private List<YourEventModel> eventsList;

//    public YourEventsAdapter(List<YourEventModel> eventsList) {
//        this.eventsList = eventsList;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_base_your_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        YourEventModel event = eventsList.get(position);
    }

    @Override
    public int getItemCount() {
//        return eventsList.size();
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.base_event_imageView);
            name = view.findViewById(R.id.base_event_activity_name);
        }
    }
}