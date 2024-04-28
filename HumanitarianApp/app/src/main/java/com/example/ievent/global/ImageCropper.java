package com.example.ievent.global;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageCropper {

    public static void startCropImageActivity(Activity activity, ActivityResultLauncher l, int x, int y) {
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(x, y)
                .setAutoZoomEnabled(true)
                .getIntent(activity);
        l.launch(intent);
    }
}
