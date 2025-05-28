package net.mrbubbleman.explorersofether;

import net.minecraft.resources.ResourceLocation;
import net.mrbubbleman.explorersofether.common.advancement.EoECriteriaTriggers;
import net.mrbubbleman.explorersofether.common.network.Networking;
import net.mrbubbleman.explorersofether.setup.proxy.ClientProxy;
import net.mrbubbleman.explorersofether.setup.proxy.IProxy;
import net.mrbubbleman.explorersofether.setup.proxy.ServerProxy;
import net.mrbubbleman.explorersofether.setup.registry.AttachmentsRegistry;
import net.mrbubbleman.explorersofether.setup.registry.CapabilityRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


@Mod(ExplorersofEther.MOD_ID)
public class ExplorersofEther
{
    public static IProxy proxy;
    public static final Logger LOGGER = LogManager.getLogger("explorers_of_ether");
    public static final String MOD_ID = "explorers_of_ether";

    public ExplorersofEther(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(Networking::register);
        AttachmentsRegistry.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(CapabilityRegistry::registerCapabilities);

        if (FMLEnvironment.dist.isClient()) {
            proxy = (new Supplier<IProxy>() {
                public IProxy get() {
                    return new ClientProxy();
                }
            }).get();
        } else {
            proxy = new ServerProxy();
        }

    }

    public static ResourceLocation prefix(String str) {
        return ResourceLocation.fromNamespaceAndPath("explorers_of_ether", str);
    }
}
