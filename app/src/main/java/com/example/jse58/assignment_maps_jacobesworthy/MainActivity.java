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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    EditText editTxtName;
    Button btnAddArtist;
    Spinner spinnerGenres;

    String lat,lon,loc;

    DatabaseReference databaseLocations;

    List<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationList = new ArrayList<>();

        databaseLocations = FirebaseDatabase.getInstance().getReference("assignmentmapsjacobesworthy");

        editTxtName = (EditText)findViewById(R.id.editTxtName);
        btnAddArtist = (Button) findViewById(R.id.btnAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);

        btnAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLoadData();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//
        databaseLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationList.clear();
                for(DataSnapshot locations : dataSnapshot.getChildren())
                {
                    Location location = locations.getValue(Location.class);
                    Log.d(TAG, location.getLocation());
                    //locationList.add(location);
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

    private void nothing()
    {

    }

    private void toastMessage(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
