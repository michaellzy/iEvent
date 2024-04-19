package com.example.ievent.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate executed");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                }
                return false;
            }
        });
        db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                Toast.makeText(MainActivity.this, "Welcome" + data.get(0).getUserName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}