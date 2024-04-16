package com.example.ievent.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "BaseActivity", Toast.LENGTH_SHORT).show();


        EventDataManager.getInstance().getAllEventsByType("boat party", new EventDataListener() {
            @Override
            public void onSuccess(ArrayList<Event> data) {
                Toast.makeText(BaseActivity.this, data.size() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

}