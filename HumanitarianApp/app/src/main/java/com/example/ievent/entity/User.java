package com.example.ievent.entity;

public class User {
    private String userName = "XXX";
    private String email;

    private String uid;

    public User() {}

    public User(String uid) {
        this.uid = uid;
    }
    public User(String uid, String email, String userName){
        this.uid = uid;
        this.email = email;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }
}
