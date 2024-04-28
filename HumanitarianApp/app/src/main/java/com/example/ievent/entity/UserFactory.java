package com.example.ievent.entity;

import java.util.HashMap;
import java.util.Map;

public class UserFactory {

    private static final Map<String, User> userMap = new HashMap<>();
    public static User createUser(String type, String uid) {
        if (userMap.containsKey(uid)) {
            return userMap.get(uid);
        }
        User newUser;
        if (type.equalsIgnoreCase("organizer")) {
            newUser = new Organizer(uid);
        } else if (type.equalsIgnoreCase("participant")) {
            newUser =  new Participant(uid);
        } else if (type.equalsIgnoreCase("user")){
            newUser = new User(uid);
        } else {
            return null;
        }

        userMap.put(uid, newUser);

        return newUser;
    }
}
