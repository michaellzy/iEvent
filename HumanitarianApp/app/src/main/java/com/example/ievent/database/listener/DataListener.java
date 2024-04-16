package com.example.ievent.database.listener;

import java.util.ArrayList;

public interface DataListener<T> {
    void onSuccess(ArrayList<T> data);

    void onFailure(String errorMessage);
}

