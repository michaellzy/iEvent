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
import com.example.ievent.entity.Event;
import com.example.ievent.entity.Organizer;
import com.example.ievent.global.ImageCropper;
import com.example.ievent.global.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReleaseActivity extends BaseActivity {

    ActivityUploadEventBinding uploadEventBinding;
    private String imageUri;

    private Uri uri;


    private static final int PICK_IMAGE_REQUEST = 1;

    private ArrayAdapter<String> eventTypeAdapter;

    private ActivityResultLauncher cropImageActivityResultLauncher;


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

//        uploadEventBinding.uploadImage.setOnClickListener(v -> {
//            Intent intentUpload = new Intent(Intent.ACTION_PICK);
//            intentUpload.setType("image/*");
//            startActivityForResult(intentUpload, 1);
//        });

        uploadEventBinding.uploadImage.setOnClickListener(v -> {
            ImageCropper.startCropImageActivity(this, cropImageActivityResultLauncher, false, 4,3);
        });
        cropImageActivityResultLauncher = getCropImageActivityResultLauncher();

        eventTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventTypeList);
        uploadEventBinding.autoCompleteEventType.setAdapter(eventTypeAdapter);
        uploadEventBinding.autoCompleteEventType.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEventType = parent.getItemAtPosition(position).toString();
            uploadEventBinding.autoCompleteEventType.setText(selectedEventType, false);
        });

        uploadEventBinding.uploadButtonConfirm.setOnClickListener(v -> {
            uploadEvent();
            // Intent returnIntent = new Intent();
            // Put the data you want to send back to MainActivity in the Intent
            // setResult(RESULT_OK, returnIntent);
            finish();
        });

    }

    private void saveEventData() {
        try {
            Intent intent = getIntent();
            String userName = intent.getStringExtra("userName");
            String email = intent.getStringExtra("email");
            //TODO: error checking for none events
            String eventTitle = uploadEventBinding.uploadEventName.getText().toString();
            String eventLocation = uploadEventBinding.uploadEventLocation.getText().toString();
            double eventPrice = Double.parseDouble(uploadEventBinding.uploadEventPrice.getText().toString());
            String eventType = uploadEventBinding.autoCompleteEventType.getText().toString();

            String formatEventDate = Utility.formatDate(uploadEventBinding.uploadEventDate.getText().toString());
            String formatEventStartTime = Utility.formatTime(uploadEventBinding.uploadStartTime.getText().toString());
            String formatEventEndTime = Utility.formatTime(uploadEventBinding.uploadEndTime.getText().toString());
            String eventDateTime = formatEventDate + ", " + formatEventStartTime + " - " + formatEventEndTime;
            String eventDescription = uploadEventBinding.uploadEventDescription.getText().toString();

            long timestamp = Utility.convertToTimestamp(uploadEventBinding.uploadEventDate.getText().toString());


            // String organizer = mAuth.getCurrentUser().getUid();

            Event event = new Event(eventType.replace(" ", ""), eventTitle, eventDescription, userName, eventLocation, eventDateTime, eventPrice, imageUri, timestamp);

            db.getOrganizer(mAuth.getCurrentUser().getUid(), new OrgDataListener() {
                @Override
                public void onSuccess(ArrayList<Organizer> data) {
                    // Toast.makeText(ReleaseActivity.this, "This user is already an organizer!", Toast.LENGTH_SHORT).show();
                    db.addNewEvent(event);
                    Toast.makeText(ReleaseActivity.this, "Events added!", Toast.LENGTH_SHORT).show();
                    Organizer curOrg = data.get(0);
                    if (event.getEventId() != null)
                        curOrg.organizeEvent(event.getEventId());
                }

                @Override
                public void onFailure(String errorMessage) {

                    Organizer org = new Organizer(mAuth.getCurrentUser().getUid(), email, userName);
                    db.addNewOrganizer(mAuth.getCurrentUser().getUid(), org);
                    db.addNewEvent(event);
                    Toast.makeText(ReleaseActivity.this, "Events added!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadEvent() {
        db.uploadEventImage(this.uri, new DataListener<String>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                imageUri = data.get(0);
                saveEventData();
            }

            @Override
            public void onFailure(String errorMessage) {

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
                    // String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
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
                    // 格式化时间字符串
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
                        uploadEventBinding.uploadImage.setImageURI(this.uri);
                        uploadEventBinding.uploadImage.setVisibility(View.VISIBLE);


                        // Upload the image to Firebase Storage
                    }
                }
            );
        }
    }