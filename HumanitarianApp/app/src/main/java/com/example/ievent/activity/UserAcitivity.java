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
import com.example.ievent.databinding.ActivityUserBinding;
import com.example.ievent.global.ImageCropper;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
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
        setupRecyclerView("Post");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Change RecyclerView content based on selected tab
                String type = Objects.requireNonNull(tab.getText()).toString();
                setupRecyclerView(type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected if needed
            }
        });

        setVariable();

        cropImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                            Bitmap resultUri = cropResult.getBitmap();
                            // save the image as a local image
                            Log.i("URLLLLL", "onCreate: "+resultUri.toString());

                            Glide.with(this)
                                    .load(resultUri)
                                    .into(binding.profileImage);

                        }
                    } else if (result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        if (result.getData() != null) {
                            CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                            Exception error = cropResult.getError();
                        }
                    }
                }
        );
    }


    private void setVariable(){



        binding.profileImage.setOnClickListener(v -> {
            // Open image picker
            ImageCropper.startCropImageActivity(this, cropImageActivityResultLauncher, 1, 1);
        });

        Glide.with(this)
                .load(R.drawable.default_avatar)
                .into(binding.profileImage);
    }

    private void setupRecyclerView(String type) {
        switch (type) {
            case "Post":
                recyclerView.setAdapter(new userfragmentposts(new ArrayList<>()));
                break;
            case "Tickets":
                recyclerView.setAdapter(new userfragmentticketsAdapter(new ArrayList<>()));
                break;
            case "Sub":
                recyclerView.setAdapter(new userfragmentsubscriptionAdapter());
                break;
            case "Followers":
                recyclerView.setAdapter(new userfragmentfollowers());
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
