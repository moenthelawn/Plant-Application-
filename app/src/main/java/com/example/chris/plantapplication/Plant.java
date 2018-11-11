package com.example.chris.plantapplication;

import static java.lang.StrictMath.pow;

public class Plant {
    // All data is in SI units.

    private int slotNumber[];


    private String Name;

    private int currentPlantHeight;
    private int plantHeightDays[];
    private int HarvestDayLength[]; // Array of the days allocated over each individual harvest period
    private double cropCoefficients[][]; //Represents the coefficients for the crop coefficients that will be used to calculate the amount of water the plant will need as a function of the number of days
    private int growthStage;


    private double pFactor; //Represents the percentage of sunlight received
    private double MeanTemperature; //As determined from Ed's database

    public Plant(String Name) {
        this.slotNumber = new int[]{-1, -1, -1};
        this.Name = Name; //We haven't named it yet
        growthStage = 1;
    }
    public String getName() {
        return Name;
    }


    public void setName(String name) {
        Name = name;
    }

    public int[] getSlotNumber() {
        return slotNumber;
    }
    public boolean slotExists(int number){
        for (int i = 0; i < slotNumber.length;i++){
            if (slotNumber[i] == number){
                return true;

            }

        }
        return false;
    }

    public double calculateCrop(double[] coefficients, int day){
        //Loop through the crop coefficients and using their polynomial coefficients, we can return the day
        double amount = 0;
        for (int i = 0; i < coefficients.length;i++){
            amount = amount + (coefficients[i] * pow( (double)day,i));
        }
        return amount;

    }
    public double evapotransIndex(){
        //The evapotrans index is calculated as, p * (0.46T + 8) where
        //P is the percentage of daylight hours, and T is the mean room temperature
        return pFactor * ((0.46 * MeanTemperature) + 8);
    }

    public double getDailyWaterAmount_millimetres(int day){
        double evapIndex = evapotransIndex();
        int totalHavestDayLengths = 0;
        for(int i =0; i < HarvestDayLength.length;i++){

            totalHavestDayLengths += HarvestDayLength[i];
            if (day <= totalHavestDayLengths){
                //With that index, we can use it to get the crop coefficient for that day
                double[] coefficients = cropCoefficients[i];
               double amount = calculateCrop(coefficients,day) * evapIndex;
               return amount;
            }

        }
        int end = HarvestDayLength.length - 1;

        return calculateCrop(cropCoefficients[end],day) * evapotransIndex(); //The end of the coefficient array always occurs to the indefinite day watering amount
    }

    public boolean setPlantSlotNumber(int number) { //This function will handle adding the plant to the correct slot number
        for (int i = 0; i < slotNumber.length; i++) {
            if (slotNumber[i] == -1 && slotExists(number) == false) {
                //Then we append that to the database
                slotNumber[i] = number;

                return true;
            }
        }
        return false;
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

    public void setGrowthStage(int growthStage) {
        this.growthStage = growthStage;
    }
}