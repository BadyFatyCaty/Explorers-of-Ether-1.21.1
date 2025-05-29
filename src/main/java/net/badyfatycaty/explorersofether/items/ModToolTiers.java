package net.badyfatycaty.explorersofether.items;

import net.badyfatycaty.explorersofether.items.categories.ForgedIronItems;
import net.minecraft.world.item.Tiers;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ModToolTiers {

    public static final Tier FORGED_IRON = new Tier() {
        @Override
        public int getUses() {
            return 900; // durability between iron and diamond
        }

        @Override
        public float getSpeed() {
            return 7.0F; // mining speed between iron and diamond
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.5F; // attack bonus between iron and diamond
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return Tiers.DIAMOND.getIncorrectBlocksForDrops(); // match diamond mining level
        }

        @Override
        public int getEnchantmentValue() {
            return 12; // enchantability between iron and diamond
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ForgedIronItems.FORGED_IRON_INGOT.get());
        }
    };
}