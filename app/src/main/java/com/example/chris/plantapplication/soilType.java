package com.example.chris.plantapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class soilType extends AppCompatActivity {

    //Add all of our buttons to monitor
    private ImageButton siltysoil;
    private ImageButton chalkkysoil;
    private ImageButton sandysoil;
    private ImageButton loamysoil;
    private ImageButton claysoil;
    private ImageButton peatysoil;
    private int slotNumber;
    private int buttonID;

    private plantDataBase plantH;
    private Plant currentPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantH = plantDataBase.getInstance();

        Intent activityThatCalled = getIntent();

        buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters


        setContentView(R.layout.activity_soil_type);

        //initiate all of the buttons
        siltysoil = (ImageButton) findViewById(R.id.imageButton16);
        chalkkysoil = (ImageButton) findViewById(R.id.imageButton17);
        sandysoil = (ImageButton) findViewById(R.id.imageButton12);
        loamysoil = (ImageButton) findViewById(R.id.imageButton);
        claysoil = (ImageButton) findViewById(R.id.imageButton15);
        peatysoil = (ImageButton) findViewById(R.id.imageButton14);

        siltysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Silty");
                callIntent(v.getContext(), PlantDepth.class);
            }
        });
        chalkkysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Chalky");
                callIntent(v.getContext(), PlantDepth.class);

            }
        });
        sandysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Sandy");
                callIntent(v.getContext(), PlantDepth.class);
            }
        });
        loamysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Loamy");
                callIntent(v.getContext(), PlantDepth.class);
            }
        });
        claysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Clay");
                callIntent(v.getContext(), PlantDepth.class);
            }
        });
        peatysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantH.setSoilType(slotNumber, "Peaty");
                callIntent(v.getContext(), PlantDepth.class);
            }
        });

    }

    public void callIntent(Context c, Class destination) {

        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
        intent.putExtra("Slot Number", slotNumber);
        startActivity(intent);
    }
}
