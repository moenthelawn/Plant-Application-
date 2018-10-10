package com.example.chris.plantapplication;

public class plantDataBase {

    int test;
    private static plantDataBase instance;
    private Plant[] allPlants;

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

    public boolean setPlantSlotNumber(String Name, int slotNumber) { //This function will handle adding the plant to the correct slot number
        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i].getName() == Name) {
                //Then we will add the slot number to that plant's data base
               if(allPlants[i].setSlotNumber(slotNumber) == true){
                   //IF it exists
                   return true;
               }
               else{
                   return false; //Then there is no more room in that plant slot since the maximum is 3

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
}
