package com.example.ievent.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.databinding.ActivityUploadEventBinding;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;

import java.util.ArrayList;

public class ReleaseActivity extends BaseActivity {

    ActivityUploadEventBinding uploadEventBinding;
    private String imageURI;

    private Uri uri;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ArrayAdapter<String> eventTypeAdapter;


    final String[] eventType = {"Boat Party", "Bollywood", "Climate Change", "Comedy", "Disability",
    "Indigenous", "Libraries Act", "Mental Health", "Motorbike Tour", "Music Festivals",
    "Museum of Australia", "School Holidays", "Warehouse Sale", "Wellness", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String email = intent.getStringExtra("email");
        uploadEventBinding = ActivityUploadEventBinding.inflate(getLayoutInflater());
        setContentView(uploadEventBinding.getRoot());

        uploadEventBinding.uploadImage.setOnClickListener(v -> {
            Intent intentUpload = new Intent(Intent.ACTION_PICK);
            intentUpload.setType("image/*");
            startActivityForResult(intentUpload, PICK_IMAGE_REQUEST);
        });

        String eventName = uploadEventBinding.uploadEventName.getText().toString();
        eventTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventType);
        uploadEventBinding.autoCompleteEventType.setAdapter(eventTypeAdapter);
        uploadEventBinding.autoCompleteEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedEventType = parent.getItemAtPosition(position).toString();
                uploadEventBinding.autoCompleteEventType.setText(selectedEventType, false);
            }
        });

        uploadEventBinding.uploadButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getOrganizer(mAuth.getCurrentUser().getUid(), new OrgDataListener() {
                    @Override
                    public void onSuccess(ArrayList<Organizer> data) {
                        Organizer org = data.get(0);
                        Toast.makeText(ReleaseActivity.this, "this user is already an organizer!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ReleaseActivity.this, "this user is not currently an organizer",Toast.LENGTH_SHORT).show();
                        Organizer org = new Organizer(mAuth.getCurrentUser().getUid(), userName, email);
                        db.addNewOrganizer(mAuth.getCurrentUser().getUid(), org);
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.uri = data.getData();
            uploadEventBinding.uploadImage.setImageURI(this.uri);
            uploadEventBinding.uploadImage.setVisibility(View.VISIBLE);

        }
    }

}