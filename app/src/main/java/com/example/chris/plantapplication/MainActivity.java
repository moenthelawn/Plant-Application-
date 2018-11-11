package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private ImageButton button;
    private ImageButton button1;
    private ImageButton button2;

    private plantDataBase plantH;
    private ButtonSlots threeButtons;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        threeButtons = new ButtonSlots();

        setContentView(R.layout.activity_main);
        plantH = plantDataBase.getInstance();
        setImageGrowthVisibility();

        addPlants();
    }
    @Override
    protected void onResume() {
        super.onResume();
        contentUpdate();
        //updateButtonText();
    }
    private void setImageGrowthVisibility(){//Set all the harvest image views to invisible
        ImageView harvestSlot1 = findViewById(R.id.imageView13); //Corresponding image button attached to it
        ImageView harvestSlot2 = findViewById(R.id.imageView16); //Corresponding image button attached to it
        ImageView harvestSlot3 = findViewById(R.id.imageView12); //Corresponding image button attached to it
        harvestSlot1.setVisibility(harvestSlot1.INVISIBLE);
        harvestSlot2.setVisibility(harvestSlot2.INVISIBLE);
        harvestSlot3.setVisibility(harvestSlot3.INVISIBLE);

    }
    private void updateButtonText(int buttonID, String Text, Plant currentPlant){
        int buttonIDSTATIC = 2131230814;

        if (buttonID == (buttonIDSTATIC)){ //Slot 1
            //  int growthStageID = R.drawable.
            ImageView harvest = findViewById(R.id.imageView13); //Corresponding image button attached to it

            //Update it to say the plants name
            TextView plantText = (TextView) findViewById(R.id.textView4);
            plantText.setText(Text);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());

        }
        else if (buttonID == (buttonIDSTATIC+1)){ //slot 2
            //Update it to say the plants name

            ImageView harvest = findViewById(R.id.imageView16);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView12);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());
            plantText.setText(Text);
        }
        else if (buttonID == (buttonIDSTATIC+2)){ //slot 3
            //Update it to say the plants name
            ImageView harvest = findViewById(R.id.imageView12);//Corresponding image button attached to it

            TextView plantText = (TextView) findViewById(R.id.textView13);
            setGrowthStageImage(harvest, currentPlant.getGrowthStage());

            plantText.setText(Text);
        }

    }
    private void setGrowthStageImage(ImageView harvest, int growthStage){
        //Sets the current growth stage of the plant
        if (growthStage == 1){
            //Then we update the plant with the first imag e
            harvest.setImageResource(R.drawable.growthstage1);
            harvest.setVisibility(harvest.VISIBLE);
        }
    }

    private void contentUpdate(){
        Plant[] allPlants = plantH.getAllPlants();
        for (int i = 0; i < allPlants.length;i++){
            int currentButtonID[] = allPlants[i].getSlotNumber();

            //Current Button ID is an array of all the buttons the plant is attached too
            for (int j = 0; j < currentButtonID.length;j++) {
                if (currentButtonID[j] != -1) {
                    ImageButton changingButton = (ImageButton) findViewById(currentButtonID[j]);
                    changingButton.setImageResource(R.drawable.planteye);//Update it to the eye symbol so that we know it is already in plac

                    Plant currentPlant = allPlants[i];


                    threeButtons.setButtonNumberToSlot(currentButtonID[j]); //Set the current slots number
                    threeButtons.setPlant(currentButtonID[j],allPlants[i]);
                    updateButtonText(currentButtonID[j],allPlants[i].getName(),allPlants[i]);

                }
            }
        }
    }
    private void addPlants() {

        button = (ImageButton) findViewById(R.id.imageButton4);
        button1 = (ImageButton) findViewById(R.id.imageButton6);
        button2 = (ImageButton) findViewById(R.id.imageButton7);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2

                //We want to set the location of the linear actuator
                moveWateringHeightMeter(1); //Move the watering meter which corresponds to the first position in the plant vase
                openActivity2(button.getId());
            }

        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                moveWateringHeightMeter(2); //Move the watering meter which corresponds to the seoncd position in the plant vase
                openActivity2(button1.getId());

            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                moveWateringHeightMeter(3);
                openActivity2(button2.getId());
            }

        });
    }
    public void moveWateringHeightMeter(int positionID){
        ImageView myView= (ImageView)findViewById(R.id.imageView7);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) myView.getLayoutParams();

        if (positionID == 1){ //If the position is the first vase in the plant
            params.horizontalBias = 0.02f; //We want to set the horizontal bias now
           // constraintSet.setHorizontalBias(R.id.game_right, biasedValue);
           // constraintSet.applyTo((ConstraintLayout) findViewById(R.id.activity_constraint));
            //WateringHeightMeter.
        }
        else if (positionID == 2){
            params.horizontalBias = 0.49f;

        }
        else if (positionID == 3){
            params.horizontalBias = 0.98f;

        }
        myView.setLayoutParams(params); // request the view to use the new modified params

    }

    public void openActivity2(int buttonID) {
        //  boolean existornah = threeButtons.buttonExists(buttonID);

        if (threeButtons.buttonExists(buttonID) == false) {
            //The button does not exist as a slot and therefore we can go to the next screen to the add plant section
            Intent intent = new Intent(this, Activity2.class);
            intent.putExtra("Button ID", buttonID);
            startActivity(intent);
        }
        else{
            //Then we want to go the activity to display the plant slots ID value
            Intent intent = new Intent(this, PlantMoniteringSlot1.class);
            intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
            startActivity(intent);
        }
    }
}
