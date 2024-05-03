package com.example.ievent.activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ievent.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.adapter.userfragmentfollowers;
import com.example.ievent.adapter.userfragmentposts;
import com.example.ievent.adapter.userfragmentsubscriptionAdapter;
import com.example.ievent.adapter.userfragmentticketsAdapter;
import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.OrganizedEventListener;
import com.example.ievent.databinding.ActivityUserBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Participant;
import com.example.ievent.entity.User;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.example.ievent.global.ImageCropper;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String type = Objects.requireNonNull(tab.getText()).toString();
                Log.d("TabSelection", "Selected tab: " + type); // Log to confirm which tab is selected
                switch (type) {
                    case "Tickets":
                        Log.d("TabSelection", "Tickets tab is selected"); // Confirm this branch executes
                        UserDataManager.getInstance().getParticipantEvents(uid, new EventDataListener() {
                            @Override
                            public void isAllData(boolean isAll) {
                            }

                            @Override
                            public void onSuccess(ArrayList<Event> events) {
                                setupRecyclerViewByEvents("Tickets", events);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(UserAcitivity.this, "Failed to load tickets: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case "Post":
                        Log.d("TabSelection", "Post tab is selected"); // Log for debugging
                        db.fetchOrganizedData(uid, new OrganizedEventListener() {
                            @Override
                            public void onEventsUpdated(List<String> eventIds) {
                                Toast.makeText(UserAcitivity.this, "List" +  eventIds.size(), Toast.LENGTH_SHORT).show();

                                ArrayList<String> temp = new ArrayList<>(eventIds);
                                Log.i(
                                        "TEMP", "onEventsUpdated: " + temp.size());
                                db.fetchDocuments(temp, new EventDataListener() {
                                    @Override
                                    public void isAllData(boolean isALl) {

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
        db.fetchOrganizedData(uid, new OrganizedEventListener() {
            @Override
            public void onEventsUpdated(List<String> eventIds) {
                Toast.makeText(UserAcitivity.this, "List" +  eventIds.size(), Toast.LENGTH_SHORT).show();

                ArrayList<String> temp = new ArrayList<>(eventIds);
                Log.i(
                        "TEMP", "onEventsUpdated: " + temp.size());
                db.fetchDocuments(temp, new EventDataListener() {
                    @Override
                    public void isAllData(boolean isALl) {

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



    private void setVariable(){
        // Set the profile image
        db.downloadAvatar(binding.profileImage, mAuth.getUid() == null ? "11111" : mAuth.getUid());

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
                recyclerView.setAdapter(new userfragmentsubscriptionAdapter());
                break;
            case "Followers":
                recyclerView.setAdapter(new userfragmentfollowers());
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
                                Glide.with(UserAcitivity.this)
                                        .load(resultUri)
                                        .into(binding.profileImage);
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
