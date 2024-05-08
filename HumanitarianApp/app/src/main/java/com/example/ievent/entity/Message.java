package com.example.ievent.entity;

public class Message {
    private String text; // 消息文本
    private boolean isSentByCurrentUser; // 当前用户是否为发送者

    public Message(String text, boolean isSentByCurrentUser) {
        this.text = text;
        this.isSentByCurrentUser = isSentByCurrentUser;
    }

    public String getText() {
        return text;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }
}

