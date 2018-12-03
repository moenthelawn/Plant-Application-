package com.example.chris.plantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class soilType extends AppCompatActivity {

    //Add all of our buttons to monitor
    private ImageButton siltysoil;
    private ImageButton chalkkysoil;
    private ImageButton sandysoil;
    private ImageButton loamysoil;
    private ImageButton claysoil;
    private ImageButton peatysoil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_type);
        //initiate all of the buttons
        siltysoil = (ImageButton) findViewById(R.id.imageButton16);
        chalkkysoil = (ImageButton) findViewById(R.id.imageButton17);
        sandysoil = (ImageButton) findViewById(R.id.imageButton12);
        loamysoil = (ImageButton) findViewById(R.id.imageButton);
        claysoil = (ImageButton) findViewById(R.id.imageButton15);
        peatysoil = (ImageButton) findViewById(R.id.imageButton14);

        siltysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        chalkkysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        sandysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        loamysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        claysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        peatysoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
