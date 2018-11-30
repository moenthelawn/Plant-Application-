package com.example.chris.plantapplication;

import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class addCustomPlant extends AppCompatActivity {
    //We will add the sensor data monitering slots
    private Sensor sensorManager;
    private Sensor temperature, humidity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_plant);
    }
}
