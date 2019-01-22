package com.example.chris.plantapplication;

import java.util.Arrays;

public class plantDataBase<E> {
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

        int empty1 = -1;
        float empty3 = -1;
        Plant empty = new Plant("", -1, -1, 0, "");
        return empty; //otherwise we return empty
    }

    public Plant getPlantBySlot(int slotNumber) {
        if (isSlotExists(slotNumber)) {

            return allPlants[slotNumber - 1];
        } else {
            return null;
        }

    }

    public boolean isSlotExists(int slotNumber) {
        //Check first if the slot number is in range
        if (slotNumber < 1 && slotNumber > 3) {
            return false;
        }
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

    public boolean deviation(float percentageDeviation, float upper, float lower) {
        //This function will return true if the "upper" portion is within a percent deviation of the lower portion
        float diff = Math.abs(upper - lower);
        float percentDiff = 100 * (diff / lower);
        if (percentDiff <= percentageDeviation / 100f) {
            return true;
        } else {
            return false;
        }
    }

    public int determineNumberRefills(float waterRemainding, float maxWaterAmount) {
        int numberOfRefills = 0;
        float totalWaterAmount = 0;

        while (true) {
            //We will need to determine how many times the remaining water will need to go into the max water amount
            if (maxWaterAmount + totalWaterAmount > waterRemainding) {
                numberOfRefills += 1;
                return numberOfRefills;
            } else if (waterRemainding >= totalWaterAmount) {
                numberOfRefills += 1;
            } else {
                return numberOfRefills;
            }
            totalWaterAmount += maxWaterAmount; // We increment based on the maximum amount that we can inpuyt
        }
    }

    public boolean humidityDeviation(float soilBaseHumidity, float maximiumAllowableSoilDepletion) {
        //This function will return true if the soilBaseHumidity falls close to the maximum allowable soil depletion (within the the percent range
        //Or the soilBaseHumidity falls below the maximumAllowableSoilDepletion (Which is not as good)
        if (deviation(GlobalConstants.MAXSOILDEVIATION, soilBaseHumidity / 100f, maximiumAllowableSoilDepletion / 100f)
                || soilBaseHumidity / 100f <= maximiumAllowableSoilDepletion / 100f) {
            return true;
        } else {
            return false;
        }
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
        allPlants = new Plant[GlobalConstants.MAXPLANTS];
        added = false;
    }

    public void addPlant(String PlantName, int buttonID, int slotNumber,
                         int harvestPeriod_days, String plantType) {

        allPlants[slotNumber - 1] = new Plant(PlantName, buttonID, slotNumber, harvestPeriod_days, plantType);
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
