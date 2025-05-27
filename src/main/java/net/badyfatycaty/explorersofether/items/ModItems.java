package net.badyfatycaty.explorersofether.items;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExplorersofEther.MOD_ID);

    public static final DeferredItem<Item> COMMON = ITEMS.register("common",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> UNCOMMON = ITEMS.register("uncommon",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RARE = ITEMS.register("rare",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EPIC = ITEMS.register("epic",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LEGENDARY = ITEMS.register("legendary",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MYTHIC = ITEMS.register("mythic",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> UNIQUE = ITEMS.register("unique",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
