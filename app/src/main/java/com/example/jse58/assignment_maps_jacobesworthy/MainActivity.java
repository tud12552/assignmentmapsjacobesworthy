package com.example.jse58.assignment_maps_jacobesworthy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "MainActivity";

    String[] dbLocations;

    Double[] dbLats, dbLons;


    TextView txtViewLat, txtViewLon, txtViewLoc;

    private Double currentLat, currentLon;
    private String currentLoc;

    // Map information.
    private SupportMapFragment supportMapFragment;
    private LatLng coorinates;
    private Marker mapMarker;

    DatabaseReference databaseLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        txtViewLat = (TextView)findViewById(R.id.txtViewLat);
//        txtViewLon = (TextView)findViewById(R.id.txtViewLon);
//        txtViewLoc = (TextView)findViewById(R.id.txtViewLoc);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
       supportMapFragment.getMapAsync(this);

        databaseLocations = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseLoadData();

    }

    private void firebaseLoadData()
    {
        toastMessage("Entered the firebaseLoadData");

        databaseLocations.child("Locations").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot databaseLocs : dataSnapshot.getChildren())
                {
                    Location location = databaseLocs.getValue(Location.class);

                    dbLocations[i] = location.getLocation();
                    dbLats[i] = location.getLatitude();
                    dbLons[i] = location.getLongitude();

                    Log.d(TAG, location.getLocation());
                    txtViewLat.setText(location.latitude.toString());
                    txtViewLon.setText(location.longitude.toString());
                    txtViewLoc.setText(location.location);

                    currentLat = location.getLatitude();
                    currentLon = location.getLongitude();
                    currentLoc = location.getLocation();
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        firebaseLoadData();

        if(currentLoc != null || currentLon != null || currentLat != null)
        {
            currentLat = dbLats[0];
            currentLon = dbLons[0];
            currentLoc = dbLocations[0];
            //currentLat = 32.71536;
            //currentLon = -117.161087;
            //currentLoc = "San Diego";
        }
        coorinates = new LatLng(currentLat, currentLon);

        googleMap.addMarker(new MarkerOptions().position(coorinates).title(currentLoc));

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
}
