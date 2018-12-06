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

    private int determineMaxGrowthDays(int growthPeriods[]) {
        //This function will determine the maximum amount of growing days based on the harvest days
        int daySum = 0;
        for (int i = 0; i < growthPeriods.length; i++) {
            daySum += growthPeriods[i];
        }
        return daySum;
    }


    private void addPlants() { //we monitor the plants

        basil = (ImageButton) findViewById(R.id.imageButton8);
        customPlant = (ImageButton) findViewById(R.id.imageButton10);
        customPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We want to move to activity addCustomPlant
                Intent activityThatCalled = getIntent();

                int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

                Intent inten1 = new Intent(v.getContext() , addCustomPlant.class);
                inten1.putExtra("Button ID",buttonID);
                inten1.putExtra("Slot Number",slotNumber);

                startActivity(inten1);
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


                int harvestPeriod_days[] = {18, 51, 30, 30, 24, 0}; //The harvest is spanned over the length of the plant's duration

                int maxGrowthDays = determineMaxGrowthDays(harvestPeriod_days);
                double cropCoefficients[][] = {{0.72}, {0.292, 0.0236}, {-0.86, 0.0236}, {-1.17, 0.0236}, {-1.7364, 0.0236}, {1.5}};


                //  basilPlant.setHarvestDayLength(harvestPeriod_days); //set the number of days
                //  basilPlant.setCropCoefficients(cropCoefficients);
                // basilPlant.setpFactor(0.25); //Hardcoded value for the plant database
                //     basilPlant.setMeanTemperature(22.5);

                allPlants.addPlant("Basil", buttonID, slotNumber, harvestPeriod_days, cropCoefficients, 0.25f, 22.5f,maxGrowthDays);
//We want to move to activity addCustomPlant

                Intent inten1 = new Intent(v.getContext() , soilType.class);
                inten1.putExtra("Button ID",buttonID);
                inten1.putExtra("Slot Number",slotNumber);
                startActivity(inten1);
                //openMainActivity("Basil");
            }
        });

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
