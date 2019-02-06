package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Activity2 extends AppCompatActivity {
    private ImageButton basil;
    private ImageButton mint;
    private ImageButton thyme;
    private ImageButton oregano;
    private ImageButton dill;
    private TextView warningLabelText;
    private ImageView ex1;

    private ImageButton customPlant;
    private plantDataBase allPlants;
    private int buttonID;
    private int slotNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Intent activityThatCalled = getIntent();

        //Also display the bottom text
        warningLabelText = (TextView) findViewById(R.id.textView18);
        ex1 = (ImageView) findViewById(R.id.imageView10);
        ex1.setVisibility(ex1.INVISIBLE);
        warningLabelText.setVisibility(warningLabelText.INVISIBLE);

        basil = (ImageButton) findViewById(R.id.imageButton10);
        mint = (ImageButton) findViewById(R.id.imageButton12);
        thyme = (ImageButton) findViewById(R.id.imageButton14);
        oregano = (ImageButton) findViewById(R.id.imageButton15);
        dill = (ImageButton) findViewById(R.id.imageButton16);
        buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters
        allPlants = plantDataBase.getInstance();

        updateButtonLabels(); // This function will update the warning labels of each of the buttons

        thyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int harvestPeriod_days = 70;//The harvest is spanned over the length of the plant's duration
                allPlants.addPlant("Thyme", buttonID, slotNumber, harvestPeriod_days, "Predetermined", 15.6f, 26.7f);

                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
            }
        });

        dill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int harvestPeriod_days = 90;//The harvest is spanned over the length of the plant's duration

                allPlants.addPlant("Dill", buttonID, slotNumber, harvestPeriod_days, "Predetermined", 10f, 21.1f);

                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
            }
        });

        oregano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int harvestPeriod_days = 60;//The harvest is spanned over the length of the plant's duration

                allPlants.addPlant("Oregano", buttonID, slotNumber, harvestPeriod_days, "Predetermined", 15.6f, 21.1f);

                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
            }
        });

        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int harvestPeriod_days = 90;//The harvest is spanned over the length of the plant's duration

                allPlants.addPlant("Mint", buttonID, slotNumber, harvestPeriod_days, "Predetermined", 15.6f, 26.7f);

                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
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

                allPlants = plantDataBase.getInstance();

                int harvestPeriod_days = 56;//The harvest is spanned over the length of the plant's duration
                //  float cropCoefficient = 0.73f;

                //  basilPlant.setHarvestDayLength(harvestPeriod_days); //set the number of days
                //  basilPlant.setCropCoefficients(cropCoefficients);
                // basilPlant.setpFactor(0.25); //Hardcoded value for the plant database
                //     basilPlant.setMeanTemperature(22.5);

                allPlants.addPlant("Basil", buttonID, slotNumber, harvestPeriod_days, "Predetermined", 23.9f, 29.4f);
                //We want to move to activity addCustomPlant
                callIntent(v.getContext(), soilType.class);
                //openMainActivity("Basil");
            }
        });
    }

    private void updateButtonLabels() {
        //We want to see the current plants that are growing and check if there are any types of plants
        //that are incompatible

        if (allPlants.isPlant("Basil")) {
            //If the user is currently growing basil, then we should issue a notification error to the user
            //Then we issue an error to either Thyme, Oregano, dill
            thyme.setImageResource(R.drawable.buttonthyme_warning);
            oregano.setImageResource(R.drawable.buttonoregano_warning);
            dill.setImageResource(R.drawable.buttondill_warning);
            ex1.setVisibility(ex1.VISIBLE);
            warningLabelText.setVisibility(warningLabelText.VISIBLE);

        }


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
