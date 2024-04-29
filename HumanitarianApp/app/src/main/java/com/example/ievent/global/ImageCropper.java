package com.example.ievent.global;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageCropper {

    /**
     * Start the crop image activity
     * @param activity your activity
     * @param l the activity result launcher
     * @param isCircle if the crop image is a circle
     * @param x the aspect ratio x
     * @param y the aspect ratio y
     */
    public static void startCropImageActivity(Activity activity, ActivityResultLauncher l, boolean isCircle, int x, int y) {

        Intent intent = CropImage.activity()
                .setCropShape(isCircle ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(isCircle)
                .setAspectRatio(x, y)
                .setAutoZoomEnabled(true)
                .getIntent(activity);
        l.launch(intent);
    }
}
