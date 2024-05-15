package com.example.ievent.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.database.listener.OnFilterAppliedListener;
import com.example.ievent.database.ordered_map.Iterator;
import com.example.ievent.database.ordered_map.OrderedEvent;
import com.example.ievent.entity.Event;
import com.example.ievent.global.Utility;
import com.example.ievent.tokenparser.AndExp;
import com.example.ievent.tokenparser.EqualExp;
import com.example.ievent.tokenparser.Exp;
import com.example.ievent.tokenparser.LessExp;
import com.example.ievent.tokenparser.MoreExp;
import com.example.ievent.tokenparser.OrExp;
import com.example.ievent.tokenparser.Parser;
import com.example.ievent.tokenparser.Tokenizer;
import com.example.ievent.tokenparser.ValueExp;
import com.example.ievent.tokenparser.VariableExp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;


/**
 * This class is responsible for searching events based on user input.
 * @author Haolin Li
 */
public class SearchActivity extends BaseActivity implements OnFilterAppliedListener {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private RecommendedActivitiesAdapter recommendedActivitiesAdapter;
    private ArrayList<Event> eventList = new ArrayList<>();
    private OrderedEvent<Double, Event> orderedEvents;
    private boolean isAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageButton filterbutton = findViewById(R.id.filter_button);
        ImageButton sortbutton = findViewById(R.id.sort_button);

        recyclerView = findViewById(R.id.searchResultRecyclerView);
        recommendedActivitiesAdapter = new RecommendedActivitiesAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recommendedActivitiesAdapter);

        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        orderedEvents = new OrderedEvent<>();

        sortbutton.setOnClickListener(v -> {
            if (eventList.isEmpty()) {
                Toast.makeText(SearchActivity.this, "No events available to sort.", Toast.LENGTH_SHORT).show();
                return; // Prevent further execution
            }
            if (!isAscending) {
                // If currently in descending order, reverse to ascending
                Collections.reverse(eventList);  // Reverse the list to make it ascending
                sortbutton.animate().rotation(0).setDuration(0).start();
            } else {
                // Sort the list in ascending order if not already sorted
                orderedEvents = new OrderedEvent<Double, Event>(); // Initialize
                for (Event event : eventList) {
                    orderedEvents.insert(event.getPrice(), event);
                }
                // Use the iterator to get sorted list
                Iterator it = orderedEvents.getIterator();
                ArrayList<Event> sortedEvents = new ArrayList<>();
                while (it.hasNext()) {
                    LinkedList<Event> eventsLinkedList = (LinkedList<Event>) it.next();
                    sortedEvents.addAll(new ArrayList<>(eventsLinkedList));
                }
                eventList.clear();
                eventList.addAll(sortedEvents);  // Now eventList is sorted in ascending
                sortbutton.animate().rotation(180).setDuration(0).start();
            }
            recommendedActivitiesAdapter.notifyDataSetChanged();  // Notify the adapter
            isAscending = !isAscending;  // Toggle the sort order for the next click
        });


        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
                    startActivity(new Intent(getApplicationContext(), TicketActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
    private void setDateField(EditText dateField) {
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Set date chosen through picker to EditText using formatted string
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(year1, monthOfYear, dayOfMonth);

                            // Formatting date as "yy-MM-dd"
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM, yyyy", Locale.ENGLISH);
                            dateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));

                            dateField.setText(dateFormat.format(selectedDate.getTime()));
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        dateField.setFocusable(false); // Make EditText not focusable to open DatePicker on click
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(false);  // This prevents the dialog from being dismissed by clicking outside
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        setupBottomSheetControls(bottomSheetView);

        Button btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);  // Disable the confirm button initially

        btnConfirm.setOnClickListener(v -> {
            Spinner spinnerFilterOptions = bottomSheetView.findViewById(R.id.spinnerFilterOptions);
            EditText startDate = bottomSheetView.findViewById(R.id.startDate);
            EditText endDate = bottomSheetView.findViewById(R.id.endDate);
            EditText minPrice = bottomSheetView.findViewById(R.id.minPrice);
            EditText maxPrice = bottomSheetView.findViewById(R.id.maxPrice);

            String selectedType = spinnerFilterOptions.getSelectedItem().toString();
            String start = startDate.getText().toString();
            String end = endDate.getText().toString();
            double min = !minPrice.getText().toString().isEmpty() ? Double.parseDouble(minPrice.getText().toString()) : 0;
            double max = !maxPrice.getText().toString().isEmpty() ? Double.parseDouble(maxPrice.getText().toString()) : Double.MAX_VALUE;

            onFilterApplied(selectedType, start, end, min, max);
            bottomSheetDialog.dismiss();
        });

        Button btnCancel = bottomSheetView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }


    private void setupBottomSheetControls(View bottomSheetView) {
        Spinner spinnerFilterOptions = bottomSheetView.findViewById(R.id.spinnerFilterOptions);
        String[] filterOptions = {
                "BoatParty", "Bollywood", "ClimateChange",
                "Comedy", "Disability", "Indigenous", "LibrariesAct",
                "MentalHealth", "MotorbikeTours", "MusicFestivals",
                "MuseumofAustralia", "SchoolHolidays", "WarehouseSale",
                "Wellness", "Other"
        }; // All type of events
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // adding all events in the spinner
        spinnerFilterOptions.setAdapter(adapter);

        setDateField((EditText) bottomSheetView.findViewById(R.id.startDate));
        setDateField((EditText) bottomSheetView.findViewById(R.id.endDate)); // get the date for start and end

        EditText minPrice = bottomSheetView.findViewById(R.id.minPrice);
        EditText maxPrice = bottomSheetView.findViewById(R.id.maxPrice);
        minPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        maxPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // limit the input type for price field

        // Initialize TextWatcher to validate fields
        TextWatcher validateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check all fields to enable the confirm button
                boolean allFieldsFilled = !TextUtils.isEmpty(minPrice.getText()) &&
                        !TextUtils.isEmpty(maxPrice.getText()) &&
                        !TextUtils.isEmpty(((EditText) bottomSheetView.findViewById(R.id.startDate)).getText()) &&
                        !TextUtils.isEmpty(((EditText) bottomSheetView.findViewById(R.id.endDate)).getText());
                Button btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
                btnConfirm.setEnabled(allFieldsFilled);
            }
        };

        // Apply the TextWatcher to all editable fields
        minPrice.addTextChangedListener(validateTextWatcher);
        maxPrice.addTextChangedListener(validateTextWatcher);
        ((EditText) bottomSheetView.findViewById(R.id.startDate)).addTextChangedListener(validateTextWatcher);
        ((EditText) bottomSheetView.findViewById(R.id.endDate)).addTextChangedListener(validateTextWatcher);
    }
    @Override
    public void onFilterApplied(String type, String startDate, String endDate, double minPrice, double maxPrice) {
        Log.d("SearchActivity", "Filters applied - Type: " + type + ", StartDate: " + startDate + ", EndDate: " + endDate + ", MinPrice: " + minPrice + ", MaxPrice: " + maxPrice);
        ArrayList<Event> filteredList = filterEvents(type, startDate, endDate, minPrice, maxPrice);
        eventList.clear();
        eventList.addAll(filteredList);
        // after the filter was applied, change the current eventList and use adapter to notice
        recommendedActivitiesAdapter.notifyDataSetChanged();
    }

    private ArrayList<Event> filterEvents(String type, String startDate, String endDate, double minPrice, double maxPrice) {
        long startTimestamp = Utility.TimeFormatter.convertToTimestamp(startDate);
        long endTimestamp = Utility.TimeFormatter.convertToTimestamp(endDate);

        Log.d("SearchActivity", "Start Date: " + startDate + ", Start Timestamp: " + startTimestamp);
        Log.d("SearchActivity", "End Date: " + endDate + ", End Timestamp: " + endTimestamp);

        ArrayList<Event> filteredList = new ArrayList<>();

        for (Event event : eventList) {
            long eventTimestamp = event.getTimestamp(); // filter the date by timestamp in db
            if (event.getType().equals(type) &&
                    eventTimestamp >= startTimestamp && eventTimestamp <= endTimestamp &&
                    event.getPrice() >= minPrice && event.getPrice() <= maxPrice) {
                filteredList.add(event);
            } else {
                Log.d("SearchActivity", "Event excluded - Type: " + event.getType() + ", Timestamp: " + eventTimestamp + ", Price: " + event.getPrice());
            }
        }

        Log.d("SearchActivity", "Filtered List Size: " + filteredList.size());

        return filteredList;
    }

    private void performSearch(String query) {
        Log.d("SearchActivityPS", "performSearch: Start, Query: " + query);
        // calling the tokenizer and parser
        Tokenizer tokenizer = new Tokenizer(query);
        Parser parser = new Parser(tokenizer);
        try {
            Exp expression = parser.parse();
            if (expression == null) { // check if the parse is valid
                Log.d("SearchActivityPS", "No valid query expression found.");
                Toast.makeText(this, "invalid query", Toast.LENGTH_SHORT).show();
                return;
            }

            EventDataListener listener = eventDataListener();
            if (expression instanceof LessExp || expression instanceof MoreExp) {
                // Handle less and more expressions
                handleComparison(expression, listener);
            } else if (expression instanceof EqualExp) {
                // Handling equal expressions
                handleEquality(expression, listener);
            } else {
                String searchKeyword = extractSearchKeyword(expression);
                if (searchKeyword.isEmpty()) {
                    Toast.makeText(this, "invalid search", Toast.LENGTH_SHORT).show();
                } else {
                    db.getAllEventsByFuzzyName(searchKeyword, listener);
                }
            }
        } catch (Parser.IllegalProductionException e) {
            Log.e("SearchActivityPS", "Parsing error: " + e.getMessage());
            Toast.makeText(this, "parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleComparison(Exp expression, EventDataListener listener) {
        double price;
        String logMessage;

        // Check if the expression is LessExp or MoreExp and extract the price accordingly
        if (expression instanceof LessExp) {
            price = Double.parseDouble(((ValueExp) ((LessExp) expression).getRight()).getValue().toString());
            logMessage = "less than: ";
            db.getLessThan(price, listener);
        } else if (expression instanceof MoreExp) {
            price = Double.parseDouble(((ValueExp) ((MoreExp) expression).getRight()).getValue().toString());
            logMessage = "more than: ";
            db.getGreaterThan(price, listener);
        } else {
            // If the expression is neither, log an error and return early
            Log.e("SearchActivityPS", "Unsupported comparison type");
            Toast.makeText(this, "Unsupported comparison type", Toast.LENGTH_LONG).show();
            return;
        }

        // Log the price comparison operation
        Log.d("SearchActivityPS", "User is searching for events with price " + logMessage + price);
    }


    private void handleEquality(Exp expression, EventDataListener listener) {
        // handle "=" casel
        EqualExp eqExp = (EqualExp) expression;
        String fieldName = ((VariableExp) eqExp.getLeft()).getVariableName();
        Exp rightExp = eqExp.getRight();
        String value;
        if (rightExp instanceof ValueExp) {
            value = ((ValueExp) rightExp).getValue().toString();
        } else if (rightExp instanceof VariableExp) {
            value = ((VariableExp) rightExp).getVariableName();
        } else {
            throw new IllegalStateException("Unsupported expression type for right-hand side of EqualExp");
        }
        Log.d("SearchActivityPS", "User is searching for equation of: " + fieldName + " = " + value);
        handleEqualitySearch(fieldName, value, listener);
    }

    private void handleEqualitySearch(String fieldName, String value, EventDataListener listener) {
        // designed 3 kinds of "=" search, type=variable, price=value, date=date
        Log.d("SearchActivity", "Field name before handleEqualitySearch: " + fieldName);
        switch (fieldName) {
            case "type":
                db.getAllEventsByType(value, listener);
                break;
            case "price":
                try {
                    double price = Double.parseDouble(value);
                    db.getAllEventsByPrice(price, listener);
                } catch (NumberFormatException e) {
                    Log.e("SearchActivity", "Error parsing price: " + value, e);
                    Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
                }
                break;
            case "date":
                try {
                    String fullDate = "2024-" + value; // Assume all dates are in the year 2024
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
                    Date date = sdf.parse(fullDate);
                    Log.d("SearchActivityPS", "User is searching for events on full date: " + fullDate);
                    long timestamp = date.getTime() / 1000; // Convert milliseconds to seconds
                    Log.d("SearchActivityPS", "User is searching for events on date: " + fullDate + " with timestamp: " + timestamp);
                    db.getAllEventsByDate(timestamp, listener);
                } catch (ParseException e) {
                    Log.e("SearchActivity", "Error parsing date: " + value, e);
                    Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.e("SearchActivity", "Unsupported field for equality search: " + fieldName);
                Toast.makeText(this, "Unsupported search field", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    private EventDataListener eventDataListener() {
        return new EventDataListener() {
            @Override
            public void isAllData(boolean isALl) {
                //;
            }

            @Override
            public void onSuccess(ArrayList<Event> events) {
                Log.d("SearchActivityEL", "Search onSuccess: Number of events found: " + events.size());
                eventList.clear();
                eventList.addAll(events);
                recommendedActivitiesAdapter.notifyDataSetChanged();

                if (eventList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No events found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("SearchActivityEL", "Search onFailure: " + error);
                Toast.makeText(SearchActivity.this, "Search Error: " + error, Toast.LENGTH_SHORT).show();
            }
        };
    }
    private String extractSearchKeyword(Exp expression) {
        if (expression instanceof VariableExp) {
            // if only 1 variable, use fuzzy search
            return ((VariableExp) expression).toString();
        } else if (expression instanceof AndExp) {
            String leftKeyword = extractSearchKeyword(((AndExp) expression).getLeft());
            String rightKeyword = extractSearchKeyword(((AndExp) expression).getRight());
            return leftKeyword + " " + rightKeyword;
        } else if (expression instanceof OrExp) {
            String leftKeyword = extractSearchKeyword(((OrExp) expression).getLeft());
            String rightKeyword = extractSearchKeyword(((OrExp) expression).getRight());
            return leftKeyword + " " + rightKeyword;
        } else if (expression instanceof LessExp) {
            return ((VariableExp) ((LessExp) expression).getLeft()).getVariableName();
        } else if (expression instanceof MoreExp) {
            return ((VariableExp) ((MoreExp) expression).getLeft()).getVariableName();
        } else if (expression instanceof EqualExp) {
            EqualExp eqExp = (EqualExp) expression;
            if (eqExp.getRight() instanceof ValueExp) {
                String value = ((ValueExp) eqExp.getRight()).getValue().toString();
                if (isDateString(value)) {
                    return convertDateToDisplayFormat(value); // Convert the date string to a more readable format or standardized format.
                }
                return value;
            } else if (eqExp.getRight() instanceof VariableExp) {
                return ((VariableExp) eqExp.getRight()).getVariableName();
            }
        }
        return "";
    }

    /**
     * Determines if the provided string is a date string.
     * You need to define what constitutes a date string for your application.
     */
    private boolean isDateString(String value) {
        return value.matches("\\d{2}-\\d{2}"); // Example pattern: MM-DD
    }

    /**
     * Converts a date string from MM-DD to a more readable or standardized format.
     * Adjust the implementation based on your needs.
     */
    private String convertDateToDisplayFormat(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            Date date = sdf.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd");
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("SearchActivity", "Error parsing date string: " + dateString, e);
            return dateString; // Return original if parsing fails.
        }
    }

}