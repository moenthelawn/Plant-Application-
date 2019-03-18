package com.example.chris.plantapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class WebServer implements Runnable {
    private String dataBase;
    private Firebase mRef;
    private plantDataBase plantH;
    private Activity _activity;
    private String currentDate;
    private float airTemperature;
    private boolean powerSystem;
    private boolean success;
    private boolean systemConnected;
    private int hardwareConnection;

    public WebServer(Activity _activity) {
        this._activity = _activity;
        this.dataBase = "https://plantsystem-9ff68.firebaseio.com/";
        this.powerSystem = false;
        //Load the data in from the serve
        this.systemConnected = false;
        mRef = new Firebase(dataBase);
        mRef.child("hardware_connection").setValue(1);
        plantH = plantDataBase.getInstance();
        this.hardwareConnection = 0; //Default is set to 0
    }


    @Override
    public void run() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                success = true;
                //   Object object = dataSnapshot.getValue(Object.class);
                int i = 1;


                float currentWaterLevels = dataSnapshot.child("Water Tank").getValue(float.class);
                powerSystem = getPowerSystem(dataSnapshot.child("Power").getValue(String.class));
                currentDate = dataSnapshot.child("Current Date").getValue(String.class);
                airTemperature = dataSnapshot.child("Air Temperature").getValue(float.class);
                hardwareConnection = dataSnapshot.child("hardware_connection").getValue(Integer.class);
                if (hardwareConnection == 0){

                    //Then we set the system connection as true
                    systemConnected = true;
                }

           /*     Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        mRef.child("phone_connection").setValue(1); //This sets the value for being online
                    }
                }, 5000);
*/

                setWaterTankNotification(currentWaterLevels / GlobalConstants.MAX_WATERTANK);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_activity);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putFloat("Water Tank", currentWaterLevels);
                editor.putBoolean("Power System", powerSystem);

                editor.apply();

                retrieve_load_data(dataSnapshot.child("Plant Vases"));

                mRef.child("Hardware System Write").setValue(0);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });

    }

    private boolean getPowerSystem(String power_system) {
        if (power_system.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public void retrieve_load_data(DataSnapshot dataSnapshot) {
        int slotNumber = 1;
        for (DataSnapshot eachVase : dataSnapshot.getChildren()) {
            String currentPlantName = eachVase.child("Plant Name").getValue(String.class);
            if (currentPlantName != "" && currentPlantName != null) {
                if (plantH.getPlantBySlot(slotNumber) == null) {

                    int harvestPeriod = eachVase.child("Harvest Period").getValue(int.class);
                    String plantType = eachVase.child("Plant Type").getValue(String.class);
                    int buttonID = eachVase.child("Button ID").getValue(int.class);
                    float minTemp = eachVase.child("Temperature Min").getValue(float.class);
                    float maxTemp = eachVase.child("Temperature Max").getValue(float.class);
                    plantH.addPlant(currentPlantName, buttonID, slotNumber, harvestPeriod, plantType);

                }
                /*Value Paramaters from the database*/
                Date lastWaterDate = getLastWaterDate(eachVase.child("Last Water Time").getValue(String.class), "MM-dd-yyyy-hh:mmaa");
                float airHumidity = eachVase.child("Air Humidity").getValue(float.class);
                float water_period = eachVase.child("Water Amount").getValue(Integer.class);

                int days = eachVase.child("Water Period").child("Days").getValue(Integer.class);
                int hours = eachVase.child("Water Period").child("Hours").getValue(Integer.class);
                int minutes = eachVase.child("Water Period").child("Minutes").getValue(Integer.class);
                int seconds = eachVase.child("Water Period").child("Seconds").getValue(Integer.class);


                String startDay = eachVase.child("Growth Start").getValue(String.class);
                int dayNumber = getTimeFrameDifference(startDay, currentDate);


                /*Setting the paramaters into that particular plant */
                Plant currentPlant = plantH.getPlantBySlot(slotNumber);

                currentPlant.setLastWaterDate(lastWaterDate);
                currentPlant.setCurrentDayNumber(dayNumber);
                currentPlant.setWaterRequirement_Period(days, hours, minutes, seconds);
                currentPlant.setWater_period(water_period);

                ArrayList<Float> soilHumidity = getArray(eachVase.child("Soil Humidity"));
                /*ArrayList<Float> waterDistribution = getArray(eachVase.child("Water Distribution"));*/
                ArrayList<Float> plantHeight = getArray(eachVase.child("Plant Height"));

                currentPlant.setAirHumidity(airHumidity);
                currentPlant.setRoomTemperature(airTemperature);
                //launchDismissDlg(this);

                currentPlant.setGrowth_EachDay(plantHeight);
                currentPlant.setHumiditySensor_harvestPeriod(soilHumidity);
/*
                currentPlant.setWaterDistribution(waterDistribution);
*/

            }

            slotNumber += 1;
            //  _activity.recreate();

        }

    }

    private Date getLastWaterDate(String message, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date lastWater = new Date();

        try {
            lastWater = sdf.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastWater;
    }

    private int getTimeFrameDifference(String startDay, String currentDay) {
        //This function will get the day number between two dates
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date date_current;
        Date date_start;
        try {
            date_current = sdf.parse(currentDay);
            date_start = sdf.parse(startDay);
            long diff = date_current.getTime() - date_start.getTime();
            int diffDays = (int) ((diff) / (24 * 60 * 60 * 1000));
            return diffDays;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

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

    public ArrayList<Float> getArray(DataSnapshot children) {
        ArrayList<Float> Values = new ArrayList();

        for (DataSnapshot eachChild : children.getChildren()) {
            //  String current = eachChild.getValue();
            float CurrentValue = eachChild.getValue(float.class);
            Values.add(CurrentValue);

        }
        return Values;
    }

    public void setWaterTankNotification(float waterTankPercentage) {
        //This will be used to display a notification if it is needed


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(_activity, "default")
                .setSmallIcon(R.drawable.tankerror)
                .setContentTitle("Low water!")
                .setContentText("Please refill your water tank")
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        int notificationID = 1;

        NotificationManager mNotificationManager = (NotificationManager) _activity.getSystemService(_activity.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        mNotificationManager.createNotificationChannel(channel);
        //  NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
        // Since android Oreo notification channel is needed.
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

        }*/
        //channel.setDescription(description);
        if ((waterTankPercentage * 100.00f) == 0) {
            mBuilder.setContentTitle("Water tank is empty!");
        }


        if ((waterTankPercentage * 100.00f) <= 20) {
            // notificationId is a unique int for each notification that you must define
            mNotificationManager.notify(notificationID, mBuilder.build());
        }
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getHardwareConnection() {
        return hardwareConnection;
    }

    public void setHardwareConnection(int hardwareConnection) {
        this.hardwareConnection = hardwareConnection;
    }

    public boolean isSystemConnected() {
        return systemConnected;
    }

    public void setSystemConnected(boolean systemConnected) {
        this.systemConnected = systemConnected;
    }
}
