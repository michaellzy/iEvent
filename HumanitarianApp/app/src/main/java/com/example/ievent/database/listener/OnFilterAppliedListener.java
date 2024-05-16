package com.example.ievent.database.listener;

public interface OnFilterAppliedListener {
    void onFilterApplied(String type, String startDate, String endDate, double minPrice, double maxPrice);
}