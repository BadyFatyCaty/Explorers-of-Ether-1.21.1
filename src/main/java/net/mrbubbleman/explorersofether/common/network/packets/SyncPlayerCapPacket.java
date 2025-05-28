package net.mrbubbleman.explorersofether.common.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerData;
import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerDataCap;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;
import net.mrbubbleman.explorersofether.setup.registry.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class SyncPlayerCapPacket extends AbstractPacket {
    CompoundTag tag;
    public static final CustomPacketPayload.Type<SyncPlayerCapPacket> TYPE = new CustomPacketPayload.Type(ExplorersofEther.prefix("sync_player_cap"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerCapPacket> CODEC = StreamCodec.ofMember(SyncPlayerCapPacket::toBytes, SyncPlayerCapPacket::new);

    public SyncPlayerCapPacket(RegistryFriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        ExplorersofEther.LOGGER.info("\n\nSync Packet WRITTEN\n\n");
    }

    public SyncPlayerCapPacket(CompoundTag famCaps) {
        this.tag = famCaps;
        ExplorersofEther.LOGGER.info("\n\nSync Packet Constructed\n\n");
    }

    public void onClientReceived(Minecraft minecraft, Player playerEntity) {
        PlayerData data = new PlayerData();
        data.deserializeNBT(playerEntity.registryAccess(), this.tag);
        playerEntity.setData(AttachmentsRegistry.PLAYER_DATA, data);
        PlayerDataCap cap = CapabilityRegistry.getPlayerDataCap(ExplorersofEther.proxy.getPlayer());
        if (cap != null) {
            cap.setPlayerData(data);
        } else {
            // CapabilityRegistry.registerCapabilities();
        }
        ExplorersofEther.LOGGER.info("\n\nSync Packet RECIEVED\n\n");
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
