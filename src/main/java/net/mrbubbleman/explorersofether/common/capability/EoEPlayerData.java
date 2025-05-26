package net.mrbubbleman.explorersofether.common.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.mrbubbleman.explorersofether.common.capability.elementclasses.ElementData;
import net.mrbubbleman.explorersofether.common.capability.elementclasses.PlayerElements;
import net.mrbubbleman.explorersofether.common.capability.giftclasses.PlayerGifts;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Map;

public class EoEPlayerData implements INBTSerializable<CompoundTag> {
    public PlayerElements playerElements = new PlayerElements();

    public PlayerGifts playerGifts = new PlayerGifts();

    public ArrayList<Character> castingCombo = new ArrayList<>();
    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        // TODO serialize PlayerGifts

        CompoundTag elementTag = new CompoundTag();
        for (ElementData val1: playerElements.getElementMap().values()) {
            CompoundTag tempElement = new CompoundTag();

            tempElement.putInt("experience", val1.experience);
            elementTag.put(val1.name, tempElement);
        }
        tag.put("elements", elementTag);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {

        // TODO de-serialize PlayerGifts

        if (compoundTag.contains("elements")) {
            CompoundTag temp = compoundTag.getCompound("elements");

            if (playerElements == null) {
                playerElements = new PlayerElements(); // safe, no randomizing now
            }

            Map<String, ElementData> map = playerElements.getElementMap();

            for (String element : temp.getAllKeys()) {
                CompoundTag dataTag = temp.getCompound(element);
                int experience = dataTag.getInt("experience");

                ElementData elementData = map.get(element);
                if (elementData != null) {
                    elementData.experience = experience;
                } else {
                    map.put(element, new ElementData(element, experience));
                }
            }

            System.out.println("Syncing player elements:");
            for (String element : temp.getAllKeys()) {
                int experience = temp.getCompound(element).getInt("experience");
                System.out.println("Element: " + element + " | XP: " + experience);
            }

        }

    }
}
