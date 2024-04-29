package com.example.ievent.database.data_manager;


import android.net.Uri;
import com.example.ievent.database.listener.DataListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Objects;

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
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            ArrayList<String> path = new ArrayList<>();
            path.add(Objects.requireNonNull(taskSnapshot.getMetadata()).getPath());
            listener.onSuccess(path);
        });
    }
}
