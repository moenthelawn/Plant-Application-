package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class chooseTiming extends AppCompatActivity {
    private SeekBar waterAmounts;
    private TextView error;
    private EditText waterPlantName;
    private EditText waterAmountText;
    private EditText plantGrowth_Hours;
    private TextView elapsedGrowthPeriod;
    private EditText plantGrowth_Days;
    private int progressChangedValue;
    private Button next;
    private Boolean touched_progress = false;
    private Boolean touched_growthDays = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_timing);

        waterAmounts = (SeekBar) findViewById(R.id.test);
        waterPlantName = (EditText) findViewById(R.id.editText4);
        plantGrowth_Hours = (EditText) findViewById(R.id.editText8);
        plantGrowth_Days = (EditText) findViewById(R.id.editText);
        elapsedGrowthPeriod = (TextView) findViewById(R.id.textView31);
        elapsedGrowthPeriod.setVisibility(elapsedGrowthPeriod.INVISIBLE);

        waterAmountText = (EditText) findViewById(R.id.editText6);
        error = (TextView) findViewById(R.id.textView5);
        error.setVisibility(error.INVISIBLE);
        next = (Button) findViewById(R.id.button3);
        waterAmountText.setVisibility(waterAmountText.INVISIBLE);


        waterAmounts.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waterAmountText.setVisibility(waterAmountText.VISIBLE);
                waterAmountText.setText(Integer.toString(progress), TextView.BufferType.EDITABLE);
                updateDisplayText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        plantGrowth_Days.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDisplayText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        plantGrowth_Hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Update the plant text
            //     int days = Integer.parseInt(plantGrowth_Days.getText().toString());
           //     if (days <= 24) {//Their entrance text must be less than 24 hours
                    updateDisplayText();
             //   }
             //   else{
                    //Update the error text
            //    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Now we want to create the plant and input it in with the correct paramaters

                if (!isError()) {
                    String name = waterPlantName.getText().toString();
                    float waterAmount = Float.parseFloat(waterAmountText.getText().toString());
                   // int HarvestDays = Integer.parseInt(plantGrowthDays.getText().toString());

                    Intent getActivityThatCalled = getIntent();
                    int buttonID = getActivityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                    int slotNumber = getActivityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters
                    float cropCoefficient = -1;

                    Plant plant = new Plant(name, buttonID, slotNumber, -1, "Manual", -1, -1);

                    //Add the plant to the database
                    plantDataBase.getInstance().addPlant_SlotNumber(slotNumber, plant);
                    int hours = Integer.parseInt(plantGrowth_Hours.getText().toString());
                    int days = Integer.parseInt(plantGrowth_Days.getText().toString());
                    plant.setGrowthInterval(convertToHours(hours,days));
                    plant.setWaterRequirement_Period(waterAmount);
                    plant.updateServerDataBase(2); //Type corresponding to the predetermined input of plants

                    callIntent(v.getContext(), MainActivity.class);

                }

                /*else if {

                }*/

            }
        });

    }

    private int convertToHours(int hours, int days) {
    //This function takes in the hours and days and converts it to hours
        int total = hours + (days * 24);
        return total;
    }

    private void updateDisplayText() {
        int plantHoursLength = plantGrowth_Hours.getText().toString().trim().length();
        int plantDaysLength = plantGrowth_Days.getText().toString().trim().length();
        int waterAmountLength = waterAmountText.getText().toString().trim().length();

        String waterAmount = waterAmountText.getText().toString();
        String hours = plantGrowth_Hours.getText().toString();
        String days = plantGrowth_Days.getText().toString();
        String total = "";

        if (waterAmountLength != 0 && (plantHoursLength != 0 || plantDaysLength != 0)) {
            total = "Your plant will be watered ";
            total += waterAmount + "mm ";
            if (plantDaysLength != 0) {
                total += "every " + days + " days";
                elapsedGrowthPeriod.setVisibility(elapsedGrowthPeriod.VISIBLE);

            } else if (plantHoursLength != 0 ) {
                total += "every " + hours + " hours";
                elapsedGrowthPeriod.setVisibility(elapsedGrowthPeriod.VISIBLE);
                elapsedGrowthPeriod.setText(total);
                return;
            }
            if ( plantHoursLength != 0) {
                total += " and " + hours + " hours";
                elapsedGrowthPeriod.setVisibility(elapsedGrowthPeriod.VISIBLE);
                elapsedGrowthPeriod.setText(total);
                return;
            }

                    /*if (hours == "" && days == "") {
                        total = "Your pla"
                    }if (hours != "") {
                        total = waterAmount + " mm" + "every " + hours;
                    }
                   */
                elapsedGrowthPeriod.setText(total);
        }
        else{
            elapsedGrowthPeriod.setVisibility(elapsedGrowthPeriod.INVISIBLE);
        }
    }

    private boolean isError() {
        boolean totalerror = false;
        int count = 0;
        if (waterPlantName.getText().toString().trim().length() == 0
                || waterAmountText.getText().toString().trim().length() == 0
                || plantGrowth_Hours.getText().toString().trim().length() == 0
                || plantGrowth_Days.getText().toString().trim().length() == 0) {
            error.setVisibility(error.VISIBLE);
            error.setText("Please enter a valid ");
            error.setTextColor(getResources().getColor(R.color.Red));
            totalerror = true;
        }
        if (waterAmountText.getText().toString().trim().length() == 0) {
            count += 1;
            error.append("water amount");
        }
        if (waterPlantName.getText().toString().trim().length() == 0) {
            if (count == 1) {
                error.append(" and ");
            }
            error.append("plant name");
            count += 1;
        }
        if (plantGrowth_Hours.getText().toString().trim().length() == 0
                || plantGrowth_Days.getText().toString().trim().length() == 0) {
            if (count >= 1) {
                error.append(" and ");
            }
            error.append("watering period");
            count += 1;
        }
        if (totalerror == true) {
            error.append(".");
        }
        return totalerror;
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

}
