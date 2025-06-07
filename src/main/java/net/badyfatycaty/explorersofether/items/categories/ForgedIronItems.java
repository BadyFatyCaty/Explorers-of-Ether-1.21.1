package net.badyfatycaty.explorersofether.items.categories;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.items.ModToolTiers;
import net.badyfatycaty.explorersofether.items.custom.weapon_types.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ForgedIronItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExplorersofEther.MOD_ID);

    public static final DeferredItem<Item> FORGED_IRON_NUGGET = ITEMS.register("forged_iron_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FORGED_IRON_INGOT = ITEMS.register("forged_iron_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FORGED_IRON_SHEET = ITEMS.register("forged_iron_sheet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FORGED_IRON_STICK = ITEMS.register("forged_iron_stick",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FORGED_IRON_STRING = ITEMS.register("forged_iron_string",
            () -> new Item(new Item.Properties()));



    public static final DeferredItem<Item> FORGED_IRON_AXE = ITEMS.register("forged_iron_axe",
            () -> new AxeItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.FORGED_IRON, 6.0F, -3.2F))));

    public static final DeferredItem<Item> FORGED_IRON_HOE = ITEMS.register("forged_iron_hoe",
            () -> new HoeItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.FORGED_IRON, 0.0F, -3.0F))));

    public static final DeferredItem<Item> FORGED_IRON_MACE = ITEMS.register("forged_iron_mace",
            () -> new ModMaceItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ModMaceItem.createAttributes(ModToolTiers.FORGED_IRON, 0.5F, -3.4F))));

    public static final DeferredItem<Item> FORGED_IRON_PICKAXE = ITEMS.register("forged_iron_pickaxe",
            () -> new PickaxeItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.FORGED_IRON, 1.0F, -2.8F))));

    public static final DeferredItem<Item> FORGED_IRON_SHOVEL = ITEMS.register("forged_iron_shovel",
            () -> new ShovelItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.FORGED_IRON, 1.5F, -3.0F))));

    public static final DeferredItem<Item> FORGED_IRON_SWORD = ITEMS.register("forged_iron_sword",
            () -> new SwordItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 3, -2.4F))));

    public static final DeferredItem<Item> FORGED_IRON_SHIELD = ITEMS.register("forged_iron_shield",
            () -> new ModShieldItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ModShieldItem.createAttributes(ModToolTiers.FORGED_IRON, 1.0F, -3.2F))));

    public static final DeferredItem<Item> FORGED_IRON_CLAYMORE = ITEMS.register("forged_iron_claymore",
            () -> new ClaymoreItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ClaymoreItem.createAttributes(ModToolTiers.FORGED_IRON, 5.5F, -3.2F))));

    public static final DeferredItem<Item> FORGED_IRON_DAGGER = ITEMS.register("forged_iron_dagger",
            () -> new DaggerItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(ModToolTiers.FORGED_IRON, 2, -2.2F))));

    public static final DeferredItem<Item> FORGED_IRON_BOW = ITEMS.register("forged_iron_bow",
            () -> new ModBowItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ModBowItem.createAttributes(ModToolTiers.FORGED_IRON, 1F, 1F, -3F))));

    public static final DeferredItem<Item> FORGED_IRON_CROSSBOW = ITEMS.register("forged_iron_crossbow",
            () -> new ModCrossbowItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(ModCrossbowItem.createAttributes(ModToolTiers.FORGED_IRON, 1.2F, 1.1F, -3F))));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
