package net.badyfatycaty.explorersofether.components;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EnchantmentLimitComponent {
    public static final Codec<EnchantmentLimitComponent> CODEC = Codec.INT.xmap(EnchantmentLimitComponent::new, EnchantmentLimitComponent::getExtraSlots);

    public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentLimitComponent> STREAM_CODEC =
            new StreamCodec<RegistryFriendlyByteBuf, EnchantmentLimitComponent>() {
                @Override
                public EnchantmentLimitComponent decode(RegistryFriendlyByteBuf buf) {
                    int slots = buf.readVarInt();
                    return new EnchantmentLimitComponent(slots);
                }
                @Override
                public void encode(RegistryFriendlyByteBuf buf, EnchantmentLimitComponent comp) {
                    buf.writeVarInt(comp.getExtraSlots());
                }
            };

    private final int extraSlots;

    public EnchantmentLimitComponent(int extraSlots) {
        this.extraSlots = extraSlots;
    }

    public int getExtraSlots() {
        return extraSlots;
    }
}