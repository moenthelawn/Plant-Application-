package com.example.chris.plantapplication;

public class GlobalConstants {
    public static final float MAX_WATERTANK = 15f; //The units are in L
    public static final int MAXPLANTS = 3;
    public static final float MAXHEIGHT = 0.5f;//Max height of the plant vase

    public static final float AVAILABILITYCOEFFICIENT = 0.5f; //This is the availability coefficient used to determine the maximum amount of water that can be applied

    public static final String Predetermined = "Predetermined"; //This is from the determined plant databases like basil
    public static final String Calculated = "Calculated"; //This is from the calculated portion of the database
    public static final String Manual = "Manual"; //This is is from when the user inputs their manual amount
}
