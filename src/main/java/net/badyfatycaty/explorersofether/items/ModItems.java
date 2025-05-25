package net.badyfatycaty.explorersofether.items;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.items.format.EtherItem;
import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExplorersofEther.MOD_ID);

    public static final DeferredItem<Item> COMMON = ITEMS.register("common",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.COMMON));

    public static final DeferredItem<Item> UNCOMMON = ITEMS.register("uncommon",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.UNCOMMON));

    public static final DeferredItem<Item> RARE = ITEMS.register("rare",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.RARE));

    public static final DeferredItem<Item> EPIC = ITEMS.register("epic",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.EPIC));

    public static final DeferredItem<Item> LEGENDARY = ITEMS.register("legendary",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.LEGENDARY));

    public static final DeferredItem<Item> MYTHIC = ITEMS.register("mythic",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.MYTHIC));

    public static final DeferredItem<Item> UNIQUE = ITEMS.register("unique",
            () -> new EtherItem(new EtherItem.Properties(), ModRarity.UNIQUE));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
