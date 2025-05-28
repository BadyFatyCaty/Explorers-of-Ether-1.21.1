package net.mrbubbleman.explorersofether.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.mrbubbleman.explorersofether.common.network.packets.AbstractPacket;
import net.mrbubbleman.explorersofether.common.network.packets.ComboKeyPressedPacket;
import net.mrbubbleman.explorersofether.common.network.packets.SyncPlayerCapPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar("1");
        // Register Packets
        reg.playToServer(ComboKeyPressedPacket.TYPE, ComboKeyPressedPacket.CODEC, Networking::handle);
        reg.playToClient(SyncPlayerCapPacket.TYPE, SyncPlayerCapPacket.CODEC, Networking::handle);
    }

    public static <T extends AbstractPacket> void handle(T message, IPayloadContext ctx) {
        if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
            handleServer(message, ctx);
        } else {
            Networking.ClientMessageHandler.handleClient(message, ctx);
        }
    }

    private static <T extends AbstractPacket> void handleServer(T message, IPayloadContext ctx) {
        MinecraftServer server = ctx.player().getServer();
        message.onServerReceived(server, (ServerPlayer)ctx.player());
    }

    public static void sendToNearbyClient(Level world, BlockPos pos, CustomPacketPayload toSend) {
        if (world instanceof ServerLevel ws) {
            PacketDistributor.sendToPlayersTrackingChunk(ws, new ChunkPos(pos), toSend, new CustomPacketPayload[0]);
        }

    }

    public static void sendToNearbyClient(Level world, Entity e, CustomPacketPayload toSend) {
        sendToNearbyClient(world, e.blockPosition(), toSend);
    }

    public static void sendToPlayerClient(CustomPacketPayload msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg, new CustomPacketPayload[0]);
    }

    public static void sendToServer(CustomPacketPayload msg) {
        PacketDistributor.sendToServer(msg, new CustomPacketPayload[0]);
    }

    private static class ClientMessageHandler {
        public static <T extends AbstractPacket> void handleClient(T message, IPayloadContext ctx) {
            Minecraft minecraft = Minecraft.getInstance();
            message.onClientReceived(minecraft, minecraft.player);
        }
    }
}
