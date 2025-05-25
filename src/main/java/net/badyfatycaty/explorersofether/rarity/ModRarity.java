package net.badyfatycaty.explorersofether.rarity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Rarity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public enum ModRarity implements StringRepresentable {
    COMMON(0, "common", ChatFormatting.WHITE, style -> style.withColor(ChatFormatting.WHITE)),
    UNCOMMON(1, "uncommon", ChatFormatting.GREEN, style -> style.withColor(ChatFormatting.GREEN)),
    RARE(2, "rare", ChatFormatting.AQUA, style -> style.withColor(ChatFormatting.AQUA)),
    EPIC(3, "epic", ChatFormatting.LIGHT_PURPLE, style -> style.withColor(ChatFormatting.LIGHT_PURPLE)),
    LEGENDARY(4, "legendary", ChatFormatting.YELLOW, style -> style.withColor(ChatFormatting.YELLOW)),
    MYTHIC(5, "mythic", ChatFormatting.RED, style -> style.withColor(ChatFormatting.RED)),
    UNIQUE(6, "unique", ChatFormatting.GOLD, style -> style.withColor(ChatFormatting.GOLD));

    private final int id;
    private final String serializedName;
    private final ChatFormatting formatting;
    private final UnaryOperator<Style> styleModifier;

    private static final Map<Integer, ModRarity> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(e -> e.id, Function.identity()));
    private static final Map<String, ModRarity> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(e -> e.serializedName, Function.identity()));

    ModRarity(int id, String serializedName, ChatFormatting formatting, UnaryOperator<Style> styleModifier) {
        this.id = id;
        this.serializedName = serializedName;
        this.formatting = formatting;
        this.styleModifier = styleModifier;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public ChatFormatting getFormatting() {
        return formatting;
    }

    public UnaryOperator<Style> getStyleModifier() {
        return styleModifier;
    }

    public static ModRarity byId(int id) {
        return BY_ID.getOrDefault(id, COMMON);
    }

    public static ModRarity byName(String name) {
        if (name == null) return COMMON;
        return BY_NAME.getOrDefault(name.toLowerCase(Locale.ROOT), COMMON);
    }

    /**
     * Convert this ModRarity to a vanilla Rarity for compatibility.
     * Custom rarities map to EPIC in vanilla context.
     */
    public Rarity toVanilla() {
        return switch (this) {
            case COMMON -> Rarity.COMMON;
            case UNCOMMON -> Rarity.UNCOMMON;
            case RARE -> Rarity.RARE;
            case EPIC -> Rarity.EPIC;
            case LEGENDARY, MYTHIC, UNIQUE -> Rarity.EPIC;
        };
    }
}