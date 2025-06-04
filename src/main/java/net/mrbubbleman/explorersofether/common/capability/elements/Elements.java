package net.mrbubbleman.explorersofether.common.capability.elements;

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
        if (other.deviant != null) {
            this.deviant = new Elements();
            this.deviant.copyFrom(other.deviant);
        } else {
            this.deviant = null;
        }
    }

    public static HashMap<String, Elements> randomizeElements() {
        HashMap<String, Elements> map;
        Random RANDOM = new Random();
        double rand = RANDOM.nextDouble();

        int elementCount;
        if (rand >= 0.50) elementCount = 1;
        else if (rand >= 0.25) elementCount = 2;
        else if (rand >= 0.125) elementCount = 3;
        else if (rand >= 0.0625) elementCount = 4;
        else if (rand >= 0.03125) elementCount = 5;
        else elementCount = 6;

        map = ElementRandomizer.assignRandomElements(elementCount);
        return map;
    }

    @Override
    public String toString() {
        String str = "Name: " + name + "\tExperience: " + experience + "\tExperience Multi: " + experienceMultiplier;
        if (deviant != null && deviant.name != null) {
            str += "\tDeviant Name: " + deviant.name;
        }
        return str;
    }

    private static class ElementRandomizer {
        private static HashMap<String, Elements> assignRandomElements(int count) {
            Random random = new Random();
            HashMap<String, Elements> map = new HashMap<>();
            List<String> availableBasics = new ArrayList<>(ElementHelper.basicElementNames);
            Collections.shuffle(availableBasics, random);

            ArrayList<Double> multipliers = getExperienceMultipliers(count, random);

            for (int i = 0; i < count && i < availableBasics.size(); i++) {
                String basic = availableBasics.get(i);
                String deviant = ElementHelper.basicToDeviantMap.get(basic);
                double multiplier = multipliers.get(i);

                boolean deviantUnlocked = random.nextDouble() >= 0.8;
                Elements deviantElement = new Elements(deviant, deviantUnlocked ? 1 : -1, multiplier, null);
                Elements basicElement = new Elements(basic, 1, multiplier, deviantElement);

                map.put(basic, basicElement);
                map.put(deviant, deviantElement);
            }

            // Add missing elements with -1 XP
            for (String basic : ElementHelper.basicElementNames) {
                if (!map.containsKey(basic)) {
                    String deviant = ElementHelper.basicToDeviantMap.get(basic);
                    Elements deviantElement = map.getOrDefault(deviant, new Elements(deviant, -1, 1.0, null));
                    Elements basicElement = new Elements(basic, -1, 1.0, deviantElement);
                    map.put(basic, basicElement);
                    map.putIfAbsent(deviant, deviantElement);
                }
            }

            return map;
        }

        private static ArrayList<Double> getExperienceMultipliers(int count, Random random) {
            ArrayList<Double> list = new ArrayList<>();
            double base = 1.0 - count * 0.05;

            for (int i = 0; i < count; i++) {
                if (count == 1) list.add(2.0);
                else if (count <= 3) list.add(1.0);
                else list.add(base);
            }
            return list;
        }
    }
}
