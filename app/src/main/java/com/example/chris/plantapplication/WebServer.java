package com.example.chris.plantapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
                    float minTemp = eachVase.child("Temperature Min").getValue(float.class);
                    float maxTemp = eachVase.child("Temperature Max").getValue(float.class);
                    plantH.addPlant(currentPlantName, buttonID, slotNumber, harvestPeriod, plantType,minTemp,maxTemp);

                }
                float roomTemperature = eachVase.child("Air Temperature").getValue(float.class);
                float airHumidity = eachVase.child("Air Humidity").getValue(float.class);
                int waterPeriod = eachVase.child("Water Period").getValue(Integer.class);
                float waterRequirement_period = eachVase.child("Water Amount").getValue(Float.class);
                Plant currentPlant = plantH.getPlantBySlot(slotNumber);
                currentPlant.setWaterRequirement_Period(waterRequirement_period);
                currentPlant.setGrowthInterval(waterPeriod);

                ArrayList<Float> soilHumidity = getArray(eachVase.child("Soil Humidity"));
                ArrayList<Float> waterDistribution = getArray(eachVase.child("Water Distribution"));
                ArrayList<Float> plantHeight = getArray(eachVase.child("Plant Height"));

                currentPlant.setAirHumidity(airHumidity);
                currentPlant.setRoomTemperature(roomTemperature);
                //launchDismissDlg(this);

                currentPlant.setGrowth_EachDay(plantHeight);
                currentPlant.setHumiditySensor_harvestPeriod(soilHumidity);
                currentPlant.setWaterDistribution(waterDistribution);
                int dayNumber = (int) eachVase.child("Soil Humidity").getChildrenCount();

            }

            slotNumber += 1;
        }
    }
    private void launchDismissDlg(Activity _Activity) {
        float optimalTemperature = plantH.plantOptimalTemperatureChange();
        optimalTemperature = Math.round((optimalTemperature*100)/100);

        if (optimalTemperature != 0f) {
            //Display the dialogue box
            if (optimalTemperature < 0f) {
                String message = "Consider cooling your plants down by " + Float.toString(Math.abs(optimalTemperature)) + "°C";
                displayDialog(_Activity, "Temperature Warning", message);
            }
            else{
                String message = "Consider warming your plants up by " + Float.toString(Math.abs(optimalTemperature)) + "°C";
                displayDialog(_Activity,"Temperature Warning",message);
            }
            //If there is a plant that is not growing in the optimal conditions
            //


        }
    }private void displayDialog(Activity _Activity, String messageType, String message) {
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
}
