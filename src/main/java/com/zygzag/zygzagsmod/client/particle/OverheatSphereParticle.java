package com.zygzag.zygzagsmod.client.particle;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverheatSphereParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected OverheatSphereParticle(ClientLevel world, double x, double y, double z, double dX, double dY, double dZ, SpriteSet sprites) {
        super(world, x, y, z, dX, dY, dZ);
        this.sprites = sprites;
        setSprite(sprites.get(world.random));
        setLifetime((int) Math.round(30 + 20 * world.random.nextDouble()));
        quadSize = 3 / 32f;
        setSize(3 / 32f, 3 / 32f);
        xd = dX;
        yd = dY;
        zd = dZ;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
        yd -= 0.0375;
        xd *= 0.8;
        yd *= 0.8;
        zd *= 0.8;
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new OverheatSphereParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, sprites);
        }
    }
}
