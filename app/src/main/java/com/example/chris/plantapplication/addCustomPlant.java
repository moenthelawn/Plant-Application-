package com.example.chris.plantapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;

public class addCustomPlant extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double longitude;
    private double latitude;
    private CheckBox indoorPlant;
    private Spinner dropDownMenus;

    private ConstraintLayout layout;
    private ConstraintSet layout_constraint;

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_custom_plant2);


        dropDownMenus = new Spinner(this);
        layout = (ConstraintLayout) findViewById(R.id.layout1); //Current layout of constraint layout

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        latitude = lastKnownLocation.getLatitude();
        longitude = lastKnownLocation.getLongitude();

        indoorPlant = (CheckBox) findViewById(R.id.checkBox2);
        indoorPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((CheckBox) v).isChecked();
                if (checked == true) {
                    GlobalConstants current;

                    //Then we will want to check some other additional paramaters
                    //Create the drop down for spinner

                    dropDownMenus.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

                    int some_random_id = 659;
                    dropDownMenus.setId(some_random_id);
                    //We also
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
                            R.layout.brand_dropdown, R.id.whatchgoood, GlobalConstants.DROPDOWNCHOICES);
                    adapter.setDropDownViewResource(R.layout.brand_dropdown);



                    dropDownMenus.setAdapter(adapter);
                    layout.addView(dropDownMenus);

                    dropDownMenus.getLayoutParams().width = convertDipToPixels(v.getContext(), 200f);
                 //   dropDownMenus.getLayoutParams().height = co;
                    dropDownMenus.setBackgroundResource(R.drawable.dropdown);

                    layout_constraint = new ConstraintSet();

                    layout_constraint.clone(layout);


                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.TOP, indoorPlant.getId(), ConstraintSet.BOTTOM, 0);

                    layout_constraint.setVerticalBias(dropDownMenus.getId(), 0);
                    layout_constraint.setHorizontalBias(dropDownMenus.getId(), 0.1f);
                    layout_constraint.applyTo(layout);

                 /*   params.topMargin = convertDipToPixels(c, 8f);
                    params.bottomMargin = convertDipToPixels(c, 8f);
                    params.horizontalBias = 0;
                    params.verticalBias = 1;
                    params.bottomToBottom = layout.getId();
                    params.endToEnd = layout.getId(); //Parent ID
                    params.topToBottom = indoorPlant.getId();
                    params.startToStart = layout.getId(); // Parent ID
                    params.constrainedHeight = true;
                    */
                    //      layout_constraint = new ConstraintSet();
                    //        layout_constraint.clone(layout);
                    //       layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0); //To acheieve, app:layout_constraintLeft_toLeftOf="@+id/parent"

                } else {
                    layout.removeView(dropDownMenus); //O
                }

            }
        });

// Define a listener that responds to location updates

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
}

