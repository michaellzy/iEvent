package com.example.ievent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ievent.R;
import com.example.ievent.adapter.RecommendedActivitiesAdapter;
import com.example.ievent.database.listener.EventDataListener;
import com.example.ievent.entity.Event;
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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SearchActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private RecommendedActivitiesAdapter recommendedActivitiesAdapter;
    private List<Event> eventList = new ArrayList<>();
//    private List<Event> temporaryResults = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button filterbutton = findViewById(R.id.filter_button);


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
//                performSearch(newText);
                return false;
            }
        });

        filterbutton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchActivity.this);

            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            bottomSheetDialog.show();
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    // finish();
                    return true;
                } else if (itemId == R.id.navigation_search) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
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


//        initSearchWidgets();
//        setUpData();
    }

    private void initSearchWidgets(){
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpData(){
        Toast.makeText(this, "Loading data", Toast.LENGTH_SHORT).show();

        // gets data here
        db.getAllEventsByFuzzyName("Saturdays", new EventDataListener() {

            @Override
            public void isAllData(boolean isALl) {

            }

            @Override
            public void onSuccess(ArrayList<Event> events) {
                // set up the data here
                // input the event title into a local file
                for (Event event : events) {
                    Log.i("EVENT1111", "onSuccess: " + event.getTitle());

                }
            }

            @Override
            public void onFailure(String error) {
                // handle the error here
                Log.i("EVENT1111", "onFailure: " + error);
            }
        });
    }
    private void performSearch(String query) {
        Log.d("SearchActivityPS", "performSearch: Start, Query: " + query);
        Tokenizer tokenizer = new Tokenizer(query);
        Parser parser = new Parser(tokenizer);
        try {
            Exp expression = parser.parse();
            if (expression == null) {
                Log.d("SearchActivityPS", "No valid query expression found.");
                Toast.makeText(this, "unknown query", Toast.LENGTH_SHORT).show();
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
            return;
        }

        // Log the price comparison operation
        Log.d("SearchActivityPS", "User is searching for events with price " + logMessage + price);
    }


    private void handleEquality(Exp expression, EventDataListener listener) {
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

                if (!eventList.isEmpty()) {
                    Event firstEvent = eventList.get(0);
                    Log.d("SearchActivity", "First event price: " + firstEvent.getPrice());
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
