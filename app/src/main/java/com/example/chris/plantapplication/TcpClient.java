package com.example.chris.plantapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.LogRecord;

public class TcpClient implements Runnable {
    private Context context;
    private Activity activity;
    Socket s;
    ServerSocket ss;
    InputStreamReader isr;
    BufferedReader bufferedReader;

    public String message;
    private plantDataBase planth;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private TextView youHave;
    private TextView testButton;

    public TcpClient(Activity _activity) {
        this.activity = _activity;
        planth = plantDataBase.getInstance();
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(7800);

            while (true) {
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                bufferedReader = new BufferedReader(isr);

                message = bufferedReader.readLine();
                while (message != null) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Message", message);
                    editor.apply();
                    UIData uiData = new UIData(message);
                    setWaterTankNotification(uiData.getWaterTank() / GlobalConstants.MAX_WATERTANK);
                    updatePlantDataBase(uiData.getPlantSlot(), uiData.getAirTemperature(), uiData.getAirHumidity(),
                            uiData.getNumberDay(), uiData.getHeight(), uiData.getSoilHumidity(),uiData.getWaterDistribution());
                    message = bufferedReader.readLine();
                    //updateUI(message);
                }

           /*     //we want to update the UI
                if (message != null) {
                    updateUI(message);
                }*/


               /* h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createSharedPreferences(String message) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Message", message);
        editor.apply();
    }

    private void updatePlantDataBase(int plantSlot, float airTemperature, float airHumidity,
                                     int numberDay, float height, float soilHumidity,float waterDistribution) {

        //int buttonNumber = threeButtons.getButtonNumber(plantSlot);
        if (planth.isSlotExists(plantSlot) == true) {
            Plant CurrentPlant = planth.getPlantBySlot(plantSlot);

            //Now we want to submit all the data to the corresponding plant
            CurrentPlant.setRoomTemperature(airTemperature);
            CurrentPlant.setAirHumidity(airHumidity);

            int DayNumber = CurrentPlant.getCurrentDayNumber();

            CurrentPlant.setHumiditySensor_harvestPeriod_dayNumber(soilHumidity, numberDay); //height
            CurrentPlant.setDayGrowth_Number(height, numberDay);
           // CurrentPlant.setHumiditySensor(soilHumidity);
            CurrentPlant.setCurrentDayNumber(numberDay);
            CurrentPlant.setWaterDistribution_harvestPeriod_dayNumber(waterDistribution,numberDay);
            //Send the correct watering amount to the server database
            // double waterAmount = CurrentPlant.getDailyWaterAmount_millimetres(numberDay);
        }
    }

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void setWaterTankNotification(float waterTankPercentage) {
        //This will be used to display a notification if it is needed

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity.getApplicationContext(), "CHANNEL_ID")
                .setSmallIcon(R.drawable.tankerror)

                .setContentTitle("Low water!")
                .setContentText("Please refill your water tank")
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        int notificationID = 1;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getApplicationContext());

        if ((waterTankPercentage * 100.00f) == 0){
            mBuilder.setContentTitle("Water tank is empty!");
        }


        if ((waterTankPercentage * 100.00f) <= 20) {
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationID, mBuilder.build());
        }
    }
}
   /* public class Utils {

        public static void runOnUiThread(Runnable runnable){
            final Handler UIHandler = new Handler(Looper.getMainLooper());
            UIHandler .post(runnable);
        }
    }

Utils.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            // UI updation related code.
        }
    })
}
*/





