package com.example.chris.plantapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlantMoniteringSlot1 extends AppCompatActivity {
    private plantDataBase plantH; //Plant Database
    private ImageButton backButton;
    int buttonID_Called;
    int slot_ID_Called;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_monitering_slot1);
        //We want to grab an instance of the plant data base that will be used for this slot

        plantH = plantDataBase.getInstance();

        Intent activityThatCalled = getIntent();
        buttonID_Called = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slot_ID_Called = activityThatCalled.getIntExtra("Slot Number", 0);

        Log.i("Passed Value", "Button " + Integer.toString(buttonID_Called) + "passed to PlanMoniteringSlot1.java");
        displayPlantData(slot_ID_Called);

        backButton = (ImageButton) findViewById(R.id.imageButton3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, we also need to change the previous button pressed from the other screen
                //If this button is clicked, then we will open activity2
                Plant currentPlant_Slot = plantH.getPlantByButtonNumber(buttonID_Called);
                openMainActivity(currentPlant_Slot.getName());
            }
        });

    }

    public void openMainActivity(String PlantName) { //We want to open that activity and navigate over to the specific class

        finish();
        //         Intent intent = new Intent(this, MainActivity.class);
        //finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //        startActivity(intent);
    }

    public void updateRoomTemperature(float roomTemperature) {
        TextView roomTemp = (TextView) findViewById(R.id.textView32); //to be able to set the room temperature to the correct value
        String temp = Float.toString(roomTemperature) + "°C";
        //temp += "°C";
        roomTemp.setText(temp);
    }

    public void updateHumidity(float humidity) {
        TextView humidityBut = (TextView) findViewById(R.id.textView35); //to be able to set the room temperature to the correct value
        String temp = Float.toString(humidity) + "%";
        //temp += "%";
        humidityBut.setText(temp);
    }
    public void animateHarvestTimeProgress(int dayNumber, int totalElapsedDays){
        final ProgressBar currentProgress = findViewById(R.id.progressBar);
        int currentProg = currentProgress.getProgress();
        double growthP = ((double) dayNumber / (double) totalElapsedDays);
        double growthP_inverse = 1 - growthP;

        int growthPercentage = (int) (100 * growthP_inverse) ;

        ValueAnimator animator = ValueAnimator.ofInt(currentProg,growthPercentage);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                currentProgress.setProgress(currentValue);
            }
        });
        animator.setDuration(1000);
        animator.start();

    }
    public void updateHarvestTime(int dayNumber,int totalElapsedDays){
        //The harvest day length will be updated with the correct day number. Once it reached the final growth stage,
        //Then we will update the plant

        TextView HarvestDay = (TextView) findViewById(R.id.textView3);
        String HarvestTime = Integer.toString(dayNumber) + " Days";
        HarvestDay.setText(HarvestTime);
        animateHarvestTimeProgress(dayNumber, totalElapsedDays); //We want to animate the change in the progress bar
    }

    public void displayPlantData(int slotNumber) {
        //Here we want to update the graphical charts to show the type of plant that we have
        plantH = plantDataBase.getInstance();
        Plant requiredPlant = plantH.getPlantBySlot(slotNumber);

        float roomTemperature = requiredPlant.getRoomTemperature();
        float humidity = requiredPlant.getAirHumidity();
        int dayNumber = requiredPlant.getCurrentDayNumber();

        int remainingDays = requiredPlant.getRemainingDaysToHarvest(dayNumber);
        int totalElapsedDays = requiredPlant.getTotalGrowthPeriodDays(dayNumber);

        updateRoomTemperature(roomTemperature);

        updateHumidity(humidity);
        updateHarvestTime(remainingDays, totalElapsedDays);

        Log.i("Plant", requiredPlant.getName() + " added to PlantMoniteringSlot1");

    }
}
