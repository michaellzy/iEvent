package com.example.ievent.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.data_manager.EventCache;
import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.OrganizerDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.EventUpdateListener;
import com.example.ievent.database.listener.FollowerNumListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityMainBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        // Initialize NavigationView  & HeaderView
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
                db.downloadAvatar(profileImageView, mAuth.getCurrentUser().getUid(), MainActivity.this);
                profileImageView.setOnClickListener(v -> {
                    startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
                });
                Toast.makeText(MainActivity.this, "Welcome, " + user.getUserName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid(), MainActivity.this);
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
                //subscribed notification

                    db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
                        @Override
                        public void onSuccess(ArrayList<User> udata) {
                            User curUser = udata.get(0);
                            for(Event event : data) {
                                if (curUser.getSubscribedList().contains(event.getOrgId())) {
                                    showEventNotification(event);
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                        }
                    });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        };


        db.downloadAvatar(binding.profileImage, mAuth.getCurrentUser().getUid(), MainActivity.this);
        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserAcitivity.class));
        });
        db.setupFollowerListener(mAuth.getCurrentUser().getUid(), new FollowerNumListener() {
            @Override
            public void reached(boolean isReached) {
                if (isReached) {
                    Log.i("MainActivity", "show notification");
                    showLocalNotification();
                }
            }
        });

    }

    private void showLocalNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("followers",
                    "Followers Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification Channel for Followers Updates");
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = new Notification.Builder(this, "followers")
                .setContentTitle("Congratulations!")
                .setContentText("You have reached 5 followers!")
                .setSmallIcon(R.mipmap.ievent_logo)  // 确保这里的图标文件存在
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1, notification);
    }

    private void showEventNotification(Event event) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Ensure notification channels are created on Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "events",
                    "Event Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for new events");
            notificationManager.createNotificationChannel(channel);
        }

        // Building the notification
        Notification notification = new Notification.Builder(this, "events")
                .setContentTitle("New Event Notification") // Using organizer's name
                .setContentText("Your followed organizer " + event.getOrganizer() + " has posted a new event: " + event.getTitle())
                .setSmallIcon(R.mipmap.ievent_logo) // Ensure this resource exists
//                .setContentIntent(pendingIntent)  // 设置点击通知后的动作
                .setAutoCancel(true)
                .build();

        // Posting the notification
        int notificationId = new Random().nextInt();
        notificationManager.notify(notificationId, notification);
        Log.d("NotificationSent", "Notification sent for new event by " + event.getOrganizer() + " with ID " + notificationId);
    }


    //navigation drawer
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
            // Handle other menu items
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