package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.listener.UserDataListener;
import com.example.ievent.databinding.ActivityEventDetailBinding;
import com.example.ievent.databinding.ActivityUploadEventBinding;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;
import com.example.ievent.entity.UserFactory;

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
                db.getLoggedInUser(mAuth.getCurrentUser().getUid(), new UserDataListener() {
                    @Override
                    public void onSuccess(ArrayList<User> data) {
                        User user = data.get(0);
                        User organizer = UserFactory.createUser("organizer", user.getUid());
                        if (organizer instanceof Organizer) {
                            Toast.makeText(ReleaseActivity.this, "Welcome " + organizer.getUserName(), Toast.LENGTH_SHORT).show();
                            ((Organizer) organizer).publishNewEvent(null);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

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