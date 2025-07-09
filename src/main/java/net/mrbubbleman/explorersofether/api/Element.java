package net.mrbubbleman.explorersofether.api;

import java.util.*;

public class Element {
    public String name;
    public int xp;

    public static final String[] elementNames = {"fire", "water", "earth", "air"};

    public Element() {
        this.name = "";
        this.xp = -1;
    }

    public Element(String name, int xp) {
        this.name = name;
        this.xp = xp;
    }

    public static Set<Element> randomizeElements() {
        Set<Element> tempSet = new HashSet<>();

        // Create a mutable list of element names
        List<String> mutableNames = new ArrayList<>(Arrays.asList(elementNames));
        Random tempRand = new Random();

        double percent = tempRand.nextDouble();

        int numToSelect;
        if (percent >= 0.85) {        // 15% chance: 3 Elements
            numToSelect = 3;
        } else if (percent >= 0.50) { // 35% chance: 2 Elements
            numToSelect = 2;
        } else {                      // 50% chance: 1 Element
            numToSelect = 1;
        }

        // Select numToSelect elements with value 1
        for (int i = 0; i < numToSelect; i++) {
            int randIndex = tempRand.nextInt(mutableNames.size());
            String name = mutableNames.remove(randIndex);
            tempSet.add(new Element(name, 1));
        }

        // Add remaining elements with value 0
        for (String name : mutableNames) {
            tempSet.add(new Element(name, 0));
        }

        return tempSet;
    }

}
