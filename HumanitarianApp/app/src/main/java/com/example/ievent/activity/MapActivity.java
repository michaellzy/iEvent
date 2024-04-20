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

// Implement OnMapReadyCallback.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapBinding mapActivityBinding = ActivityMapBinding.inflate(getLayoutInflater());

        // Set the layout file as the content view.
        setContentView(mapActivityBinding.getRoot());

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapActivityBinding.map.getId());

        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // set the position in Canberra ANU and set the center as it
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(zoom);

        LatLng target = new LatLng(-35.282001, 149.128998);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(target));
        googleMap.addMarker(new MarkerOptions()
                .position(target)
                .title("ANU"));
    }
}