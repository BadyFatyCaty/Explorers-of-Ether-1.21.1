package net.mrbubbleman.explorersofether;

import net.minecraft.resources.ResourceLocation;
import net.mrbubbleman.explorersofether.common.network.Networking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(ExplorersofEther.MOD_ID)
public class ExplorersofEther
{
    public static final Logger LOGGER = LogManager.getLogger("explorers_of_ether");
    public static final String MOD_ID = "explorers_of_ether";

    public ExplorersofEther(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(Networking::register);
    }

    public static ResourceLocation prefix(String str) {
        return ResourceLocation.fromNamespaceAndPath("explorers_of_ether", str);
    }
}
