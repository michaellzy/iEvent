package com.example.ievent.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.adapter.userfragmentfollowers;
import com.example.ievent.adapter.userfragmentposts;
import com.example.ievent.adapter.userfragmentsubscriptionAdapter;
import com.example.ievent.adapter.userfragmentticketsAdapter;
import com.example.ievent.database.data_manager.OrganizerDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityUserBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;
import com.example.ievent.global.ImageCropper;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserAcitivity extends BaseActivity {

    private RecyclerView recyclerView;

    private TabLayout tabLayout;

    private ActivityUserBinding binding;

    private ActivityResultLauncher cropImageActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        recyclerView = binding.textViewUsernameProfile;
        tabLayout = binding.tabLayout;

        // Initial setup
//        setupRecyclerView("Post");
        String uid = mAuth.getUid();
        ImageView profileImageView = binding.profileImage;
        TextView usernameTextView = binding.tvName;
        TextView emailTextView = binding.tvEmail;
        db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                User user = data.get(0);
                usernameTextView.setText(user.getUserName());
                emailTextView.setText(user.getEmail());
                db.downloadAvatar(profileImageView, mAuth.getCurrentUser().getUid(),UserAcitivity.this);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });


        profileImageView.setOnClickListener(v -> {
            // Open image picker
            ImageCropper.startCropImageActivity(this, cropImageActivityResultLauncher, true, 1,1);
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String type = Objects.requireNonNull(tab.getText()).toString();
                switch (type) {
                    case "Tickets":
                        Log.d("TabSelection", "Tickets tab is selected"); // Confirm this branch executes
                        db.getParticipantEvents(uid, new EventDataListener() {
                            @Override
                            public void isAllData(boolean isAll) {

                            }

                            @Override
                            public void onSuccess(ArrayList<Event> events) {
                                setupRecyclerViewByEvents("Tickets", events);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                setupRecyclerViewByEvents("Tickets", new ArrayList<>());
                                Toast.makeText(UserAcitivity.this, "Failed to load tickets: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case "Post":
                        Log.d("TabSelection", "Post tab is selected");
                        setPosts();
                        break;

                    case "Followers":
                        Log.d("TabSelection", "Followers tab is selected");
                        setupFollowersView();
                        break;

                    case "Sub":
                        Log.d("TabSelection", "sub tab is selected");
                        setupSubscriptionsView();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);  // This will reload the data when the tab is reselected
            }
        });

        setVariable();
        cropImageActivityResultLauncher = getCropImageActivityResultLauncher();
        setPosts();

    }
    private void setupFollowersView() {
        String uid = FirebaseAuth.getInstance().getUid();  // Get current user's UID
        if (uid == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取用户的 organizer 实例
        db.getOrganizer(uid, new OrgDataListener() {
            @Override
            public void onSuccess(ArrayList<Organizer> organizers) {
                if (!organizers.isEmpty()) {
                    Organizer organizer = organizers.get(0);
                    ArrayList<String> followerIds = organizer.getFollowersList();
                    db.getAllUsersByIds(followerIds, new UserDataListener() {
                        @Override
                        public void onSuccess(ArrayList<User> users) {
                            setupRecyclerViewByUsers("Followers", users);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(UserAcitivity.this, "Failed to load followers: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UserAcitivity.this, "No organizer found for the user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(UserAcitivity.this, "Failed to get organizer: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setPosts(){
        String uid = mAuth.getUid();
        db.fetchOrganizedData(uid, new OrganizedEventListener() {
            @Override
            public void onEventsUpdated(List<String> eventIds) {
                Toast.makeText(UserAcitivity.this, "List" +  eventIds.size(), Toast.LENGTH_SHORT).show();

                ArrayList<String> temp = new ArrayList<>(eventIds);
                Log.i(
                        "TEMP", "onEventsUpdated: " + temp.size());
                db.fetchDocuments(temp, new EventDataListener() {
                    @Override
                    public void isAllData(boolean isAll) {

                    }

                    @Override
                    public void onSuccess(ArrayList<Event> data) {
                        setupRecyclerViewByEvents("Post", data);
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(UserAcitivity.this, "List" +  eventIds.size(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String error) {
                Toast.makeText(UserAcitivity.this, "List error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupSubscriptionsView() {
        String uid = mAuth.getUid();
        db.getLoggedInUser(uid, new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                User currentUser = users.get(0);
                ArrayList<String> subscriptionIds = currentUser.getSubscribedList();

                db.getAllUsersByIds(subscriptionIds, new UserDataListener() {
                    @Override
                    public void onSuccess(ArrayList<User> subscribedUsers) {
                        setupRecyclerViewByUsers("Sub", subscribedUsers);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        setupRecyclerViewByUsers("Sub", new ArrayList<>());
                        Toast.makeText(UserAcitivity.this, "Failed to load subscriptions: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(UserAcitivity.this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setVariable(){
        // Set the profile image
        db.downloadAvatar(binding.profileImage, mAuth.getUid() == null ? "11111" : mAuth.getUid(), this);

        binding.profileImage.setOnClickListener(v -> {
            // Open image picker
            ImageCropper.startCropImageActivity(this, cropImageActivityResultLauncher, true, 1,1);
        });
    }

    private void setupRecyclerViewByEvents(String type, ArrayList<Event> events){
        switch (type) {
            case "Post":
                recyclerView.setAdapter(new userfragmentposts(events));
                break;
            case "Tickets":
                recyclerView.setAdapter(new userfragmentticketsAdapter(events));
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerViewByUsers(String type, ArrayList<User> users){
        switch (type) {
            case "Sub":
                recyclerView.setAdapter(new userfragmentsubscriptionAdapter(users));
                break;
            case "Followers":
                recyclerView.setAdapter(new userfragmentfollowers(users));
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private ActivityResultLauncher getCropImageActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if(data == null) return;

                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                        Uri resultUri = cropResult.getUri();


                        // Upload the image to Firebase Storage
                        String uid = mAuth.getUid() == null ? "11111" : mAuth.getUid();
                        db.uploadAvatar(uid, resultUri, new DataListener<String>() {
                            @Override
                            public void onSuccess(ArrayList<String> data) {

                                // Load the image into the image view
                                db.updateUserAvatar(uid, data.get(0), new UserDataListener() {
                                    @Override
                                    public void onSuccess(ArrayList<User> data) {
                                        Toast.makeText(UserAcitivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        Toast.makeText(UserAcitivity.this, "Failed to update user avatar: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(UserAcitivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        if (result.getData() != null) {
                            CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                            Exception error = cropResult.getError();
                        }
                    }
                }
        );
    }

}
