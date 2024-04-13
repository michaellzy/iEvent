package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.UserDataManager;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;

public class MainActivity extends AppCompatActivity implements UserDataListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userId = getIntent().getStringExtra("UID");
//        UserDataManager.getInstance().getLoggedInUser(userId, new UserDataListener() {
//            @Override
//            public void onCurrentUser(User user) {
//                Toast.makeText(MainActivity.this, "Welcome, " + user.getUserName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        UserDataManager.getInstance().getLoggedInUser(userId, this);
    }


    @Override
    public void onCurrentUser(User user) {
        Toast.makeText(MainActivity.this, "Welcome, " + user.getUserName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}