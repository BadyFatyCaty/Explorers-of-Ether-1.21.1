package net.mrbubbleman.explorersofether.setup.registry;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.common.capability.playercaps.PlayerDataCap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CapabilityRegistry {
    // public static final EntityCapability<ManaCap, Void> MANA_CAPABILITY = EntityCapability.createVoid(ArsNouveau.prefix("mana"), ManaCap.class);
    public static final EntityCapability<PlayerDataCap, Void> PLAYER_DATA_CAP = EntityCapability.createVoid(ExplorersofEther.prefix("player_data"), PlayerDataCap.class);

    public static PlayerDataCap getPlayerDataCap(LivingEntity entity) {
        return entity == null ? null : (PlayerDataCap)entity.getCapability(PLAYER_DATA_CAP);
    }

    // @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(PLAYER_DATA_CAP, EntityType.PLAYER, (player, ctx) -> new PlayerDataCap(player));
    }

    @EventBusSubscriber(
            modid = ExplorersofEther.MOD_ID
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
            ExplorersofEther.LOGGER.info("Sync Player Cap Method Fired");
            if (player instanceof ServerPlayer serverPlayer) {
                ExplorersofEther.LOGGER.info("Is Instance of Server Player");
                PlayerDataCap playerData = CapabilityRegistry.getPlayerDataCap(serverPlayer);
                if (playerData != null) {
                    ExplorersofEther.LOGGER.info("\n\nCapabilities Exist\n\n");
                    playerData.syncToClient(serverPlayer);
                } else {
                    ExplorersofEther.LOGGER.info("\n\nCapabilities Dont Exist\n\n");
                    // registerCapabilities();
                }
            }

        }
    }
}
