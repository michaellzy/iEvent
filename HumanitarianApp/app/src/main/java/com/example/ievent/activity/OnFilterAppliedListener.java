package com.example.ievent.activity;

public interface OnFilterAppliedListener {
    void onFilterApplied(String type, String startDate, String endDate, double minPrice, double maxPrice);
}