package com.water.eldowas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.water.eldowas.ui.activity.DescriptionActivity;


public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICES_RES_REQUEST = 7001;

    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference drivers;
    public static final String DRIVERS_CHILD = "DriversLocation";
    Marker mCurrent;
    SupportMapFragment mapFragment;
    private String TAG = "DriverHomeActivity";



    private PlaceAutocompleteFragment places;
    AutocompleteFilter typeFilter;
    private String destination;

    private String imageUrl, index;


    private LatLng currentPosition;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;


    DatabaseReference onlineRef, currentUserRef;

    private Button verifiyBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veritfy_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        index  = intent.getStringExtra("INDEX");
        imageUrl = intent.getStringExtra("IMAGEURL");

        verifiyBtn = (Button) findViewById(R.id.verify_loc_rnd_btn);

        verifiyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MapsActivity.this, DescriptionActivity.class);
                intent.putExtra("INDEX", index);
                intent.putExtra("IMAGEURL", imageUrl);
                intent.putExtra("LONGITUDE", mLastLocation.getLongitude());
                intent.putExtra("LATITUDE", mLastLocation.getLatitude());
                startActivity(intent1);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        setUpLocation();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationCallback();
                    buildLocationRequest();
                        displayLocation();

                }

        }
    }


    private void setUpLocation() {



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request Runtime permission

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {

            buildLocationRequest();
            buildLocationCallback();
                displayLocation();
        }

    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location: locationResult.getLocations())
                {
                    mLastLocation = location;
                }
                displayLocation();
            }
        };

    }

    private void buildLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    private void displayLocation() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mLastLocation = location;

                        if (mLastLocation != null) {


                                final double latitude = mLastLocation.getLatitude();
                                final double longitude = mLastLocation.getLongitude();

                            mCurrent = mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                    .position(new LatLng(latitude, longitude))
                                    .title("You"));
                            //Move camera to position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));


                            }

                    }
                });


    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }





    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Update location
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
        if (mLastLocation != null) {
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();
            if (mCurrent != null)
                mCurrent.remove();
            mCurrent = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .position(new LatLng(latitude, longitude))
                    .title("You"));
            //Move camera to position
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));

        }
    }
}
