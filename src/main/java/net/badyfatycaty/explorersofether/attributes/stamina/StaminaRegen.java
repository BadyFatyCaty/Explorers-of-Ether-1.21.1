package net.badyfatycaty.explorersofether.attributes.stamina;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class StaminaRegen {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, ExplorersofEther.MODID);
    public static final DeferredHolder<Attribute, Attribute> STAMINA_REGEN = REGISTRY.register("stamina_regen", () -> new RangedAttribute("attribute.explorers_of_ether.stamina_regen",
            1, 0, 2).setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE));

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, STAMINA_REGEN);
    }
}
