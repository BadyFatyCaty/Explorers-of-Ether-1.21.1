package net.badyfatycaty.explorersofether.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class BloodParticles extends TextureSheetParticle {
    protected BloodParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet,
                               double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.gravity = 0.5f;  // stronger downward pull
        this.friction = 0.98f;  // light friction so it slows down gently

        this.xd *= 0.1;  // reduce horizontal speed
        this.zd *= 0.1;

        this.yd = 0.1;  // slight initial upward or neutral movement

        this.lifetime = 40 + this.random.nextInt(20);  // shorter lifetime like drips
        this.setSprite(spriteSet.get(this.random));  // random sprite from set

        this.rCol = 1f;
        this.gCol = 0f;
        this.bCol = 0f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BloodParticles(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}