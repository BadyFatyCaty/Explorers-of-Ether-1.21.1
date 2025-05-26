package net.mrbubbleman.explorersofether.setup.registry;

import net.mrbubbleman.explorersofether.common.capability.EoEPlayerData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentsRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    public static final Supplier<AttachmentType<EoEPlayerData>> PLAYER_DATA;

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "explorers_of_ether");
        PLAYER_DATA = ATTACHMENT_TYPES.register("player_cap", () -> AttachmentType.serializable(EoEPlayerData::new).copyOnDeath().build());
    }
}
