package com.example.jse58.assignment_maps_jacobesworthy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jse58.assignment_maps_jacobesworthy.BroadcastReceiverMap;
import com.example.jse58.assignment_maps_jacobesworthy.Location;

public class MainActivity2 extends AppCompatActivity {
    
    IntentFilter intentFilter = null;
    Broadcastreceiver broadcastReceiver = null;

    EditText editTxtLat, editTxtLong, editTxtLoc, editTxtDesc;

    Button btnCustomLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editTxtLat = (EditText)findViewById(R.id.editTxtLat);
        editTxtLong= (EditText)findViewById(R.id.editTxtLong);
        editTxtLoc = (EditText)findViewById(R.id.editTxtLoc);
        editTxtDesc=(EditText)findViewById(R.id.editTxtDesc);

        btnCustomLoc = (Button)findViewById(R.id.btnNav);

        btnCustomLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMapAct = new Intent(MainActivity2.this, MainActivity.class);

                goToMapAct.putExtra("LATITUDE", editTxtLat.getText().toString());
                goToMapAct.putExtra("LONGITUDE", editTxtLong.getText().toString());
                goToMapAct.putExtra("LOCATION", editTxtLoc.getText().toString());
                goToMapAct.putExtra("DESCRIPTION", editTxtDesc.getText().toString());

                startActivity(goToMapAct);

            }
        });
        
        @Override
        protected void onStart()
        {
            super.onStart();
            intentFilter = new IntentFilter(BroadcastReceiverMap.NEW_MAP_LOCATION_BROADCAST);
            broadcastReceiverMap = new BroadcastReceiverMap();
            registerReceiver(broadcastReceiverMap, intentFilter);
        }
        
        @Override
        protected void onStop()
        {
            unregisterReceiver(broadcastReceiverMap);
            super.onStop();
        }
    }
}
