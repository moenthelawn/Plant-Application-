package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private ImageButton button;
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button_updates;
    Thread myThread;
    TcpClient client;

    private GlobalConstants constants;
    private plantDataBase plantH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        constants = new GlobalConstants();


        setContentView(R.layout.activity_main);
        plantH = plantDataBase.getInstance();
        setImageGrowthVisibility();

        client = new TcpClient(this.getApplicationContext());
        myThread = new Thread(client); //Creating the new TCP thread
        myThread.start();

        addPlants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentUpdate();
        //updateButtonText();
    }

    private void setImageGrowthVisibility() {//Set all the harvest image views to invisible
        ImageView harvestSlot1 = findViewById(R.id.imageView13); //Corresponding image button attached to it
        ImageView harvestSlot2 = findViewById(R.id.imageView16); //Corresponding image button attached to it
        ImageView harvestSlot3 = findViewById(R.id.imageView12); //Corresponding image button attached to it

        harvestSlot1.setVisibility(harvestSlot1.INVISIBLE);
        harvestSlot2.setVisibility(harvestSlot2.INVISIBLE);
        harvestSlot3.setVisibility(harvestSlot3.INVISIBLE);
    }

    private void updateButtonText(int slotNumber, String Text, Plant currentPlant) {
        if (slotNumber == (1)) { //Slot 1
            //  int growthStageID = R.drawable.
            ImageView harvest = findViewById(R.id.imageView13); //Corresponding image button attached to it

            //Update it to say the plants name
            TextView plantText = (TextView) findViewById(R.id.textView4);
            plantText.setText(Text);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());

        } else if (slotNumber == (2)) { //slot 2
            //Update it to say the plants name

            ImageView harvest = findViewById(R.id.imageView16);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView12);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());
            plantText.setText(Text);
        } else if (slotNumber == (3)) { //slot 3
            //Update it to say the plants name
            ImageView harvest = findViewById(R.id.imageView12);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView13);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());

            plantText.setText(Text);
        }

    }

    private void setGrowthStageImage(ImageView harvest, int growthStage) {
        //Sets the current growth stage of the plant
        if (growthStage == 1) {
            //Then we update the plant with the first imag e
            harvest.setImageResource(R.drawable.growthstage1);
            harvest.setVisibility(harvest.VISIBLE);
        }
    }

    private void contentUpdate() {
        Plant[] allPlants = plantH.getAllPlants();
        if (plantH.isAdded() == true) {

            for (int i = 0; i < allPlants.length; i++) {

                //Current Button ID is an array of all the buttons the plant is attached too
                Plant currentPlant = allPlants[i];
                if (currentPlant != null) {
                    int currentButtonID = currentPlant.getButtonNumber();
                    int slotNumber = currentPlant.getSlotNumber();
                    if (currentButtonID != -1) {
                        ImageButton changingButton = (ImageButton) findViewById(currentButtonID);
                        changingButton.setImageResource(R.drawable.planteye);//Update it to the eye symbol so that we know it is already in plac

                        //   threeButtons.setButtonNumberToSlot(currentButtonID, slotNumber); //Set the current slots number
                        //   threeButtons.setPlant(currentButtonID, allPlants[i], slotNumber);
                        updateButtonText(slotNumber, allPlants[i].getName(), allPlants[i]);
                    }
                }
            }
        }
    }

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void updateWaterTankLevels(float waterTank) {
        //grab the image view portion of it
        ImageView layout = findViewById(R.id.imageView5);
        TextView WaterTankLevel = findViewById(R.id.textView2);

        //The MAXWATERTANKSIZE is the maximum height of the image such that it will reach the top portion of the water tank
        int MAXWATERTANKSIZE_IMAGE_HEIGHT = 157;
        int MAXWATERTANKSIZE_IMAGE_WIDTH = 172;
        float waterPercentage = waterTank / constants.MAX_WATERTANK;

        int waterHeight_Adjustable = (int) (100 * waterPercentage);
        float requiredHeight_Pixels = (waterPercentage * MAXWATERTANKSIZE_IMAGE_HEIGHT);

        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();

        String TankLevel = Float.toString(waterHeight_Adjustable) + " %";
        WaterTankLevel.setText(TankLevel); //Also we need to set the percentage of the water tank to the value of the percentage left

        params.height = convertDipToPixels(this.getApplicationContext(), requiredHeight_Pixels);
        layout.setLayoutParams(params);

    }

    private void updatePlantDataBase(int plantSlot, float airTemperature, float airHumidity, int numberDay, float height) {
        //int buttonNumber = threeButtons.getButtonNumber(plantSlot);
        plantH = plantDataBase.getInstance();
        if (plantH.isSlotExists(plantSlot) == true) {
            Plant CurrentPlant = plantH.getPlantBySlot(plantSlot);

            //Now we want to submit all the data to the corresponding plant
            CurrentPlant.setRoomTemperature(airTemperature);
            CurrentPlant.setAirHumidity(airHumidity);
            CurrentPlant.setCurrentDayNumber(numberDay);

            //Send the correct watering amount to the server database
           // double waterAmount = CurrentPlant.getDailyWaterAmount_millimetres(numberDay);
        }
    }

    private void updateIU_PacketUpdate(String message) {
        String[] values = message.split(";"); //Split the message into each component
        float airHumidity = Float.parseFloat(values[0]); //Air Humidity as a percentage
        float height = Float.parseFloat(values[1]);
        float Angle = Float.parseFloat(values[2]);
        float airTemperature = Float.parseFloat(values[3]);
        float waterTank = Float.parseFloat(values[4]);
        int numberDay = Integer.parseInt(values[5]);
        int plantSlot = Integer.parseInt(values[6]);

        //Now we need to update the entire UI of the system
        updateWaterTankLevels(waterTank); //This will update the level of the water tank based on the image provided
        updatePlantDataBase(plantSlot, airTemperature, airHumidity, numberDay, height);
    }

    private void addPlants() {

        button = (ImageButton) findViewById(R.id.imageButton4);
        button1 = (ImageButton) findViewById(R.id.imageButton6);
        button2 = (ImageButton) findViewById(R.id.imageButton7);
        button_updates = (ImageButton) findViewById(R.id.imageButton2);

        button_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                String message = client.message;
                if (message != null && !message.isEmpty()) {
                    updateIU_PacketUpdate(message);
                }
                //client.run();
                // //frame.execute(e1.getTest)
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                //We want to set the location of the linear actuator
                moveWateringHeightMeter(1); //Move the watering meter which corresponds to the first position in the plant vase
                openActivity2(button.getId(), 1);
            }

        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                moveWateringHeightMeter(2); //Move the watering meter which corresponds to the seoncd position in the plant vase
                openActivity2(button1.getId(), 2);

            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                moveWateringHeightMeter(3);
                openActivity2(button2.getId(), 3);
            }

        });
    }

    public void moveWateringHeightMeter(int positionID) {
        ImageView myView = (ImageView) findViewById(R.id.imageView7);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) myView.getLayoutParams();

        if (positionID == 1) { //If the position is the first vase in the plant
            params.horizontalBias = 0.02f; //We want to set the horizontal bias now
            // constraintSet.setHorizontalBias(R.id.game_right, biasedValue);
            // constraintSet.applyTo((ConstraintLayout) findViewById(R.id.activity_constraint));
            //WateringHeightMeter.
        } else if (positionID == 2) {
            params.horizontalBias = 0.49f;

        } else if (positionID == 3) {
            params.horizontalBias = 0.98f;

        }
        myView.setLayoutParams(params); // request the view to use the new modified params

    }

    public void openActivity2(int buttonID, int position_slot) {
        //  boolean existornah = threeButtons.buttonExists buttonID);

        if (plantH.buttonExists(buttonID) == false) {
            //The button does not exist as a slot and therefore we can go to the next screen to the add plant section
            Intent intent = new Intent(this, Activity2.class);

            intent.putExtra("Button ID", buttonID);
            intent.putExtra("Slot Number", position_slot);

            startActivity(intent);
        } else {
            //Then we want to go the activity to display the plant slots ID value
            Intent intent = new Intent(this, PlantMoniteringSlot1.class);

            intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
            intent.putExtra("Slot Number", position_slot);

            startActivity(intent);
        }
    }
}

