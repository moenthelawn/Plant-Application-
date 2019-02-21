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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlantMoniteringSlot1 extends AppCompatActivity {
    private plantDataBase plantH; //Plant Database
    public Plant currentPlant;

    private TextView growth_day;
    private TextView plantName;
    private TextView addWater;
    private ImageView waterEditSlot;
    private EditText waterEditValues;
    private TextView waterAmountMessage;
    private Button confirmButton;

    private ImageButton HarvestButton;
    private ImageButton back;
    int buttonID_Called;
    int slot_ID_Called;

    /*private ImageView needle;*/
    private Needle needle;
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_monitering_slot1);

        addWater = (TextView) findViewById(R.id.textView40);
        waterEditSlot = (ImageView) findViewById(R.id.imageView21);
        waterEditValues = (EditText) findViewById(R.id.editText2);
        confirmButton = (Button) findViewById(R.id.button2);
        waterAmountMessage = (TextView) findViewById(R.id.textView31);

        confirmButton.setVisibility(confirmButton.INVISIBLE);
        waterAmountMessage.setVisibility(waterAmountMessage.INVISIBLE);
        waterEditSlot.setVisibility(waterEditSlot.INVISIBLE);
        addWater.setVisibility(addWater.INVISIBLE);
        waterEditValues.setVisibility(waterEditValues.INVISIBLE);

        //We want to grab an instance of the plant data base that will be used for this slot
        //needle = findViewById(R.id.imageView6); //The image of the needle

        needle = (Needle) findViewById(R.id.needle);
        HarvestButton = (ImageButton) findViewById(R.id.imageButton5);
        growth_day = (TextView) findViewById(R.id.textView25);
        plantName = (TextView) findViewById(R.id.textView16);
        TextView harvestTime = (TextView) findViewById(R.id.textView36);
        harvestTime.setVisibility(harvestTime.INVISIBLE);
        plantH = plantDataBase.getInstance();
        back = (ImageButton) findViewById(R.id.imageButton3);
        Intent activityThatCalled = getIntent();
        buttonID_Called = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slot_ID_Called = activityThatCalled.getIntExtra("Slot Number", 0);

        createSharedPreferences(slot_ID_Called); //Create the shared preferences so that the fragments can access it
        currentPlant = plantH.getPlantBySlot(slot_ID_Called);
        plantName.setText(currentPlant.getName());

        setNotifications();
        //Set the needle to point to the current soil moisture direction
        setNeedleDirection(currentPlant.getHumiditySensor(), currentPlant.getPreviousHumiditySensor());

        Log.i("Passed Value", "Button " + Integer.toString(buttonID_Called) + "passed to PlanMoniteringSlot1.java");
        displayPlantData(slot_ID_Called);
        displayManual_Predetermined(currentPlant);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

        waterEditValues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Edit the water text values
                int s_length = s.length();
                if (s_length > 0) {
                    String message = s.toString() + "mL";
                    waterAmountMessage.setVisibility(waterAmountMessage.VISIBLE);
                    confirmButton.setVisibility(confirmButton.VISIBLE);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we update the text of the on click listener with the correct value
                String waterAmountValue = waterEditValues.getText().toString();
                currentPlant.setWater_period(Float.parseFloat(waterAmountValue));
                String message = "Your plant will be watered " + waterAmountValue + ".0mm each cycle";
                waterAmountMessage.setText(message);
                String plantType = currentPlant.getPlantType();
                currentPlant.updateServerDataBase(2, currentPlant.getSlotNumber());

                confirmButton.setVisibility(confirmButton.INVISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        vpPager.setAdapter(adapterViewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String plantType = currentPlant.getPlantType();
        if (plantType.equals("Manual")) {
            currentPlant.updateServerDataBase(2, currentPlant.getSlotNumber());
        }
        // Do extra stuff here
    }

    private void displayManual_Predetermined(Plant currentPlant) {
        if (currentPlant.getPlantType().equals("Manual")) {

            waterEditSlot.setVisibility(waterEditSlot.VISIBLE);
            addWater.setVisibility(addWater.VISIBLE);
            waterEditValues.setVisibility(waterEditValues.VISIBLE);

            //This function will display the plant data responsible for either
            TextView harvestTime = (TextView) findViewById(R.id.textView36);
            TextView message = (TextView) findViewById(R.id.textView30);
            waterAmountMessage.setVisibility(waterAmountMessage.VISIBLE);

            //Then we set hte watering amount
            float water_period = currentPlant.getWater_period();
            String waterAmount_current = Float.toString(water_period);
            String waterAmount_message = "Your plant will be watered " + waterAmount_current + "mm each cycle";

            waterAmountMessage.setText(waterAmount_message);
            message.setText("Edit your plant watering values");
            ImageButton harvestButton = (ImageButton) findViewById(R.id.imageButton5);
            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

            TextView number = (TextView) findViewById(R.id.textView3);
            number.setVisibility(number.INVISIBLE);


            harvestButton.setVisibility(harvestButton.INVISIBLE);
            progress.setVisibility(progress.INVISIBLE);
            harvestTime.setVisibility(harvestTime.INVISIBLE);
        }
    }

  /*  private void displayString_water_message(float waterRequirement_period, int growthInterval) {
        String waterRequirements = Float.toString(waterRequirement_period);

        TextView wateringRequirements = (TextView) findViewById(R.id.textView30);
        String wateringPeriod = "";
        if (growthInterval <= 24) {
            String floatvalue = Float.toString(growthInterval);
            wateringPeriod = floatvalue + " hours"; //Just hours
        } else {
            //Convert the hours to days
            wateringPeriod = convertHours_Days(growthInterval); //
            // We have to convert it to something else like
        }

        String wateringMessage = "Your plant will be watered " + waterRequirements + "mm every " + wateringPeriod;
        wateringRequirements.setText(wateringMessage);
    }*/

    private String convertHours_Days(int growthInterval) {
        //This function converts the growth intervals from hours to days.
        int currentCount = 0;
        int multFactor = 0;
        double days = (growthInterval / 24.0f);
        double days_rounded = Math.floor(days);
        double diff = (float) (days - days_rounded);


        double hours = diff * 24.0f;
        float hours_rounded = (float) Math.round(hours);
        if (Math.round(hours) == 0) {

            String message = Float.toString((int) days_rounded) + " days";
            return message;
        }

        if (days_rounded > 1) {

            String message = Float.toString((int) days_rounded) + " days and " + Float.toString(hours_rounded) + " hours";
            return message;
        } else {

            String message = Float.toString((int) days_rounded) + " day and " + Float.toString(hours_rounded) + " hours";
            return message;
        }

    }

    public void setNotifications() {
        //This will set the corresponding notifications to the user
        setTemperatureNotification();
    }

    public void setTemperatureNotification() {

    }

    public void createSharedPreferences(int slotNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("SlotNumber", slotNumber);
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

    public void animateHarvestTimeProgress(int remainingDays, int totalHarvestPeriod) {
        final ProgressBar currentProgress = findViewById(R.id.progressBar);
        int currentProg = currentProgress.getProgress();
        double growthP = ((double) remainingDays / (double) totalHarvestPeriod);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

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

            if (currentPlant.getPlantType().equals("Predetermined")) {
                switch (position) {
                    case 0: // Fragment # 0 - This will show FirstFragment
                        return fragment_chart1.newInstance(1, "Page #1");
                    case 1: // Fragment # 0 - This will show FirstFragsment different title
                        return GraphHumiditySensor.newInstance(2, "Page #2");
                    //case 2: // Fragment # 1 - This will show SecondFragment
                     //   return Fragment_chart_water.newInstance(3, "Page #3");
                    default:
                        return null;
                }
            } else {
                switch (position) {
                    case 0: // Fragment # 0 - This will show FirstFragment
                        return fragment_chart1.newInstance(1, "Page #1");
                    case 1: // Fragment # 0 - This will show FirstFragsment different title
                        return GraphHumiditySensor.newInstance(2, "Page #2");
                //    case 2: // Fragment # 0 - This will show FirstFragsment different title
                   //     return null;
                    default:
                        return null;
                }
            }
        }


            // Returns the page title for the top indicator
            @Override
            public CharSequence getPageTitle ( int position){
                return "Page " + position;
            }
        }

        public void updateHarvestTime(int remainingDays, int totalHarvestPeriod) {
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

                harvestName.setVisibility(harvestName.VISIBLE);// This will control the visibility for the text that overlays the harvest button
                //timeTillHarvest.setVisibility(timeTillHarvest.INVISIBLE);
                animateHarvestTimeProgress(remainingDays, remainingDays); //We want to animate the change in the progress bar

            } else {
                harvestButton.setVisibility(harvestButton.INVISIBLE); //If we have time remaining in the slot, then there is no point in displaying it
                harvestName.setVisibility(harvestName.INVISIBLE);
                //  timeTillHarvest.setVisibility(timeTillHarvest.VISIBLE);

                HarvestDay.setText(HarvestTime);
                animateHarvestTimeProgress(remainingDays, totalHarvestPeriod); //We want to animate the change in the progress bar
            }
        }

        public void updatePlantGrowth(float growth_currentDay) {
            String total = growth_currentDay + " cm";
            growth_day.setText(total);
        }

        public void displayPlantData(int slotNumber) {
            //Here we want to update the graphical charts to show the type of plant that we have
            plantH = plantDataBase.getInstance();
            Plant requiredPlant = plantH.getPlantBySlot(slotNumber);
            float roomTemperature = requiredPlant.getRoomTemperature();
            float humidity = requiredPlant.getAirHumidity();
            int dayNumber = requiredPlant.getCurrentDayNumber();
            int totalHarvestPeriod = requiredPlant.getHarvestDayLength();
            int remainingDays = requiredPlant.getRemainingDays_Harvest();
            //float growth_EachDay = requiredPlant.getGrowthStage()
            // float growth_EachWeek[] = requiredPlant.getWeek_days((dayNumber / 7)); //getting the total number of days from the entire period
            float growth_currentDay = Math.round((requiredPlant.getCurrentDayGrowth() * 100) / 100);
            Date lastWaterDate = currentPlant.getLastWaterDate();

            //Update various paramaters for our plant statistics
            updateRoomTemperature(roomTemperature);
            updateLastWateringDate(lastWaterDate);
            if (requiredPlant.getPlantType().equals("Predetermined")) {
                updateHarvestTime(remainingDays, totalHarvestPeriod);
            }
            updatePlantGrowth(growth_currentDay);

            Log.i("Plant", requiredPlant.getName() + " added to PlantMoniteringSlot1");

        }

    private void updateLastWateringDate(Date lastWaterDate) {
        TextView lastWaterTime = (TextView) findViewById(R.id.textView35); //to be able to set the room temperature to the correct value


        if (lastWaterDate == null || lastWaterDate.toString() == ""){
            //Then display something along the lines of
            String message = "Plant watering has not begun";
            lastWaterTime.setText(message);
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM, h:mma");
            String currentDateandTime = sdf.format(lastWaterDate);

            lastWaterTime.setText(currentDateandTime);
        }

    }
}
