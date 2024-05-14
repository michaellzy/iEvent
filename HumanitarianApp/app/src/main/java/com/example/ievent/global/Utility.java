package com.example.ievent.global;


import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is used to store all the global variables and methods that are used in the project
 * all the fields and methods are static so that they can be accessed without creating an object of this class
 * the way to use this class: Utility.methodName() or Utility.fieldName
 */
public class Utility {

    private Utility() {}

    public static class TimeFormatter{
        /**
         * This method is used to format the timestamp to a readable format
         * @param timestamp the timestamp to be formatted
         * @return the formatted timestamp
         */

        public static String formatTimestamp(long timestamp) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            LocalDate today = LocalDate.now();
            LocalDate dateOnly = dateTime.toLocalDate();

            if (dateOnly.equals(today)) {
                // Today within the current day, show time in hour and minute
                return dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
            } else if (dateOnly.equals(today.minusDays(1))) {
                // Yesterday, show "Yesterday"
                return "Yesterday";
            } else if (dateOnly.isAfter(today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) && dateOnly.isBefore(today)) {
                // This week (Monday to before today), show day of the week
                return dateTime.format(DateTimeFormatter.ofPattern("EEEE"));
            } else if (dateTime.getYear() == today.getYear()) {
                // Within this year but past this week, show month and day
                return dateTime.format(DateTimeFormatter.ofPattern("MM-dd"));
            } else {
                // For previous years, show year, month, and day
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }



        public static String formatDate(String inputDate) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, dd MMM, yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("E, dd MMM", Locale.ENGLISH);
            String formattedDate;
            try {
                Date date = inputFormat.parse(inputDate);
                formattedDate = outputFormat.format(date);
            } catch (ParseException e) {
                formattedDate = "";
                System.out.println(e.getMessage());
            }
            return formattedDate;
        }

        public static String formatTime(String inputTime) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);  // 24-hour format
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH); // 12-hour format with AM/PM
            String formattedTime;
            try {
                Date date = inputFormat.parse(inputTime);
                formattedTime = outputFormat.format(date);
            } catch (ParseException e) {
                formattedTime = "";
                System.out.println("Error parsing the time: " + e.getMessage());
            }
            return formattedTime;
        }

        public static long convertToTimestamp(String inputDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM, yyyy", Locale.ENGLISH);

            // Set the timezone to Australian Eastern Standard Time (AEST)
            sdf.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            long timestamp;
            try {
                Date date = sdf.parse(inputDate);
                timestamp = date.getTime() / 1000; // Convert milliseconds to seconds
            } catch (ParseException e) {
                timestamp = 0;
                System.out.println("Error parsing the date: " + e.getMessage());
            }
            return timestamp;
        }
    }

    public static class ImageCropper{
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
}
