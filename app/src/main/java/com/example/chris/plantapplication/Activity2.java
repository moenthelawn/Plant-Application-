package com.example.chris.plantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.app.Activity;

public class Activity2 extends AppCompatActivity {
    private ImageButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        button = (ImageButton) findViewById(R.id.imageButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this button is clicked, we also need to change the previous button pressed from the other screen
                //If this button is clicked, then we will open activity2

                openMainActivity();
            }
        });

    }

    public void openMainActivity() { //We want to open that activity and navigate over to the specific class
        int buttonID= getIntent().getIntExtra("Button ID",0); //get the button session ID so we can modify its .xml paramaters

        ImageButton btn_tmp;
        btn_tmp = (ImageButton)findViewById(buttonID);
      //  btn_tmep
       // btn_tmp.setImageResource(R.drawable.eyeme); //set that button's ID to be the eye icon

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
