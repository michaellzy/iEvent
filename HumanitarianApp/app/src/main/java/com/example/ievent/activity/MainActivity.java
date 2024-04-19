package com.example.ievent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.adapter.YourEventsAdapter;
import com.example.ievent.database.UserDataManager;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements UserDataListener {
    private RecyclerView recommended_recyclerview;
    private RecyclerView your_event_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate executed");

        // 确保底部导航的设置没有被覆盖
//        if (bottomNavigationView != null) {
//            Log.d("MainActivity", "BottomNavigationView is not null");
//        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    viewPager.setCurrentItem(1);
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    viewPager.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
//                    viewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
        String userId = getIntent().getStringExtra("UID");
//        UserDataManager.getInstance().getLoggedInUser(userId, new UserDataListener() {
//            @Override
//            public void onCurrentUser(User user) {
//                Toast.makeText(MainActivity.this, "Welcome, " + user.getUserName(), Toast.LENGTH_SHORT).show();
//            }
//        });

        recommended_recyclerview=findViewById(R.id.recycler_view_recommended);
        recommended_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        your_event_recyclerview=findViewById(R.id.recycler_view_your_event);
        your_event_recyclerview.setLayoutManager(new LinearLayoutManager(this));


        // UserDataManager.getInstance().getLoggedInUser(userId, this);
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