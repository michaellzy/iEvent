package com.example.ievent.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.data_manager.EventCache;
import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.EventUpdateListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityMainBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView recyclerViewRec;

    private RecommendedActivitiesAdapter recEventAdapter;

    private ProgressBar progressBar;

    private boolean isLoading = false;

    private ArrayList<Event> events;

    private EventUpdateListener updateListener;

    @Override
    protected void onStart() {
        super.onStart();
        EventDataManager.getInstance().addEventListener(updateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventDataManager.getInstance().removeEventListener(updateListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid());
        // manageDataOperations();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("MainActivity", "onCreate executed");
        progressBar = findViewById(R.id.progressBar_main);

        events = new ArrayList<>();
        recyclerViewRec = findViewById(R.id.recycler_view_recommended);
        recEventAdapter = new RecommendedActivitiesAdapter(events);
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRec.setAdapter(recEventAdapter);

        ArrayList<Event> cachedEvents = EventCache.getInstance().getEvents();
        if (!cachedEvents.isEmpty()) {
            recEventAdapter.setEvents(cachedEvents);
            recEventAdapter.notifyDataSetChanged();
        } else {
            // show events stored in FireStore
            if (updateListener == null)
                loadMoreEvents();
        }


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
                    startActivity(new Intent(getApplicationContext(), TicketActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    return true;
                }
                return false;
            }
        });

        // 初始化NavigationView和HeaderView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer_include);
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImageView = headerView.findViewById(R.id.profile_image);
        TextView usernameTextView = headerView.findViewById(R.id.textView_header_name);
        TextView emailTextView = headerView.findViewById(R.id.textView_header_email);

        db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                User user = data.get(0);
                usernameTextView.setText(user.getUserName());
                emailTextView.setText(user.getEmail());
                db.downloadAvatar(profileImageView, mAuth.getCurrentUser().getUid());
                profileImageView.setOnClickListener(v -> {
                    startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
                });
                Toast.makeText(MainActivity.this, "Welcome, " + user.getUserName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid());
        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
        });

        setupNavigationView(navigationView);


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
                        // mStartForResult.launch(intent);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        });

        updateListener = new EventUpdateListener() {
            @Override
            public void onEventsUpdated(List<Event> data) {
                runOnUiThread(() -> {
                    // Additional check for safety
                    events.addAll(0, data);
                    recEventAdapter.notifyItemRangeInserted(0, data.size());
                    Toast.makeText(MainActivity.this, "Updated data", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        };


        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid());
        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
        });
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name_followers);
            String description = getString(R.string.channel_description_followers);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("followers", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    // 设置 NavigationView 的选项监听器
    private void setupNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        Intent intent;

        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_home) {
            // Handle navigation to home
        } else if (itemId == R.id.nav_settings) {
            // Handle navigation to settings
        } else if (itemId == R.id.nav_logout) {
            // Handle logout
            intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close current activity
        } else {
            // Handle other menu items if needed
        }

        // Highlight the selected item in the navigation drawer
        menuItem.setChecked(true);

        // Set action bar title if you have a toolbar set up
        setTitle(menuItem.getTitle());

        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
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
                    EventCache.getInstance().setEvents(data);  // Cache the loaded events
                    recEventAdapter.setEvents(data);
                    recEventAdapter.notifyDataSetChanged();
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                });
            }

        });
    }
}