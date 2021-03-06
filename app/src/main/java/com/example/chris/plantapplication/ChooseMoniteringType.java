package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChooseMoniteringType extends AppCompatActivity {
    private ImageButton herbs;
    private ImageButton manualInput;
    private int buttonID;
    private int slotNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_monitering_type);

        Intent activityThatCalled = getIntent();

        buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

        herbs = (ImageButton) findViewById(R.id.imageButton8);
        manualInput = (ImageButton) findViewById(R.id.imageButton17);

        herbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity2.class); //Off to let the user chose their soil
                intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
                intent.putExtra("Slot Number", slotNumber);
                startActivity(intent);

                // callIntent(v.getContext(), Activity2.class);
            }
        });

        manualInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chooseTiming.class); //Off to let the user chose their soil
                intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
                intent.putExtra("Slot Number", slotNumber);
                startActivity(intent);
                //callIntent(v.getContext(),chooseTiming.class);
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
