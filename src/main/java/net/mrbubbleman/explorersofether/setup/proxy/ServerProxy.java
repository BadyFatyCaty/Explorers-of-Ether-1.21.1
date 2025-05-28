package net.mrbubbleman.explorersofether.setup.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy {
    public void init() {
    }

    public Level getClientWorld() {
        throw new IllegalStateException("Accessing client world on server proxy");
    }

    public Minecraft getMinecraft() {
        throw new IllegalStateException("Accessing client Minecraft on server proxy");
    }

    public Player getPlayer() {
        throw new IllegalStateException("Accessing client player on server proxy");
    }

    public boolean isClientSide() {
        return false;
    }
}
