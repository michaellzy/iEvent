package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;

// 对于推荐活动的RecyclerView
public class RecommendedActivitiesAdapter extends RecyclerView.Adapter<RecommendedActivitiesAdapter.ViewHolder> {
//    private List<ActivityModel> activitiesList;

//    public RecommendedActivitiesAdapter(List<ActivityModel> activitiesList) {
//        this.activitiesList = activitiesList;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_base_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ActivityModel activity = activitiesList.get(position);
        // 在这里设置你的ImageView和TextViews
    }

    @Override
    public int getItemCount() {
//        return activitiesList.size();
        return -1;
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
