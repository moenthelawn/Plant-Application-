package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseParamaterType extends AppCompatActivity {

    private Button customType;
    private Button paramaterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_paramater_type);

        //Getting the button IDs
        customType = (Button) findViewById(R.id.button4);
        paramaterType = (Button) findViewById(R.id.button5);

        customType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),addCustomPlant.class);
            }
        });
        paramaterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent(v.getContext(),chooseTiming.class);

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
