package net.badyfatycaty.explorersofether.items;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExplorersofEther.MOD_ID);

    public static final Supplier<CreativeModeTab> EOE_FUNCTIONAL_BLOCKS_TAB = CREATIVE_MODE_TAB.register("explorers_of_ether_functional_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.FORGE))
                    .title(Component.translatable("creativetab.explorers_of_ether.explorers_of_ether_functional_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.FORGE);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}