package net.badyfatycaty.explorersofether.effect;

import net.badyfatycaty.explorersofether.particle.ModParticles;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {
    public BleedingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        // Every 2 seconds (40 ticks), spawn between 2 and 10 blood particles in a scattered burst
        if (livingEntity.tickCount % 20 == 0 && livingEntity.level().isClientSide) {
            int particleCount = 2 + livingEntity.getRandom().nextInt(9); // 2 to 10 particles
            for (int i = 0; i < particleCount; i++) {
                double offsetX = (livingEntity.getRandom().nextDouble() - 0.5) * 0.5;
                double offsetY = livingEntity.getRandom().nextDouble() * 0.5;
                double offsetZ = (livingEntity.getRandom().nextDouble() - 0.5) * 0.5;
                livingEntity.level().addParticle(ModParticles.BLOOD_PARTICLES.get(),
                    livingEntity.getX() + offsetX,
                    livingEntity.getY() + offsetY + livingEntity.getBbHeight() / 2,
                    livingEntity.getZ() + offsetZ,
                    0, 0, 0);
            }
        }

        // Deal damage every second (20 ticks)
        if (livingEntity.tickCount % 20 == 0) {
            float basePercent = 0.01f + (amplifier * 0.005f);
            float damageAmount = livingEntity.getMaxHealth() * basePercent;
            livingEntity.hurt(livingEntity.damageSources().magic(), damageAmount);
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}