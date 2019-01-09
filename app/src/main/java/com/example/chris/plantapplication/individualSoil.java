package com.example.chris.plantapplication;

public class individualSoil {
    private String soilType;
    private float moistureContent_MAD; //Also as a percentage
    private float fieldCapacity; //As a percentage
    private float storageWaterCapacity; //Units are cm/m

    public individualSoil(String soilType, float fieldCapacity, float moistureContent_MAD, float storageWaterCapacity) {
        this.soilType = soilType;
        this.fieldCapacity = fieldCapacity;
        this.moistureContent_MAD = moistureContent_MAD;
        this.storageWaterCapacity = storageWaterCapacity;
    }

    public float getStorageWaterCapacity() {
        return storageWaterCapacity;
    }

    public void setStorageWaterCapacity(float storageWaterCapacity) {
        this.storageWaterCapacity = storageWaterCapacity;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public float getMoistureContent_MAD() {
        return moistureContent_MAD;
    }

    public void setMoistureContent_MAD(float moistureContent_MAD) {
        this.moistureContent_MAD = moistureContent_MAD;
    }

    public float getFieldCapacity() {
        return fieldCapacity;
    }

    public void setFieldCapacity(float fieldCapacity) {
        this.fieldCapacity = fieldCapacity;
    }
}
