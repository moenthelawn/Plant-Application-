package com.example.chris.plantapplication;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ImageButton button;
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button_updates;
    Thread myThread;

    private GlobalConstants constants;
    private WebServer currentDB;
    private plantDataBase plantH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        constants = new GlobalConstants();

        setContentView(R.layout.activity_main);
        // new Thread(client).start();


        plantH = plantDataBase.getInstance();
        setImageGrowthVisibility();
        contentUpdate();
        launchDismissDlg(this);

        /* myThread = new Thread(client); //Creating the new TCP thread
        myThread.start();*/
       /* Runnable_thread runnable = new Runnable_thread(this);
        new Thread(runnable).start();*/

   /*    new Thread(new Runnable() {
           @Override
           public void run() {

               TcpClient client = new TcpClient(MainActivity.this, mainHandler);
           }
       }).start();
*/
        addPlants();
    }

    private void launchDismissDlg(Activity _Activity) {
        float optimalTemperature = plantH.plantOptimalTemperatureChange();
        optimalTemperature = Math.round((optimalTemperature * 100) / 100);

        if (optimalTemperature != 0f) {
            //Display the dialogue box
            if (optimalTemperature < 0f) {
                String message = "Consider cooling your plants down by " + Float.toString(Math.abs(optimalTemperature)) + "°C";
                displayDialog(_Activity, "Temperature Warning", message);
            } else {
                String message = "Consider warming your plants up by " + Float.toString(Math.abs(optimalTemperature)) + "°C";
                displayDialog(_Activity, "Temperature Warning", message);
            }
            //If there is a plant that is not growing in the optimal conditions
            //


        }
    }

    private void displayDialog(Activity _Activity, String messageType, String message) {
        final Dialog dialog;
        dialog = new Dialog(_Activity, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notificationsymbol);
        dialog.setCanceledOnTouchOutside(true);

        TextView messageT = (TextView) dialog.findViewById(R.id.textView10);
        TextView messageError = (TextView) dialog.findViewById(R.id.textView11);
        messageT.setText(messageType);
        messageError.setText(message);

        ImageButton btnCancelId = (ImageButton) dialog.findViewById(R.id.imageButton18);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnCancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateButtonText();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        float water_tank = preferences.getFloat("Water Tank", -1);
        if (water_tank != -1) {
            //Update the level of the water tank and also the plant data base paramaters for the slot number sent
            updateWaterTankLevels(water_tank);

        }

       /* SharedPreferences prefs = getPreferences(MODE)
      //  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String message = preferences.getString("SlotNumber", null);
*/
        contentUpdate();

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

        } else if (slotNumber == (2)) { //slot 2
            //Update it to say the plants name
            ImageView harvest = findViewById(R.id.imageView16);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView12);
            plantText.setText(Text);
        } else if (slotNumber == (3)) { //slot 3
            //Update it to say the plants name
            ImageView harvest = findViewById(R.id.imageView12);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView13);
            plantText.setText(Text);
        }

    }

    public int getGrowthStage(int harvestDayLength, int currentDayNumber) {
        float fraction = currentDayNumber / harvestDayLength;
        if (fraction >= 0f && fraction < 0.25f) {
            return 1;
        } else if (fraction >= 0.25f && fraction < 0.50f) {
            return 2;
        } else if (fraction >= 0.5f && fraction < 0.75f) {
            return 3;
        } else {
            return 4;
        }
    }

    private void setGrowthStage(Plant plant) {
        int slotNumber = plant.getSlotNumber();
        int currentDayNumber = plant.getCurrentDayNumber();
        /*int totalDays = plant.geg*/
        int harvestDayLength = plant.getHarvestDayLength();
        int growthStage = getGrowthStage(harvestDayLength, currentDayNumber);

        if (slotNumber == 1) {
            ImageView harvest = findViewById(R.id.imageView13);
            setGrowthStageImage(harvest, growthStage);
        } else if (slotNumber == 2) {
            ImageView harvest = findViewById(R.id.imageView16);
            setGrowthStageImage(harvest, growthStage);
        } else {
            ImageView harvest = findViewById(R.id.imageView12);
            setGrowthStageImage(harvest, growthStage);
        }

    }

    private void setGrowthStageImage(ImageView harvest, int growthStage) {
        //Sets the current growth stage of the plant

        //We have all of our image views corresponding to each image
        //Slot 3
        if (growthStage == 0) {
            harvest.setImageResource(R.drawable.growthstage1);
            harvest.setVisibility(harvest.INVISIBLE);
        } else if (growthStage == 1) {
            //Then we update the plant with the first imag e
            harvest.setImageResource(R.drawable.growthstage1);
            harvest.setVisibility(harvest.VISIBLE);
        } else if (growthStage == 2) {
            harvest.setImageResource(R.drawable.growthstage2);
            harvest.setVisibility(harvest.VISIBLE);
        } else if (growthStage == 3) {
            harvest.setImageResource(R.drawable.growthstage3);
            harvest.setVisibility(harvest.VISIBLE);
        } else if (growthStage == 4) {
            harvest.setImageResource(R.drawable.growthstage4);
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
                    String plantType = currentPlant.getPlantType();

                    if (slotNumber != -1) {
                        ImageButton changingButton = (ImageButton) findViewById(currentButtonID);
                        changingButton.setImageResource(R.drawable.addplantmonitor);//Update it to the eye symbol so that we know it is already in plac

                        if (plantType.equals("Predetermined")) {
                            setGrowthStage(currentPlant);
                        }
                        //   threeButtons.setButtonNumberToSlot(currentButtonID, slotNumber); //Set the current slots number
                        //   threeButtons.setPlant(currentButtonID, allPlants[i], slotNumber);
                        updateButtonText(slotNumber, allPlants[i].getName(), allPlants[i]);
                    }
                } else if (currentPlant == null) {
                    //If the plant is null then we will remove that plant from the diagram
                    removePlant(i + 1);
                }
            }
        }
    }

    private void updateWaterTankLevels(float waterTank) {
        ImageView layout = (ImageView) findViewById(R.id.imageView5);
        TextView WaterTankLevel = (TextView) findViewById(R.id.textView2);

        //The MAXWATERTANKSIZE is the maximum height of the image such that it will reach the top portion of the water tank
        int MAXWATERTANKSIZE_IMAGE_HEIGHT = 157;
        int MAXWATERTANKSIZE_IMAGE_WIDTH = 172;

        float waterPercentage = waterTank / GlobalConstants.MAX_WATERTANK;

        int waterHeight_Adjustable = (int) (100 * waterPercentage);
        float requiredHeight_Pixels = (waterPercentage * MAXWATERTANKSIZE_IMAGE_HEIGHT);

        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();

        String TankLevel = Float.toString(waterHeight_Adjustable) + " %";
        WaterTankLevel.setText(TankLevel); //Also we need to set the percentage of the water tank to the value of the percentage left

        if ((waterPercentage * 100) > 1) {
            params.height = convertDipToPixels(this, requiredHeight_Pixels);
            layout.setLayoutParams(params);
        } else {
            params.height = convertDipToPixels(this, 1);
            layout.setLayoutParams(params);
        }
        //setWaterTankNotification(waterPercentage);
    }


    public void removePlant(int slotNumber) {
        ImageView harvestSlot1 = findViewById(R.id.imageView13); //Corresponding image button attached to it
        ImageView harvestSlot2 = findViewById(R.id.imageView16); //Corresponding image button attached to it
        ImageView harvestSlot3 = findViewById(R.id.imageView12); //Corresponding image button attached to it

        ImageButton addPlantSlot1 = findViewById(R.id.imageButton4);
        ImageButton addPlantSlot2 = findViewById(R.id.imageButton6);
        ImageButton addPlantSlot3 = findViewById(R.id.imageButton7);

        TextView plantTextSlot1 = findViewById(R.id.textView4);
        TextView plantTextSlot2 = findViewById(R.id.textView12);
        TextView plantTextSlot3 = findViewById(R.id.textView13);

        //For each slot, we will remove the image and put back the image of the empty icon
        if (slotNumber == 1) {
            harvestSlot1.setVisibility(harvestSlot1.INVISIBLE);
            addPlantSlot1.setImageResource(R.drawable.buttonaddplant);
            plantTextSlot1.setText("Vase 1");

        } else if (slotNumber == 2) {
            harvestSlot2.setVisibility(harvestSlot2.INVISIBLE);
            addPlantSlot2.setImageResource(R.drawable.buttonaddplant);
            plantTextSlot2.setText("Vase 2");
        } else {
            harvestSlot3.setVisibility(harvestSlot3.INVISIBLE);
            addPlantSlot3.setImageResource(R.drawable.buttonaddplant);
            plantTextSlot3.setText("Vase 3");

        }

    }

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
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
                //String message = client.message;


                /*  plantH.getPlantBySlot(1).setHumiditySensor(12);
                 */
                /* plantH.getPlantBySlot(1).setWaterRequirement_Predetermined(500);*//*

                float waterAmount = plantH.getWaterAmount_Interrupt(1);
                plantH.getPlantBySlot(1).setHumiditySensor(1);
                waterAmount = plantH.getWaterAmount_Interrupt(1);
                plantH.getPlantBySlot(1).setHumiditySensor(30);
                waterAmount = plantH.getWaterAmount_Interrupt(1);
                plantH.getPlantBySlot(1).setHumiditySensor(25);
                waterAmount = plantH.getWaterAmount_Interrupt(1);
*/

/*
                if (message != null && !message.isEmpty()) {
                    //This is a test so that we can determine how well the water amount works

                    updateIU_PacketUpdate(message);
                }*/
                //client.run();
                // //frame.execute(e1.getTest)
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                //We want to set the location of the linear actuator
            //    String plantType = plantDataBase.getInstance().getPlantBySlot(1).getPlantType();
                
                moveWateringHeightMeter(1); //Move the watering meter which corresponds to the first position in the plant vase
                openActivity2(button.getId(), 1);
            }

        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
              ///  String plantType = plantDataBase.getInstance().getPlantBySlot(2).getPlantType();

                moveWateringHeightMeter(2); //Move the watering meter which corresponds to the seoncd position in the plant vase
                openActivity2(button1.getId(), 2);
            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
             //   String plantType = plantDataBase.getInstance().getPlantBySlot(3).getPlantType();

                moveWateringHeightMeter(3);
                openActivity2(button2.getId(), 3);
            }
        });
    }

    public void moveWateringHeightMeter(int positionID) {
        ImageView myView = (ImageView) findViewById(R.id.imageView8);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) myView.getLayoutParams();

        if (positionID == 1) { //If the position is the first vase in the plant
            params.horizontalBias = 0.05f; //We want to set the horizontal bias now
            // constraintSet.setHorizontalBias(R.id.game_right, biasedValue);
            // constraintSet.applyTo((ConstraintLayout) findViewById(R.id.activity_constraint));
            //WateringHeightMeter.
        } else if (positionID == 2) {
            params.horizontalBias = 0.48f;

        } else if (positionID == 3) {
            params.horizontalBias = 0.90f;

        }
        myView.setLayoutParams(params); // request the view to use the new modified params

    }

    public void openActivity2(int buttonID, int position_slot) {
        //  boolean existornah = threeButtons.buttonExists buttonID);

        if (plantH.buttonExists(buttonID) == false) {
            //The button does not exist as a slot and therefore we can go to the next screen to the add plant section
            Intent intent = new Intent(this, ChooseMoniteringType.class);

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
   /* class Runnable_thread implements Runnable{
        Activity _activity;
        TcpClient client;
        public Runnable_thread(Activity _activity){
            this._activity = _activity;
        }

        @Override
        public void run() {
            client = new TcpClient( _activity, mainHandler);
        }
    }*/
}

