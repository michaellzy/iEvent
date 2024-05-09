package com.example.ievent.global;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
