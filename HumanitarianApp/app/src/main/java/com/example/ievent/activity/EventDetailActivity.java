package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ievent.R;
import com.example.ievent.databinding.ActivityEventDetailBinding;
import com.example.ievent.entity.Event;


public class EventDetailActivity extends AppCompatActivity {

    ActivityEventDetailBinding eventDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventDetailBinding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(eventDetailBinding.getRoot());

        setVariables();

        eventDetailBinding.imageViewDetailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setVariables() {
        Event event = (Event) getIntent().getSerializableExtra("event");

        // pre-process the event fields
        assert event != null;
        // rewrite the empty fields
        Event.preprocessData(event);


        // bind event to map button
        eventDetailBinding.imageViewMap.setOnClickListener(v -> {
            // Open map activity and add description to the intent
            android.content.Intent intent = new android.content.Intent(this, MapActivity.class);
            // here put the name of destination
            intent.putExtra("destination", event.getLocation());
            startActivity(intent);
        });

        eventDetailBinding.imageViewDetailOrganizerPic.setOnClickListener(v -> {
            // Open organizer profile activity
            android.content.Intent intent = new android.content.Intent(this, UserAcitivity.class);
            startActivity(intent);
        });




        // bind data to the view
        Glide.with(this)
                .load(event.getImg())
                .into(eventDetailBinding.imageViewDetailEventPic);


        Glide.with(this)
                .load("https://t.mwm.moe/mp")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.default_avatar)
                .into(eventDetailBinding.imageViewDetailOrganizerPic);


        eventDetailBinding.textViewDetailActivityName.setText(event.getTitle());
        eventDetailBinding.textViewDetailActivityTime.setText(event.getDateTime());
        eventDetailBinding.textViewDetailActivityLocationContent.setText(event.getLocation());
        eventDetailBinding.textViewDetailActivityPriceContent.setText(" AUD " + event.getPrice() );
        eventDetailBinding.textViewDetailActivityDescriptionContent.setText(event.getDescription());
    }
}