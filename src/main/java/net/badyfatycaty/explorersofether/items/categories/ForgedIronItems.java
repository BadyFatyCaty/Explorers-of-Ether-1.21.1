package net.badyfatycaty.explorersofether.items.categories;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.items.ModToolTiers;
import net.badyfatycaty.explorersofether.items.custom.weapon_types.ClaymoreItem;
import net.badyfatycaty.explorersofether.items.custom.weapon_types.ModMaceItem;
import net.badyfatycaty.explorersofether.items.custom.weapon_types.ModShieldItem;
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
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 6.0F, -3.2F))));

    public static final DeferredItem<Item> FORGED_IRON_HOE = ITEMS.register("forged_iron_hoe",
            () -> new HoeItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 0.0F, -3.0F))));

    public static final DeferredItem<Item> FORGED_IRON_MACE = ITEMS.register("forged_iron_mace",
            () -> new ModMaceItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 0.5F, -3.4F))));

    public static final DeferredItem<Item> FORGED_IRON_PICKAXE = ITEMS.register("forged_iron_pickaxe",
            () -> new PickaxeItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 1.0F, -2.8F))));

    public static final DeferredItem<Item> FORGED_IRON_SHOVEL = ITEMS.register("forged_iron_shovel",
            () -> new ShovelItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 1.5F, -3.0F))));

    public static final DeferredItem<Item> FORGED_IRON_SWORD = ITEMS.register("forged_iron_sword",
            () -> new SwordItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 3, -2.4F))));

    public static final DeferredItem<Item> FORGED_IRON_SHIELD = ITEMS.register("forged_iron_shield",
            () -> new ModShieldItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 1.0F, -3.2F))));

    public static final DeferredItem<Item> FORGED_IRON_CLAYMORE = ITEMS.register("forged_iron_claymore",
            () -> new ClaymoreItem(ModToolTiers.FORGED_IRON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.FORGED_IRON, 5.5F, -3.2F))));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
