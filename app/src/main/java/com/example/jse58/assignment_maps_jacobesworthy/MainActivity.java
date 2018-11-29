package com.example.jse58.assignment_maps_jacobesworthy;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "MainActivity";

    Double currentLat,currentLong;
    String currentLoc;

    ArrayList<Location> dbLocations = null;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseLocations;

    // Map information.
    private GoogleMap mGoogleMap;
    private SupportMapFragment supportMapFragment;
    private LatLng coorinates;
    private Marker mapMarker;

    public Activity MainActivity()
    {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
       supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //useMapClickListener(mGoogleMap);
        useMarkerClickListener(mGoogleMap);
        mapCameraConfiguration(mGoogleMap);
        loadFirebaseData();

    }

    private void mapCameraConfiguration(GoogleMap googleMap){

        LatLng latlng = new LatLng(42,75);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)
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

                    // Adding a new element from the collection
                    MarkerOptions markerOptions = createMarkerFromCurrentLocation(currentLocation.getCoordinates());
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

    private MarkerOptions createMarkerFromCurrentLocation(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        Location tempLocation = new Location();

        markerOptions.position(latLng)
                .title(tempLocation.getLocation())
                .snippet(tempLocation.toString());

        return markerOptions;
    }


//    private void useMapClickListener(final GoogleMap googleMap){
//
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latltn) {
//                if(mapMarker != null){
//                    // Remove current marker from the map.
//                    mapMarker.remove();
//                }
//                // The current marker is updated with the new position based on the click.
//                createCustomMapMarkers(
//                        googleMap,
//                        new LatLng(latltn.latitude, latltn.longitude),
//                        "New Marker",
//                        "Listener onMapClick - new position"
//                                +"lat: "+latltn.latitude
//                                +" lng: "+ latltn.longitude);
//            }
//        });
//    }

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
