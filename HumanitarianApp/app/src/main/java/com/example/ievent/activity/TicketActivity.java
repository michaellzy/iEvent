package com.example.ievent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.adapter.userfragmentticketsAdapter;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * This class is used to handle the ticket activity.
 * This class is used to display the tickets that the user has purchased.
 *
 * @author Qianwen Shen
 */
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_ticket) {
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                return true;
            }
            return false;
        });


        String uid = mAuth.getUid();
        UserDataManager.getInstance().
                getParticipantEvents(uid, new EventDataListener() {
                    @Override
                    public void isAllData(boolean isAll) {

                    }

                    @Override
                    public void onSuccess (ArrayList < Event > events) {
                        recyclerView.setAdapter(new userfragmentticketsAdapter(events));
                    }

                    @Override
                    public void onFailure (String errorMessage){
                        Log.d("Ticket","Failed to load tickets: " + errorMessage);
                    }
                });


    }
}