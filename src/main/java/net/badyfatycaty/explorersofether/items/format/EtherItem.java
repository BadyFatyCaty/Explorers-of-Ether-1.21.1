package net.badyfatycaty.explorersofether.items.format;

import net.badyfatycaty.explorersofether.components.ModRarityComponent;
import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;

public class EtherItem extends Item {
    private static ModRarity rarity = ModRarity.COMMON;

    public EtherItem(Properties properties, ModRarity rarity) {
        super(properties);
        EtherItem.rarity = rarity;
    }

    public EtherItem(Properties properties) {
        super(properties);
        EtherItem.rarity = ModRarity.COMMON;
    }

    @Override
    public DataComponentMap components() {
        return DataComponentMap.builder()
            .addAll(super.components())
            .set(ModRarityComponent.RARITY, rarity)
            .build();
    }
}