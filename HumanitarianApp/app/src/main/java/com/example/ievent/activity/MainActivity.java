package com.example.ievent.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
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
        UserDataManager.getInstance().getLoggedInUser(userId, new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                Toast.makeText(MainActivity.this, "Welcome, " + data.get(0).getUserName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}