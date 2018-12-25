package com.example.chris.plantapplication;

public class individualSoil {
    private String soilType;
    private float moistureContent;
    private float storageWaterCapacity;

    public individualSoil(String soilType, float moistureContent, float storageWaterCapacity){
        this.soilType = soilType;
        this.moistureContent = moistureContent;
        this.storageWaterCapacity = storageWaterCapacity;
    }

    public float getMoistureContent() {
        return moistureContent;
    }

    public void setMoistureContent(float moistureContent) {
        this.moistureContent = moistureContent;
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
}
