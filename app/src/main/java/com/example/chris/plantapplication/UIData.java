package com.example.chris.plantapplication;

public class UIData {
    private float airHumidity;
    private float height;
    private float angle;
    private float airTemperature;
    private  float waterTank;
    private int numberDay;
    private  int plantSlot;
    private  float soilHumidity;


    public UIData(String message) {
        //decompose the string message into its components
        String[] values = message.split(";"); //Split the message into each component
        this.airHumidity = Float.parseFloat(values[0]); //Air Humidity as a percentage
        this.height = Float.parseFloat(values[1]);
        this.angle = Float.parseFloat(values[2]);
        this.airTemperature = Float.parseFloat(values[3]);
        this.waterTank = Float.parseFloat(values[4]);
        this.numberDay = Integer.parseInt(values[5]);
        this.plantSlot = Integer.parseInt(values[6]);
        this.soilHumidity = Float.parseFloat(values[7]);
    }

    public float getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(float airHumidity) {
        this.airHumidity = airHumidity;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(float airTemperature) {
        this.airTemperature = airTemperature;
    }

    public float getWaterTank() {
        return waterTank;
    }

    public void setWaterTank(float waterTank) {
        this.waterTank = waterTank;
    }

    public int getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }

    public int getPlantSlot() {
        return plantSlot;
    }

    public void setPlantSlot(int plantSlot) {
        this.plantSlot = plantSlot;
    }

    public float getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(float soilHumidity) {
        this.soilHumidity = soilHumidity;
    }
}
