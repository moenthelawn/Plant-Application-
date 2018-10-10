package com.example.chris.plantapplication;

public class plantDataBase {

    int test;
    private static plantDataBase instance;
    private Basil basil;

    private Basil getBasil(){
        return basil;
    }

    public static plantDataBase getInstance() {
        if (instance == null) {
            instance = new plantDataBase();
        }
        return instance;
    }

    private plantDataBase() {
        //we want to add a Basil Plant
        basil = new Basil(); //Create a new basil plant

    }
}
