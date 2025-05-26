package net.mrbubbleman.explorersofether.common.capability.giftclasses;

import java.util.Random;

public class PlayerGifts {
    public boolean isElementalGenius; // Bonus XP for Elemental Magic
    public boolean isMagicalGenius; // Bonus XP for Magic
    public boolean isPhysicalGenius; // Bonus XP for Physical Skill

    public PlayerGifts() {
        Random rand = new Random();

        this.isElementalGenius = rand.nextDouble() >= .95;
        this.isMagicalGenius = rand.nextDouble() >= .95;
        this.isPhysicalGenius = rand.nextDouble() >= .95;
    }
}
