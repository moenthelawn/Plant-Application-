package com.example.chris.plantapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class soilType extends AppCompatActivity {

    //Add all of our buttons to monitor
    private ImageButton sandysoil;
    private ImageButton loamysoil;
    private ImageButton claysoil;


    private CheckBox sandysoil_check;
    private CheckBox loamysoil_check;
    private CheckBox claysoil_check;

    private int slotNumber;
    private int buttonID;

    private plantDataBase plantH;
    private Plant currentPlant;
    private TextView soilText;
    private Button nextPage;
    private TextView warningLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plantH = plantDataBase.getInstance();


        Intent activityThatCalled = getIntent();

        buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
        slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

        setContentView(R.layout.activity_soil_type);

        soilText = (TextView) findViewById(R.id.textView7);

        //The checkboxes
        sandysoil_check = (CheckBox) findViewById(R.id.checkBox3);
        claysoil_check = (CheckBox) findViewById(R.id.checkBox2);
        loamysoil_check = (CheckBox) findViewById(R.id.checkBox4);
        nextPage = (Button) findViewById(R.id.button);

        //The image buttons
        sandysoil = (ImageButton) findViewById(R.id.imageButton);
        loamysoil = (ImageButton) findViewById(R.id.imageButton11);
        claysoil = (ImageButton) findViewById(R.id.imageButton9);

        warningLabel = (TextView) findViewById(R.id.textView8);
        warningLabel.setVisibility(warningLabel.INVISIBLE);

        sandysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   plantH.setSoilType(slotNumber, "Sandy");

                if (sandysoil_check.isChecked() == true){
                    sandysoil_check.setChecked(false);
                    sandysoil.setImageResource(R.drawable.sandsoilselect);
                    sandysoil_check.setButtonDrawable(android.R.drawable.checkbox_off_background);
                    setEditText();

                }
                //Set the text of the currently selected options
                else if (claysoil_check.isChecked() == false) {
                    //Set the check
                    sandysoil_check.setChecked(true);
                    sandysoil.setImageResource(R.drawable.sandsoilselect_selected);
                    sandysoil_check.setButtonDrawable(android.R.drawable.checkbox_on_background);
                    setEditText();
                }
                //   callIntent(v.getContext(), PlantDepth.class);
            }
        });
        loamysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //plantH.setSoilType(slotNumber, "Loamy");
                if (loamysoil_check.isChecked() == true){
                    loamysoil_check.setChecked(false);
                    loamysoil.setImageResource(R.drawable.loamsoilselect);
                    loamysoil_check.setButtonDrawable(android.R.drawable.checkbox_off_background);
                    setEditText();
                }
                else if ((claysoil_check.isChecked() == false && sandysoil_check.isChecked() == true)
                        || (claysoil_check.isChecked() == true && sandysoil_check.isChecked() == false)
                        || (claysoil_check.isChecked() == false) && (sandysoil_check.isChecked()) == false){
                    loamysoil_check.setChecked(true);
                    loamysoil.setImageResource(R.drawable.loamsoilselect_selected);
                    loamysoil_check.setButtonDrawable(android.R.drawable.checkbox_on_background);
                    setEditText();
                }
               // callIntent(v.getContext(), PlantDepth.class);
            }
        });
        claysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (claysoil_check.isChecked() == true){
                    claysoil_check.setChecked(false);
                    claysoil.setImageResource(R.drawable.claysoilselect);
                    setEditText();
                    claysoil_check.setButtonDrawable(android.R.drawable.checkbox_off_background);
                }
                else if (sandysoil_check.isChecked() == false){
                    claysoil_check.setChecked(true);
                    claysoil.setImageResource(R.drawable.claysoilselected);
                    setEditText();
                    claysoil_check.setButtonDrawable(android.R.drawable.checkbox_on_background);

                }
                //plantH.setSoilType(slotNumber, "Clay");
               // callIntent(v.getContext(), PlantDepth.class);
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (claysoil_check.isChecked() == false &&
                        sandysoil_check.isChecked() == false &&
                        loamysoil_check.isChecked() == false){
                    warningLabel.setVisibility(warningLabel.VISIBLE);
                }
                else{
                    plantH.setSoilType(slotNumber,soilText.getText().toString());
                    callIntent(v.getContext(),PlantDepth.class);
                }
            }
        });


    }

    public void setEditText() {
        //this function will set the edit text based on the currently selected items
        if (loamysoil_check.isChecked() == true && claysoil_check.isChecked() == false
                && sandysoil_check.isChecked() == false) {//If only the loam soil checkbox is checked
            //Set the edit text to just read
            soilText.setText("Loam");
        }
        else if (loamysoil_check.isChecked() == false && claysoil_check.isChecked() == true
                && sandysoil_check.isChecked() == false){
            //If only clay soil is checked
            soilText.setText("Clay");
        }
        else if (loamysoil_check.isChecked() == false && claysoil_check.isChecked() == false
                && sandysoil_check.isChecked() == true){
            //If only clay soil is checked
            soilText.setText("Sandy Soil");
        }
        else if (loamysoil_check.isChecked() == true && claysoil_check.isChecked() == false
                && sandysoil_check.isChecked() == true){
            //If only clay soil is checked
            soilText.setText("Loamy Sand");
        }
        else if (loamysoil_check.isChecked() == true && claysoil_check.isChecked() == true
                && sandysoil_check.isChecked() == false){
            //If only clay soil is checked
            soilText.setText("Clay Loam");
        }
        else if (loamysoil_check.isChecked() == false && claysoil_check.isChecked() == false
                && sandysoil_check.isChecked() == false){
            soilText.setText("");
        }
    }

    public void callIntent(Context c, Class destination) {

        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        intent.putExtra("Button ID", buttonID); //Add the button ID as extra such that we can monitor the plant's graph
        intent.putExtra("Slot Number", slotNumber);
        startActivity(intent);
    }
}
