package com.example.chris.plantapplication;

public class plantDataBase {

    int test;
    private static plantDataBase instance;
    private Plant[] allPlants;

    public Plant getPlantByButtonNumber(int buttonNumber) {
        //Here we will want to grab the plants data based on the inputted button number
        for (int i = 0; i < allPlants.length; i++) {
            Plant CurrentPlant = allPlants[i];
            int[] slotNumbers = CurrentPlant.getSlotNumber();
            for (int j = 0; j < slotNumbers.length; j++) {
                if (buttonNumber == slotNumbers[j]) {
                    return CurrentPlant;
                }
            }
        }
        Plant empty = new Plant("");
        return empty; //otherwise we return empty
    }

    public boolean setPlantSlotByString(String Name, int slotNumberID) {
        for (int i = 0; i < allPlants.length; i++) {
            Plant currentPlant = allPlants[i];
            if (currentPlant.getName() == Name) {
                //Then we return the slot number
                if (currentPlant.setPlantSlotNumber(slotNumberID) == true) {
                    return true;
                } else {
                    return false;
                }

            }

        }
        return false;
    }

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
        allPlants = new Plant[]{new Plant("Basil"), new Plant("Tomato")};
    }

    public Plant getPlant(String Name) {
        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i].getName() == Name) {
                //Then we have found the correct string name
                return allPlants[i];
            }
        }

        Plant emptyPlant = new Plant(""); //Return an empty plant since we do not need to reference it
        return emptyPlant;
    }
}
