package com.example.ievent.database.listener;

import com.example.ievent.entity.Event;

import java.util.List;

public interface EventUpdateListener {
    void onEventsUpdated(List<Event> events);
    void onError(String error);
}
