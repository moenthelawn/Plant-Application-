package com.example.chris.plantapplication;

import android.util.Pair;

public class SoilStorage {
    //These are for the available water storage capacity for the plant
    private float clay;
    private float silt;
    private float sand;
    private float chalk;
    private float loam;
    private float peaty;

    private individualSoil[] allSoil;

    public SoilStorage() {

        individualSoil Clay = new individualSoil("Clay", 40f,34f, 20f);
        individualSoil Loam = new individualSoil("Loam", 25f, 17.5f,16f);
        individualSoil Sandy = new individualSoil("Sandy", 15f,11f,8f);
        individualSoil SandyLoam = new individualSoil("Sandy Loam", 18f,14f,18.33f);
        individualSoil ClayLoam = new individualSoil("Clay Loam", 40f,32.5f,18.33f);
        /*allSoil[0] = Clay; */

        allSoil = new individualSoil[GlobalConstants.MAXSOILTYPES];
        allSoil[0] = Clay;
        allSoil[1] = Sandy;
        allSoil[2] = Loam;
        allSoil[3] = SandyLoam;
        allSoil[4] = ClayLoam;

        /*allSoil[0] =  new individualSoil("Clay",34f,200f); */
    }

    public float getMaximumAllowableDepletion_moisture_Percentage(String soilType_Input) {
        //This function will take the plant depth and calculate the corrresponding Allowable Soil Water Capacity
        int maxSoilTypes = GlobalConstants.MAXSOILTYPES;
        for (int i = 0; i < maxSoilTypes; i++) {
            String soilType = allSoil[i].getSoilType();
            if (soilType == soilType_Input) {
                return allSoil[i].getMoistureContent_MAD();
            }
        }
        return -1f;
    }
    public float getMaxWaterAmount(float planDepth, String soilType_Input){
        int maxSoilTypes = GlobalConstants.MAXSOILTYPES;
        for (int i = 0; i < maxSoilTypes; i++) {
            String soilType = allSoil[i].getSoilType();
            if (soilType == soilType_Input) {
                return planDepth * allSoil[i].getStorageWaterCapacity()/2f;
            }
        }
        return -1f;
    }

    /*public float getMaxWaterAmount(float plantDepth, String plantType) {
        //This function will take the plant depth and calculate the corrresponding Allowable Soil Water Capacity
        if (plantType == "Clay") {
            return (plantDepth * this.clay / 2f);
        } else if (plantType == "Silt") {
            return (plantDepth * this.silt / 2f);
        } else if (plantType == "Loam") {
            return (plantDepth * this.loam / 2f);
        } else if (plantType == "Sand") {
            return (plantDepth * this.sand / 2f);
        } else if (plantType == "Peaty") {
            return (plantDepth * this.peaty / 2f);
        } else if (plantType == "Chalk") {
            return (plantDepth * this.chalk / 2f);
        }
        return 0;
    }*/


}
