package com.example.chris.plantapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Matrix;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

public class PlantMoniteringSlot1 extends AppCompatActivity {
    private plantDataBase plantH; //Plant Database
    private Plant currentPlant;

    private ImageButton backButton;
    private ImageButton HarvestButton;
    int buttonID_Called;
    int slot_ID_Called;
    private LineGraphSeries<DataPoint> series;
    /*private ImageView needle;*/
    private Needle needle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_monitering_slot1);
        //We want to grab an instance of the plant data base that will be used for this slot
        //needle = findViewById(R.id.imageView6); //The image of the needle

        needle = (Needle) findViewById(R.id.needle);

        HarvestButton = (ImageButton) findViewById(R.id.imageButton5);

        double x = 0;
        double y = 0;

        GraphView graph = (GraphView) findViewById(R.id.graph_plants);
        series = new LineGraphSeries<DataPoint>();

        //  for(int i =0;i < 500;i++){
        //    x = x + 0.1;
        //    y = Math.sin(x);
        //     series.appendData(new DataPoint(x,y),true,500);
        //  }
        graph.addSeries(series);

        plantH = plantDataBase.getInstance();

        Intent activityThatCalled = getIntent();
        buttonID_Called = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slot_ID_Called = activityThatCalled.getIntExtra("Slot Number", 0);

        currentPlant = plantH.getPlantBySlot(slot_ID_Called);

        //Set the needle to point to the current soil moisture direction
        currentPlant.setHumiditySensor(0);
        setNeedleDirection(currentPlant.getHumiditySensor());

        Log.i("Passed Value", "Button " + Integer.toString(buttonID_Called) + "passed to PlanMoniteringSlot1.java");
        displayPlantData(slot_ID_Called);

        /*needle.onDraw();*/

        backButton = (ImageButton) findViewById(R.id.imageButton3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, we also need to change the previous button pressed from the other screen
                //If this button is clicked, then we will open activity2

                openMainActivity();
            }
        });

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
    public void setNeedleDirection(float humiditySensor) {
        //This function will set the orientation of the needle based on the humidity value
        float maxHumiditySensor = GlobalConstants.MAXHUMIDITYVALUE;
        float minHumidityValue = GlobalConstants.MINHUMIDITYVALUE;
        humiditySensor = 20;
        float percentage = (humiditySensor + minHumidityValue) / maxHumiditySensor;
        //The angle of the rotation ranges from 90 to -90
        float angle = percentage * 180;
        float offset = -90 + angle;
          needle.setRotation_Needle(offset + 180);
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
