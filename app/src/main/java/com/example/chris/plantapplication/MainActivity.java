package com.example.chris.plantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageButton;
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
        addPlants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentUpdate();
        //updateButtonText();
    }
private void updateButtonText(int buttonID, String Text) {
    if (buttonID == 2131230805){
        //Update it to say the plants name
        TextView plantText = (TextView) findViewById(R.id.textView13);
        plantText.setText(Text);
    }
    else if (buttonID == 2131230807){
        //Update it to say the plants name
        TextView plantText = (TextView) findViewById(R.id.textView12);
        plantText.setText(Text);
    }
    else if (buttonID == 2131230806){
        //Update it to say the plants name
        TextView plantText = (TextView) findViewById(R.id.textView4);
        plantText.setText(Text);
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
                    changingButton.setImageResource(R.drawable.eyeme);//Update it to the eye symbol so that we know it is already in plac

                    threeButtons.setButtonNumberToSlot(currentButtonID[j]); //Set the current slots number
                    threeButtons.setPlant(currentButtonID[j],allPlants[i]);
                    updateButtonText(currentButtonID[j],allPlants[i].getName());

                }
            }
        }
    }
    private void addPlants() {

        button = (ImageButton) findViewById(R.id.imageButton13);
        button1 = (ImageButton) findViewById(R.id.imageButton10);
        button2 = (ImageButton) findViewById(R.id.imageButton9);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                openActivity2(button.getId());
            }

        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                openActivity2(button1.getId());

            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, then we will open activity2
                openActivity2(button2.getId());
            }

        });
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
