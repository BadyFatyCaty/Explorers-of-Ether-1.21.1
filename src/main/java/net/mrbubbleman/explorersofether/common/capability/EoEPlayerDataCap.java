package net.mrbubbleman.explorersofether.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;

import java.util.ArrayList;

public class EoEPlayerDataCap implements IPlayerCap {
    private EoEPlayerData playerData;
    LivingEntity entity;

    public EoEPlayerDataCap(LivingEntity livingEntity) {
        this.playerData = (EoEPlayerData)livingEntity.getData(AttachmentsRegistry.PLAYER_DATA);
        this.entity = livingEntity;
    }

    @Override
    public ArrayList<Character> getCurrentCombo() {
        return playerData.castingCombo;
    }

    @Override
    public void addToCurrentCombo(char var1) {
        playerData.castingCombo.add(var1);
    }

    public void setPlayerData(EoEPlayerData data) {
        this.playerData = data;
    }

    public void syncToClient(ServerPlayer player) {
        CompoundTag tag = this.playerData.serializeNBT(player.registryAccess());
        // Networking.sendToPlayerClient(new PacketSyncPlayerCap(tag), player);
    }
}
