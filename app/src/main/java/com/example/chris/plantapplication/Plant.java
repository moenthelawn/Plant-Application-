package com.example.chris.plantapplication;

import android.provider.Settings;
import android.widget.Button;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.pow;

public class Plant {
    // All data is in SI units.
    private int slotNumber;
    private int buttonNumber;

    private String Name;
    private Calendar startDate;

    private int currentPlantHeight;
    private float previousHumiditySensor;
    private float humiditySensor; //Relative humidity sensor as a percentage
    private int HarvestDayLength; // Array of the days allocated over each individual harvest period
    private float cropCoefficients; //Represents the coefficients for the crop coefficients that will be used to calculate the amount of water the plant will need as a function of the number of days
    private float growth_EachDay[];
    private float RoomTemperature;
    private float airHumidity;
    private String plantType;
    private float cropCoefficient_SoilEvaporation;
    private float[] humiditySensor_harvestPeriod;
    private String SoilType;

    private float water_remaining_current_day;
    private float pFactor; //Represents the percentage of sunlight received
    private float MeanTemperature; //As determined from Ed's database
    private float plantDepth;
    private float waterRequirement_Manual; //This is the amount of manual water that is required for the manual input
    private float waterRequirement_Predetermined;

    public Plant(String Name, int buttonID, int slotNumber, int harvestPeriod_days, float cropCoefficient, float p, float temperature, String plantType) {
        this.slotNumber = slotNumber;
        this.Name = Name; //We haven't named it yet
        this.buttonNumber = buttonID;
        this.HarvestDayLength = harvestPeriod_days;
        this.cropCoefficients = cropCoefficient;
        this.pFactor = p;
        this.plantType = plantType;
        this.previousHumiditySensor = 0;
        this.humiditySensor = 0;
        this.RoomTemperature = temperature;
        this.waterRequirement_Manual = -1; //Default is set to -1 in that we haven't started using it yet unless it is specified as a manual inputted plant
        this.SoilType = ""; //set the soil type to a non value
        this.plantDepth = 0;
        this.startDate = Calendar.getInstance();
        this.growth_EachDay = new float[harvestPeriod_days]; //Declaring a double array to hold the amount of days we have in our
        this.water_remaining_current_day = 0;
        this.humiditySensor_harvestPeriod = new float[harvestPeriod_days];
        initializeGrowth_Each_Day();
        initializeHumidity_Each_Day();
    }

    private void initializeHumidity_Each_Day() {
        for (int i = 0; i < humiditySensor_harvestPeriod.length; i++) {
            humiditySensor_harvestPeriod[i] = 0.00f; //We want to initialize all paramaters to zero for the default plant height growth
        }
    }

    public int getHarvestDayLength() {
        return this.HarvestDayLength;
    }

    public int getCurrentDayNumber() {
        long msDiff = Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        int difference = (int) daysDiff;
        return difference; //The difference between the current date and the start date will give us our current day number
    }

    public int getRemainingDays_Harvest() {
        int remaining = HarvestDayLength - getCurrentDayNumber();
        return remaining;
    }

    private void initializeGrowth_Each_Day() {
        for (int i = 0; i < growth_EachDay.length; i++) {
            growth_EachDay[i] = 0.00f; //We want to initialize all paramaters to zero for the default plant height growth
        }
    }

    public String getName() {
        return Name;
    }

    public float determineKcSoilEvaporation() {
        //This function will return the kc soil evaporation based on
        int currentDayNumber = getCurrentDayNumber();
        float currentPlantHeight = getCurrentHeight_dayNumber(currentDayNumber);
        float currentHumidity = getAirHumidity(); // As a percentage
        float value0 = 0.04f;
        float value1 = 0.004f;
        float power =(float) Math.pow((currentPlantHeight / 3f ),0.3f);
        return (-1f * (2f * value0) - (value1 * (currentHumidity - 45f))) * power;

    }

    public float getCurrentHeight_dayNumber(int dayNumber) {
        //This function will return the current height based on the day number that we are in
        return this.growth_EachDay[dayNumber - 1];
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public int getButtonNumber() {
        return buttonNumber;
    }

    public void setPlantDepth(float depth) {
        this.plantDepth = depth;
    }

//    public int pastIndices(int past) {
//        int daySum = 0;
//        if (past > 0) {
//            for (int i = 0; i < past; i++) {
//                daySum += HarvestDayLength[i];
//            }
//            return daySum;
//
//        } else {
//            return 0;
//        }
//    }
/*
    public int getRemainingDaysToHarvest(int dayNumber) {
        int daySum = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {
            daySum += HarvestDayLength[i];
            if (dayNumber <= daySum) {
                return (daySum - dayNumber);
            }
        }
        return 0;
    }*/

    public void setDayGrowth_Number(int index, float Growth) {
        this.growth_EachDay[index] = Growth;
    }

    public float[] getDayGrowth() {
        return this.growth_EachDay;
    }

/*
    public int getTotalGrowthPeriodDays(int dayNumber) {
        //This function will take in the day number, and find the corresponding harvest period

        int daySum = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {
            daySum += HarvestDayLength[i];
            if (dayNumber <= daySum) {
                //         if (i == 1) {
                //             return HarvestDayLength[i] + HarvestDayLength[0];
                //}
                //else {
                //   return HarvestDayLength[i];
                //}
                return HarvestDayLength[i];

            }
        }
        return 1;
    }*/

/*
    public double calculateCrop(float coefficient, int day) {
        //Loop through the crop coefficients and using their polynomial coefficients, we can return the day
        return

    }*/

    public float evapotransIndex() {
        //The evapotrans index is calculated as, p * (0.46T + 8) where
        //P is the percentage of daylight hours, and T is the mean room temperature
        return pFactor * ((0.46f * MeanTemperature) + 8.00f);
    }

    public void intiliazeWeeks(float weeksAverageHeight[]) {
        if (weeksAverageHeight.length == 0) {
            //Initialize the paramater to 0
            weeksAverageHeight[0] = 0;
        } else {
            for (int i = 0; i < weeksAverageHeight.length; i++) {
                weeksAverageHeight[i] = 0.00f;
            }
        }
    }

    public int days_Available() {
        //This function will go through the days and return the number that is available
        for (int i = 0; i < growth_EachDay.length; i++) {
            if (growth_EachDay[i] == 0) {
                return i;
            }
        }
        return growth_EachDay.length;
    }

    public float[] getWeek_days(int weeks) {
        //This function will iterate through the float array of the different days with their heigh and convert a similiar array to that of weeks
        float weekSum = 0;

        float weeksAverageHeights[] = new float[weeks + 1];
        int daysAvailable = days_Available();
        //

        int j = 0;
        if (weeks != 0) {
            for (int i = 0; i < growth_EachDay.length; i++) {
                weekSum += growth_EachDay[i];
                if (i % 7 == 0) {
                    weeksAverageHeights[j] = weekSum / 7;
                    j += 1;
                    weekSum = 0;
                }
            }
        } else if (weeks == 0) {
            int averageheight = 0;
            for (int i = 0; i < daysAvailable; i++) {
                averageheight += growth_EachDay[i];
            }
            weeksAverageHeights[0] = averageheight / 7;
        }
        return weeksAverageHeights; // Return the plant height statistics back to the user
    }

    public float calculateWater_PreDetermined() {

        float evapIndex = evapotransIndex();

        return (cropCoefficients + this.getCropCoefficient_SoilEvaporation()) * evapIndex;
       /* int totalHavestDayLengths = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {

            totalHavestDayLengths += HarvestDayLength[i];
            if (this.currentDayNumber <= totalHavestDayLengths) {
                //With that index, we can use it to get the crop coefficient for that day
                double[] coefficients = cropCoefficients[i];
                double amount = calculateCrop(coefficients, this.currentDayNumber) * evapIndex;
                return amount;
            }*/

    }
  /*  public double calculatedWater_Calulated(int day){
        //This function will return the corrresponding water calculation based on the
    }*/

    public double MaxWaterAmount() {
        //This function returns the maximum watering amount possible depending on the plant root's depth.
        float availabilityCoefficient = GlobalConstants.AVAILABILITYCOEFFICIENT;
        return availabilityCoefficient;
    }

    public boolean setPlantSlotNumber(int ButtonID, int SlotNumber) { //This function will handle adding the plant to the correct slot number
        if (buttonNumber == -1) {
            buttonNumber = ButtonID;
            slotNumber = SlotNumber;
            return true;
        } else {
            return false;
        }
    }

    public void setpFactor(float pFactor) {
        this.pFactor = pFactor;
    }


    public String getPlantType() {
        return this.plantType;
    }

    public float getRoomTemperature() {
        return RoomTemperature;
    }

    public void setRoomTemperature(float roomTemperature) {
        RoomTemperature = roomTemperature;
    }

    public float getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(float airHumidity) {
        this.airHumidity = airHumidity;
    }


    public float getHumiditySensor() {
        return humiditySensor;
    }
    public float[] getHumititySensor_harvestPeriod(){
        return humiditySensor_harvestPeriod;
    }
    public void setHumiditySensor(float humiditySensor) {
        this.previousHumiditySensor = this.humiditySensor;
        this.humiditySensor = humiditySensor;
    }

    public float getPlantDepth() {
        return plantDepth;
    }

    public String getSoilType() {
        return SoilType;
    }

    public void setSoilType(String soilType) {
        SoilType = soilType;
    }

    public float getWaterRequirement_Manual() {
        return waterRequirement_Manual;
    }

    public void setWaterRequirement_Manual(float waterRequirement_Manual) { //The units for this our [mm]
        this.waterRequirement_Manual = waterRequirement_Manual;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public float getWater_remaining_current_day() {
        return water_remaining_current_day;
    }

    public void setWater_remaining_current_day(float water_remaining_current_day) {
        this.water_remaining_current_day = water_remaining_current_day;
    }

    public void setMeanTemperature(float meanTemperature) {
        MeanTemperature = meanTemperature;
    }

    public int getCurrentPlantHeight() {
        return currentPlantHeight;
    }

    public void setCurrentPlantHeight(int currentPlantHeight) {
        this.currentPlantHeight = currentPlantHeight;
    }

    public float getWaterRequirement_Predetermined() {
        return waterRequirement_Predetermined;
    }

    public void setWaterRequirement_Predetermined(float waterRequirement_Predetermined) {
        this.waterRequirement_Predetermined = waterRequirement_Predetermined;
    }

    public float getCropCoefficient_SoilEvaporation() {
        return cropCoefficient_SoilEvaporation;
    }

    public void setCropCoefficient_SoilEvaporation(float cropCoefficient_SoilEvaporation) {
        this.cropCoefficient_SoilEvaporation = cropCoefficient_SoilEvaporation;
    }

    public float getPreviousHumiditySensor() {
        return previousHumiditySensor;
    }

    public void setPreviousHumiditySensor(float previousHumiditySensor) {
        this.previousHumiditySensor = previousHumiditySensor;
    }

    public void setHumiditySensor_harvestPeriod_dayNumber(float humiditySensor_Day,int dayNumber) {
        this.humiditySensor_harvestPeriod[dayNumber - 1] = humiditySensor_Day;
    }
}