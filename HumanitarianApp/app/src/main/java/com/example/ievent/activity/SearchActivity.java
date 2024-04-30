package com.example.ievent.activity;

import android.content.ClipData;
import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private RecommendedActivitiesAdapter recommendedActivitiesAdapter;
    private List<Event> eventList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button filterbutton = findViewById(R.id.filter_button);


        recyclerView = findViewById(R.id.searchResultRecyclerView);
        recommendedActivitiesAdapter = new RecommendedActivitiesAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recommendedActivitiesAdapter);

        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                performSearch(newText);
                return false;
            }
        });

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


//        initSearchWidgets();
//        setUpData();
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
    private void performSearch(String query) {
        Log.d("SearchActivity", "performSearch: Start, Query: " + query);
        db.getAllEventsByFuzzyName(query, new EventDataListener() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                Log.d("SearchActivity", "Search onSuccess: Number of events found: " + events.size());
                eventList.clear();
                eventList.addAll(events);
                recommendedActivitiesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Log.e("SearchActivity", "Search onFailure: " + error);
                Toast.makeText(SearchActivity.this, "Search Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
