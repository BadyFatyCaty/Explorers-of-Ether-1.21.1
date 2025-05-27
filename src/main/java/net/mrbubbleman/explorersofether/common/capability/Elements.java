package net.mrbubbleman.explorersofether.common.capability;

import java.util.*;

public class Elements {
    public String name;
    public int experience;
    public double experienceMultiplier;
    public Elements deviant;

    public Elements() {
        this.name = "";
        this.experience = 0;
        this.experienceMultiplier = 1.0;
        this.deviant = null;
    }

    public Elements(String name) {
        this.name = name;
        this.experience = 0;
        this.experienceMultiplier = 1.0;
        this.deviant = null;
    }

    public Elements(String name, Elements var1, int var2) { // For making Basics
        Random tempRand = new Random();
        this.name = name;
        this.experience = var2;
        double tempDoub = tempRand.nextDouble();
        if (tempDoub >= .9) {
            this.experienceMultiplier = 2;
        } else if (tempDoub >= .8) {
            this.experienceMultiplier = 1.5;
        } else {
            this.experienceMultiplier = 1.0;
        }
        this.deviant = new Elements();

        if (var1 != null) {
            this.deviant.copyFrom(var1);
        } else {
            this.deviant = null;
        }
    }

    public Elements(String name, int experience, double experienceMultiplier) {
        this.name = name;
        this.experience = experience;
        this.experienceMultiplier = experienceMultiplier;
        this.deviant = null;
    }

    public void copyFrom(Elements other) {
        this.name = other.name;
        this.experience = other.experience;
        this.experienceMultiplier = other.experienceMultiplier;

        if (other.deviant == null) {
            this.deviant = null;
        } else {
            this.deviant.copyFrom(other.deviant);
        }
    }

    public static Map<String, Elements> randomizeElements() {
        List<String> allElementNames = new ArrayList<>(List.of("fire", "water", "wind", "earth", "light", "plant",
                                                            "lightning", "ice", "sound", "gravity", "dark", "fungi"));
        HashMap<String, Elements> map = new HashMap<>();
        Random tempRand = new Random();
        double randDoub = tempRand.nextDouble();

        if (randDoub >= .50) { // 1 Element
            map = randomizeStatsHelper(1, allElementNames);
        } else if (randDoub >= .25) { // 2 Element
            map = randomizeStatsHelper(2, allElementNames);
        } else if (randDoub >= .125) { // 3 Element
            map = randomizeStatsHelper(3, allElementNames);
        } else if (randDoub >= .0625) { // 4 Element
            map = randomizeStatsHelper(4, allElementNames);
        } else if (randDoub >= .03125){ // 5 Element
            map = randomizeStatsHelper(5, allElementNames);
        } else { // 6 Element
            map = randomizeStatsHelper(6, allElementNames);
        }

        HashMap<String, Elements> newMap = randomizeNonUnlockedVariables(map, allElementNames);

        return newMap;
    }

    private static HashMap<String, Elements> randomizeStatsHelper(int numberOfElements, List<String> allElementNames) {
        HashMap<String, Elements> map = new HashMap<>();
        List<String> tempNames = new ArrayList<>(allElementNames); // make a copy
        Random tempRand = new Random();

        for (int i = 0; i < numberOfElements; i++) {
            if (tempNames.size() < 2) break; // avoid crash if too few elements left

            int initialHalfSize = tempNames.size()/2;
            // Pick two distinct element names
            int index1 = tempRand.nextInt(initialHalfSize);
            String deviantName = tempNames.remove(index1+initialHalfSize);
            String elementName = tempNames.remove(index1);

            Elements tempDeviant;
            if (tempRand.nextDouble() >= .9) {
                tempDeviant = new Elements(deviantName, null, 1);
            } else {
                tempDeviant = new Elements(deviantName, null, -1);
            }

            Elements tempElement = new Elements(elementName, tempDeviant, 1);

            map.put(tempElement.name, tempElement);
            map.put(tempDeviant.name, tempDeviant);
        }

        return map;
    }

    private static HashMap<String, Elements> randomizeNonUnlockedVariables(HashMap<String, Elements> currentMap, List<String> allElementNames) {
        HashMap<String, Elements> map = currentMap;
        for (int i = 0; i < allElementNames.size()/2; i++) {
            if (! currentMap.containsKey(allElementNames.get(i))) {
                Elements tempDeviant = new Elements(allElementNames.get(i + allElementNames.size()/2), null, -1);
                Elements tempElement = new Elements(allElementNames.get(i), tempDeviant, -1);

                map.put(tempDeviant.name, tempDeviant);
                map.put(tempElement.name, tempElement);
            }
        }
        return map;
    }
}
