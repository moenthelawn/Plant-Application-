package com.example.chris.plantapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;

public class Login extends AppCompatActivity {
    private Button connect;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        WebServer webServer = new WebServer(this);
        new Thread(webServer).start();

        connect = (Button)findViewById (R.id.button4);
        connect.setText("Connect");
        progress = (ProgressBar) findViewById(R.id.progressBar2);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(progress.VISIBLE);
                callIntent(v.getContext(),MainActivity.class);

            }
        });
    }
    public void callIntent(Context c, Class destination) {
        Intent intent = new Intent(c, destination); //Off to let the user chose their soil
        startActivity(intent);
    }
}
