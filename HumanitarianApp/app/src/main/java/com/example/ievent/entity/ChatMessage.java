package com.example.ievent.entity;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String message;

    private String userId;

    private long time;


    public ChatMessage() {
    }

    public ChatMessage(String message, String userId, long time) {
        this.message = message;
        this.userId = userId;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
