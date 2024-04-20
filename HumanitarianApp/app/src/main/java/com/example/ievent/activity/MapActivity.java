package com.example.ievent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ievent.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class is used to display the map.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityMapBinding mapActivityBinding;

    String destination;

    LatLng destinationLatLng;


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

        setVariables();
    }

    /**
     * This method is used to set the variables for the activity.
     */
    private void setVariables() {
        // get the destination from the intent
        destination = getIntent().getStringExtra("destination");
        // get the latitude and longitude of the destination by searching it from the google map api
        // and set the destinationLatLng

        mapActivityBinding.btnReturn.setOnClickListener(v -> finish());
        mapActivityBinding.btnOpenGoogleMap.setOnClickListener(v -> {
            // Open Google Map
            String uri = "geo:0,0?q=-35.282001,149.128998(ANU)";
            startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri)));
        });
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

        // Add a large marker at ANU.
        googleMap.addMarker(new MarkerOptions()
                .position(target)
                .title("ANU"));
    }
}