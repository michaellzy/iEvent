package com.example.ievent.entity;


import java.util.ArrayList;

public class User {
    private String userName;
    private String email;

    private String uid;




    public String getAvatar() {
        return avatar;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    private String avatar;

    public User() {

    }

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

    public String getUid() {
        return uid;
    }


}
