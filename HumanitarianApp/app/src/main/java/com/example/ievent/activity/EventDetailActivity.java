package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ievent.R;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        setVariables();
    }

    private void setVariables() {
        findViewById(R.id.imageView_map).setOnClickListener(v -> {
            // Open map activity and add description to the intent
            android.content.Intent intent = new android.content.Intent(this, MapActivity.class);
            // here put the name of destination
            intent.putExtra("destination", "ANU");
            startActivity(intent);
        });
    }


}