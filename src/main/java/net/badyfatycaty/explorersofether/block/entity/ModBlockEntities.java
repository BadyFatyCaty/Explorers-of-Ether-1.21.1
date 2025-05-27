package net.badyfatycaty.explorersofether.block.entity;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExplorersofEther.MOD_ID);

    public static final Supplier<BlockEntityType<ForgeBlockEntity>> FORGE_BE =
            BLOCK_ENTITIES.register("forge_be", () -> BlockEntityType.Builder.of(
                    ForgeBlockEntity::new, ModBlocks.FORGE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}