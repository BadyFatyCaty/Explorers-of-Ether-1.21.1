package net.badyfatycaty.explorersofether.block;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.block.custom.ForgeBlock;
import net.badyfatycaty.explorersofether.items.ModItems;
import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ExplorersofEther.MOD_ID);

    public static final DeferredBlock<Block> COMMON_BLOCK = registerBlock("common_block",
            () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> FORGE = registerBlock("forge",
            () -> new ForgeBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.BASALT)
                    .lightLevel(state -> state.getValue(ForgeBlock.LIT) ? 5 : 0)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}