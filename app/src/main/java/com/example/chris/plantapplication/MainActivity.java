package com.example.chris.plantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton button;
    private ImageButton button1;
    private ImageButton button2;

    private plantDataBase allPlants;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allPlants = plantDataBase.getInstance();



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


        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra("Button ID", buttonID);

        startActivity(intent);
    }
}
