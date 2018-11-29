package com.example.jse58.assignment_maps_jacobesworthy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

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
    }
}
