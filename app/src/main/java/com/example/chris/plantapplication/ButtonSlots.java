package com.example.chris.plantapplication;

import android.util.Log;

import java.util.ArrayList;

public class ButtonSlots {
    //Creating the slots which will be used to hold the button ID and the string of the current plant growing
    private Pair<Integer, Plant> slot1;
    private Pair<Integer, Plant> slot2;
    private Pair<Integer, Plant> slot3;
    private ArrayList<Pair> allSlots;

    public ButtonSlots() {
        //Slot Declaration
        allSlots = new ArrayList<Pair>();
        slot1 = new Pair<Integer, Plant>(-1, new Plant(""));
        slot2 = new Pair<Integer, Plant>(-1, new Plant(""));
        slot3 = new Pair<Integer, Plant>(-1, new Plant(""));
        allSlots.add(slot1);
        allSlots.add(slot2);
        allSlots.add(slot3);
    }

    public void setPlant(int button, Plant plantInput) {
        for (int i = 0; i < allSlots.size(); i++) {
            Pair<Integer, Plant> slotType = allSlots.get(i);
            int buttonValue = slotType.getFirst();
            if (buttonValue == button) {
                slotType.setSecond(plantInput);
            }
        }

    }

    public boolean buttonExists(int buttonNumber) {
        //We want to make sure that the button does not already exist in the database
        for (int i = 0; i < allSlots.size(); i++) {
            Pair<Integer, Plant> slotType = allSlots.get(i);
            int buttonValue = slotType.getFirst();
            if (buttonNumber == buttonValue) {
                return true;
            }

        }
        return false;

    }

    public void setButtonNumberToSlot(int buttonNumber) {
        for (int i = 0; i < allSlots.size(); i++) {
            Pair<Integer, Plant> slotType = allSlots.get(i);
            int buttonValue = slotType.getFirst();
            if (buttonValue == -1 && buttonExists(buttonNumber) == false) {
                slotType.setFirst(buttonNumber);
                Log.i("Button","The plant has been added to slot ");
                return;
            }
        }
        return;
    }

    public int getSlotNumber(int buttonNumber) {
        for (int i = 0; i < allSlots.size(); i++) {
            Pair<Integer, Plant> slotType = allSlots.get(i);
            int buttonValue = slotType.getFirst();
            if (buttonValue == -1) {
                slotType.setFirst(buttonNumber);
                return i;
            }
        }
        return -1; //Otherwise we will return a 0
    }

    public Plant getPlantType(int buttonNumber) {
        for (int i = 0; i < allSlots.size(); i++) {
            Pair<Integer, Plant> slotType = allSlots.get(i);
            int buttonValue = slotType.getFirst();
            if (buttonNumber == buttonValue) {
                Plant plantValue = slotType.getSecond();
                return plantValue;
            }
        }
        Plant empty = new Plant("");
        //Otherwise we do not return anything
        return empty;
    }
}