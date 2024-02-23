package io.github.zygzaggaming.zygzagsmod.client.particle;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShockwaveParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected ShockwaveParticle(ClientLevel world, double x, double y, double z, double dX, double dY, double dZ, SpriteSet sprites, int minLifetime, int maxLifetime) {
        super(world, x, y, z, dX, dY, dZ);
        this.sprites = sprites;
        setLifetime((int) Math.round(Math.random() * (maxLifetime - minLifetime) + minLifetime));
        this.setSpriteFromAge(this.sprites);
        roll = (float) (Math.random() * 2 * Math.PI);
        oRoll = roll;
        quadSize = 0.09375f;
        setSize(0.09375f, 0.09375f);
        xd = 0;
        yd = 0;
        zd = 0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public void tick() {
        super.tick();
        xd /= 8;
        yd /= 8;
        zd /= 8;
        yd -= 8;
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final int minLifetime, maxLifetime;

        public Provider(SpriteSet sprites, int minLifetime, int maxLifetime) {
            this.sprites = sprites;
            this.minLifetime = minLifetime;
            this.maxLifetime = maxLifetime;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new ShockwaveParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, sprites, minLifetime, maxLifetime);
        }
    }
}
