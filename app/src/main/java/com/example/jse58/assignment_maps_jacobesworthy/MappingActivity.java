package com.example.jse58.assignment_maps_jacobesworthy;

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

import java.util.ArrayList;

public class MappingActivity extends AppCompatActivity implements OnMapReadyCallback
{

    String TAG = "MappingActivity";
    String currentLoc, currentLat,currentLong, currentDesc;

    Double custLatitude, custLongitude;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseLocations;

    // Map information.
    private GoogleMap mGoogleMap;
    private SupportMapFragment supportMapFragment;

    private Marker mapMarker = null;

    private IntentFilter intentFilter;
    private BroadcastReceiverMap broadcastReceiverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_mapping);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
       supportMapFragment.getMapAsync(this);

        intentFilter = new IntentFilter("com.example.jse58.assignment_maps_jacobesworthy.action.NEW_MAP_LOCATION_BROADCAST");
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
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Intent prevData = getIntent();
        
       currentLat = prevData.getStringExtra("LATITUDE");
       currentLong = prevData.getStringExtra("LONGITUDE");
       currentLoc = prevData.getStringExtra("LOCATION");
       currentDesc = prevData.getStringExtra("DESCRIPTION");

       custLatitude = Double.valueOf(currentLat);
       custLongitude = Double.valueOf(currentLong);
       LatLng custCoords = new LatLng(custLatitude, custLongitude);
        
        // Plot the custom marker here and call function to get the marker options.
        mGoogleMap.addMarker(createMarkerFromCurrentLocation(currentLoc, custCoords, currentDesc));
        
        useMapClickListener();
        useMarkerClickListener();
        useInfoWindowClick();
        useLongInfoWindowClick();
        mapCameraConfiguration();
        firebaseLoadData();
    }

    private void mapCameraConfiguration(){

        Location loc = new Location();
        LatLng latLng = loc.customLocation(custLatitude, custLongitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(3)
                .bearing(0)
                .build();

        // Camera that makes reference to the maps view
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mGoogleMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }});
    }

    private MarkerOptions createMarkerFromCurrentLocation(String location, LatLng latlng, String desc)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latlng)
                .title(location)
                .snippet(desc);

        return markerOptions;
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

    private MarkerOptions userCreatedMarker(LatLng position,  String title, String snippet)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(position)
                .title(title)
                .snippet(snippet);
        
        return markerOptions;
    }

    private void useMapClickListener(){
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latlon) {
                if(mapMarker != null)
                {
                    mapMarker.remove();
                }

                mGoogleMap.addMarker(userCreatedMarker(latlon,"User created position."," "));
            }
        });
    }

    private void useMarkerClickListener(){
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });
    }

    private void useInfoWindowClick()
    {
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });
    }

    private void useLongInfoWindowClick()
    {
        mGoogleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                marker.remove();
            }
        });
    }

    private void firebaseLoadData() {
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

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
