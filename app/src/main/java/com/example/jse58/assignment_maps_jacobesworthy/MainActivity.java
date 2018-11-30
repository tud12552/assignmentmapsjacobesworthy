package com.example.jse58.assignment_maps_jacobesworthy;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    
    IntentFilter intentFilter = null;
    BroadcastReceiverMap broadcastReceiver = null;

    EditText editTxtLat, editTxtLong, editTxtLoc, editTxtDesc;

    Button btnCustomLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxtLat = (EditText)findViewById(R.id.editTxtLat);
        editTxtLong= (EditText)findViewById(R.id.editTxtLong);
        editTxtLoc = (EditText)findViewById(R.id.editTxtLoc);
        editTxtDesc=(EditText)findViewById(R.id.editTxtDesc);

        btnCustomLoc = (Button)findViewById(R.id.btnNav);

        btnCustomLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String loc = editTxtLoc.getText().toString();
                Double lat = Double.valueOf(editTxtLat.getText().toString());
                Double lon = Double.valueOf(editTxtLong.getText().toString());
                
                Serializable customLoc = new Location(loc, lat, lon);
                Intent intentSendBroadcast = new Intent("com.example.jse58.assignment_maps_jacobesworthy.action.NEW_MAP_LOCATION_BROADCAST");
                intentSendBroadcast.putExtra("CUSTOM_LOCATION",customLoc);
                sendBroadcast(intentSendBroadcast);
                
                Intent goToMapAct = new Intent(MainActivity.this, MappingActivity.class);

                goToMapAct.putExtra("LATITUDE", editTxtLat.getText().toString());
                goToMapAct.putExtra("LONGITUDE", editTxtLong.getText().toString());
                goToMapAct.putExtra("LOCATION", editTxtLoc.getText().toString());
                goToMapAct.putExtra("DESCRIPTION", editTxtDesc.getText().toString());

                startActivity(goToMapAct);

            }
        });
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        intentFilter = new IntentFilter("com.example.jse58.assignment_maps_jacobesworthy.action.NEW_MAP_LOCATION_BROADCAST");
        broadcastReceiver = new BroadcastReceiverMap();
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
