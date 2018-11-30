package com.example.chris.plantapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    public void animateHarvestTimeProgress(int dayNumber, int totalElapsedDays) {
        final ProgressBar currentProgress = findViewById(R.id.progressBar);
        int currentProg = currentProgress.getProgress();
        double growthP = ((double) dayNumber / (double) totalElapsedDays);
        double growthP_inverse = 1 - growthP;

        int growthPercentage = (int) (100 * growthP_inverse);

        ValueAnimator animator = ValueAnimator.ofInt(currentProg, growthPercentage);
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

    public void updateHarvestTime(int remainingDays, int totalElapsedDays) {
        //The harvest day length will be updated with the correct day number. Once it reached the final growth stage,
        //Then we will update the plant

        TextView HarvestDay = (TextView) findViewById(R.id.textView3);
        TextView harvestName = (TextView) findViewById(R.id.textView36);
        TextView timeTillHarvest = (TextView) findViewById(R.id.textView30);

        ImageButton harvestButton = (ImageButton) findViewById(R.id.imageButton5);
        String HarvestTime = Integer.toString(remainingDays);

        if (remainingDays == 0) {
            //Then we need to add a button that says we should harvest the plant
            harvestButton.setVisibility(harvestButton.VISIBLE); //This will control the visibility for the Harvest button
            harvestName.setVisibility(harvestName.VISIBLE);// This will control the visibility for the text that overlays the harvest button
            timeTillHarvest.setVisibility(timeTillHarvest.INVISIBLE);

        } else {
            harvestButton.setVisibility(harvestButton.INVISIBLE); //If we have time remaining in the slot, then there is no point in displaying it
            harvestName.setVisibility(harvestName.INVISIBLE);
            timeTillHarvest.setVisibility(timeTillHarvest.VISIBLE);

            HarvestDay.setText(HarvestTime);
            animateHarvestTimeProgress(remainingDays, totalElapsedDays); //We want to animate the change in the progress bar
        }
    }

    public void updatePlantGrowth(int dayNumber, float[] growth_EachDay) {
        //This function will take in the current plant's growth height statistics for each day, and adjust the graph
        //based on each height value

        //First Bar on Graph
        ImageView firstBar = (ImageView) findViewById(R.id.imageView28);
        TextView firstBar_Text = (TextView) findViewById(R.id.textView18);

        //Second Bar on Graph
        ImageView secondBar = (ImageView) findViewById(R.id.imageView27);
        TextView secondBar_Text = (TextView) findViewById(R.id.textView17);

        //Third Bar on Graph
        ImageView thirdBar = (ImageView) findViewById(R.id.imageView30);
        TextView thirdBar_Text = (TextView) findViewById(R.id.textView19);

        //Fourth Bar on Graph
        ImageView fourthBar = (ImageView) findViewById(R.id.imageView31);
        TextView fourthBar_Text = (TextView) findViewById(R.id.textView20);

        //Fith Bar on Graph
        ImageView fithBar = (ImageView) findViewById(R.id.imageView32);
        TextView fithBar_Text = (TextView) findViewById(R.id.textView21);

        //6th Bar on Graph
        ImageView sixthBar = (ImageView) findViewById(R.id.imageView29);
        TextView sixthBar_Text = (TextView) findViewById(R.id.textView22);

        //7th Bar on Graph
        ImageView seventhBar = (ImageView) findViewById(R.id.imageView33);
        TextView seventhBar_Text = (TextView) findViewById(R.id.textView23);

        ImageView[] barGraphs = {firstBar,secondBar,thirdBar,fourthBar,fithBar,sixthBar,seventhBar};
        TextView[] textGraphs = {firstBar_Text,secondBar_Text,thirdBar_Text,fourthBar_Text,fithBar_Text,sixthBar_Text,seventhBar_Text};

        //Before we start updating eve
        int MAXBARHEIGHT = 152; //dP
        int length_BarGraphs = barGraphs.length;
        int length_Weeks = (dayNumber / 7);
        for (int i = 0; i < length_BarGraphs; i++){
            if (i <= length_Weeks){
                float currentH = growth_EachDay[i];
                String currentHeight = Float.toString(currentH);
                textGraphs[i].setText(currentHeight + " cm"); // we want set the text of the bar graphs

                ViewGroup.LayoutParams params = barGraphs[i].getLayoutParams();

                //The max height each one can go is about 152 dP


                int currentHeight_Bar = params.height;


                // barGraphs[i].getHeight=

              /*
                ValueAnimator animator = ValueAnimator.ofInt(currentProg, growthPercentage);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int currentValue = (int) animation.getAnimatedValue();
                        currentProgress.setProgress(currentValue);
                    }
                });
                animator.setDuration(1000);
                animator.start();
                */
            }
            else{
                //Otherwise we will set all of the other paramaters to 0
                float zeroedHeight = 0;
                String currentHeight = Float.toString(zeroedHeight);
                textGraphs[i].setText(currentHeight + " cm");
            }
        }
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
        //float growth_EachDay = requiredPlant.getGrowthStage()
        float growth_EachWeek[] = requiredPlant.getWeek_days((dayNumber / 7)); //getting the total number of days from the entire period

        //Update various paramaters for our plant statistics
        updateRoomTemperature(roomTemperature);
        updateHumidity(humidity);
        updateHarvestTime(remainingDays, totalElapsedDays);
        updatePlantGrowth(dayNumber, growth_EachWeek);

        Log.i("Plant", requiredPlant.getName() + " added to PlantMoniteringSlot1");

    }
}
