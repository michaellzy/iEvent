package com.example.ievent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ievent.R;
import com.example.ievent.adapter.ChatLogAdapter;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.databinding.ActivityNotificationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class NotificationActivity extends BaseActivity {

    ActivityNotificationBinding binding;

    private static final String TAG = "NotificationActivity11111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);


        // TODO: it's a reusable code, consider moving it to BaseActivity
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_ticket) {
                startActivity(new Intent(getApplicationContext(), TicketActivity.class));
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                return true;
            }
            return false;
        });
        setVariables();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showMessage();
    }

    private void setVariables(){
        binding.subRecycler.setLayoutManager(new LinearLayoutManager(this));
        showMessage();
    }


    private void showMessage(){
        showLoading(true);
        showEmptyView(false);

        db.getChatLog(mAuth.getUid(), new DataListener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(ArrayList<String> data) {
                Log.i(TAG, "onSuccess: " + data.size());
                ChatLogAdapter adapter = new ChatLogAdapter(NotificationActivity.this, data, mAuth.getUid());
                binding.subRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                showLoading(false);
                showEmptyView(data.isEmpty());
            }

            @Override
            public void onFailure(String message) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }


    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.subRecycler.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyView(boolean show) {
        binding.TextViewEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}