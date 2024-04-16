package com.example.ievent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ievent.R;
import com.example.ievent.entity.Event;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BaseEventAdapter extends RecyclerView.Adapter<BaseEventAdapter.BaseEventViewHolder> {

    private List<Event> events;

    public BaseEventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public BaseEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_base_event, parent, false);
        return new BaseEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseEventViewHolder holder, int position) {
        Event event = events.get(position);
        // 绑定数据到ViewHolder

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class BaseEventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        // 定义更多的视图

        public BaseEventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName); // 根据你的布局更改ID
            // 初始化更多的视图
        }
    }
}
