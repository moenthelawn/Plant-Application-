package com.example.chris.plantapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.media.Image;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlantMoniteringSlot1 extends AppCompatActivity {
    private plantDataBase plantH; //Plant Database
    private Plant currentPlant;

    private ImageButton HarvestButton;
    int buttonID_Called;
    int slot_ID_Called;

    /*private ImageView needle;*/
    private Needle needle;
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_monitering_slot1);
        //We want to grab an instance of the plant data base that will be used for this slot
        //needle = findViewById(R.id.imageView6); //The image of the needle

        needle = (Needle) findViewById(R.id.needle);
        HarvestButton = (ImageButton) findViewById(R.id.imageButton5);

        plantH = plantDataBase.getInstance();

        Intent activityThatCalled = getIntent();
        buttonID_Called = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slot_ID_Called = activityThatCalled.getIntExtra("Slot Number", 0);
        createSharedPreferences(slot_ID_Called); //Create the shared preferences so that the fragments can access it
        currentPlant = plantH.getPlantBySlot(slot_ID_Called);

        //Set the needle to point to the current soil moisture direction
        setNeedleDirection(currentPlant.getHumiditySensor(), currentPlant.getPreviousHumiditySensor());

        Log.i("Passed Value", "Button " + Integer.toString(buttonID_Called) + "passed to PlanMoniteringSlot1.java");
        displayPlantData(slot_ID_Called);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public void createSharedPreferences(int slotNumber){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("SlotNumber",slotNumber);
        editor.apply();
    }
    public void openMainActivity() { //We want to open that activity and navigate over to the specific class

        finish();
        //         Intent intent = new Intent(this, MainActivity.class);
        //finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //        startActivity(intent);
    }

    /* public void rotateNeedle(float angle){
         //This function will rotate the needle. As seen in the picture, the needle is bounded by -180 degrees to 180 degrees

         Matrix matrix = new Matrix();
         needle.setScaleType(ImageView.ScaleType.MATRIX);
         matrix.postRotate((float) angle);
         needle.setImageMatrix(matrix);
     }*/
    public float getHumidity_Angle(float humiditySensor) {
        //This function will set the orientation of the needle based on the humidity value
        float maxHumiditySensor = GlobalConstants.MAXHUMIDITYVALUE;
        float minHumidityValue = GlobalConstants.MINHUMIDITYVALUE;
        float percentage = (humiditySensor + minHumidityValue) / maxHumiditySensor;

        //The angle of the rotation ranges from 90 to -90
        float angle = percentage * 180;
        float offset = -90f + angle;
        return offset + 180f;
    }

    public void setNeedleDirection(float humiditySensor, float humiditySensor_Before) {

        float angle0 = getHumidity_Angle(humiditySensor);
        float angle1 = getHumidity_Angle(humiditySensor_Before);
        ValueAnimator animator = ValueAnimator.ofFloat(angle1, angle0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                needle.setRotation_Needle(currentValue);
            }
        });
        animator.setDuration(1000);
        animator.start();

        // needle.setRotation_Needle(-20);
        //With the percentage, we can now move the meedle to the appropriate position
        // needle.setRotationX(90);
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
        double growthP = ((double) totalElapsedDays / (double) dayNumber);
        double growthP_inverse = growthP;

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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return fragment_chart1.newInstance(1,"Page #1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return fragment_chart1.newInstance(2,"Page#2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return fragment_chart1.newInstance(3,"Page#3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    public void updateHarvestTime(int remainingDays, int currentDayNumber) {
        //The harvest day length will be updated with the correct day number. Once it reached the final growth stage,
        //Then we will update the plant

        TextView HarvestDay = (TextView) findViewById(R.id.textView3);
        TextView harvestName = (TextView) findViewById(R.id.textView36);
        TextView timeTillHarvest = (TextView) findViewById(R.id.textView30);

        ImageButton harvestButton = (ImageButton) findViewById(R.id.imageButton5);
        String HarvestTime = Integer.toString(remainingDays);

        if (remainingDays <= 0) {
            //Then we need to add a button that says we should harvest the plant
            harvestButton.setVisibility(harvestButton.VISIBLE); //This will control the visibility for the Harvest button
            HarvestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Once we do this, we will delete the plant from the database since it is no longer needed to harvest
                    plantH.deletePlant(currentPlant);
                }
            });
            harvestName.setVisibility(harvestName.VISIBLE);// This will control the visibility for the text that overlays the harvest button
            timeTillHarvest.setVisibility(timeTillHarvest.INVISIBLE);
            animateHarvestTimeProgress(remainingDays, remainingDays); //We want to animate the change in the progress bar

        } else {
            harvestButton.setVisibility(harvestButton.INVISIBLE); //If we have time remaining in the slot, then there is no point in displaying it
            harvestName.setVisibility(harvestName.INVISIBLE);
            timeTillHarvest.setVisibility(timeTillHarvest.VISIBLE);

            HarvestDay.setText(HarvestTime);
            animateHarvestTimeProgress(remainingDays, currentDayNumber); //We want to animate the change in the progress bar
        }
    }

    public void updatePlantGrowth(int dayNumber, float[] growth_EachDay) {

    }


    public void displayPlantData(int slotNumber) {
        //Here we want to update the graphical charts to show the type of plant that we have
        plantH = plantDataBase.getInstance();
        Plant requiredPlant = plantH.getPlantBySlot(slotNumber);

        float roomTemperature = requiredPlant.getRoomTemperature();
        float humidity = requiredPlant.getAirHumidity();
        int dayNumber = requiredPlant.getCurrentDayNumber();

        int remainingDays = requiredPlant.getRemainingDays_Harvest();
        //float growth_EachDay = requiredPlant.getGrowthStage()
        float growth_EachWeek[] = requiredPlant.getWeek_days((dayNumber / 7)); //getting the total number of days from the entire period

        //Update various paramaters for our plant statistics
        updateRoomTemperature(roomTemperature);
        updateHumidity(humidity);
        updateHarvestTime(remainingDays, dayNumber);
        updatePlantGrowth(dayNumber, growth_EachWeek);

        Log.i("Plant", requiredPlant.getName() + " added to PlantMoniteringSlot1");

    }
}
