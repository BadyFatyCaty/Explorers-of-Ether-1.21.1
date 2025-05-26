package net.badyfatycaty.explorersofether.client;

import net.badyfatycaty.explorersofether.Config;
import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.components.EnchantmentLimitComponent;
import net.badyfatycaty.explorersofether.components.ModDataComponents;
import net.badyfatycaty.explorersofether.components.ModRarityComponent;
import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber(modid = ExplorersofEther.MODID, bus = EventBusSubscriber.Bus.GAME)
public class TooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        // ─── Rarity line ───────────────────────────────────────────────────────
        ModRarity rarity = stack.getOrDefault(ModRarityComponent.RARITY, ModRarity.COMMON);
        List<Component> tooltip = event.getToolTip();
        if (!tooltip.isEmpty()) {
            // Style the first line with rarity color
            Component first = tooltip.get(0);
            tooltip.set(0, first.copy().withStyle(rarity.getStyleModifier()));
        }
        // Add the rarity icon using private-use font
        char icon = (char)('\uE000' + rarity.getId());
        ResourceLocation fontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarities");
        tooltip.add(1, Component.literal("" + icon)
                .setStyle(Style.EMPTY.withFont(fontLocation).withColor(ChatFormatting.WHITE)));
        // ────────────────────────────────────────────────────────────────────────



        // ─── Enchantments ───────────────────────────────────────────────────────
        if (stack.isDamageableItem()) {
        //  ──── remove vanilla enchant lines ───────────────────────────────────────
        tooltip.removeIf(line -> {
            if (line.getContents() instanceof TranslatableContents tc) {
                String key = tc.getKey();
                return key.startsWith("enchantment.")
                        || key.equals("item.minecraft.enchanted_book");
            }
            return false;
        });

        // ─── Custom Enchantment Tooltip ───────────────────────────────────────────────
            // Blank line after rarity
            tooltip.add(Component.literal(" "));
            int baseMax = Config.baseMaxEnchantments;
            int extraSlots = stack.getOrDefault(ModDataComponents.ENCHANT_LIMIT.get(), new EnchantmentLimitComponent(0)).getExtraSlots();
            int maxEnchants = baseMax + extraSlots;

            // Gather actual enchantments on the item
            Map<Holder<Enchantment>, Integer> enchantMap = new LinkedHashMap<>();
            EnchantmentHelper.runIterationOnItem(stack, enchantMap::put);
            // Enchantments header as a translatable tag
            tooltip.add(Component.literal("☄ ")
                .append(Component.translatable("tooltip.explorersofether.enchantments"))
                    .append(Component.literal(" (" + enchantMap.size() + "/" + maxEnchants + "):      ")
                        .withStyle(ChatFormatting.GRAY))
                .withStyle(ChatFormatting.WHITE));

            // Actual enchantments and empty placeholders
            List<Map.Entry<Holder<Enchantment>, Integer>> entries = new ArrayList<>(enchantMap.entrySet());

            for (int i = 0; i < maxEnchants; i++) {
                if (i < entries.size()) {
                    Map.Entry<Holder<Enchantment>, Integer> e = entries.get(i);
                    Component name = Enchantment.getFullname(e.getKey(), e.getValue());
                    // Filled slot: bright green names
                    tooltip.add(Component.literal("  ◆ ").append(name)
                            .withStyle(ChatFormatting.GREEN));
                } else {
                    // Empty slot placeholder
                    tooltip.add(Component.literal("  ◇ ")
                                    .append(Component.translatable("tooltip.explorersofether.empty_slot"))
                            .withStyle(ChatFormatting.GRAY));
                }
            }

            // Blank line after enchants
            tooltip.add(Component.empty());
        }
        // ────────────────────────────────────────────────────────────────────────
    }
}
