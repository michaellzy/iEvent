package com.example.ievent.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityMainBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    private RecyclerView recyclerViewRec;

    private RecommendedActivitiesAdapter recEventAdapter;

    private ProgressBar progressBar;

    private boolean isLoading = false;

    private boolean isAllEvents = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("MainActivity", "onCreate executed");
        progressBar = findViewById(R.id.progressBar_main);

        recyclerViewRec = findViewById(R.id.recycler_view_recommended);
        recEventAdapter = new RecommendedActivitiesAdapter(new ArrayList<>());
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRec.setAdapter(recEventAdapter);

        // show events stored in Firestore
        loadMoreEvents();
        recyclerViewRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                if (totalItem <= (lastVisible + 3)) {
                    if (!isLoading) {
                        loadMoreEvents();
                        isLoading = true;
                    }
                }
            }
        });
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

        // Initialize FloatingActionButton and set its click listener
        FloatingActionButton fabRelease = findViewById(R.id.fab_release);
        fabRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to open EventReleaseActivity
                db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
                    @Override
                    public void onSuccess(ArrayList<User> data) {
                        User curUser = data.get(0);
                        Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
                        intent.putExtra("userName", curUser.getUserName());
                        intent.putExtra("email", curUser.getEmail());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        });


        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid());
        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
        });
    }

    private void loadMoreEvents() {
        progressBar.setVisibility(View.VISIBLE);
        db.getEvents(25, new EventDataListener() {
            @Override
            public void isAllData(boolean isALl) {
                if (isALl) {
                    isLoading = false;
                }
            }

            @Override
            public void onSuccess(ArrayList<Event> data) {
                runOnUiThread(() -> {
                    recEventAdapter.setEvents(data);
                    isLoading = false;
                    recEventAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }

        });
    }
}