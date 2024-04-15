package com.example.ievent.database.listener;

import com.example.ievent.entity.User;

public interface UserDataListener {
    void onCurrentUser(User user);

    void onFailure(String errorMessage);
}
