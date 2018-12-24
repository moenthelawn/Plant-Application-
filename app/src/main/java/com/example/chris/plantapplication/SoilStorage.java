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

    public SoilStorage() {
        //Pair<float, String> soilTypes = new Pair< <"Clay", 200>, <"Silt",>
        this.clay = 200; //The units are mm water/ m soil
        this.silt = 208;
        this.loam = 175;
        this.sand = 83;
        this.peaty = 80;
        this.chalk = 208;
    }

    public float getMaxWaterAmount(float plantDepth, String plantType) {
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
    }

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
