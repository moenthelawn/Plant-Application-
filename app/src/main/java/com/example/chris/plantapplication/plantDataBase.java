package com.example.chris.plantapplication;

import java.util.Arrays;

public class plantDataBase<E> {

    int test;

    private static GlobalConstants constants;
    private static plantDataBase instance;
    private Plant[] allPlants;
    private boolean added;
    private Plant plant;
    private static SoilStorage soilData;

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

        int empty1 = -1;
        float empty3 = -1;
        Plant empty = new Plant("", -1, -1, empty1, -1f, -1f, -1, "");
        return empty; //otherwise we return empty
    }

    public Plant getPlantBySlot(int slotNumber) {
        return allPlants[slotNumber - 1];

    }

    public boolean isSlotExists(int slotNumber) {
        if (allPlants[slotNumber - 1] == null) {
            return false;
        } else {
            return true; //the plant does exist in the datbase
        }
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

    public void setSoilType(int slotNumber, String soilType) {
        allPlants[slotNumber - 1].setSoilType(soilType);
    }

    public void setPlantGrowingDepth(int slotNumber, float depth) {
        allPlants[slotNumber - 1].setPlantDepth(depth);
    }

    public void deletePlant(Plant currentPlant) {
        int slotNumber = currentPlant.getSlotNumber();
        allPlants[slotNumber - 1] = null;
        /*allPlants[slotNumber -1 ]*/
    }

    public float getWaterAmount_Interrupt(int slotNumber) {
        //This function will be called when there is a timer interrupt to determine how much water a plant should get after a certain amount of time
        Plant currentPlant = allPlants[slotNumber - 1];
        float maxWaterAmount = soilData.getMaxWaterAmount(currentPlant.getPlantDepth(), currentPlant.getSoilType()); //Current plant depth

        // if (maxWaterAmount > currentPlant.get)
        float humiditySensor = currentPlant.getHumiditySensor();
        float water_plant_remaining = currentPlant.getWater_remaining_current_day();

        /*
        Sudo Code
        if (the plant water needs exceed the maximum amount of water and the humidity soil sensor is at the minimum required amount ){
            * Then we will send the maximum portion of the water
            * Store the leftover portion and save that for the next time around
        }
        else if (the plant water needs do not exceed the amount of water and the humidity sensor is at the minmium required amount){
             * Then we will send the amount of water that is needed

        }


         */
        //This is if there is no more water remaining to be be fse
        if (water_plant_remaining == 0) {
            if (currentPlant.getPlantType() == GlobalConstants.Predetermined) {
                //Then we wil execute the predetermined watering amount calculation which is based on the crop factor etc
                float waterRequirement_Predetermined = currentPlant.calculateWater_PreDetermined();

                if (waterRequirement_Predetermined <= maxWaterAmount) {
                    //Send the water off
                    return waterRequirement_Predetermined;
                } else {
                    //set the leftover water amount
                    float leftOver = waterRequirement_Predetermined - maxWaterAmount;
                    currentPlant.setWater_remaining_current_day(leftOver);
                }

            } else if (currentPlant.getPlantType() == GlobalConstants.Manual) {
                //   double currentPlantWaterAmount = currentPlant.get
                float currentPlantWaterAmount_Manual = currentPlant.getWaterRequirement_Manual();//There is no need to compute the maximum amount of water that could be sent at a given time since the user specifies how much they would like to get at any given point in time
                return currentPlantWaterAmount_Manual;
            }
        } else {
            return water_plant_remaining;
        }
        return maxWaterAmount;
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

    public void addPlant(String PlantName, int buttonID, int slotNumber,
                         int harvestPeriod_days, float cropCoefficient, float p, float temp, String plantType) {

        allPlants[slotNumber - 1] = new Plant(PlantName, buttonID, slotNumber, harvestPeriod_days, cropCoefficient, p, temp, plantType);
        added = true;
    }

    public void addPlant_SlotNumber(int slotNumber, Plant plant) {
        allPlants[slotNumber - 1] = plant;
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
