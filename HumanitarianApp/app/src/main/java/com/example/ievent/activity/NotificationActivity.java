package com.example.ievent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ievent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setContentView(R.layout.activity_notification);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
                    startActivity(new Intent(getApplicationContext(), TicketActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
//                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    return true;
                }
                return false;
            }
        });

    }
}