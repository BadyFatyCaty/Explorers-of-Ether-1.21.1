package net.badyfatycaty.explorersofether;

import com.mojang.logging.LogUtils;
import net.badyfatycaty.explorersofether.attributes.ModAttributes;
import net.badyfatycaty.explorersofether.block.ModBlocks;
import net.badyfatycaty.explorersofether.block.entity.ModBlockEntities;
import net.badyfatycaty.explorersofether.components.ModDataComponents;
import net.badyfatycaty.explorersofether.effect.ModEffects;
import net.badyfatycaty.explorersofether.events.ModBowEvents;
import net.badyfatycaty.explorersofether.items.ModCreativeModeTabs;
import net.badyfatycaty.explorersofether.items.ModItems;
import net.badyfatycaty.explorersofether.items.categories.CrucibleItems;
import net.badyfatycaty.explorersofether.items.categories.ForgedIronItems;
import net.badyfatycaty.explorersofether.particle.BloodParticles;
import net.badyfatycaty.explorersofether.particle.CritSkullParticle;
import net.badyfatycaty.explorersofether.particle.ModParticles;
import net.badyfatycaty.explorersofether.screen.ModMenuTypes;
import net.badyfatycaty.explorersofether.screen.custom.ForgeScreen;
import net.badyfatycaty.explorersofether.util.ModItemProperties;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExplorersofEther.MOD_ID)
public class ExplorersofEther
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "explorers_of_ether";
    public static final String MODID = "explorers_of_ether";

    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExplorersofEther(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated function in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModAttributes.registerAllAttributes(modEventBus);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);

        CrucibleItems.register(modEventBus);
        ForgedIronItems.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModEffects.register(modEventBus);
        ModParticles.register(modEventBus);

        NeoForge.EVENT_BUS.register(ModBowEvents.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModItemProperties.addCustomItemProperties();
            ItemProperties.register(ForgedIronItems.FORGED_IRON_SHIELD.get(), ResourceLocation.fromNamespaceAndPath("minecraft", "blocking"),
                (stack, level, entity, seed) -> {
                    return (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F : 0.0F;
                });
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.FORGE_MENU.get(), ForgeScreen::new);
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.BLOOD_PARTICLES.get(), BloodParticles.Provider::new);
            event.registerSpriteSet(ModParticles.CRIT_SKULL.get(), CritSkullParticle.Provider::new);
        }
    }
}
