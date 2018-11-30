package com.example.chris.plantapplication;

import android.widget.Button;

import static java.lang.StrictMath.pow;

public class Plant {
    // All data is in SI units.

    private int slotNumber;
    private int buttonNumber;

    private String Name;

    private int currentPlantHeight;
    private int plantHeightDays[];
    private int HarvestDayLength[]; // Array of the days allocated over each individual harvest period
    private double cropCoefficients[][]; //Represents the coefficients for the crop coefficients that will be used to calculate the amount of water the plant will need as a function of the number of days
    private int growthStage;
    private int currentDayNumber;
    private int totalNumberDays;

    private float growth_EachDay[];
    private float RoomTemperature;
    private float airHumidity;

    private double pFactor; //Represents the percentage of sunlight received
    private double MeanTemperature; //As determined from Ed's database


    public Plant(String Name, int buttonID, int slotNumber, int[] harvestPeriod_days, double[][] cropCoefficient, double p, float temperature, int totalNumberDays) {
        this.slotNumber = slotNumber;
        this.Name = Name; //We haven't named it yet
        this.buttonNumber = buttonID;
        this.HarvestDayLength = harvestPeriod_days;
        this.cropCoefficients = cropCoefficient;
        this.pFactor = p;
        this.RoomTemperature = temperature;
        this.totalNumberDays = totalNumberDays;

        this.growth_EachDay = new float[totalNumberDays]; //Declaring a double array to hold the amount of days we have in our
        initializeGrowth_Each_Day();


        this.currentDayNumber = 1; //start the day counter
        growthStage = 1; //automatic default set to one
    }

    private void initializeGrowth_Each_Day() {
        for (int i = 0; i < growth_EachDay.length; i++) {
            growth_EachDay[i] = 0.00f; //We want to initialize all paramaters to zero for the default plant height growth
        }

    }

    public String getName() {
        return Name;
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

    public int pastIndices(int past) {
        int daySum = 0;
        if (past > 0) {
            for (int i = 0; i < past; i++) {
                daySum += HarvestDayLength[i];
            }
            return daySum;

        } else {
            return 0;
        }
    }

    public int getRemainingDaysToHarvest(int dayNumber) {
        int daySum = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {
            daySum += HarvestDayLength[i];
            if (dayNumber <= daySum) {
                return (daySum - dayNumber);
            }
        }
        return 0;
    }

    public void setDayGrowth_Number(int index, float Growth) {
        this.growth_EachDay[index] = Growth;
    }

    public float[] getDayGrowth() {
        return this.growth_EachDay;
    }

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
    }


    public double calculateCrop(double[] coefficients, int day) {
        //Loop through the crop coefficients and using their polynomial coefficients, we can return the day
        double amount = 0;
        for (int i = 0; i < coefficients.length; i++) {
            amount = amount + (coefficients[i] * pow((double) day, i));
        }
        return amount;

    }

    public double evapotransIndex() {
        //The evapotrans index is calculated as, p * (0.46T + 8) where
        //P is the percentage of daylight hours, and T is the mean room temperature
        return pFactor * ((0.46 * MeanTemperature) + 8);
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

    public double getDailyWaterAmount_millimetres(int day) {
        double evapIndex = evapotransIndex();
        int totalHavestDayLengths = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {

            totalHavestDayLengths += HarvestDayLength[i];
            if (day <= totalHavestDayLengths) {
                //With that index, we can use it to get the crop coefficient for that day
                double[] coefficients = cropCoefficients[i];
                double amount = calculateCrop(coefficients, day) * evapIndex;
                return amount;
            }

        }
        int end = HarvestDayLength.length - 1;

        return calculateCrop(cropCoefficients[end], day) * evapotransIndex(); //The end of the coefficient array always occurs to the indefinite day watering amount
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

    public int[] getHarvestDayLength() {
        return HarvestDayLength;
    }

    public void setHarvestDayLength(int[] harvestDayLength) {
        HarvestDayLength = harvestDayLength;
    }

    public double[][] getCropCoefficients() {
        return cropCoefficients;
    }

    public void setCropCoefficients(double[][] cropCoefficients) {
        this.cropCoefficients = cropCoefficients;
    }

    public void setpFactor(double pFactor) {
        this.pFactor = pFactor;
    }

    public void setMeanTemperature(double meanTemperature) {
        MeanTemperature = meanTemperature;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage_DayNumber(int dayNumber) {
        //This function will take in an arbitrary day number and return the correct growth stage
        int daySum = 0;
        for (int i = 0; i < HarvestDayLength.length; i++) {
            daySum += HarvestDayLength[i];
            if (dayNumber <= daySum) {
                this.growthStage = i;
                return;
            }
        }
        this.growthStage = HarvestDayLength.length;
        return;
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

    public int getCurrentDayNumber() {
        return currentDayNumber;
    }

    public void setCurrentDayNumber(int currentDayNumber) {
        this.currentDayNumber = currentDayNumber;
    }
}