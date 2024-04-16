package com.example.ievent.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity implements UserDataListener {
    protected FirebaseAuth mAuth;

    protected Firebase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (Event bollywoodEventsSydney : EventDataManager.getInstance().getAllEventsByType("bollywood events sydney")) {
            System.out.println(bollywoodEventsSydney);
        }
    }

    @Override
    public void onCurrentUser(User user) {
    }

    @Override
    public void onFailure(String errorMessage) {
    }
}