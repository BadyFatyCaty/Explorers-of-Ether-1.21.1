package net.badyfatycaty.explorersofether.client;

import net.minecraft.client.Minecraft;
import net.bettercombat.logic.EntityAttributeHelper;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.client.WeaponAttributeTooltip;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
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
    public static ChatFormatting PRIMARY_COLOR = ChatFormatting.WHITE;
    public static ChatFormatting SECONDARY_COLOR = ChatFormatting.GRAY;

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        // ─── Rarity line ───────────────────────────────────────────────────────
        ModRarity rarity;  // default fallback
        if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/common")))) {
            rarity = ModRarity.COMMON;
            PRIMARY_COLOR = ChatFormatting.WHITE;
            SECONDARY_COLOR = ChatFormatting.GRAY;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/uncommon")))) {
            rarity = ModRarity.UNCOMMON;
            PRIMARY_COLOR = ChatFormatting.GREEN;
            SECONDARY_COLOR = ChatFormatting.DARK_GREEN;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/rare")))) {
            rarity = ModRarity.RARE;
            PRIMARY_COLOR = ChatFormatting.AQUA;
            SECONDARY_COLOR = ChatFormatting.DARK_AQUA;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/epic")))) {
            rarity = ModRarity.EPIC;
            PRIMARY_COLOR = ChatFormatting.LIGHT_PURPLE;
            SECONDARY_COLOR = ChatFormatting.DARK_PURPLE;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/legendary")))) {
            rarity = ModRarity.LEGENDARY;
            PRIMARY_COLOR = ChatFormatting.YELLOW;
            SECONDARY_COLOR = ChatFormatting.GOLD;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/mythic")))) {
            rarity = ModRarity.MYTHIC;
            PRIMARY_COLOR = ChatFormatting.RED;
            SECONDARY_COLOR = ChatFormatting.DARK_RED;
        } else if (stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarity/unique")))) {
            rarity = ModRarity.UNIQUE;
            PRIMARY_COLOR = ChatFormatting.GOLD;
            SECONDARY_COLOR = ChatFormatting.DARK_RED;
        } else {
            rarity = ModRarity.COMMON;
            PRIMARY_COLOR = ChatFormatting.WHITE;
            SECONDARY_COLOR = ChatFormatting.GRAY;
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
        ResourceLocation raritiesFontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "rarities");
        tooltip.add(1, Component.literal("" + icon)
                .setStyle(Style.EMPTY.withFont(raritiesFontLocation).withColor(ChatFormatting.WHITE)));
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
                .append(Component.literal(" (" + enchantMap.size() + "/" + maxEnchants + ")")
                        .withStyle(SECONDARY_COLOR))
                .append(Component.literal(":      "))
                .withStyle(PRIMARY_COLOR));

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
                            .withStyle(SECONDARY_COLOR));
                }
            }
            // Blank line after enchants
            tooltip.add(Component.empty());
        }
        // ─── Attribute Modifiers Section ───────────────────────────────────────────────
        // ─── Remove Vanilla Attribute Lines ─────────────────────────────────────
        tooltip.removeIf(line -> {
            String raw = line.getString().toLowerCase();
            return raw.contains("when equipped")
                    || raw.contains("slot:")
                    || raw.contains("armor")
                    || raw.contains("armor toughness")
                    || raw.contains("attack damage")
                    || raw.contains("attack knockback")
                    || raw.contains("attack speed")
                    || raw.contains("block break speed")
                    || raw.contains("block interaction range")
                    || raw.contains("burning time")
                    || raw.contains("entity interaction range")
                    || raw.contains("explosion knockback resistance")
                    || raw.contains("fall damage multiplier")
                    || raw.contains("flying speed")
                    || raw.contains("follow range")
                    || raw.contains("gravity")
                    || raw.contains("jump strength")
                    || raw.contains("knockback resistance")
                    || raw.contains("luck")
                    || raw.contains("max absorption")
                    || raw.contains("max health")
                    || raw.contains("mining efficiency")
                    || raw.contains("movement efficiency")
                    || raw.contains("movement speed")
                    || raw.contains("oxygen bonus")
                    || raw.contains("safe fall distance")
                    || raw.contains("scale")
                    || raw.contains("spawn reinforcements")
                    || raw.contains("sneaking speed")
                    || raw.contains("step height")
                    || raw.contains("submerged mining speed")
                    || raw.contains("sweeping damage ratio")
                    || raw.contains("tempt range")
                    || raw.contains("water movement efficiency")
                    || raw.contains("projectile damage")
                    || raw.contains("draw speed")
                    ;
        });

        // Remove Better Combat attack range line (dark green formatted with attack_range key)
        tooltip.removeIf(line -> {
            String raw = line.getString().toLowerCase();
            var color = line.getStyle().getColor();
            return raw.contains("attack range")
                    && color != null
                    && color.getValue() == ChatFormatting.DARK_GREEN.getColor();
        });

        // ─── Custom Attribute Tooltip ─────────────────────────────────────
        net.minecraft.world.item.component.ItemAttributeModifiers attributeModifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, net.minecraft.world.item.component.ItemAttributeModifiers.EMPTY);
        if (attributeModifiers.modifiers().isEmpty() && stack.getItem() instanceof net.minecraft.world.item.ArmorItem armorItem) {
            attributeModifiers = armorItem.getDefaultAttributeModifiers();
        }
        if (!attributeModifiers.modifiers().isEmpty()) {
            // Group attribute modifiers by EquipmentSlotGroup
            Map<EquipmentSlotGroup, List<net.minecraft.world.item.component.ItemAttributeModifiers.Entry>> modifiersBySlot = new LinkedHashMap<>();
            for (var entry : attributeModifiers.modifiers()) {
                modifiersBySlot.computeIfAbsent(entry.slot(), k -> new ArrayList<>()).add(entry);
            }

            // Merge MAINHAND and OFFHAND into a single 'Hand' entry
            if (modifiersBySlot.containsKey(EquipmentSlotGroup.MAINHAND) || modifiersBySlot.containsKey(EquipmentSlotGroup.OFFHAND) || modifiersBySlot.containsKey(EquipmentSlotGroup.HAND)) {
                List<net.minecraft.world.item.component.ItemAttributeModifiers.Entry> handList = new ArrayList<>();
                if (modifiersBySlot.containsKey(EquipmentSlotGroup.MAINHAND)) {
                    handList.addAll(modifiersBySlot.remove(EquipmentSlotGroup.MAINHAND));
                }
                if (modifiersBySlot.containsKey(EquipmentSlotGroup.OFFHAND)) {
                    handList.addAll(modifiersBySlot.remove(EquipmentSlotGroup.OFFHAND));
                }
                if (modifiersBySlot.containsKey(EquipmentSlotGroup.HAND)) {
                    handList.addAll(modifiersBySlot.remove(EquipmentSlotGroup.HAND));
                }
                modifiersBySlot.put(EquipmentSlotGroup.MAINHAND, handList); // store back as MAINHAND (representing 'Hand')
            }

            for (var slotEntry : modifiersBySlot.entrySet()) {
                var slot = slotEntry.getKey();
                var modifiersList = slotEntry.getValue();

                String rawSlot = slot.name().toLowerCase();
                String slotLabel = (slot == EquipmentSlotGroup.MAINHAND) ? "Hand"
                        : rawSlot.substring(0, 1).toUpperCase() + rawSlot.substring(1);

                // Section header per slot
                tooltip.add(Component.literal("✦ When equipped:")
                        .withStyle(PRIMARY_COLOR));

                // Equipment slot label
                tooltip.add(
                    Component.literal("  Equipment Slot: ")
                        .withStyle(SECONDARY_COLOR)
                        .append(Component.literal(slotLabel).withStyle(PRIMARY_COLOR))
                );

                // Attack Range as attribute line
                var player = Minecraft.getInstance().player;
                if (player != null) {
                    double vanillaRange = 3.0;
                    boolean hasRangeAttr = EntityAttributeHelper.itemHasRangeAttribute(stack);
                    double range = PlayerAttackHelper.getStaticRange(player, stack);
                    if (hasRangeAttr || Math.abs(range - vanillaRange) > 0.001) {
                        String range_icon = "\uA062";
                        tooltip.add(Component.literal("  ")
                                .append(Component.literal(range_icon)
                                        .setStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "attributes")).withColor(PRIMARY_COLOR)))
                                .append(Component.literal(" "))
                                .append(Component.translatable("attribute.name.generic.attack_range").withStyle(SECONDARY_COLOR))
                                .append(Component.literal(": ").withStyle(SECONDARY_COLOR))
                                .append(Component.literal(String.format("%.2f", range)).withStyle(PRIMARY_COLOR))
                        );
                    }
                }

                for (var entry : modifiersList) {
                    var attribute = entry.attribute().value();
                    var modifier = entry.modifier();
                    String attributeId = attribute.getDescriptionId();
                    double amount = Math.round(modifier.amount() * 10.0) / 10.0;

                    // Skip custom armor toughness lines if value is 0
                    if (attributeId.equals("attribute.name.generic.armor_toughness") && amount == 0.0) {
                        continue;
                    }

                    // Fix attack speed display to what is usually shown in tooltips
                    if (attributeId.equals("attribute.name.generic.attack_speed")) {
                        amount = Math.round((modifier.amount() + 4.0) * 10.0) / 10.0;
                    }

                    // Format the amount: remove trailing .0 if integer
                    String displayAmount = (amount % 1.0 == 0.0)
                            ? String.format("%.0f", amount)
                            : String.format("%.1f", amount);

                    String attribute_icon = switch (attributeId) {
                        case "attribute.name.generic.armor" -> "\uA000";
                        case "attribute.name.generic.armor_toughness" -> "\uA001";
                        case "attribute.name.generic.attack_damage" -> "\uA002";
                        case "attribute.name.generic.attack_knockback" -> "\uA003";
                        case "attribute.name.generic.attack_speed" -> "\uA004";
                        case "attribute.name.generic.block_break_speed" -> "\uA005";
                        case "attribute.name.generic.block_interaction_range" -> "\uA006";
                        case "attribute.name.generic.burning_time" -> "\uA007";
                        case "attribute.name.generic.entity_interaction_range" -> "\uA008";
                        case "attribute.name.generic.explosion_knockback_resistance" -> "\uA009";
                        case "attribute.name.generic.fall_damage_multiplier" -> "\uA010";
                        case "attribute.name.generic.flying_speed" -> "\uA011";
                        case "attribute.name.generic.follow_range" -> "\uA012";
                        case "attribute.name.generic.gravity" -> "\uA013";
                        case "attribute.name.generic.jump_strength" -> "\uA014";
                        case "attribute.name.generic.knockback_resistance" -> "\uA015";
                        case "attribute.name.generic.luck" -> "\uA016";
                        case "attribute.name.generic.max_absorption" -> "\uA017";
                        case "attribute.name.generic.max_health" -> "\uA018";
                        case "attribute.name.generic.mining_efficiency" -> "\uA019";
                        case "attribute.name.generic.movement_efficiency" -> "\uA020";
                        case "attribute.name.generic.movement_speed" -> "\uA021";
                        case "attribute.name.generic.oxygen_bonus" -> "\uA022";
                        case "attribute.name.generic.safe_fall_distance" -> "\uA023";
                        case "attribute.name.generic.scale" -> "\uA024";
                        case "attribute.name.generic.spawn_reinforcements" -> "\uA025";
                        case "attribute.name.generic.sneaking_speed" -> "\uA026";
                        case "attribute.name.generic.step_height" -> "\uA027";
                        case "attribute.name.generic.submerged_mining_speed" -> "\uA028";
                        case "attribute.name.generic.sweeping_damage_ratio" -> "\uA029";
                        case "attribute.name.generic.tempt_range" -> "\uA030";
                        case "attribute.explorers_of_ether.crit_rate" -> "\uA031";
                        case "attribute.explorers_of_ether.crit_damage" -> "\uA032";
                        case "attribute.explorers_of_ether.dark_damage" -> "\uA033";
                        case "attribute.explorers_of_ether.dark_resistance" -> "\uA034";
                        case "attribute.explorers_of_ether.earth_damage" -> "\uA035";
                        case "attribute.explorers_of_ether.earth_resistance" -> "\uA036";
                        case "attribute.explorers_of_ether.fire_damage" -> "\uA037";
                        case "attribute.explorers_of_ether.fire_resistance" -> "\uA038";
                        case "attribute.explorers_of_ether.light_damage" -> "\uA039";
                        case "attribute.explorers_of_ether.light_resistance" -> "\uA040";
                        case "attribute.explorers_of_ether.plant_damage" -> "\uA041";
                        case "attribute.explorers_of_ether.plant_resistance" -> "\uA042";
                        case "attribute.explorers_of_ether.water_damage" -> "\uA043";
                        case "attribute.explorers_of_ether.water_resistance" -> "\uA044";
                        case "attribute.explorers_of_ether.wind_damage" -> "\uA045";
                        case "attribute.explorers_of_ether.wind_resistance" -> "\uA046";
                        case "attribute.explorers_of_ether.magic_damage" -> "\uA047";
                        case "attribute.explorers_of_ether.magic_resistance" -> "\uA048";
                        case "attribute.explorers_of_ether.mana" -> "\uA049";
                        case "attribute.explorers_of_ether.max_mana" -> "\uA050";
                        case "attribute.explorers_of_ether.mana_regen" -> "\uA051";
                        case "attribute.explorers_of_ether.draw_speed" -> "\uA052";
                        case "attribute.explorers_of_ether.projectile_damage" -> "\uA053";
                        case "attribute.explorers_of_ether.projectile_resistance" -> "\uA054";
                        case "attribute.explorers_of_ether.spirit_attack_speed" -> "\uA055";
                        case "attribute.explorers_of_ether.spirit_damage" -> "\uA056";
                        case "attribute.explorers_of_ether.spirit_health" -> "\uA057";
                        case "attribute.explorers_of_ether.spirit_movement_speed" -> "\uA058";
                        case "attribute.explorers_of_ether.stamina" -> "\uA059";
                        case "attribute.explorers_of_ether.max_stamina" -> "\uA060";
                        case "attribute.explorers_of_ether.stamina_regen" -> "\uA061";
                        case "attribute.explorers_of_ether.health_regen" -> "\uA062";
                        case "attribute.name.neoforge.creative_flight" -> "\uA063";
                        case "attribute.name.neoforge.nametag_distance" -> "\uA064";
                        case "attribute.name.neoforge.swim_speed" -> "\uA065";
                        case "attribute.name.generic.attack_range" -> "\uA062";
                        default -> "";
                    };

                    ResourceLocation attributeFontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "attributes");
                    ResourceLocation elementFontLocation = ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MODID, "elements");

                    tooltip.add(Component.literal("  ")
                        .append(Component.literal(attribute_icon)
                            .setStyle(Style.EMPTY.withFont(attributeFontLocation).withColor(PRIMARY_COLOR)))
                        .append(Component.literal(" "))
                        .append(Component.translatable(attribute.getDescriptionId()).withStyle(SECONDARY_COLOR))
                        .append(Component.literal(": ").withStyle(SECONDARY_COLOR))
                        .append(Component.literal(displayAmount).withStyle(PRIMARY_COLOR))
                    );
                }
                tooltip.add(Component.empty());
            }
        }
        // ────────────────────────────────────────────────────────────────────────
    }
}
