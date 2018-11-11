package com.example.chris.plantapplication;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FrameSend extends AsyncTask<Void,Void,Void>
{

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;
    protected Void doInBackground(Void... voids){

        try{
            s = new Socket("192.168.2.11", 7800);
        } catch (IOException e)
        {
            e.printStackTrace();

        }




        return null;
    }

}
