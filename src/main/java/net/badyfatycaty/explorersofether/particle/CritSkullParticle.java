package net.badyfatycaty.explorersofether.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CritSkullParticle extends TextureSheetParticle {
    protected CritSkullParticle(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.lifetime = 10; // Short-lived
        this.gravity = 0;
        this.alpha = 1.0F;
        this.scale(scale);
    }

    @Override
    public void tick() {
        super.tick();

        // Animate stretch and fade
        float lifeRatio = (float) age / lifetime;
        float stretch = Mth.lerp(lifeRatio, 0.2F, 1.0F); // horizontal stretch
        float alphaFade = 1.0F - lifeRatio;

        this.quadSize = stretch; // assumes scale is handled here
        this.alpha = alphaFade;

        if (alpha <= 0.05F) this.remove();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            CritSkullParticle particle = new CritSkullParticle(level, x, y, z, 1.5F);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}