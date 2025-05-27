package net.badyfatycaty.explorersofether.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record ForgeRecipe(Ingredient inputItem, ItemStack output) implements Recipe<ForgeRecipeInput> {
    // inputItem & output ==> Read From JSON File!
    // GrowthChamberRecipeInput --> INVENTORY of the Block Entity

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(ForgeRecipeInput forgeRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(forgeRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(ForgeRecipeInput forgeRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FORGE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ForgeRecipe> {
        public static final MapCodec<ForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(ForgeRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(ForgeRecipe::output)
        ).apply(inst, ForgeRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ForgeRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, ForgeRecipe::inputItem,
                        ItemStack.STREAM_CODEC, ForgeRecipe::output,
                        ForgeRecipe::new);

        @Override
        public MapCodec<ForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}