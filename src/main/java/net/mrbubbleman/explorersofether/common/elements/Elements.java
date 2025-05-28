package net.mrbubbleman.explorersofether.common.elements;

import it.unimi.dsi.fastutil.Hash;

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

    public Elements(String name, int experience, double experienceMultiplier, Elements deviant) {
        this.name = name;
        this.experience = experience;
        this.experienceMultiplier = experienceMultiplier;
        this.deviant = deviant;
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

    public static HashMap<String, Elements> randomizeElements() {
        List<String> allElementNames = new ArrayList<>(List.of("fire", "water", "wind", "earth", "light", "plant",
                "lightning", "ice", "sound", "gravity", "dark", "fungi"));
        HashMap<String, Elements> map = new HashMap<>();
        Random RANDOM = new Random();
        double randDoub = RANDOM.nextDouble();

        if (randDoub >= .50) { // 1 Element
            map = ElementRandomizer.assignRandomElements(1, allElementNames);
        } else if (randDoub >= .25) { // 2 Element
            map = ElementRandomizer.assignRandomElements(2, allElementNames);
        } else if (randDoub >= .125) { // 3 Element
            map = ElementRandomizer.assignRandomElements(3, allElementNames);
        } else if (randDoub >= .0625) { // 4 Element
            map = ElementRandomizer.assignRandomElements(4, allElementNames);
        } else if (randDoub >= .03125){ // 5 Element
            map = ElementRandomizer.assignRandomElements(5, allElementNames);
        } else { // 6 Element
            map = ElementRandomizer.assignRandomElements(6, allElementNames);
        }

        return map;
    }

    public String toString() {
        String temp = "";
        temp += "Name: " + name + "\tExperience: " + experience + "\tExperience Multi: " + experienceMultiplier;
        if (deviant != null && deviant.name != null) {
            temp += "\tDeviant Name: " + deviant.name;
        }
        return temp;
    }

    private static class ElementRandomizer {
        private static HashMap<String, Elements> assignRandomElements(int numberOfElements, List<String> allElementNames) {
            HashMap<String, Elements> map = randomizeVariables(numberOfElements, allElementNames);
            map = randomizeRestOfElements(map, allElementNames);

            return map;
        }

        private static HashMap<String, Elements> randomizeVariables(int numberOfElements, List<String> allElementNames) {
            HashMap<String, Elements> map = new HashMap<>();
            Random RANDOM = new Random();
            List<String> tempAllElementnames = new ArrayList<>(allElementNames);

            ArrayList<Double> expMulti = randomizeExperienceMultiplier(numberOfElements, RANDOM); // Gets the experience for all the elemnts
            int randInt = 0;
            for (int i = 0; i < numberOfElements; i++) {
                int initialAvailableElements = tempAllElementnames.size();
                randInt = RANDOM.nextInt(initialAvailableElements/2);

                Elements deviantElement;
                if (RANDOM.nextDouble() >= .80) {
                    // Access to Deviants
                    deviantElement = new Elements(tempAllElementnames.get(randInt+ initialAvailableElements/2), 1, expMulti.get(i), null);
                } else {
                    deviantElement = new Elements(tempAllElementnames.get(randInt+ initialAvailableElements/2), -1, expMulti.get(i), null);
                }

                Elements basicElement = new Elements(tempAllElementnames.get(randInt), 1, expMulti.get(i), deviantElement);

                map.put(tempAllElementnames.get(randInt), basicElement);
                map.put(tempAllElementnames.get(randInt+ initialAvailableElements/2), deviantElement);

                tempAllElementnames.remove(randInt + initialAvailableElements / 2);
                tempAllElementnames.remove(randInt);
            }

            return map;
        }

        /**
         * @param numberOfElements
         * @param RANDOM
         * @return
         */
        private static ArrayList<Double> randomizeExperienceMultiplier(int numberOfElements, Random RANDOM) {
            ArrayList<Double> ar = new ArrayList<>();
            // double randDoub = RANDOM.nextDouble();

            /*
            // More Random
            for (int i = 0; i < numberOfElements; i++) {
                ar.add(1.0 - numberOfElements * 0.05 + RANDOM.nextDouble() * 0.2); // Add random variance
            }
             */

            if (numberOfElements == 6) {
                for (int i = 0; i < 6; i++) {
                    ar.add(1.0 - numberOfElements * .05);
                }
            } else if (numberOfElements == 5) {
                for (int i = 0; i < 5; i++) {
                    ar.add(1.0 - numberOfElements * .05);
                }
            } else if (numberOfElements == 4) {
                for (int i = 0; i < 4; i++) {
                    ar.add(1.0 - numberOfElements * .05);
                }
            } else if (numberOfElements == 3) {
                ar.add(1.0);
                ar.add(1.0);
                ar.add(1.0);
            } else if (numberOfElements == 2) {
                ar.add(1.0);
                ar.add(1.0);
            } else if (numberOfElements == 1) {
                ar.add(2.0);
            }

            /*
            // Only 1 for testing purposes
            for (int i = 0; i < numberOfElements; i++) {
                ar.add(1.0);
            }
             */

            return ar;
        }

        private static HashMap<String, Elements> randomizeRestOfElements(HashMap<String, Elements> map, List<String> allElementNames) {
            HashMap<String, Elements> tempMap = map;
            for (int i = 0; i < allElementNames.size()/2; i++) {
                String var1 = allElementNames.get(i);
                String var2 = allElementNames.get(i + allElementNames.size()/2);
                if (! tempMap.containsKey(var1)) {
                    if (! tempMap.containsKey(var2)) {
                        Elements deviantElement = new Elements(var2, -1, 1, null);
                        Elements basicElement = new Elements(var1, -1, 1, deviantElement);
                        tempMap.put(var1, basicElement);
                        tempMap.put(var2, deviantElement);
                    } else {
                        Elements basicElement = new Elements(var1, -1, 1, tempMap.get(var2));
                        tempMap.put(var1, basicElement);
                    }
                }
            }

            return tempMap;
        }
    }
}
