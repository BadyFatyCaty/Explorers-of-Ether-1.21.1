package net.mrbubbleman.explorersofether.common.capability.elementclasses;

import java.util.*;
import java.util.stream.Stream;

public class PlayerElements {
    private static final Map<String, String> elementDeviants = Map.of(
            "fire", "lightning",
            "water", "ice",
            "earth", "gravity",
            "wind", "sound",
            "light", "dark",
            "plant", "fungi"
    );

    private Map<String, ElementData> playerElements;

    public PlayerElements() {
        this.playerElements = new HashMap<>();
        initializeDefaultElements(); // ensures map is fully populated
    }


    public void initializeDefaultElements() {
        randomizeElements();
    }

    public Map<String, ElementData> getElementMap() {
        return playerElements;
    }

    private void randomizeElements() {
        Random random = new Random();

        // Combine base and deviant elements into one set to avoid duplicates
        List<String> allElements = Stream.concat(
                elementDeviants.keySet().stream(),
                elementDeviants.values().stream()
        ).distinct().toList();

        // Shuffle to randomize selection
        List<String> shuffled = new ArrayList<>(allElements);
        Collections.shuffle(shuffled, random);

        // Determine how many to unlock
        double randDoub = random.nextDouble();
        int unlockCount;
        if (randDoub >= 0.50) unlockCount = 1;
        else if (randDoub >= 0.25) unlockCount = 2;
        else if (randDoub >= 0.125) unlockCount = 3;
        else if (randDoub >= 0.0625) unlockCount = 4;
        else unlockCount = 5;

        Set<String> unlocked = new HashSet<>(shuffled.subList(0, unlockCount));

        // Initialize the playerElements map
        for (String element : allElements) {
            int experience = unlocked.contains(element) ? 1 : 0;

            // Check for deviant
            String deviantName = elementDeviants.get(element);
            ElementData deviant = null;
            if (deviantName != null) {
                deviant = new ElementData(deviantName, 0); // Deviant always starts locked
            }

            playerElements.put(element, new ElementData(element, experience, deviant));
        }
    }

    private void unlockAchivements() {
        for (ElementData val1: playerElements.values()) {
            if (val1.experience > 0) {
                // Sends the Method to unlock the Achievment
            }
        }
    }
}
