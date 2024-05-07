package com.example.ievent.database.data_manager;


import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.ievent.R;
import com.example.ievent.database.listener.DataListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;


/**
 *  With this class, we can upload the media files to the firebase storage
 */
public class MediaManager {
    private static MediaManager instance;

    StorageReference storageRef;

    private MediaManager(){
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public static synchronized MediaManager getInstance() {
        if (instance == null) {
            synchronized (MediaManager.class) {
                if (instance == null) {
                    instance = new MediaManager();
                }
            }
        }
        return instance;
    }


    /**
     * upload the image to the firebase storage
     * @param uid the user id
     * @param file the Uri of image to upload
     * @param listener the listener to handle the result
     */
    public void uploadAvatar(String uid, Uri file, DataListener<String> listener){
        StorageReference avatarRef = storageRef.child("Avatar/" + uid + ".jpg");

        // check bitmap is valid
        UploadTask uploadTask = avatarRef.putFile(file);

        // start the upload task
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            listener.onFailure(exception.getMessage());
        }).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                ArrayList<String> uriList = new ArrayList<>();
                uriList.add(uri.toString());
                listener.onSuccess(uriList);
            });
        });
    }


    /**
     * load the avatar image into the image view
     * @param imageView the image view to load the image
     * @param uid the user id
     */
    public void loadAvatarIntoView(ImageView imageView, String uid) {
        StorageReference avatarRef = storageRef.child("Avatar/" + uid + ".jpg");
        loadImageIntoView(imageView, avatarRef, R.drawable.default_avatar);
    }

    /**
     * load the image into the image view
     * @param imageView the image view to load the image
     * @param storageRef the storage reference of the image
     * @param defaultImage the default image to load if the image is not found such like R.drawable.default_avatar for avatar
     */
    private void loadImageIntoView(ImageView imageView, StorageReference storageRef, int defaultImage) {

        Glide.with(imageView.getContext())
                .load(defaultImage)
                .into(imageView);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(imageView.getContext())
                    .load(uri.toString())
                    .placeholder(defaultImage)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            imageView.setImageResource(defaultImage);
        });
    }


    public void uploadEventImg(Uri file, DataListener<String> listener){
        StorageReference eventRef = storageRef.child("eventImages").child(file.getLastPathSegment());

        // check bitmap is valid
        UploadTask uploadTask = eventRef.putFile(file);

        // start the upload task
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            listener.onFailure(exception.getMessage());
        }).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                ArrayList<String> uriList = new ArrayList<>();
                uriList.add(uri.toString());
                listener.onSuccess(uriList);
            });
        });
    }

}
