package net.mrbubbleman.explorersofether.common.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.mrbubbleman.explorersofether.ExplorersofEther;

public class PacketComboKeyPressed extends AbstractPacket {
    public static final CustomPacketPayload.Type<PacketComboKeyPressed> TYPE = new CustomPacketPayload.Type(ExplorersofEther.prefix("ex_play_to_server"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketComboKeyPressed> CODEC = StreamCodec.ofMember(PacketComboKeyPressed::toBytes, PacketComboKeyPressed::new);

    public char comboDirection;

    public PacketComboKeyPressed(RegistryFriendlyByteBuf buf) {
        this.comboDirection = buf.readChar();
    }

    public PacketComboKeyPressed(char comboDirection) {
        this.comboDirection = comboDirection;
    }

    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeChar(this.comboDirection);
    }

    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
        // ExplorersofEther.LOGGER.info("Key Pressed: " + comboDirection);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}