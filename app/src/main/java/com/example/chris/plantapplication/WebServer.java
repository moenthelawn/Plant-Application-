package com.example.chris.plantapplication;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class WebServer implements Runnable {
    private String dataBase;
    private Firebase mRef;
    private plantDataBase plantH;
    private Activity _activity;
    private boolean success;

    public WebServer(Activity _activity) {
        this._activity = _activity;
        this.dataBase = "https://plantsystem-9ff68.firebaseio.com/";
        //Load the data in from the serve

        mRef = new Firebase(dataBase);
        plantH = plantDataBase.getInstance();

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
                setWaterTankNotification(currentWaterLevels / GlobalConstants.MAX_WATERTANK);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_activity);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("Water Tank", currentWaterLevels);
                editor.apply();

                retrieve_load_data(dataSnapshot.child("Plant Vases"));
            }




            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }
    public void retrieve_load_data(DataSnapshot dataSnapshot){
        int slotNumber = 1;
        for (DataSnapshot eachVase : dataSnapshot.getChildren()) {
            String currentPlantName = eachVase.child("Plant Name").getValue(String.class);

            if (currentPlantName != "" && currentPlantName != null) {
                if (plantH.getPlantBySlot(slotNumber) == null) {

                    int harvestPeriod = eachVase.child("Harvest Period").getValue(int.class);
                    String plantType = eachVase.child("Plant Type").getValue(String.class);
                    int buttonID = eachVase.child("Button ID").getValue(int.class);

                    plantH.addPlant(currentPlantName, buttonID, slotNumber, harvestPeriod, plantType);

                }
                float roomTemperature = eachVase.child("Air Temperature").getValue(float.class);
                float airHumidity = eachVase.child("Air Humidity").getValue(float.class);

                Plant currentPlant = plantH.getPlantBySlot(slotNumber);

                ArrayList<Float> soilHumidity = getArray(eachVase.child("Soil Humidity"));
                ArrayList<Float> waterDistribution = getArray(eachVase.child("Water Distribution"));
                ArrayList<Float> plantHeight = getArray(eachVase.child("Plant Height"));

                currentPlant.setAirHumidity(airHumidity);
                currentPlant.setRoomTemperature(roomTemperature);

                currentPlant.setGrowth_EachDay(plantHeight);
                currentPlant.setHumiditySensor_harvestPeriod(soilHumidity);
                currentPlant.setWaterDistribution(waterDistribution);
                int dayNumber = (int) eachVase.child("Soil Humidity").getChildrenCount();

            }

            slotNumber += 1;
        }
    } public ArrayList<Float> getArray(DataSnapshot children) {
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
}
