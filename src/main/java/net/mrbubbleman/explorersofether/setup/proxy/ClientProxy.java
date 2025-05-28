package net.mrbubbleman.explorersofether.setup.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientProxy implements IProxy {
    public void init() {
    }

    public Level getClientWorld() {
        return Minecraft.getInstance().level;
    }

    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public boolean isClientSide() {
        return true;
    }
}
