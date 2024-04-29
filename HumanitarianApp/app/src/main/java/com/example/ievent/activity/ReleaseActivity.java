package com.example.ievent.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ievent.R;
import com.example.ievent.database.listener.OrgDataListener;
import com.example.ievent.databinding.ActivityUploadEventBinding;
import com.example.ievent.entity.Organizer;
import com.example.ievent.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReleaseActivity extends BaseActivity {

    ActivityUploadEventBinding uploadEventBinding;
    private String imageURI;

    private Uri uri;

    private String eventTitle;

    private String eventType;

    private String eventLocation;

    private int eventPrice;

    private String eventDateTime;

    private String eventDescription;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ArrayAdapter<String> eventTypeAdapter;


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
            Intent intentUpload = new Intent(Intent.ACTION_PICK);
            intentUpload.setType("image/*");
            startActivityForResult(intentUpload, 1);
        });

        eventTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, eventTypeList);
        uploadEventBinding.autoCompleteEventType.setAdapter(eventTypeAdapter);
        uploadEventBinding.autoCompleteEventType.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEventType = parent.getItemAtPosition(position).toString();
            uploadEventBinding.autoCompleteEventType.setText(selectedEventType, false);
        });

        uploadEventBinding.uploadButtonConfirm.setOnClickListener(v -> validateAndUploadEvent());
    }

    private void validateAndUploadEvent() {
        try {
            Intent intent = getIntent();
            String userName = intent.getStringExtra("userName");
            String email = intent.getStringExtra("email");

            eventTitle = uploadEventBinding.uploadEventName.getText().toString();
            eventLocation = uploadEventBinding.uploadEventLocation.getText().toString();
            eventPrice = Integer.parseInt(uploadEventBinding.uploadEventPrice.getText().toString());
            eventType = uploadEventBinding.autoCompleteEventType.getText().toString();
            eventDateTime = uploadEventBinding.uploadEventDate.getText().toString() + ", " +
                    uploadEventBinding.uploadStartTime.getText().toString() + " to " +
                    uploadEventBinding.uploadEndTime.getText().toString();
            eventDescription = uploadEventBinding.uploadEventDescription.getText().toString();
            db.getOrganizer(mAuth.getCurrentUser().getUid(), new OrgDataListener() {
                @Override
                public void onSuccess(ArrayList<Organizer> data) {
                    Toast.makeText(ReleaseActivity.this, "This user is already an organizer!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ReleaseActivity.this, "This user is not currently an organizer", Toast.LENGTH_SHORT).show();
                    Organizer org = new Organizer(mAuth.getCurrentUser().getUid(), userName, email);
                    db.addNewOrganizer(mAuth.getCurrentUser().getUid(), org);
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
        }
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
                    // 格式化日期字符串
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