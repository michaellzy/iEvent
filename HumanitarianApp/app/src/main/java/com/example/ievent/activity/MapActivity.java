package com.example.ievent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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


/**
 * This class is used to display the map.
 */
public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    ActivityMapBinding mapActivityBinding;


    /**
     * The destination should be passed from the activity detail activity or it will be set as default value.
     */
    String destination;
    LatLng destinationLatLng = new LatLng(-35.27718949006979, 149.11829184198544);
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
        Toast.makeText(this, destination, Toast.LENGTH_SHORT).show();
        if(destination == null){
            destination = "18 Argyle Street The Rocks, NSW 2000";
        }

        setPlacesAPI();

        // Set the return button.
        mapActivityBinding.btnReturn.setOnClickListener(v -> finish());
    }



    /**
     * Get a handle to the GoogleMap object and display marker.
     * @param googleMap The GoogleMap object.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // set the position in Canberra ANU and set the center as it as default map
        setMarker(destinationLatLng, googleMap, destination, 15);
        searchDestination(destination, googleMap);
    }


    /**
     * This method is used to search the destination and display the marker on the map.
     *
     * @param destination The destination to search.
     * @param googleMap   The GoogleMap object.
     */
    public void searchDestination(String destination, GoogleMap googleMap) {
        // Get the ID, name, and latitude fields of the Place object.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Define latitude and longitude coordinates of the search area.
        LatLng southWest = new LatLng(-34.09249, 150.59938);
        LatLng northEast = new LatLng(-33.57232, 151.33821);

        // Use the builder to create a SearchByTextRequest object.
        final SearchByTextRequest searchByTextRequest = SearchByTextRequest.builder(destination, placeFields)
                .setMaxResultCount(1)
                .setLocationRestriction(RectangularBounds.newInstance(southWest, northEast)).build();


        // Call PlacesClient.searchByText() to perform the search.
        // Define a response handler to process the returned List of Place objects.
        placesClient.searchByText(searchByTextRequest)
                .addOnSuccessListener(response -> {
                    List<Place> places = response.getPlaces();

                    if (places.isEmpty()) {
                        Toast.makeText(this, "No Places Found", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(this, "Places Found", Toast.LENGTH_SHORT).show();
                    }

                    // Get the first Place object from the list.
                    Place place = places.get(0);

                    // Set the marker on the map.
                    setMarker(place.getLatLng(), googleMap, destination, 15);
                    // Set the open Google Map button.
                    setOpenGoogleMapButton(place.getLatLng(), destination);
                });
    }

    // ----------------------------------------------------- tool methods -----------------------------------------------------

    /**
     * This method is used to set the marker on the map.
     *
     * @param latLng      The latitude and longitude of the marker.
     * @param googleMap   The GoogleMap object.
     * @param markerTitle The title of the marker.
     * @param zoomLevel   The zoom level of the map.
     */
    public void setMarker(LatLng latLng, GoogleMap googleMap, String markerTitle, int zoomLevel) {
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);
        googleMap.moveCamera(zoom);

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(markerTitle));
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


    /**
     * This method is used to set the open Google Map button.
     *
     * @param latLng The latitude and longitude of the marker.
     * @param title  The title of the marker.
     */
    private void setOpenGoogleMapButton(LatLng latLng, String title) {
        mapActivityBinding.btnOpenGoogleMap.setOnClickListener(v -> {
            // Open Google Map
            String uri = "geo:0,0?q=" + latLng.latitude + ","+ latLng.longitude + "(" + title + ")";
            startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri)));
        });
    }
    // ----------------------------------------------------- tool methods end -----------------------------------------------------
}