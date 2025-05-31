package net.badyfatycaty.explorersofether.effect;

import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ExplorersofEther.MOD_ID);

    public static final Holder<MobEffect> BLEEDING_EFFECT = MOB_EFFECTS.register("bleeding",
            () -> new BleedingEffect(MobEffectCategory.HARMFUL, 0xdc1717));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}