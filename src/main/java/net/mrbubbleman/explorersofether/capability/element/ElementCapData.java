package net.mrbubbleman.explorersofether.capability.element;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrbubbleman.explorersofether.api.Element;

import java.util.Set;

public class ElementCapData implements IElementCap{
    // private NBTElementData data;
    private Set<Element> elementSet = Element.randomizeElements();
    LivingEntity player;

    public ElementCapData(LivingEntity var1) {
        this.player = (Player) var1;
    }

    public Element getElement(String elementName) {
        for (Element var1: elementSet) {
            if (var1.name.equals(elementName)) {
                return var1;
            }
        }
        return null;
    }
}
