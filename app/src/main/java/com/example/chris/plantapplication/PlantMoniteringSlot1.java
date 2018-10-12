package com.example.chris.plantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

public class PlantMoniteringSlot1 extends AppCompatActivity {
    private plantDataBase plantH; //Plant Database
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_monitering_slot1);
        //We want to grab an instance of the plant data base that will be used for this slot

        Intent activityThatCalled = getIntent();
        int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        Log.i("Passed Value", "Button "+ Integer.toString(buttonID) + "passed to PlanMoniteringSlot1.java");
        displayPlantData(buttonID);
        
    }

    public void displayPlantData(int buttonID) {
        //Here we want to update the graphical charts to show the type of plant that we have
        plantH = plantDataBase.getInstance();
        Plant requiredPlant = plantH.getPlantByButtonNumber(buttonID);
        Log.i("Plant",requiredPlant.getName() +" added to PlantMoniteringSlot1");

    }
}
