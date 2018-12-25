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
        //Pair<float, String> soilTypes = new Pair< <"Clay", 200>, <"Silt",>
        this.clay = 200; //The units are mm water/ m soil
        this.silt = 208;
        this.loam = 175;
        this.sand = 83;
        this.peaty = 80;
        this.chalk = 208;

        individualSoil Clay = new individualSoil("Clay", 34f, 200f);
        individualSoil Silt = new individualSoil("Silt", 30f, 208f);
        individualSoil Loam = new individualSoil("Loam", 34f, 200f);
        individualSoil Sand = new individualSoil("Sand", 11f, 83f);
        individualSoil Peaty = new individualSoil("Peaty", 34f, 80f);
        individualSoil Chalk = new individualSoil("Chalk", 208f, 34f);

        /*allSoil[0] = Clay; */
        allSoil = new individualSoil[GlobalConstants.MAXSOILTYPES];
        allSoil[0] = Clay;
        allSoil[1] = Silt;
        allSoil[2] = Loam;
        allSoil[3] = Sand;
        allSoil[4] = Peaty;
        allSoil[5] = Chalk;

        /*allSoil[0] =  new individualSoil("Clay",34f,200f); */
    }

    public float getMinimumAllowableDepletion_moisture_Percentage(String soilType_Input) {
        //This function will take the plant depth and calculate the corrresponding Allowable Soil Water Capacity
        int maxSoilTypes = GlobalConstants.MAXSOILTYPES;
        for (int i = 0; i < maxSoilTypes; i++) {
            String soilType = allSoil[i].getSoilType();
            if (soilType == soilType_Input) {
                return allSoil[i].getMoistureContent();
            }
        }
        return -1f;
    }
    public float getMaxWwaterAmount(float planDepth, String soilType_Input){
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

    public float getClay() {
        return clay;
    }

    public void setClay(float clay) {
        this.clay = clay;
    }

    public float getSilt() {
        return silt;
    }

    public void setSilt(float silt) {
        this.silt = silt;
    }

    public float getChalk() {
        return chalk;
    }

    public void setChalk(float chalk) {
        this.chalk = chalk;
    }

    public float getSand() {
        return sand;
    }

    public void setSand(float sand) {
        this.sand = sand;
    }

    public float getLoam() {
        return loam;
    }

    public void setLoam(float loam) {
        this.loam = loam;
    }

    public float getPeaty() {
        return peaty;
    }

    public void setPeaty(float peaty) {
        this.peaty = peaty;
    }
}
