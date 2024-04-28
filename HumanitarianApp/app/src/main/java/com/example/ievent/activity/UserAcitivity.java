package com.example.ievent.activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.ievent.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.adapter.userfragmentfollowers;
import com.example.ievent.adapter.userfragmentposts;
import com.example.ievent.adapter.userfragmentsubscriptionAdapter;
import com.example.ievent.adapter.userfragmentticketsAdapter;
import com.example.ievent.databinding.ActivityUserBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;


public class UserAcitivity extends BaseActivity {

    private RecyclerView recyclerView;

    private TabLayout tabLayout;

    private ActivityUserBinding binding;


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
    }


    private void setVariable(){



        binding.profileImage.setOnClickListener(v -> {
            // Open image picker
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
