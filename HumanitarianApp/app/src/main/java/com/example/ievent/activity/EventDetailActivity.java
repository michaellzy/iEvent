package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ievent.R;
import com.example.ievent.database.data_manager.EventDataManager;
import com.example.ievent.database.data_manager.OrganizerDataManager;
import com.example.ievent.database.data_manager.UserDataManager;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.databinding.ActivityEventDetailBinding;
import com.example.ievent.entity.Event;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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

        setupTicketButton();

    }

    private void setupTicketButton() {
        Event event = (Event) getIntent().getSerializableExtra("event");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Adjust based on your user session management

        eventDetailBinding.buttonGetTicket.setOnClickListener(v -> {
            if (event != null && userId != null) {
                UserDataManager.getInstance().addEventToUser(userId, event.getEventId(), new DataListener<Void>() {
                    @Override
                    public void onSuccess(ArrayList<Void> result) {
                        Toast.makeText(EventDetailActivity.this, "Ticket acquired successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(EventDetailActivity.this, "Failed to get ticket: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Error: Event details not available or user not logged in.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onAddFollowClick(View view) {
        Event event = (Event) getIntent().getSerializableExtra("event");
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (event != null && userId != null) {
            String organizerID = event.getOrganizer();
            if (organizerID != null) {
                // 添加条件检查，确保用户不能关注自己
                if (userId.equals(organizerID)) {
                    Toast.makeText(EventDetailActivity.this, "Can't follow yourself!", Toast.LENGTH_SHORT).show();
                    return; // 结束方法，不执行后续操作
                }
                view.setEnabled(false); // Disable the button to prevent multiple clicks
                OrganizerDataManager.getInstance().addFollower(organizerID, userId, new DataListener<Void>() {
                    @Override
                    public void onSuccess(ArrayList<Void> data) {
                        runOnUiThread(() -> {
                            ImageView followIcon = findViewById(R.id.imageView_add_follow);
                            followIcon.setImageResource(R.drawable.ic_follow_y); // Change the icon to indicate followed
                            view.setEnabled(true); // Re-enable the button
                        });
                        UserDataManager.getInstance().addSubscription(userId, organizerID, new DataListener<Void>() {
                            @Override
                            public void onSuccess(ArrayList<Void> subscriptionData) {
                                runOnUiThread(() -> {
                                    ImageView followIcon = findViewById(R.id.imageView_add_follow);
                                    followIcon.setImageResource(R.drawable.ic_follow_y); // Change the icon to indicate followed
                                    Toast.makeText(EventDetailActivity.this, "Followed and subscribed successfully!", Toast.LENGTH_SHORT).show();
                                    view.setEnabled(true); // Re-enable the button
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                runOnUiThread(() -> {
                                    Toast.makeText(EventDetailActivity.this, "Failed to subscribe: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    view.setEnabled(true); // Re-enable the button
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> {
                            Toast.makeText(EventDetailActivity.this, "Failed to follow: " + errorMessage, Toast.LENGTH_SHORT).show();
                            view.setEnabled(true); // Re-enable the button if there was a failure
                        });
                    }
                });
            } else {
                Toast.makeText(this, "Invalid organizer information.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Event details not available or user not logged in.", Toast.LENGTH_LONG).show();
        }
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