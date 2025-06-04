package net.mrbubbleman.explorersofether.common.capability.playercaps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.mrbubbleman.explorersofether.common.capability.elements.Elements;
import net.mrbubbleman.explorersofether.common.network.Networking;
import net.mrbubbleman.explorersofether.common.network.packets.SyncPlayerCapPacket;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerDataCap implements IPlayerCap{
    private PlayerData playerData;
    LivingEntity entity;

    public PlayerDataCap(LivingEntity entity) {
        this.playerData = (PlayerData)entity.getData(AttachmentsRegistry.PLAYER_DATA);
        this.entity = entity;
    }

    public ArrayList<Character> getCombo() {
        return playerData.combo;
    }

    public ArrayList<Character> addToCombo(char var1) {
        playerData.combo.add(var1);
        return playerData.combo;
    }

    public void setCombo(ArrayList<Character> var1) {
        playerData.combo.clear();
        playerData.combo.addAll(var1);
    }

    public int getSpellCharge() {
        return playerData.spellCharge;
    }

    public int addSpellCharge(int var1) {
        playerData.spellCharge += var1;
        return playerData.spellCharge;
    }

    public void setSpellCharge(int var1) {
        playerData.spellCharge = var1;
    }

    public void addAdvancementsFromElements() {
        // TODO
    }

    public Elements getElement(String var1) {
        return playerData.playerElements.get(var1);
    }

    public Elements getDeviant(String var1) {
        return playerData.playerElements.get(var1).deviant;
    }

    public Elements getDeviant(Elements var1) {
        return var1.deviant;
    }

    public HashMap<String, Elements> getPlayerElements() {
        return playerData.playerElements;
    }

    public void setPlayerElements(HashMap<String, Elements> map) {

        for (String var1: map.keySet()) {
            this.playerData.playerElements.get(var1).copyFrom(map.get(var1));
        }
    }

    public void setElement(String var1, Elements var2) {
        playerData.playerElements.get(var1).copyFrom(var2);
    }

    public void copyFrom(IPlayerCap var1) {
        setPlayerElements(var1.getPlayerElements());
        setCombo(var1.getCombo());
        setSpellCharge(var1.getSpellCharge());
    }

    public void setPlayerData(PlayerData data) {
        this.playerData = data;
    }

    public void copyPlayerCap(IPlayerCap m) {
        this.setPlayerElements(m.getPlayerElements());
        this.setSpellCharge(m.getSpellCharge());
        this.setCombo(m.getCombo());
    }

    public void syncToClient(ServerPlayer player) {
        CompoundTag tag = this.playerData.serializeNBT(player.registryAccess());
        Networking.sendToPlayerClient(new SyncPlayerCapPacket(tag), player);
    }
}
