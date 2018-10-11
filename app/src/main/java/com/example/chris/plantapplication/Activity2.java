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
                openMainActivity("Basil");
            }
        });

    }
    public void openMainActivity(String PlantName) { //We want to open that activity and navigate over to the specific class
        Intent activityThatCalled = getIntent();

        int buttonID= activityThatCalled.getIntExtra("Button ID",0); //get the button session ID so we can modify its .xml paramaters

        //Based on the slot number that we clicked, we will add the plant number to it
        allPlants = plantDataBase.getInstance();
        boolean b = allPlants.setPlantSlotByString(PlantName, buttonID);
        finish();
        //         Intent intent = new Intent(this, MainActivity.class);
        //finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //        startActivity(intent);
    }
}
