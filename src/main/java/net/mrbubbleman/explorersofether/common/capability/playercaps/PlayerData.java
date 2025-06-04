package net.mrbubbleman.explorersofether.common.capability.playercaps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.mrbubbleman.explorersofether.common.capability.elements.Elements;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerData implements INBTSerializable<CompoundTag> {
    public HashMap<String, Elements> playerElements = Elements.randomizeElements();
    public ArrayList<Character> combo = new ArrayList<>();
    public int spellCharge = 0;



    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();

        CompoundTag playerElements = new CompoundTag();
        Set<String> elementSet = this.playerElements.keySet();
        for (String var1: elementSet) {
            CompoundTag tempElement = new CompoundTag();
            tempElement.putString("name", this.playerElements.get(var1).name);
            tempElement.putInt("experience", this.playerElements.get(var1).experience);
            tempElement.putDouble("experienceMultiplier", this.playerElements.get(var1).experienceMultiplier);
            if (this.playerElements.get(var1) != null) {
                if (this.playerElements.get(var1).deviant != null) {
                    tempElement.putString("deviantName", this.playerElements.get(var1).deviant.name);
                } else {
                    tempElement.putString("deviantName", "null");
                }

            } else {
                tempElement.putString("deviantName", "null");
            }
            playerElements.put(var1, tempElement);
        }
        nbt.put("playerElements", playerElements);

        nbt.putInt("spellCharge", spellCharge);

        CompoundTag combo = new CompoundTag();
        for (int i = 0; i < combo.size(); i++) {
            if (this.combo.get(i) == 'U') {
                // combo.putInt(i+"", 1);
                combo.putString(i+"", "U");
            } else if (this.combo.get(i) == 'D') {
                // combo.putInt(i+"", 2);
                combo.putString(i+"", "D");
            } else if (this.combo.get(i) == 'L') {
                // combo.putInt(i+"", 3);
                combo.putString(i+"", "L");
            } else if (this.combo.get(i) == 'R') {
                // combo.putInt(i+"", 4);
                combo.putString(i+"", "R");
            }
        }
        nbt.put("combo", combo);

        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        // Deserialize playerElements
        if (compoundTag.contains("playerElements")) {
            CompoundTag elementTag = compoundTag.getCompound("playerElements");
            this.playerElements = new HashMap<>();

            // First pass: create Elements without deviants
            for (String key : elementTag.getAllKeys()) {
                CompoundTag tempTag = elementTag.getCompound(key);

                Elements element = new Elements();
                element.name = tempTag.getString("name");
                element.experience = tempTag.getInt("experience");
                element.experienceMultiplier = tempTag.getDouble("experienceMultiplier");

                this.playerElements.put(key, element);
            }

            // Second pass: assign deviants now that all Elements exist
            for (String key : elementTag.getAllKeys()) {
                CompoundTag tempTag = elementTag.getCompound(key);
                String deviantName = tempTag.getString("deviantName");

                if (!deviantName.equals("null") && this.playerElements.containsKey(deviantName)) {
                    this.playerElements.get(key).deviant = this.playerElements.get(deviantName);
                } else {
                    this.playerElements.get(key).deviant = null;
                }
            }
        } else {
            this.playerElements = Elements.randomizeElements();
        }

        // Deserialize combo
        this.combo = new ArrayList<>();
        if (compoundTag.contains("combo")) {
            CompoundTag comboTag = compoundTag.getCompound("combo");

            for (String key : comboTag.getAllKeys()) {
                int index = Integer.parseInt(key);
                String value = comboTag.getString(key);

                if (value.equals("U")) {
                    this.combo.add('U');
                } else if (value.equals("L")) {
                    this.combo.add('L');
                } else if (value.equals("R")) {
                    this.combo.add('R');
                } else if (value.equals("D")) {
                    this.combo.add('D');
                }

                /*
                char direction = switch (value) {
                    case "U" -> 'U';
                    case "D" -> 'D';
                    case "L" -> 'L';
                    case "R" -> 'R';
                    default -> '?';
                };
                if (direction != '?') {
                    while (this.combo.size() <= index) {
                        this.combo.add(' ');
                    }
                    this.combo.set(index, direction);
                }
                 */
            }
        }

        // Deserialize spellCharge
        if (compoundTag.contains("spellCharge")) {
            this.spellCharge = compoundTag.getInt("spellCharge");
        } else {
            this.spellCharge = 0;
        }
    }
}
