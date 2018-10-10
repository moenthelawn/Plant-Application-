package com.example.chris.plantapplication;

public class Plant {
    private int slotNumber[];
    private String Name;

    public Plant(String Name) {
        this.slotNumber = new int[]{-1,-1,-1};
        this.Name = Name; //We haven't name it yet
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSlotNumber() {
        return slotNumber[];
    }

    public boolean setSlotNumber(int slotNumber) {

    }
}
