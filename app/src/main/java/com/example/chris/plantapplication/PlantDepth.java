package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlantDepth extends AppCompatActivity {

    private plantDataBase plantH;
    private int slotNumber;
    private int buttonID;
    private TextView depthMetre;

    private Button next;
    private SeekBar PlantDepths;
    private TextView plantDepthsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_depth);
        plantH = plantDataBase.getInstance();

        depthMetre = findViewById(R.id.textView44);

        Intent activityThatCalled = getIntent();
        PlantDepths = (SeekBar) findViewById(R.id.seekBar);

        plantDepthsText = (TextView) findViewById(R.id.textView43);
        plantDepthsText.setText("0.0");
        next = (Button) findViewById(R.id.button6);
        buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

        PlantDepths.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //PlantDepths.get
                float percentage = progress / 100.0f;
                float depth = percentage * GlobalConstants.MAXHEIGHT;

                plantDepthsText.setText(Float.toString(depth));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float plantDepth = Float.parseFloat(plantDepthsText.getText().toString());
                if (plantDepth >= 0) {
                    plantH.setPlantGrowingDepth(slotNumber, plantDepth);
                } else {
                    plantH.setPlantGrowingDepth(slotNumber, 0.0f); //Default is set to 0
                }
                Plant currentPlant = plantH.getPlantBySlot(slotNumber);
                currentPlant.updateServerDataBase(1,slotNumber); //Type corresponding to the predetermined input of plants
                callIntent(v.getContext(), MainActivity.class);
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
//waterAmounts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


