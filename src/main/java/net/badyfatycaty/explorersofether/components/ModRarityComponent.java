package net.badyfatycaty.explorersofether.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.badyfatycaty.explorersofether.rarity.ModRarity;

/**
 * Data component for storing a ModRarity on an ItemStack.
 */
public final class ModRarityComponent {
    // Codec to persist ModRarity as its serialized name
    public static final Codec<ModRarity> CODEC = Codec.STRING
        .xmap(ModRarity::byName, ModRarity::getSerializedName);

    // StreamCodec for network synchronization of ModRarity
    public static final StreamCodec<RegistryFriendlyByteBuf, ModRarity> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC);

    // The DataComponentType instance for attaching a ModRarity to an ItemStack
    public static final DataComponentType<ModRarity> RARITY =
        DataComponentType.<ModRarity>builder()
            .persistent(CODEC)
            .networkSynchronized(STREAM_CODEC)
            .cacheEncoding()
            .build();
}
