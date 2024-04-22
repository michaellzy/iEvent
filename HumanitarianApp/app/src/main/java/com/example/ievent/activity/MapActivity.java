package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.ievent.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchByTextRequest;

import java.util.Arrays;
import java.util.List;

import io.grpc.android.BuildConfig;

/**
 * This class is used to display the map.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityMapBinding mapActivityBinding;

    String destination;

    LatLng destinationLatLng;

    PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapActivityBinding = ActivityMapBinding.inflate(getLayoutInflater());

        // Set the layout file as the content view.
        setContentView(mapActivityBinding.getRoot());

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapActivityBinding.map.getId());

        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        try {
            setVariables();
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to set the variables for the activity.
     */
    private void setVariables() throws PackageManager.NameNotFoundException {
        // get the destination from the intent
        destination = getIntent().getStringExtra("destination");

        setPlacesAPI();


        mapActivityBinding.btnReturn.setOnClickListener(v -> finish());
        mapActivityBinding.btnOpenGoogleMap.setOnClickListener(v -> {
            // Open Google Map
            String uri = "geo:0,0?q=-35.282001,149.128998(ANU)";
            startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri)));
        });
    }

    /**
     * This method is used to set the Places API and get the PlacesClient.
     */
    private void setPlacesAPI() throws PackageManager.NameNotFoundException {
        // Define a variable to hold the Places API key.
        ApplicationInfo applicationInfo = this.getPackageManager().getApplicationInfo(
                this.getPackageName(), PackageManager.GET_META_DATA
        );
        Bundle metaData = applicationInfo.metaData;

        String apiKey = metaData.getString("com.google.android.geo.API_KEY");

        // Log an error if apiKey is not set.
        if (TextUtils.isEmpty(apiKey) || apiKey.equals("DEFAULT_API_KEY")) {
            Log.e("Places test", "No api key");
            finish();
            return;
        }
        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);
        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);
    }


    /**Get a handle to the GoogleMap object and display marker.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // set the position in Canberra ANU and set the center as it
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(zoom);

        LatLng target = new LatLng(-35.282001, 149.128998);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(target));

        // Add a large marker at the destination in the map
        googleMap.addMarker(new MarkerOptions()
                .position(target)
                .title(destination));

        searchDestination(destination);
    }

    public void searchDestination(String destination) {
        // search the destination from the google map api and set the destinationLatLng
        // Specify the list of fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Define latitude and longitude coordinates of the search area.
        LatLng southWest = new LatLng(37.38816277477739, -122.08813770258874);
        LatLng northEast = new LatLng(37.39580487866437, -122.07702325966572);

        // Use the builder to create a SearchByTextRequest object.
        final SearchByTextRequest searchByTextRequest = SearchByTextRequest.builder("Spicy Vegetarian Food", placeFields)
                .setMaxResultCount(10)
                .setLocationRestriction(RectangularBounds.newInstance(southWest, northEast)).build();

        // Call PlacesClient.searchByText() to perform the search.
        // Define a response handler to process the returned List of Place objects.
        placesClient.searchByText(searchByTextRequest)
                .addOnSuccessListener(response -> {
                    List<Place> places = response.getPlaces();
                });

    }
}