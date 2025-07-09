package net.mrbubbleman.explorersofether.networking.payloadhandling;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.networking.packets.SyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleDataOnMain(final SyncPayload data, final IPayloadContext context) {
        // Do something with the data, on the main thread
        Player player = context.player();

        // IElementCap cap = player.getData(CapabilityRegistry.ELEMENT_CAPS_NO_CONTEXT);
        // Element fire = cap.getElement("fire");


        /*
        var object = entity.getCapability(CAP, context);
        if (object != null) { // Checking if the capability exists
            // Use object
        }
         */

        /*
        player.getCapability(MyCapProvider.INSTANCE).ifPresent(cap -> {
            cap.deserializeNBT(data.tag()); // assuming you're syncing NBT
        });
        */

        // CompoundTag simplifiedData = removeRedundancy(data);
    }

    public static CompoundTag removeRedundancy(SyncPayload data) {
        CompoundTag temp = new CompoundTag();

        return null;
    }
}