package com.cs407.lab6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    //TODO: need because:???
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; // could've been any number!

    //global var to use throughout our class. Note: private
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //just a set/final location used for this assignment lab
    private final LatLng mDestinationLatLng = new LatLng(43.075404393142115, -89.40341145630344);

    //Our map object data itself. We get via google API
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we auto set our <fragment>. SupportMapFragment is the name because thats the name of our map fragment
        //TODO: can we choose the name in the xml file or is this a set name?
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_map);

        //TODO: what does getMapAsync do? "to be notified when the mapFragment is ready"
        mapFragment.getMapAsync(googleMap ->{
            mMap = googleMap;

            //we make markers with this googleMap
            //TODO: why is it via googleMap and not mMap since we do mMap=google map
            googleMap.addMarker(new MarkerOptions()
                    .position (mDestinationLatLng)
                    .title("Destination"));


        });

        //Obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        displayMyLocation();


    }


    private void displayMyLocation() {
        // Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);


        // If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED) {
                //TODO: what happens if they deny it again?
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        //If permission granted, display marker at current location
        else {
            //via our fusedLocationProviderCLIENT and its getLastLocation
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                if (task.isSuccessful() && mLastKnownLocation != null) {
                    //we add our visual polyLine in OUR mMap
                    mMap. addPolyline(new PolylineOptions ().add(
                            new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                            mDestinationLatLng));



                    //TODO
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                            findFragmentById(R.id.fragment_map);
                    mapFragment.getMapAsync(googleMap ->{
                        mMap = googleMap;

                        //we make markers with this googleMap
                        googleMap.addMarker(new MarkerOptions()
                                .position (new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                                .title("Current"));

                    });

                }
            });
        }

    }


}
