package com.example.ievent.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.databinding.ActivityUploadEventBinding;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;

import java.util.ArrayList;

public class ReleaseActivity extends BaseActivity {

    ActivityUploadEventBinding uploadEventBinding;
    private String imageURL;

    private Uri uri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadEventBinding = ActivityUploadEventBinding.inflate(getLayoutInflater());
        setContentView(uploadEventBinding.getRoot());

        uploadEventBinding.uploadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                        Organizer org = new Organizer(mAuth.getCurrentUser().getUid());
                        db.addNewOrganizer(mAuth.getCurrentUser().getUid(), org);
                    }
                });
            }
        });

//        setContentView(R.layout.activity_release);
//
//        ImageView backBtn = findViewById(R.id.imageView_release_back_btn);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        ImageView uploadButton = findViewById(R.id.imageView_release_upload_btn);
//        uploadButton.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, PICK_IMAGE_REQUEST);
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // Correct variable usage
            uploadEventBinding.uploadImage.setImageURI(imageUri); // Use binding to set the URI
            uploadEventBinding.uploadImage.setVisibility(View.VISIBLE); // Make sure the ImageView is visible

        }
    }



}