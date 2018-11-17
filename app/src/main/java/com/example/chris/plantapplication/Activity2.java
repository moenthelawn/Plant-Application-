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
    private plantDataBase allPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        addPlants();

    }
    private void addPlants(){ //we monitor the plants


        basil = (ImageButton) findViewById(R.id.imageButton);

        basil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, we also need to change the previous button pressed from the other screen
                //If this button is clicked, then we will open activity2

               // int lightingPerDay = 6 * 60; //6 hours per day converted to minutes per day

               // float temp_min = (float) 21.2; //Min Temp:
                //All data below only relates to Basil
                allPlants = plantDataBase.getInstance();
                Plant basilPlant = allPlants.getPlant("Basil");

                int harvestPeriod_days[] = {18,51,30,30,24,0}; //The harvest is spanned over the length of the plant's duration
                double cropCoefficients[][] = {{0.72},{0.292,0.0236},{-0.86,0.0236}, {-1.17,0.0236},{-1.7364,0.0236},{1.5}};

                basilPlant.setHarvestDayLength(harvestPeriod_days); //set the number of days
                basilPlant.setCropCoefficients(cropCoefficients);
                basilPlant.setpFactor(0.25); //Hardcoded value for the plant database
                basilPlant.setMeanTemperature(22.5);

                int testDay = 120;
                double waterAmount = basilPlant.getDailyWaterAmount_millimetres(testDay);

                openMainActivity("Basil");
            }
        });

    }
    public void openMainActivity(String PlantName) { //We want to open that activity and navigate over to the specific class
        Intent activityThatCalled = getIntent();

        int buttonID= activityThatCalled.getIntExtra("Button ID",0); //get the button session ID so we can modify its .xml paramaters
        int slotNumber = activityThatCalled.getIntExtra("Slot Number",0); //get the button session ID so we can modify its .xml paramaters

        //Based on the slot number that we clicked, we will add the plant number to it
        allPlants = plantDataBase.getInstance();
        boolean b = allPlants.setPlantSlotByString(PlantName, buttonID, slotNumber );
        finish();
        //         Intent intent = new Intent(this, MainActivity.class);
        //finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //        startActivity(intent);
    }
}
