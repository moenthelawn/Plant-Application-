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

    private plantDataBase plantH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_type);
        //initiate all of the buttons
        siltysoil = (ImageButton) findViewById(R.id.imageButton16);
        chalkkysoil = (ImageButton) findViewById(R.id.imageButton17);
        sandysoil = (ImageButton) findViewById(R.id.imageButton12);
        loamysoil = (ImageButton) findViewById(R.id.imageButton);
        claysoil = (ImageButton) findViewById(R.id.imageButton15);
        peatysoil = (ImageButton) findViewById(R.id.imageButton14);


        plantH = plantDataBase.getInstance();

        siltysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityThatCalled = getIntent();

                int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

                Intent intent = new Intent(v.getContext(), MainActivity.class); //Off to let the user chose their soil
                intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
                intent.putExtra("Slot Number", slotNumber);
                startActivity(intent);
            }
        });
        chalkkysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),MainActivity.class);

            }
        });
        sandysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),MainActivity.class);
            }
        });
        loamysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),MainActivity.class);
            }
        });
        claysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),MainActivity.class);
            }
        });
        peatysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),MainActivity.class);
            }
        });

    }
    public void callIntent(Context c, Class destination){

        Intent activityThatCalled = getIntent();

        int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
        intent.putExtra("Slot Number", slotNumber);
        startActivity(intent);
    }
}
