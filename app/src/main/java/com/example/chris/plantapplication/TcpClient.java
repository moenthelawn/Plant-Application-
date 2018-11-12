package com.example.chris.plantapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.LogRecord;

public class TcpClient implements Runnable{
    private Context context;

    Socket s;
    ServerSocket ss;
    InputStreamReader isr;
    BufferedReader bufferedReader;
    public String message;


    Handler h = new Handler();

    public TcpClient(Context context){
        this.context = context;
    }

    @Override
    public void run(){
        try{
            ss  = new ServerSocket(7800);

            while(true) {
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                bufferedReader = new BufferedReader(isr);
                message = bufferedReader.readLine();

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
