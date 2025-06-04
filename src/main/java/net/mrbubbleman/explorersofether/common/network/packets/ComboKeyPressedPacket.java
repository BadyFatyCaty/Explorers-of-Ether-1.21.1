package net.mrbubbleman.explorersofether.common.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.common.capability.playercaps.IPlayerCap;
import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerData;
import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerDataCap;
import net.mrbubbleman.explorersofether.common.network.Networking;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;
import net.mrbubbleman.explorersofether.setup.registry.CapabilityRegistry;
import net.neoforged.neoforge.common.NeoForge;

public class ComboKeyPressedPacket extends AbstractPacket {
    public static final CustomPacketPayload.Type<ComboKeyPressedPacket> TYPE = new CustomPacketPayload.Type(ExplorersofEther.prefix("ex_play_to_server"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComboKeyPressedPacket> CODEC = StreamCodec.ofMember(ComboKeyPressedPacket::toBytes, ComboKeyPressedPacket::new);

    public char comboDirection;

    public ComboKeyPressedPacket(RegistryFriendlyByteBuf buf) {
        this.comboDirection = buf.readChar();
    }

    public ComboKeyPressedPacket(char comboDirection) {
        this.comboDirection = comboDirection;
    }

    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeChar(this.comboDirection);
    }

    public void onServerReceived(MinecraftServer server, ServerPlayer player) {

    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}