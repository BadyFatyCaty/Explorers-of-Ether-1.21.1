package net.mrbubbleman.explorersofether.setup.registry;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.common.capability.EoEPlayerDataCap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

public class CapabilityRegistry {
    public static final EntityCapability<EoEPlayerDataCap, Void> PLAYER_DATA_CAP = EntityCapability.createVoid(ExplorersofEther.prefix("player_data"), EoEPlayerDataCap.class);

    public static EoEPlayerDataCap getPlayerDataCap(LivingEntity entity) {
        return entity == null ? null : (EoEPlayerDataCap)entity.getCapability(PLAYER_DATA_CAP);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(PLAYER_DATA_CAP, EntityType.PLAYER, (player, ctx) -> new EoEPlayerDataCap(player));
    }

    @EventBusSubscriber(
            modid = "explorers_of_ether"
    )
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void onPlayerStartTrackingEvent(PlayerEvent.StartTracking event) {
            if (event.getTarget() instanceof Player && event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void onPlayerDimChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        public static void syncPlayerCap(Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                EoEPlayerDataCap playerData = CapabilityRegistry.getPlayerDataCap(serverPlayer);
                if (playerData != null) {
                    playerData.syncToClient(serverPlayer);
                }
            }

        }
    }
}
