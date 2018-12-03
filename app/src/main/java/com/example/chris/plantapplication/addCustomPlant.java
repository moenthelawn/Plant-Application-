package com.example.chris.plantapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class addCustomPlant extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double longitude;
    private double latitude;
    private CheckBox indoorPlant;
    private Spinner dropDownMenus;

    private TextView sunlightParamaters;
    private ImageView hours_Sunlight;

    private EditText sunlight_text;
    private EditText plantName;
    private EditText plantGrowth;

    private Plant plantAdded;

    private Button next;

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
        sunlightParamaters = (TextView) findViewById(R.id.textView39);
        hours_Sunlight = (ImageView) findViewById(R.id.imageView10);


        //These are values that correspond to what the user will need to fill in
        sunlight_text = (EditText) findViewById(R.id.editText2);
        plantName = (EditText) findViewById(R.id.editText);
        plantGrowth = (EditText) findViewById(R.id.editText5);
        next = (Button) findViewById(R.id.button);

        layout = (ConstraintLayout) findViewById(R.id.layout1); //Current layout of constraint layout
        layout_constraint = new ConstraintSet();

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
        //  latitude = lastKnownLocation.getLatitude();
        // longitude = lastKnownLocation.getLongitude();

        int ID_drop_down = 659;
        int ID_text_sunlight_hours = 661;

        dropDownMenus.setId(ID_drop_down);

        GlobalConstants current;

        //Then we will want to check some other additional paramaters
        //Create the drop down for spinner

        dropDownMenus.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

        //We also
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.brand_dropdown, R.id.whatchgoood, GlobalConstants.DROPDOWNCHOICES);
        adapter.setDropDownViewResource(R.layout.brand_dropdown);

        dropDownMenus.setAdapter(adapter);

        indoorPlant = (CheckBox) findViewById(R.id.checkBox2);

        setSunlightAddition(false); //default is set to false unless it is otherwise specified.
        displayNextButton(false);

        plantGrowth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredeFields(); //We will want to check to make sure the sequence is valid
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sunlight_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredeFields(); //We will want to check to make sure the sequence is valid
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        plantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredeFields(); //We will want to check to make sure the sequence is valid
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dropDownMenus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Listening to see if there are any changes to the clicked drop down
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                if (selectedItem == GlobalConstants.DROPDOWNCHOICES[1] && indoorPlant.isChecked() == true) {
                    setSunlightAddition(true);
                } else {
                    setSunlightAddition(false);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We will go to the next screen

                // we can also add of the data paramaters
                Intent activityThatCalled = getIntent();

                int buttonID = activityThatCalled.getIntExtra("Button ID", 0); //get the button session ID so we can modify its .xml paramaters
                int slotNumber = activityThatCalled.getIntExtra("Slot Number", 0); //get the button session ID so we can modify its .xml paramaters

                int growthPeriod = Integer.parseInt(plantGrowth.getText().toString());
                String plantNamed = plantName.getText().toString();
                int[] harvestPeriod = {growthPeriod};
                double[][] cropCoefficients = {{0}}; //For now the crop coefficients we will be set to zero until we figure this out


                plantDataBase.getInstance().addPlant(plantNamed, buttonID, slotNumber, harvestPeriod, cropCoefficients, 0.2f, 3, growthPeriod);

                Intent intent = new Intent(v.getContext(), soilType.class);
                startActivity(intent);
            }
        });
        indoorPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((CheckBox) v).isChecked();
                if (checked == true) {

                    layout.addView(dropDownMenus);

                    dropDownMenus.getLayoutParams().width = convertDipToPixels(v.getContext(), 200f);
                    //   dropDownMenus.getLayoutParams().height = co;
                    dropDownMenus.setBackgroundResource(R.drawable.dropdown);

                    layout_constraint.clone(layout);


                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                    layout_constraint.connect(dropDownMenus.getId(), ConstraintSet.TOP, indoorPlant.getId(), ConstraintSet.BOTTOM, 0);

                    layout_constraint.setVerticalBias(dropDownMenus.getId(), 0);
                    layout_constraint.setHorizontalBias(dropDownMenus.getId(), 0.1f);
                    layout_constraint.applyTo(layout);
                    if (GlobalConstants.DROPDOWNCHOICES[1] == dropDownMenus.getSelectedItem().toString()) {
                        setSunlightAddition(true); //Definitely want to check this to false
                    }

                } else {
                    layout.removeView(dropDownMenus); //
                    setSunlightAddition(false); //Definitely want to check this to false
                }

            }
        });

    }

    public void displayNextButton(boolean display) {
        if (display == true) {
            next.setVisibility(next.VISIBLE);

        } else {
            next.setVisibility(next.INVISIBLE);
        }
    }

    public void checkRequiredeFields() {
//This function will take in the char sequence and determine if the sequence has been validated
        //Scenario 1, we have all our entries entered, and the growing indoors button is unchecked
        if (plantGrowth.getText().toString().isEmpty() != true
                && sunlight_text.getText().toString().isEmpty() != true
                && plantName.getText().toString().isEmpty() != true
                && GlobalConstants.DROPDOWNCHOICES[1] == dropDownMenus.getSelectedItem().toString()) {
//If this is is the case, then we have all of our paramaters checked
            displayNextButton(true);
            //Enable the button
        } else if (plantGrowth.getText().toString().isEmpty() != true
                && plantName.getText().toString().isEmpty() != true
                && GlobalConstants.DROPDOWNCHOICES[0] == dropDownMenus.getSelectedItem().toString()
                ) {
            //Then we will set the enable button
            displayNextButton(true);
        } else {
            //Otherwise it will be turned off
            displayNextButton(false);
        }


    }

    public void setSunlightAddition(boolean visibility) { //This function will set the visibility of the ability to add paramaters
        if (visibility == true) {
            sunlightParamaters.setVisibility(sunlightParamaters.VISIBLE);
            hours_Sunlight.setVisibility(hours_Sunlight.VISIBLE);
            sunlight_text.setVisibility(sunlight_text.VISIBLE);
        } else {
            sunlightParamaters.setVisibility(sunlightParamaters.INVISIBLE);
            hours_Sunlight.setVisibility(hours_Sunlight.INVISIBLE);
            sunlight_text.setVisibility(sunlight_text.INVISIBLE);
        }
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

