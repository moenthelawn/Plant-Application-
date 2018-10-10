package com.example.chris.plantapplication;

//This class will control all of the data flow through,
//1. Water Tank Values
//2. Slot Values for each plant type

public class mainDataStorage {
    private float waterTank; //Specifies the water tank value
    private String stringSlots[]; //Specifies the plant string name based on what has been inputted into each slot
    

    //----------------------Getters/Setters----------------------//
    public float getWaterTank() {
        return waterTank;
    }

    public void setWaterTank(float waterTank) {
        this.waterTank = waterTank;
    }

    public String[] getStringSlots() {
        return stringSlots;
    }

    public void setStringSlots(String[] stringSlots) {
        this.stringSlots = stringSlots;
    }
}
