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

    public float getWaterAmount_Interrupt(int slotNumber) {
        //This function will be called when there is a timer interrupt to determine how much water a plant should get after a certain amount of time
        Plant currentPlant = allPlants[slotNumber - 1];
        String soilType = currentPlant.getSoilType();
        float humiditySensor = currentPlant.getHumiditySensor();
        float water_plant_remaining = currentPlant.getWater_remaining_current_day();
        SoilStorage soil;
        soil = new SoilStorage();

        float maxWaterAmount = soil.getMaxWaterAmount(currentPlant.getPlantDepth(), soilType); //How much water the plant will need
        float maximumAllowableSoilDepletion = soil.getMaximumAllowableDepletion_moisture_Percentage(soilType);

        if (currentPlant.getPlantType() == GlobalConstants.Predetermined) {
            if (water_plant_remaining > 0 && humidityDeviation(humiditySensor, maximumAllowableSoilDepletion)) { //if there is a need to determine more water r
                //Then we will send the appropriate amount next and decreasee the appropriate amount
                int numberOfRefills = determineNumberRefills(water_plant_remaining, maxWaterAmount);
                //If our waterremaining is bigger than the remaining plant water, then send it
                if (numberOfRefills == 1) {
                    currentPlant.setWater_remaining_current_day(0);//set the water to 0 since we will no longer need to continue any refills
                    return water_plant_remaining;
                } else {
                    currentPlant.setWater_remaining_current_day(water_plant_remaining - maxWaterAmount);
                    return maxWaterAmount;
                }
            } else {
                //minimumAllowableSoilDepltion / 100f is a percentage
                //This is when the soil moisture depletion has reached a value that could correlate to more plant stress.
                if (humidityDeviation(humiditySensor, maximumAllowableSoilDepletion)) {
                    //Then we are safe to water the plant
                    float water_predetermined = currentPlant.calculateWater_PreDetermined(); //How much water the plagiven time nt vase can get at any*/

                    if (water_predetermined > maxWaterAmount) {//Ie we need to do other refills
                        //Then we need to figure out how many refills we will need to satisfy the overlay
                        //Send the first water amount, and after that we will have
                        currentPlant.setWater_remaining_current_day(water_predetermined - maxWaterAmount);
                        return maxWaterAmount; //Return the max water amount since this will be broken up into
                    } else if (water_predetermined <= maxWaterAmount) {
                        //Then we can just send the current watering amount since we will not need any additional water refills senT
                        return water_predetermined;
                    }
                }
            }
        } else if (currentPlant.getPlantType() == GlobalConstants.Manual) {
            float water_Manual = currentPlant.getWaterRequirement_Manual();
            return water_Manual; //We do not need to calculate the water requirements
        }
        return 0f;
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
