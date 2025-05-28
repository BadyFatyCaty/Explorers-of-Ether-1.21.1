package net.mrbubbleman.explorersofether.setup.registry;

import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentsRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    // public static final Supplier<AttachmentType<ManaData>> MANA_ATTACHMENT;
    public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA;

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "explorers_of_ether");
        // MANA_ATTACHMENT = ATTACHMENT_TYPES.register("mana_cap", () -> AttachmentType.serializable(ManaData::new).copyOnDeath().build());
        PLAYER_DATA = ATTACHMENT_TYPES.register("player_cap", () -> AttachmentType.serializable(PlayerData::new).copyOnDeath().build());
    }
}