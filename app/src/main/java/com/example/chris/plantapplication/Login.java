package com.example.chris.plantapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private Button connect;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        final WebServer webServer = new WebServer(this);
        new Thread(webServer).start();

        connect = (Button) findViewById(R.id.button4);
        connect.setText("Connect");
        progress = (ProgressBar) findViewById(R.id.progressBar2);

        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progress.setVisibility(progress.VISIBLE);

                if (webServer.isSystemConnected() == true){
                    //Then we can connect
                    callIntent(v.getContext(), MainActivity.class);
                }
                else{
                    displayDialog((Activity) v.getContext(), "You system is disconnected", "Please connect your system.");
                    callIntent(v.getContext(), MainActivity.class);

                }
                /*boolean hardwareConnection= webServer.isSystemConnected();
                long timeoutExpiredMs = System.currentTimeMillis() + 3000;
                while (!hardwareConnection) {

                    long waitMs = timeoutExpiredMs - System.currentTimeMillis();
                    if (waitMs <= 0) {
                        // timeout expired
                        break;
                    }
                    // we assume we are in a synchronized (object) here
                    //webServer.listenFirebase_hardware();

                    // we might get improperly awoken here so we loop around to see if we timed out
                }


                if (hardwareConnection == true) {
                    callIntent(v.getContext(), MainActivity.class);

                } else {

                    //Display a warning message
                    displayDialog((Activity) v.getContext(), "You system is not connected", "Please connect your system.");
                    callIntent(v.getContext(), MainActivity.class);

                }*/


            }
        });

    }


    public void callIntent(Context c, Class destination) {
        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        startActivity(intent);
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

}
