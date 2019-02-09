package com.example.chris.plantapplication;

import android.provider.Settings;
import android.widget.Button;


import com.firebase.client.Firebase;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    private float maxTemp;
    private float minTemp;

    private int currentDayNumber;
    private int currentPlantHeight;
    private int growthInterval;
    private float waterRequirement_Period;
    private float humiditySensor; //Relative humidity sensor as a percentage
    private int HarvestDayLength; // Array of the days allocated over each individual harvest period

    private ArrayList<Float> growth_EachDay;
    private ArrayList<Float> humiditySensor_harvestPeriod;
    private ArrayList<Float> waterDistribution;

    private float RoomTemperature;
    private float airHumidity;
    private String plantType;
    private float cropCoefficient_SoilEvaporation;
    private String SoilType;

    private float water_remaining_current_day;
    private float pFactor; //Represents the percentage of sunlight received
    private float MeanTemperature; //As determined from Ed's database
    private float plantDepth;
    private float waterRequirement_Predetermined;

    public Plant(String Name, int buttonID, int slotNumber, int harvestPeriod_days, String plantType, float minTemp, float maxTemp) {
        this.slotNumber = slotNumber;
        this.Name = Name; //We haven't named it yet
        this.buttonNumber = buttonID;
        this.HarvestDayLength = harvestPeriod_days;

        this.maxTemp = maxTemp;
        this.minTemp = minTemp;

        this.plantType = plantType;
        this.humiditySensor = 0;
        this.SoilType = ""; //set the soil type to a non value
        this.plantDepth = 0;
        this.startDate = Calendar.getInstance();

        this.growth_EachDay = new ArrayList<>();
        this.humiditySensor_harvestPeriod = new ArrayList<>();
        this.waterDistribution = new ArrayList<>();

        this.water_remaining_current_day = 0;
        currentDayNumber = 1;

    }

    private void initializeGraphs(float graph[]) {
        for (int i = 0; i < graph.length; i++) {
            graph[i] = 0.00f; //We want to initialize all paramaters to zero for the default plant height growth
        }
    }

    public void updateServerDataBase(int type) {
        //Set the dummy credentials
        Firebase mRef;
        if (this.slotNumber == 1) {

            mRef = new Firebase("https://plantsystem-9ff68.firebaseio.com/Plant Vases/Plant Vase 1");
            setChilds(mRef, type);
        } else if (slotNumber == 2) {
            mRef = new Firebase("https://plantsystem-9ff68.firebaseio.com/Plant Vases/Plant Vase 2");
            setChilds(mRef, type);
        } else if (slotNumber == 3) {
            mRef = new Firebase("https://plantsystem-9ff68.firebaseio.com/Plant Vases/Plant Vase 3");
            setChilds(mRef, type);
        } else {
            return;
        }

    }

    public void setChilds(Firebase mRef, int type) {

        mRef.child("Plant Name").setValue(this.Name);

        mRef.child("Plant Type").setValue(this.plantType);
        mRef.child("Button ID").setValue(this.buttonNumber);


        if (type == 1) {
            mRef.child("Soil Type").setValue(this.SoilType);
            mRef.child("Soil Depth").setValue(this.plantDepth);
            mRef.child("Harvest Period").setValue(this.HarvestDayLength);
            mRef.child("Temperature Min").setValue(this.minTemp);
            mRef.child("Temperature Max").setValue(this.maxTemp);
        }
        if (type == 2) {
            mRef.child("Water Period").setValue(this.growthInterval);
            mRef.child("Water Amount").setValue(this.waterRequirement_Period);
        }
    }

    public int getHarvestDayLength() {
        return this.HarvestDayLength;
    }

    public int getCurrentDayNumber() {
        //long msDiff = Calendar.getInstance().getTimeInMillis() - startDate.getTimeInMillis();
        //  long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        //  int difference = (int) daysDiff + 1; //plus one since this indexes from zero
        return currentDayNumber; //The difference between the current date and the start date will give us our current day number
    }

    public int getRemainingDays_Harvest() {
        int remaining = HarvestDayLength - getCurrentDayNumber();
        return remaining;
    }


    public float getCurrentDayGrowth() {
        if (this.growth_EachDay.size() > 0) {
            return this.growth_EachDay.get(this.growth_EachDay.size() - 1);
        }
        return 0;
    }

    public void setCurrentDayNumber(int dayNumber) {
        this.currentDayNumber = dayNumber;
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
        float power = (float) Math.pow((currentPlantHeight / 3f), 0.3f);
        return (-1f * (2f * value0) - (value1 * (currentHumidity - 45f))) * power;

    }

    public float getCurrentHeight_dayNumber(int dayNumber) {
        //This function will return the current height based on the day number that we are in
        if (this.growth_EachDay.size() <= getCurrentDayNumber()) {
            return this.growth_EachDay.get(dayNumber - 1);
        }
        return 0;
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

    public void setDayGrowth_Number(float Growth, int Day) {
        if (this.growth_EachDay.size() <= getCurrentDayNumber()) {
            this.growth_EachDay.set(Day - 1, Growth);
        }
    }

    public ArrayList<Float> getDayGrowth() {
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
        for (int i = 0; i < growth_EachDay.size(); i++) {
            if (growth_EachDay.get(i) == 0) {
                return i;
            }
        }
        return growth_EachDay.size();
    }

    public float[] getWeek_days(int weeks) {
        //This function will iterate through the float array of the different days with their heigh and convert a similiar array to that of weeks
        float weekSum = 0;

        float weeksAverageHeights[] = new float[weeks + 1];
        int daysAvailable = days_Available();
        //

        int j = 0;
        if (weeks != 0) {
            for (int i = 0; i < growth_EachDay.size(); i++) {
                weekSum += growth_EachDay.get(i);
                if (i % 7 == 0) {
                    weeksAverageHeights[j] = weekSum / 7;
                    j += 1;
                    weekSum = 0;
                }
            }
        } else if (weeks == 0) {
            int averageheight = 0;
            for (int i = 0; i < daysAvailable; i++) {
                averageheight += growth_EachDay.get(i);
            }
            weeksAverageHeights[0] = averageheight / 7;
        }
        return weeksAverageHeights; // Return the plant height statistics back to the user
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
        if (humiditySensor_harvestPeriod.size() > 0) {
            return this.humiditySensor_harvestPeriod.get(humiditySensor_harvestPeriod.size() - 1);
        } else {
            return 0;
        }
    }

    public ArrayList<Float> getHumititySensor_harvestPeriod() {
        return humiditySensor_harvestPeriod;
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
        if (humiditySensor_harvestPeriod.size() > 1) {
            return this.humiditySensor_harvestPeriod.get(humiditySensor_harvestPeriod.size() - 2);
        } else {
            return 0;
        }
    }

    public void setHumiditySensor_harvestPeriod_dayNumber(float humiditySensor_Day, int dayNumber) {
        if (dayNumber < getHarvestDayLength()) {
            this.humiditySensor_harvestPeriod.set(dayNumber - 1, humiditySensor_Day);
        }
    }

    public void setWaterDistribution_harvestPeriod_dayNumber(float waterDistribution, int dayNumber) {
        if (dayNumber < getHarvestDayLength()) {
            this.waterDistribution.set(dayNumber - 1, waterDistribution);
        }
    }

    public ArrayList<Float> getWaterDistribution() {
        return this.waterDistribution;
    }

    public void setGrowth_EachDay(ArrayList<Float> growth_EachDay) {
        this.growth_EachDay = growth_EachDay;
    }

    public void setHumiditySensor_harvestPeriod(ArrayList<Float> humiditySensor_harvestPeriod) {
        this.humiditySensor_harvestPeriod = humiditySensor_harvestPeriod;
    }

    public void setWaterDistribution(ArrayList<Float> waterDistribution) {
        this.waterDistribution = waterDistribution;
    }

    public float getMaxTemperatureRange() {
        return maxTemp;
    }

    public void setMaxTemperatureRange(float maxTemperatureRange) {
        this.maxTemp = maxTemperatureRange;
    }

    public float getMinTemperatureRange() {
        return minTemp;
    }

    public void setMinTemperatureRange(float minTemperatureRange) {
        this.minTemp = minTemperatureRange;
    }

    public int getGrowthInterval() {
        return growthInterval;
    }

    public void setGrowthInterval(int hours) {
        //This function converts the inputted hours and days to the corresponding minutes\
        this.growthInterval = hours; //interval is set to hours
        return;
    }

    public float getWaterRequirement_Period() {
        return waterRequirement_Period;
    }

    public void setWaterRequirement_Period(float waterRequirement_Period) {
        this.waterRequirement_Period = waterRequirement_Period;
    }
}