//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.mrbubbleman.explorersofether.common.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractPacket implements CustomPacketPayload {
    
    void toBytes(RegistryFriendlyByteBuf buf) {
    }

    public void onClientReceived(Minecraft minecraft, Player player) {
    }

    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player) {
    }
}
