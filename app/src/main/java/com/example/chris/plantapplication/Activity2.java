package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.app.Activity;
import android.widget.RelativeLayout;


public class Activity2 extends AppCompatActivity {
    private ImageButton basil;
    private ImageButton customPlant;
    private plantDataBase allPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        addPlants();

    }

    private void addPlants() { //we monitor the plants

        basil = (ImageButton) findViewById(R.id.imageButton10);
        customPlant = (ImageButton) findViewById(R.id.imageButton10);
        customPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(), chooseTiming.class);

            }
        });

        basil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, we also need to change the previous button pressed from the other screen
                //If this button is clicked, then we will open activity2

                // int lightingPerDay = 6 * 60; //6 hours per day converted to minutes per day

                // float temp_min = (float) 21.2; //Min Temp:
                //All data below only relates to Basil

                Intent activityThatCalled = getIntent();

                int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

                allPlants = plantDataBase.getInstance();

                int harvestPeriod_days = 56;//The harvest is spanned over the length of the plant's duration
                float cropCoefficient = 0.73f;

                //  basilPlant.setHarvestDayLength(harvestPeriod_days); //set the number of days
                //  basilPlant.setCropCoefficients(cropCoefficients);
                // basilPlant.setpFactor(0.25); //Hardcoded value for the plant database
                //     basilPlant.setMeanTemperature(22.5);

                allPlants.addPlant("Basil", buttonID, slotNumber, harvestPeriod_days, cropCoefficient, 0.25f, 22.5f, "Predetermined");
                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
                //openMainActivity("Basil");
            }
        });

    }

    public void callIntent(Context c, Class destination) {

        Intent activityThatCalled = getIntent();

        int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
        intent.putExtra("Slot Number", slotNumber);
        startActivity(intent);
    }

    public void openMainActivity(String PlantName) { //We want to open that activity and navigate over to the specific class
        Intent activityThatCalled = getIntent();

        int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters
        finish();
        //         Intent intent = new Intent(this, MainActivity.class);
        //finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //        startActivity(intent);
    }
}
