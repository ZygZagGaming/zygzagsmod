package com.zygzag.zygzagsmod.client.particle;

import com.zygzag.zygzagsmod.common.block.EndSandBlock;
import com.zygzag.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EndSandParticle extends TextureSheetParticle {
    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = 100 * 100; // stolen from Particle class :3
    private final float uo;
    private final float vo;
    private final Vec3 origin;
    private final BlockPos originBlock;
    private int whenReversed;
    private boolean savingTrajectory = true;
    private List<Vec3> pastPositionsPerTick = new ArrayList<>();
    public static final double tolerance = 0.0625;
    public static final int ticksToReturn = 2;
    protected EndSandParticle(ClientLevel pLevel, double pX, double pY, double pZ, double dX, double dY, double dZ) {
        super(pLevel, pX, pY, pZ, dX, dY, dZ);
        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.xd = (x - Math.floor(x) - 0.5 + random.nextGaussian() * 0.2) / 4;
        this.yd = (y - Math.floor(y) - 0.5 + random.nextGaussian() * 0.2) / 4;
        this.zd = (z - Math.floor(z) - 0.5 + random.nextGaussian() * 0.2) / 4;
        origin = new Vec3(pX, pY, pZ);
        originBlock = BlockPos.containing(pX, pY, pZ);
        setLifetime(10000);
        gravity = 1f;
    }

    @Override
    public void tick() {
        baseTick();
        if (savingTrajectory) savePosition();
        BlockState originState = level.getBlockState(originBlock);
        if (!originState.hasProperty(EndSandBlock.POWER)) remove();
        else if (originState.getValue(EndSandBlock.POWER) == 0) {
            if (savingTrajectory) {
                whenReversed = age;
                savingTrajectory = false;
                hasPhysics = false;
            }

            if (whenReversed == age) {
                var pos = pastPositionsPerTick.get(pastPositionsPerTick.size() / 3);
                x = pos.x();
                y = pos.y();
                z = pos.z();
            } else if (whenReversed == age - 1) {
                x = origin.x();
                y = origin.y();
                z = origin.z();
            } else remove();
        }
        if (position().distanceToSqr(origin) >= 30 * 30) remove();
    }

    public void baseTick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double)this.gravity;
            if (savingTrajectory) this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

            this.xd *= (double)this.friction;
            this.yd *= (double)this.friction;
            this.zd *= (double)this.friction;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

        }
    }

    boolean stoppedByCollision = false;
    @Override
    public void move(double pX, double pY, double pZ) {
        double d0 = pX;
        double d1 = pY;
        double d2 = pZ;
        if (this.hasPhysics && (pX != 0 || pY != 0 || pZ != 0) && pX * pX + pY * pY + pZ * pZ < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(pX, pY, pZ), this.getBoundingBox(), this.level, List.of());
            pX = vec3.x;
            pY = vec3.y;
            pZ = vec3.z;
        }

        if (!stoppedByCollision) {
            if (pX != 0 || pY != 0 || pZ != 0) {
                this.setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
                this.setLocationFromBoundingbox();
            }

            if (Math.abs(d1) >= 1e-5 && Math.abs(pY) < 1e-5) {
                this.stoppedByCollision = true;
            }

            this.onGround = d1 != pY && d1 < 0;
            if (d0 != pX) {
                this.xd = 0;
            }

            if (d2 != pZ) {
                this.zd = 0;
            }
        }
    }

    public void savePosition() {
        if (pastPositionsPerTick.size() == 0 || position().distanceToSqr(pastPositionsPerTick.get(pastPositionsPerTick.size() - 1)) <= tolerance) pastPositionsPerTick.add(position());
    }

    public Vec3 position() {
        return new Vec3(x, y, z);
    }

    public Vec3 getPositionTicksInPast(double n) {
        if (n > pastPositionsPerTick.size() - 1) {
            return origin;
        }
        if (n < 0) return pastPositionsPerTick.get(pastPositionsPerTick.size() - 1);
        var less = pastPositionsPerTick.get((int) Math.floor(n));
        var more = pastPositionsPerTick.get((int) Math.ceil(n));
        var t = n % 1;
        return less.scale(t).add(more.scale(1 - t));
    }

    protected float getU0() {
        return this.sprite.getU(((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV(((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new EndSandParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed)
                    .updateSprite(BlockWithItemRegistry.END_SAND.getDefaultBlockState(), BlockPos.ZERO);
        }
    }

    public Particle updateSprite(BlockState state, BlockPos pos) {
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(state, level, pos));
        return this;
    }
}
