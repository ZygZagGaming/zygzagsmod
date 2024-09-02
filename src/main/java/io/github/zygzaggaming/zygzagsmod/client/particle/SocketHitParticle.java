package io.github.zygzaggaming.zygzagsmod.client.particle;

import io.github.zygzaggaming.zygzagsmod.common.particle.SocketParticleOption;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SocketHitParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected SocketHitParticle(ClientLevel world, double x, double y, double z, double dX, double dY, double dZ, SpriteSet sprites) {
        super(world, x, y, z, dX, dY, dZ);
        this.sprites = sprites;
        setSprite(sprites.get(world.random));
        setLifetime((int) Math.round(1 + 7 * world.random.nextDouble()));
        quadSize = 2 / 32f;
        setSize(quadSize, quadSize);
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
        yd -= 0.25 * 0.0375;
        xd *= 0.7;
        yd *= 0.7;
        zd *= 0.7;
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SocketParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SocketParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            var particle = new SocketHitParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, sprites);
            var color = pType.socket().color;
            int red = (color >> 16) & 0xff; int green = (color >> 8) & 0xff; int blue = color & 0xff;
            particle.setColor(red / 256f, green / 256f, blue / 256f);
            return particle;
        }
    }
}
