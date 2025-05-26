package net.mrbubbleman.explorersofether.common.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.common.capability.EoEPlayerData;
import net.mrbubbleman.explorersofether.common.capability.EoEPlayerDataCap;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;
import net.mrbubbleman.explorersofether.setup.registry.CapabilityRegistry;

public class PacketSyncPlayerCap extends AbstractPacket {
    CompoundTag tag;
    public static final CustomPacketPayload.Type<PacketSyncPlayerCap> TYPE = new CustomPacketPayload.Type(ExplorersofEther.prefix("sync_player_cap"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncPlayerCap> CODEC = StreamCodec.ofMember(PacketSyncPlayerCap::toBytes, PacketSyncPlayerCap::new);

    public PacketSyncPlayerCap(RegistryFriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public PacketSyncPlayerCap(CompoundTag famCaps) {
        this.tag = famCaps;
    }

    public void onClientReceived(Minecraft minecraft, Player playerEntity) {
        EoEPlayerData data = new EoEPlayerData();
        data.deserializeNBT(playerEntity.registryAccess(), this.tag);
        playerEntity.setData(AttachmentsRegistry.PLAYER_DATA, data);
        EoEPlayerDataCap cap = CapabilityRegistry.getPlayerDataCap(minecraft.player);
        if (cap != null) {
            cap.setPlayerData(data);
        }

    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}