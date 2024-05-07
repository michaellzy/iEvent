package com.example.ievent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ievent.R;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button filterbutton = findViewById(R.id.filter_button);

        filterbutton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchActivity.this);

            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            bottomSheetDialog.show();
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    // finish();
                    return true;
                } else if (itemId == R.id.navigation_search) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
                    return true;
                }
                return false;
            }
        });


        initSearchWidgets();
        setUpData();
    }

    private void initSearchWidgets(){
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpData(){
        Toast.makeText(this, "Loading data", Toast.LENGTH_SHORT).show();

        // gets data here
        db.getAllEventsByFuzzyName("Saturdays", new EventDataListener() {

            @Override
            public void onSuccess(ArrayList<Event> events) {
                // set up the data here
                // input the event title into a local file
                for (Event event : events) {
                    Log.i("EVENT1111", "onSuccess: " + event.getTitle());

                }
            }

            @Override
            public void onFailure(String error) {
                // handle the error here
                Log.i("EVENT1111", "onFailure: " + error);
            }
        });
    }
}
