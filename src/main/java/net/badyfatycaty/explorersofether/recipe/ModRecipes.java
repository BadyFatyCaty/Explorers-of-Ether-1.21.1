package net.badyfatycaty.explorersofether.recipe;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExplorersofEther.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ExplorersofEther.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ForgeRecipe>> FORGE_SERIALIZER =
            SERIALIZERS.register("forge", ForgeRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ForgeRecipe>> FORGE_TYPE =
            TYPES.register("forge", () -> new RecipeType<ForgeRecipe>() {
                @Override
                public String toString() {
                    return "forge";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
