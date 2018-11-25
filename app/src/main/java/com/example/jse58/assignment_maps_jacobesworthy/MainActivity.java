package com.example.jse58.assignment_maps_jacobesworthy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    TextView txtViewLat, txtViewLon, txtViewLoc;

    // Map information.
    private SupportMapFragment supportMapFragment;
    private LatLng coorinates;
    private Marker mapMarker;

    DatabaseReference databaseLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewLat = (TextView)findViewById(R.id.txtViewLat);
        txtViewLon = (TextView)findViewById(R.id.txtViewLon);
        txtViewLoc = (TextView)findViewById(R.id.txtViewLoc);

       supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        databaseLocations = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseLocations.child("Locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot databaseLocs : dataSnapshot.getChildren())
                {
                    Location location = databaseLocs.getValue(Location.class);
                    Log.d(TAG, location.getLocation());
                    txtViewLat.setText(location.latitude.toString());
                    txtViewLon.setText(location.longitude.toString());
                    txtViewLoc.setText(location.location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firebaseLoadData()
    {

    }

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
