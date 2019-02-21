package com.example.chris.plantapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        Plant empty = new Plant("", -1, "");
        return empty; //otherwise we return empty
    }

    public boolean isPlant(String plantName) {
        //This function will loop through and see if that particular plant is growing
        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i] != null) {
                String plantType = allPlants[i].getName();
                if (plantType.equals(plantName)) {
                    return true;
                }
            }
        }
        return false;
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
/*

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
*/

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

    private ArrayList<Pair<Float, Float>> getPlants_Range() {
        ArrayList<Pair<Float, Float>> tempRange = new ArrayList<>();
        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i] != null) {
                Plant currentPlant = allPlants[i];
                String plantType = currentPlant.getPlantType();
                if (plantType.equals("Predetermined")) {
                    tempRange.add(new Pair<Float, Float>(currentPlant.getMinTemperatureRange(), currentPlant.getMaxTemperatureRange()));
                }
            }
        }
        return tempRange;
    }

    private Pair<Float, Float> getOverlappingRange(ArrayList<Pair<Float, Float>> tempRange) {
        //This function will loop through the temperature range and get the overlap between all the temperature ranges
        //And then return as a pair in the form <MinTemp,MaxTemp>
        Pair<Float, Float> currentRange = new Pair(-1000000000f, 1000000000f);

        for (int i = 0; i < tempRange.size(); i++) {
            if (i == 0 && i < tempRange.size() - 1) {
                float x1 = tempRange.get(i).getFirst();
                float x2 = tempRange.get(i).getSecond();

                float y1 = tempRange.get(i + 1).getFirst();
                float y2 = tempRange.get(i + 1).getSecond();
                i += 1;
                if (Float.max(x1, y1) <= Float.min(x2, y2)) {
                    //Then there is an overlap in the range
                    float e = Math.max(x1, y1);
                    float f = Math.min(x2, y2);
                    currentRange.setFirst(Float.min(e, f));
                    currentRange.setSecond(Float.max(e, f));
                }
            } else {
                //We are at the last index
                float x1 = currentRange.getFirst();
                float x2 = currentRange.getSecond();

                float y1 = tempRange.get(i).getFirst();
                float y2 = tempRange.get(i).getSecond();

                if (x1 <= y2 && y1 <= x2) {
                    //Then there is an overlap in the range
                    float e = Math.max(x1, y1);
                    float f = Math.min(x2, y2);
                    currentRange.setFirst(e);
                    currentRange.setSecond(f);
                }


            }
        }
        return currentRange;
    }

    public float plantOptimalTemperatureChange() {
        ArrayList<Pair<Float, Float>> tempRange = getPlants_Range(); //This function will get the current plants growing and return the list of their respective ranges
        Pair<Float, Float> overlappingRange = getOverlappingRange(tempRange);

        for (int i = 0; i < allPlants.length; i++) {
            if (allPlants[i] != null && allPlants[i].getPlantType().equals("Predetermined")) {
                float currentTemperature = allPlants[i].getRoomTemperature();
                float minTemperature = allPlants[i].getMinTemperatureRange();
                float maxTemperature = allPlants[i].getMaxTemperatureRange();

                if (currentTemperature > maxTemperature) {
                    //Then we will get the range to the
                    return (overlappingRange.getSecond() - currentTemperature);
                }
                else if (overlappingRange.getFirst() == -1e9f || overlappingRange.getSecond() == 1e9f){
                    return 0;
                }
                else if (currentTemperature < minTemperature) {
                    return overlappingRange.getFirst() - currentTemperature;
                }

            }
        }
        return 0f;
 /*       //PlantParamaters
        for (int i = 0; i < allPlants.length; i++) {
            if (isOverlap) {
                //Find the amount of temperature overlap
                float currentTempMin = allPlants[i].getMinTemperatureRange();
                float currentPlantTemperature = allPlants[i].getRoomTemperature();
                if (current)
            }

        }
        return true;*/
    }

    private plantDataBase() {
        //we want to add a Basil Plant
        allPlants = new Plant[GlobalConstants.MAXPLANTS];
        added = false;
    }

    public void addPlant(String PlantName, int buttonID, int slotNumber,
                         int harvestPeriod_days, String plantType) {

        allPlants[slotNumber - 1] = new Plant(PlantName, slotNumber, plantType);
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
