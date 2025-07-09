package net.mrbubbleman.explorersofether.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.mrbubbleman.explorersofether.ExplorersofEther;
import net.mrbubbleman.explorersofether.capability.element.ElementCapData;
import net.mrbubbleman.explorersofether.capability.element.IElementCap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.security.DrbgParameters;

public class CapabilityRegistry {

    public static final EntityCapability<IElementCap, Void> ELEMENT_CAPS_NO_CONTEXT =
            EntityCapability.createVoid(
                    // Provide a name to uniquely identify the capability.
                    ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID, "element_caps"),
                    // Provide the queried type. Here, we want to look up `IElementCap` instances.
                    IElementCap.class);

    @EventBusSubscriber(modid = ExplorersofEther.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public class CapabilityEvents {
        @SubscribeEvent  // on the mod event bus
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerEntity(
                    ELEMENT_CAPS_NO_CONTEXT,
                    EntityType.PLAYER,
                    (player, ctx) -> new ElementCapData(player));

        }
    }
}
