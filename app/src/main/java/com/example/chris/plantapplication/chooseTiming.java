package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class chooseTiming extends AppCompatActivity {
    private SeekBar waterAmounts;
    private EditText waterPlantName;
    private EditText waterAmountText;
    private EditText plantGrowthDays;
    private int progressChangedValue;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_timing);

        waterAmounts = (SeekBar) findViewById(R.id.seekBar);
        waterAmountText = (EditText)findViewById(R.id.editText6);
        waterPlantName = (EditText) findViewById(R.id.editText4);
        plantGrowthDays = (EditText)findViewById(R.id.editText8);

        next = (Button) findViewById(R.id.button3);
        waterAmountText.setVisibility(waterAmountText.INVISIBLE);

        waterAmounts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waterAmountText.setVisibility(waterAmountText.VISIBLE);
                waterAmountText.setText(Integer.toString(progress), TextView.BufferType.EDITABLE);
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

                //Now we want to create the plant and input it in with the correct paramaters
                String name = waterPlantName.getText().toString();
                float waterAmount = Float.parseFloat(waterAmountText.getText().toString());
                int HarvestDays = Integer.parseInt(plantGrowthDays.getText().toString());

                Intent getActivityThatCalled = getIntent();
                int buttonID = getActivityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                int slotNumber = getActivityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters
                float cropCoefficient = -1;
                Plant plant = new Plant(name, buttonID,slotNumber, HarvestDays,"Manual");

                //Add the plant to the database
                plantDataBase.getInstance().addPlant_SlotNumber(slotNumber, plant);
                plant.setWaterRequirement_Manual(waterAmount);
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
