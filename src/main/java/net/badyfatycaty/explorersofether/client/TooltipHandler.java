package net.badyfatycaty.explorersofether.client;

import net.badyfatycaty.explorersofether.Config;
import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.badyfatycaty.explorersofether.components.EnchantmentLimitComponent;
import net.badyfatycaty.explorersofether.components.ModDataComponents;
import net.badyfatycaty.explorersofether.rarity.ModRarity;
import net.bettercombat.BetterCombatMod;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.component.BetterCombatDataComponents;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.tags.ItemTags;

@EventBusSubscriber(modid = ExplorersofEther.MODID, bus = EventBusSubscriber.Bus.GAME, value = {Dist.CLIENT})
public class TooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        // ─── Rarity line ───────────────────────────────────────────────────────
        ModRarity rarity;  // default fallback
        if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/common")))) {
            rarity = ModRarity.COMMON;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/uncommon")))) {
            rarity = ModRarity.UNCOMMON;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/rare")))) {
            rarity = ModRarity.RARE;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/epic")))) {
            rarity = ModRarity.EPIC;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/legendary")))) {
            rarity = ModRarity.LEGENDARY;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/mythic")))) {
            rarity = ModRarity.MYTHIC;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/unique")))) {
            rarity = ModRarity.UNIQUE;
        } else {
            rarity = ModRarity.COMMON;
        }
        // Set the CUSTOM_NAME component to use the rarity color and italics for renamed items
        if (stack.get(net.minecraft.core.component.DataComponents.CUSTOM_NAME) != null) {
            Component originalName = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
            Component recolored = originalName.copy().withStyle(style -> style.withColor(rarity.getFormatting()).withItalic(false));
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, recolored);
        } else {
            Component defaultName = stack.getHoverName().copy().withStyle(style -> style.withColor(rarity.getFormatting()));
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, defaultName);
        }
        List<Component> tooltip = event.getToolTip();
        // The tooltip.set(0, ...) logic is now handled by directly modifying the CUSTOM_NAME component.
        // Add the rarity icon using private-use font
        char icon = (char)('\uE000' + rarity.getId());
        ResourceLocation fontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarities");
        tooltip.add(1, Component.literal("" + icon)
                .setStyle(Style.EMPTY.withFont(fontLocation).withColor(ChatFormatting.WHITE)));
        // ────────────────────────────────────────────────────────────────────────



        // ─── Enchantments ───────────────────────────────────────────────────────
        if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "enchantable/enchantable")))) {
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
                .append(Component.translatable("tooltip.explorers_of_ether.enchantments"))
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
                                    .append(Component.translatable("tooltip.explorers_of_ether.empty_slot"))
                            .withStyle(ChatFormatting.GRAY));
                }
            }

            // Blank line after enchants
            tooltip.add(Component.empty());
        }
        // ────────────────────────────────────────────────────────────────────────
    }
}
