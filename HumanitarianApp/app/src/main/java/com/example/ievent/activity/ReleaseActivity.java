package com.example.ievent.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.ievent.database.listener.DataListener;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.databinding.ActivityUploadEventBinding;
import com.example.ievent.entity.ConcreteUserFactory;
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.global.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class is used to release the event.
 * The event information will be uploaded to the database.
 * @author Zhiyuan Lu
 * @author HaoLin Li
 * @author Qianwen Shen
 */
public class ReleaseActivity extends BaseActivity {

    ActivityUploadEventBinding uploadEventBinding;
    private String imageUri;

    private Uri uri;

    private ArrayAdapter<String> eventTypeAdapter;

    private ActivityResultLauncher cropImageActivityResultLauncher;

    // List of predefined event types for selection
    final String[] eventTypeList = {"Boat Party", "Bollywood", "Climate Change", "Comedy", "Disability",
    "Indigenous", "Libraries Act", "Mental Health", "Motorbike Tour", "Music Festivals",
    "Museum of Australia", "School Holidays", "Warehouse Sale", "Wellness", "Other"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadEventBinding = ActivityUploadEventBinding.inflate(getLayoutInflater());
        setContentView(uploadEventBinding.getRoot());

        uploadEventBinding.uploadStartTime.setOnClickListener(v -> showTimePickerDialog(uploadEventBinding.uploadStartTime));
        uploadEventBinding.uploadEndTime.setOnClickListener(v -> showTimePickerDialog(uploadEventBinding.uploadEndTime));

        setupDatePicker();

        uploadEventBinding.uploadImage.setOnClickListener(v -> {
            Utility.ImageCropper.startCropImageActivity(this, cropImageActivityResultLauncher, false, 4,3);
        });
        cropImageActivityResultLauncher = getCropImageActivityResultLauncher();

        eventTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventTypeList);
        uploadEventBinding.autoCompleteEventType.setAdapter(eventTypeAdapter);
        uploadEventBinding.autoCompleteEventType.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEventType = parent.getItemAtPosition(position).toString();
            uploadEventBinding.autoCompleteEventType.setText(selectedEventType, false);
        });

        uploadEventBinding.uploadButtonConfirm.setOnClickListener(v -> {
            runOnUiThread(() -> uploadEventBinding.progressBarUpload.setVisibility(View.VISIBLE));
            uploadEvent();
            finish();
        });

    }

    /**
     * Save event data to the database
     */
    private void saveEventData() {
        try {
            // Intent to receive user data from main activity
            Intent intent = getIntent();
            String userName = intent.getStringExtra("userName");
            String email = intent.getStringExtra("email");

            String eventTitle = uploadEventBinding.uploadEventName.getText().toString();
            String eventLocation = uploadEventBinding.uploadEventLocation.getText().toString();
            double eventPrice = Double.parseDouble(uploadEventBinding.uploadEventPrice.getText().toString());
            String eventType = uploadEventBinding.autoCompleteEventType.getText().toString();
            String eventDate = uploadEventBinding.uploadEventDate.getText().toString();
            String eventStartTime = uploadEventBinding.uploadStartTime.getText().toString();
            String eventEndTime = uploadEventBinding.uploadEndTime.getText().toString();

            // Check for empty fields
            if (eventTitle.isEmpty() || eventLocation.isEmpty() || String.valueOf(eventPrice).isEmpty()
                    || eventType.isEmpty() || eventDate.isEmpty() || eventStartTime.isEmpty() ||
                    eventEndTime.isEmpty()) {
                Toast.makeText(this, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
                return;
            }

            String formatEventDate = Utility.TimeFormatter.formatDate(uploadEventBinding.uploadEventDate.getText().toString());
            String formatEventStartTime = Utility.TimeFormatter.formatTime(uploadEventBinding.uploadStartTime.getText().toString());
            String formatEventEndTime = Utility.TimeFormatter.formatTime(uploadEventBinding.uploadEndTime.getText().toString());

            String eventDateTime = formatEventDate + ", " + formatEventStartTime + " - " + formatEventEndTime;
            String eventDescription = uploadEventBinding.uploadEventDescription.getText().toString();
            if (eventDescription.isEmpty()) {
                eventDescription = "";
            }

            long timestamp = Utility.TimeFormatter.convertToTimestamp(uploadEventBinding.uploadEventDate.getText().toString());


            String orgId = mAuth.getCurrentUser().getUid();

            Event event = new Event(eventType.replace(" ", ""), eventTitle, eventDescription, userName, orgId, eventLocation, eventDateTime, eventPrice, imageUri, timestamp);

            db.getOrganizer(mAuth.getCurrentUser().getUid(), new OrgDataListener() {
                @Override
                public void onSuccess(ArrayList<Organizer> data) {
                    // if current user is already an organizer, add new events into the database
                    db.addNewEvent(event);
                    Toast.makeText(ReleaseActivity.this, "Events added!", Toast.LENGTH_SHORT).show();
                    Organizer curOrg = data.get(0);
                    if (event.getEventId() != null)
                        curOrg.organizeEvent(event.getEventId());
                }

                @Override
                public void onFailure(String errorMessage) {
                    // if current user is not an organizer, save the current user into "Organizer" database
                    Organizer org = (Organizer) ConcreteUserFactory.getInstance().createUser("Organizer", mAuth.getCurrentUser().getUid(), email, userName);
                    db.addNewOrganizer(mAuth.getCurrentUser().getUid(), org);
                    // then add new event into the database.
                    db.addNewEvent(event);
                    Toast.makeText(ReleaseActivity.this, "Events added!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Upload the event image to Firebase Storage
     */
    private void uploadEvent() {
        db.uploadEventImage(this.uri, new DataListener<String>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                imageUri = data.get(0);
                saveEventData();
                runOnUiThread(() -> uploadEventBinding.progressBarUpload.setVisibility(View.GONE));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReleaseActivity.this, "upload event failed", Toast.LENGTH_SHORT).show();
                uploadEventBinding.progressBarUpload.setVisibility(View.GONE);
            }
        });
    }

    private void setupDatePicker() {
        uploadEventBinding.uploadEventDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelect, monthOfYear, dayOfMonth) -> {

                    c.set(Calendar.YEAR, yearSelect);
                    c.set(Calendar.MONTH, monthOfYear);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String formattedDate = String.format(Locale.getDefault(), "%1$tA, %1$td %1$tB, %1$tY", c);
                    uploadEventBinding.uploadEventDate.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText timeField) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    timeField.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }


    private ActivityResultLauncher getCropImageActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data == null) return;

                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                        this.uri = cropResult.getUri();
                        // Upload the image to Firebase Storage
                        uploadEventBinding.uploadImage.setImageURI(this.uri);
                        uploadEventBinding.uploadImage.setVisibility(View.VISIBLE);
                    }
                }
            );
        }
    }