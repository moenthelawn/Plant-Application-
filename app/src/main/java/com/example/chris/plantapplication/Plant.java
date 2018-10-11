package com.example.chris.plantapplication;

public class Plant {
    private int slotNumber[];
    private String Name;

    public Plant(String Name) {
        this.slotNumber = new int[]{-1, -1, -1};
        this.Name = Name; //We haven't name it yet
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
}