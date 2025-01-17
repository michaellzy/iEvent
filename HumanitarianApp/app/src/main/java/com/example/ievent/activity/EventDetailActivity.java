package com.example.ievent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.database.listener.BlockstateListener;
import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityEventDetailBinding;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Activity for displaying event details
 * @author Qianwen Shen
 * @author Tengkai Wang
 * @author Xuan Li
 * @author HaoLin Li
 */
public class EventDetailActivity extends BaseActivity {

    ActivityEventDetailBinding eventDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventDetailBinding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(eventDetailBinding.getRoot());

        setVariables();
        eventDetailBinding.imageViewDetailBackBtn.setOnClickListener(v -> finish());
        setupTicketButton();
    }

    /**
     * Setup the ticket tab to allow users to get their tickets
     */
    private void setupTicketButton() {
        Event event = (Event) getIntent().getSerializableExtra("event");
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();  // Adjust based on your user session management

        eventDetailBinding.buttonGetTicket.setOnClickListener(v -> {
            if (event != null && userId != null) {
                db.addEventToUser(userId, event.getEventId(), new DataListener<Void>() {
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
                Log.d("event","Error: Event details not available or user not logged in.");

            }
        });
    }

    /**
     * Handle the follow button click event
     * @param view The view that was clicked
     */
    public void onAddFollowClick(View view) {
        Event event = (Event) getIntent().getSerializableExtra("event");
        String userId = mAuth.getCurrentUser().getUid();
        if (event != null && userId != null) {
            String organizerID = event.getOrgId();
            if (organizerID != null) {
                view.setEnabled(false); // Disable the button to prevent multiple clicks
                ImageView followIcon = findViewById(R.id.imageView_add_follow);
                db.getLoggedInUser(userId, new UserDataListener() {
                    @Override
                    public void onSuccess(ArrayList<User> data) {
                        User user = data.get(0);
                        if (user.getSubscribedList().contains(organizerID)) {
                            // User already followed, perform unfollow
                            removeFollowerAndSubscription(userId, organizerID, followIcon, view);
                        } else {
                            // User not followed yet, add follow and subscription
                            addFollowerAndSubscribe(organizerID, userId, view);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d("subscriptions", "Failed to check subscriptions: " + errorMessage);
                        view.setEnabled(true); // Re-enable the button on failure
                    }
                });
            } else {
                Log.d("organizer", "Invalid organizer information.");
                view.setEnabled(true); // Re-enable the button when data is not correct
            }
        } else {
            Log.d("event", "Error: Event details not available or user not logged in.");
            view.setEnabled(true); // Ensure button is enabled if checks fail
        }
    }

    private void removeFollowerAndSubscription(String userId, String organizerID, ImageView followIcon, View view) {
        // Remove the subscription from the user's list
        db.removeSubscription(userId, organizerID, new DataListener<Void>() {
            @Override
            public void onSuccess(ArrayList<Void> data) {
                // Now remove the follower from the organizer's list
                db.removeFollower(organizerID, userId, new DataListener<Void>() {
                    @Override
                    public void onSuccess(ArrayList<Void> data) {
                        runOnUiThread(() -> {
                            followIcon.setImageResource(R.drawable.ic_add_follow); // Update icon to show unfollow
                            Toast.makeText(EventDetailActivity.this, "You have unfollowed this organizer", Toast.LENGTH_SHORT).show();
                            view.setEnabled(true); // Re-enable the button
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        runOnUiThread(() -> {
                            Toast.makeText(EventDetailActivity.this, "Failed to remove follower: " + errorMessage, Toast.LENGTH_SHORT).show();
                            view.setEnabled(true); // Re-enable the button
                        });
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(EventDetailActivity.this, "Failed to unsubscribe: " + errorMessage, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true); // Re-enable the button
                });
            }
        });
    }





    /**
     * Add a follower and subscribe to an organizer
     * @param organizerID The ID of the organizer
     * @param userId The ID of the user
     * @param view The view that was clicked
     */
    private void addFollowerAndSubscribe(String organizerID, String userId, View view) {
        ImageView followIcon = findViewById(R.id.imageView_add_follow);
        db.addFollower(organizerID, userId, new DataListener<Void>() {
            @Override
            public void onSuccess(ArrayList<Void> data) {
                runOnUiThread(() -> {
                    followIcon.setImageResource(R.drawable.ic_follow_y); // Change the icon to indicate followed
                    view.setEnabled(true); // Re-enable the button
                });
                db.addSubscription(userId, organizerID, new DataListener<Void>() {
                    @Override
                    public void onSuccess(ArrayList<Void> subscriptionData) {
                        runOnUiThread(() -> {
                            followIcon.setImageResource(R.drawable.ic_follow_y); // Change the icon to indicate followed
                            Toast.makeText(EventDetailActivity.this, "Followed successfully!", Toast.LENGTH_SHORT).show();
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
    }


    /**
     * Set the variables for the event details
     */
    @SuppressLint("SetTextI18n")
    private void setVariables() {
        Event event = (Event) getIntent().getSerializableExtra("event");
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String organizerId = event.getOrgId();
        assert event != null;


        setStaticDataToView(event);


        // Check if the user is viewing their own event, remove unnecessary views
        if (uid != null && uid.equals(organizerId)) {
            // Hide follow button
            eventDetailBinding.imageViewAddFollow.setVisibility(View.GONE);
            eventDetailBinding.imageViewChat.setVisibility(View.GONE);
            eventDetailBinding.imageViewBlock.setVisibility(View.GONE);
        } else {
            eventDetailBinding.imageViewAddFollow.setVisibility(View.VISIBLE);
            // Check if the user has already subscribed to this organizer
            db.getLoggedInUser(uid, new UserDataListener() {
                @Override
                public void onSuccess(ArrayList<User> data) {
                    User current_user=data.get(0);
                    if(organizerId!=null&&current_user.getSubscribedList().contains(organizerId)){
                        eventDetailBinding.imageViewAddFollow.setImageResource(R.drawable.ic_follow_y);
                    }else eventDetailBinding.imageViewAddFollow.setImageResource(R.drawable.ic_add_follow);
                }

                @Override
                public void onFailure(String errorMessage) {
                    // placeholder here
                }
            });
        }



        // bind event listeners
        eventDetailBinding.imageViewMap.setOnClickListener(v -> {
            // Open map activity and add description to the intent
            Intent intent = new Intent(this, MapActivity.class);
            // here put the name of destination
            intent.putExtra("destination", event.getLocation());
            startActivity(intent);
        });

        eventDetailBinding.imageViewChat.setOnClickListener(v -> {
            // get the organizer by organizer id
            if(event.getOrgId() == null){
                return;
            }

            Log.i("ORGANIZEDID", "setVariables: " + event.getOrgId());
            db.getUserById(event.getOrgId(), new UserDataListener() {


                @Override
                public void onSuccess(ArrayList<User> data) {
                    Toast.makeText(EventDetailActivity.this, "success to get the organizer", Toast.LENGTH_SHORT).show();
                    if (!data.isEmpty()) {
                        Intent intent = new Intent(EventDetailActivity.this, P2PChatActivity.class);
                        User receiver = data.get(0);
                        intent.putExtra("receiver", receiver);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(EventDetailActivity.this, "Failed to get the organizer: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        eventDetailBinding.imageViewBlock.setOnClickListener(v ->{
            String senderId = mAuth.getUid();
            String receiverId = event.getOrgId();
            if (senderId == null || receiverId == null) {
                Toast.makeText(EventDetailActivity.this, "User ID or Organizer ID is null", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("BlockOperation", "Attempting to change block status for: " + senderId + " and " + receiverId);
            db.CheckBlockStatus(senderId, receiverId, new BlockstateListener() {
                @Override
                public void onSuccess(Boolean result) {
                    db.getLoggedInUser(receiverId, new UserDataListener() {
                        @Override
                        public void onSuccess(ArrayList<User> data) {
                            User user = data.get(0);
                            String username = user.getUserName();

                            AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailActivity.this);
                            builder.setTitle(result ? "Unblock " + username : "Block " + username);
                            builder.setMessage(result ? "Now you can talk with he/she" : "He/She can not be able to talk with you.");


                            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                            builder.setPositiveButton(result ? "Unblock" : "Block", (dialog, which) -> {
                                db.AddBlockMessage(senderId, receiverId); // 调用unblock函数
                            });

                            builder.create().show();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(EventDetailActivity.this, "Failed to retrieve user data: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(EventDetailActivity.this, "Error checking block status: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     *  bind the data to the event detail views
     */
    private void setStaticDataToView(Event event){
        db.getUserById(event.getOrgId(), new UserDataListener() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                if (!data.isEmpty() && data.get(0).getAvatar() != null && !EventDetailActivity.this.isDestroyed()) {
                    User organizer = data.get(0);

                    Glide.with(EventDetailActivity.this)
                            .load(organizer.getAvatar())
                            .placeholder(R.drawable.default_avatar)
                            .into(eventDetailBinding.imageViewDetailOrganizerPic);
                }else{
                    eventDetailBinding.imageViewDetailOrganizerPic.setImageResource(R.drawable.default_avatar);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("organizer", "Failed to get organizer: " + errorMessage);
            }
        });


        // bind data to the detailEventPic
        Glide.with(this)
                .load(event.getImg())
                .into(eventDetailBinding.imageViewDetailEventPic);
        eventDetailBinding.textViewDetailActivityName.setText(event.getTitle());
        eventDetailBinding.textViewDetailActivityTime.setText(event.getDateTime());
        eventDetailBinding.textViewDetailActivityLocationContent.setText(event.getLocation());
        eventDetailBinding.textViewDetailActivityPriceContent.setText(" AUD " + event.getPrice() );
        eventDetailBinding.textViewDetailActivityDescriptionContent.setText(event.getDescription());
    }

}