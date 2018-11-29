package com.example.jse58.assignment_maps_jacobesworthy;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.jse58.assignment_maps_jacobesworthy.BroadcastReceiverMap;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "MainActivity";
    String currentLoc, currentLat,currentLong, currentDesc;

    Double custLatitude, custLongitude;

    ArrayList<Location> dbLocations = null;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseLocations;

    // Map information.
    private GoogleMap mGoogleMap;
    private SupportMapFragment supportMapFragment;
    private LatLng coorinates;
    private Marker mapMarker;

    private IntentFilter intentFilter;
    private BroadcastReceiverMap broadcastReceiverMap;

    public Activity MainActivity()
    {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       Intent prevData = getIntent();

       currentLat = prevData.getStringExtra("LATITUDE");
       currentLong = prevData.getStringExtra("LONGITUDE");
       currentLoc = prevData.getStringExtra("LOCATION");
       currentDesc = prevData.getStringExtra("DESCRIPTION");

       custLatitude = Double.valueOf(currentLat);
       custLongitude = Double.valueOf(currentLong);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
       supportMapFragment.getMapAsync(this);

        intentFilter = new IntentFilter("com.example.jse58.assignment_maps_jacobesworthy.NEW_MAP_LOCATION_BROADCAST");
        broadcastReceiverMap = new BroadcastReceiverMap();
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(broadcastReceiverMap, intentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiverMap);

        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        useMapClickListener();
        useMarkerClickListener(mGoogleMap);
        mapCameraConfiguration(mGoogleMap);
        loadFirebaseData();

    }

    private void mapCameraConfiguration(GoogleMap googleMap){

        Location loc = new Location();
        LatLng latLng = loc.initialCameraPosition();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(2)
                .bearing(0)
                .build();

        // Camera that makes reference to the maps view
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        googleMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }});
    }

    private void loadFirebaseData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseLocations = firebaseDatabase.getReference();

        databaseLocations.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                for(DataSnapshot databaseLocations : dataSnapshot.getChildren()) {
                    Location currentLocation = databaseLocations.getValue(Location.class);

                    Log.d(TAG, "Location is " + currentLocation.getLocation() + "\nLat: " + currentLocation.getLatitude() + "\nLong: " + currentLocation.getLongitude());

                    MarkerOptions markerOptions = createMarkerFromCurrentLocation(currentLocation);
                    mGoogleMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                toastMessage("Entered the onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                toastMessage("Entered the onChildRemoved");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                toastMessage("Entered the onChildMoved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage("Entered the onCancelled");
            }
        });
    }

    private MarkerOptions createMarkerFromCurrentLocation(Location tempLocation)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        LatLng latLng = tempLocation.getCoordinates();
        String title = tempLocation.getLocation();

        markerOptions.position(latLng)
                .title(title);

        return markerOptions;
    }

    private void userCreatedMarker(LatLng position, String title, @Nullable String Snippet)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(position)
                .title(title);

        mGoogleMap.addMarker(markerOptions);
    }


    private void useMapClickListener(){

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlon) {
                if(mapMarker != null){
                    // Remove current marker from the map.
                    mapMarker.remove();
                }

                userCreatedMarker(
                        latlon,
                        "User created new position"
                                +"lat: "+ latlon.latitude
                                +" lng: "+ latlon.longitude,
                        "");
            }
        });
    }

    private void useMarkerClickListener(GoogleMap googleMap){
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();


                return false;
            }
        });
    }

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
