//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mrbubbleman.explorersofether.networking;

import net.mrbubbleman.explorersofether.networking.packets.SyncPayload;
import net.mrbubbleman.explorersofether.networking.payloadhandling.ClientPayloadHandler;
import net.mrbubbleman.explorersofether.networking.payloadhandling.ServerPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar reg = event.registrar("1");

        reg.playBidirectional(
                SyncPayload.TYPE,
                SyncPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleDataOnMain,
                        ServerPayloadHandler::handleDataOnMain
                )
        );
    }

    /*
    public static void sendingPacketExample() {
        // ON THE CLIENT

        // Send payload to server
        PacketDistributor.sendToServer(new MyData(...));

        // ON THE SERVER

        // Send to one player (ServerPlayer serverPlayer)
        PacketDistributor.sendToPlayer(serverPlayer, new MyData(...));

        /// Send to all players tracking this chunk (ServerLevel serverLevel, ChunkPos chunkPos)
        PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunkPos, new MyData(...));

        /// Send to all connected players
        PacketDistributor.sendToAllPlayers(new MyData(...));
    }
     */
}
