package com.example.ievent.interfaces;

import com.example.ievent.entity.User;


public interface UserFactory {
    User createUser(String type, String uid, String email, String userName);
}
