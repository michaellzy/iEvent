package com.example.ievent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.adapter.userfragmentticketsAdapter;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;


public class TicketActivity extends BaseActivity {
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        recyclerView = findViewById(R.id.recycler_tickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView.setSelectedItemId(R.id.navigation_ticket);
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
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                }
                return false;
            }
        });

        String uid = mAuth.getUid();
        UserDataManager.getInstance().

                getParticipantEvents(uid, new EventDataListener() {
                    @Override
                    public void onSuccess (ArrayList < Event > events) {
                        recyclerView.setAdapter(new userfragmentticketsAdapter(events));
                    }

                    @Override
                    public void onFailure (String errorMessage){
                        Toast.makeText(TicketActivity.this, "Failed to load tickets: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


}
}