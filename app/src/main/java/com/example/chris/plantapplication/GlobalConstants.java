package com.example.chris.plantapplication;

public class GlobalConstants {
    public static final float MAX_WATERTANK = 5f; //The units are in L
    public static final int MAXPLANTS = 3;
    public static final float MAXHEIGHT = 0.1397f;//Max height of the plant vase
    public static final int MAXSOILTYPES = 6;
    public static final float AVAILABILITYCOEFFICIENT = 0.5f; //This is the availability coefficient used to determine the maximum amount of water that can be applied
    public static final float MAXSOILDEVIATION = 5f; //Max deviation above is ~5%
    public static final float MAXHUMIDITYVALUE = 1024;
    public static final float MINHUMIDITYVALUE = 0;

    public static final String Predetermined = "Predetermined"; //This is from the determined plant databases like basil
    public static final String Manual = "Manual"; //This is is from when the user inputs their manual amount
}
