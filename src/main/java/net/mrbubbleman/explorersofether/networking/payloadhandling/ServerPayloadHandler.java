package net.mrbubbleman.explorersofether.networking.payloadhandling;

import net.mrbubbleman.explorersofether.networking.packets.SyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleDataOnMain(SyncPayload payload, final IPayloadContext context) {
        // No-op: This should never be called, because SyncPayload is only sent from server to client.
    }
}
