package com.pusair.smopi.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pusair.smopi.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private FrameLayout frameProsesLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Activity activity = this;
        gpsTracker = new GPSTracker(activity);
        frameProsesLokasi = findViewById(R.id.frameProsesLokasi);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Posisi Anda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 16.0f));

        googleMap.setOnMapClickListener(latLng -> {

            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);

            // Clears the previously touched position
            googleMap.clear();

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

            frameProsesLokasi.setVisibility(View.VISIBLE);
            proccessToCheckout(latLng);
        });
    }

    private void proccessToCheckout(LatLng latLng){
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latLng.latitude);
        bundle.putDouble("longitude", latLng.longitude);

        frameProsesLokasi.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("bundle", bundle);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
    }
}