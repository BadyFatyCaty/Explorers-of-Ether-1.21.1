package net.mrbubbleman.explorersofether.networking.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.mrbubbleman.explorersofether.ExplorersofEther;

public record SyncPayload(CompoundTag newNBTData) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID, "sync_payload"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'newNBTData' will be encoded and decoded as a Compoundtag
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, SyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SyncPayload::newNBTData,
            SyncPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}