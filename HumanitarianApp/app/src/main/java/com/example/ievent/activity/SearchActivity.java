package com.example.ievent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ievent.R;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

public class SearchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        String userId = getIntent().getStringExtra("UID");
//        UserDataManager.getInstance().getLoggedInUser(userId, this);
        Button filterbutton = findViewById(R.id.filter_button);

        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchActivity.this);

                View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();
            }
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
//                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_search) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    viewPager.setCurrentItem(1);
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
        // gets data here
    }
}
