package com.example.chris.plantapplication;

import java.util.Arrays;

public class plantDataBase<E> {

    int test;

    private static GlobalConstants constants;
    private static plantDataBase instance;
    private Plant[] allPlants;
    private boolean added;

    public Plant getPlantByButtonNumber(int buttonNumber) {
        //Here we will want to grab the plants data based on the inputted button number
        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i] != null) {
                Plant CurrentPlant = allPlants[i];

                int slotNumber = CurrentPlant.getSlotNumber();
                if (slotNumber == buttonNumber) {
                    return CurrentPlant;
                }
            }
        }

        Plant empty = new Plant("", -1, -1);
        return empty; //otherwise we return empty
    }

    public Plant getPlantBySlot(int slotNumber) {
        return allPlants[slotNumber - 1];

    }

    public boolean buttonExists(int buttonNumber) {
        if (added == true) {
            for (int i = 0; i < allPlants.length; i++) {
                if (allPlants[i] != null) {
                    int buttonValue = allPlants[i].getButtonNumber();
                    if (buttonNumber == buttonValue) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //public void setPlantByString(String Name, int buttonID, int slotNumberID) {


    //}
    //public boolean setPlantSlotByString(String Name, int buttonID, int slotNumberID) {
    //   Plant currentPlant = allPlants[slotNumberID];


    //   for (int i = 0; i < allPlants.length; i++) {
    //       Plant currentPlant = allPlants[i];
    //      if (currentPlant.getName() == Name) {
    //         //Then we return the slot number
    //        if (currentPlant.setPlantSlotNumber(buttonID, slotNumberID) == true) {
    //            return true;
    //    } else {
    //        return false;
    //    }

    //    }

    //    }
    //      return false;
    //  }

    public Plant[] getAllPlants() {
        return allPlants;
    }

    public static plantDataBase getInstance() {
        if (instance == null) {
            instance = new plantDataBase();
        }
        return instance;
    }

    private plantDataBase() {
        //we want to add a Basil Plant
        allPlants = new Plant[GlobalConstants.MAXPLANTS];
        added = false;
    }

    public void addPlant(String PlantName, int buttonID, int slotNumber) {

        allPlants[slotNumber - 1] = new Plant(PlantName, buttonID, slotNumber);
        added = true;

    }


    public Plant getPlant(String Name, int slotNumber) {
        return allPlants[slotNumber - 1];

    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
