package com.example.ievent.database.listener;

public interface BlockstateListener {
    void onSuccess(Boolean result);
    void onFailure(String error);
}
