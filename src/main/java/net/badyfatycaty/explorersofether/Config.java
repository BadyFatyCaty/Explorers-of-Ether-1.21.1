package net.badyfatycaty.explorersofether;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ExplorersofEther.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Base max enchantments on any item by default
    public static final ModConfigSpec.IntValue BASE_MAX_ENCHANTMENTS = BUILDER
            .comment("Base maximum number of enchantments allowed on any item (before adding extra slots)")
            .defineInRange("baseMaxEnchantments", 3, 0, 100);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int baseMaxEnchantments;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        baseMaxEnchantments = BASE_MAX_ENCHANTMENTS.get();
    }
}