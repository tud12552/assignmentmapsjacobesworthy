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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "MainActivity";

    public int i = 0;

    TextView txtViewLoc;
    TextView txtViewLat;
    TextView txtViewLon;

    Double currentLat,currentLong;
    String currentLoc;

    ArrayList<Location> dbLocations = null;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseLocations;

    // Map information.
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

//       txtViewLoc = (TextView)findViewById(R.id.txtViewLocation);
//       txtViewLat = (TextView)findViewById(R.id.txtViewLat);
//       txtViewLon = (TextView) findViewById(R.id.txtViewLon);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
       supportMapFragment.getMapAsync(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseLocations = firebaseDatabase.getReference("Locations");

//        firebaseLoadData();
    }

    /*public void firebaseLoadData()
    {
        dbLocations = new ArrayList<>();


        databaseLocations.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                toastMessage("Entered the onChildAdded");

                for (DataSnapshot databaseLocs : dataSnapshot.getChildren())
                {
                    Location loc = dataSnapshot.getValue(Location.class);

                    Log.d(TAG,loc.toString());

                    Log.d(TAG, "Location: " + location.getLocation() + " Latitude: " + location.getLatitude().toString()
                    + " Longitude: " + location.getLongitude().toString());

                    //dbLocations.add(location);


                    txtViewLoc.setText(location.getLocation());
                    txtViewLat.setText(location.getLatitude().toString());
                    txtViewLon.setText(location.getLongitude().toString());
                    i++;
                }
                txtViewLat.setText(String.valueOf(i));
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                toastMessage("Entered the onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {
                toastMessage("Entered the onChildRemoved");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                toastMessage("Entered the onChildMoved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                toastMessage("Entered the onCancelled");
            }
        });
    }
    */

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        ArrayList<Location> allLocations = loadData();

        for( int ii = 0; ii < allLocations.size(); ii++)
        {
            Location tmpLocation = dbLocations.get(ii);
            coorinates = new LatLng(tmpLocation.getLatitude(), tmpLocation.getLongitude());
            createCustomMapMarkers(googleMap, coorinates, tmpLocation.getLocation(), "HI");
        }
        //googleMap.addMarker(new MarkerOptions().position(coorinates).title(tmpLocation.getLocation()));

        useMapClickListener(googleMap);
        useMarkerClickListener(googleMap);
        mapCameraConfiguration(googleMap);


    }

    private void mapCameraConfiguration(GoogleMap googleMap){

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(coorinates)
                .zoom(10)
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

    private void createCustomMapMarkers(GoogleMap googleMap, LatLng latlng, String title, String snippet){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng) // coordinates
                .title(title) // location name
                .snippet(snippet); // location description

        // Update the global variable (currentMapMarker)
        mapMarker = googleMap.addMarker(markerOptions);
    }

    private void useMapClickListener(final GoogleMap googleMap){

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latltn) {
                if(mapMarker != null){
                    // Remove current marker from the map.
                    mapMarker.remove();
                }
                // The current marker is updated with the new position based on the click.
                createCustomMapMarkers(
                        googleMap,
                        new LatLng(latltn.latitude, latltn.longitude),
                        "New Marker",
                        "Listener onMapClick - new position"
                                +"lat: "+latltn.latitude
                                +" lng: "+ latltn.longitude);
            }
        });
    }

    private void useMarkerClickListener(GoogleMap googleMap){
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });
    }


    private ArrayList<Location> loadData(){

        ArrayList<Location> mapLocations = new ArrayList<>();

        mapLocations.add(new Location("New York",39.953348, -75.163353));
        mapLocations.add(new Location("Paris",48.856788, 2.351077));
        mapLocations.add(new Location("Las Vegas", 36.167114, -115.149334));
        mapLocations.add(new Location("Tokyo", 35.689506, 139.691700));

        return mapLocations;
    }
}
