package com.example.ievent.database.listener;

import com.example.ievent.entity.Event;

import java.util.List;

public interface OrganizedEventListener {
    void onEventsUpdated(List<String> eventIds);
    void onError(String error);
}

