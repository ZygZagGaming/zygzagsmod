package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HomingWitherSkull extends WitherSkull {
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID = SynchedEntityData.defineId(HomingWitherSkull.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_AUTO_TARGET = SynchedEntityData.defineId(HomingWitherSkull.class, EntityDataSerializers.BOOLEAN);
    Predicate<Entity> targetSelector = (e) -> e instanceof LivingEntity && e != getOwner();
    double speed = 1.2;

    public HomingWitherSkull(EntityType<? extends HomingWitherSkull> type, Level world) {
        super(type, world);
    }

    public HomingWitherSkull(Level world, LivingEntity owner, double xPower, double yPower, double zPower) {
        super(EntityTypeRegistry.HOMING_WITHER_SKULL.get(), world);
        setOwner(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.moveTo(owner.getX(), owner.getY() + owner.getEyeHeight() - 0.5, owner.getZ(), owner.getYRot(), owner.getXRot());
        this.reapplyPosition();
        double d0 = Math.sqrt(xPower * xPower + yPower * yPower + zPower * zPower);
        if (d0 != 0.0D) {
            this.xPower = xPower / d0;
            this.yPower = yPower / d0;
            this.zPower = zPower / d0;
        }
    }

    public HomingWitherSkull(Level world, LivingEntity owner, double x, double y, double z, LivingEntity target) {
        this(world, owner, x, y, z);
        setTarget(target.getUUID());
        setAutoTarget(false);
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

    public Optional<UUID> getTargetUUID() {
        return entityData.get(DATA_TARGET_UUID);
    }

    @Nullable
    public Entity getTarget() {
        Optional<UUID> o = getTargetUUID();
        if (o.isEmpty()) return null;
        UUID targetUUID = o.get();
        List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(50.0), (e) -> e.getUUID() == targetUUID);
        if (entities.size() < 1) return null;
        return entities.get(0);
    }

    public void setTarget(UUID uuid) {
        entityData.set(DATA_TARGET_UUID, Optional.of(uuid));
    }

    public void setNoTarget() {
        entityData.set(DATA_TARGET_UUID, Optional.empty());
    }

    public boolean shouldAutoTarget() {
        return entityData.get(DATA_AUTO_TARGET);
    }

    public void setAutoTarget(boolean autoTarget) {
        entityData.set(DATA_AUTO_TARGET, autoTarget);
    }

    @Nullable
    public Entity findTarget() {
        List<Entity> players = level().getEntities(this, getBoundingBox().inflate(80), (e) -> e instanceof Player && targetSelector.test(e));
        List<Entity> monsters = level().getEntities(this, getBoundingBox().inflate(30), (e) -> e instanceof Monster && targetSelector.test(e));
        List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(10), targetSelector);
        if (players.size() > 0) {
            float shortestDistance = Float.MAX_VALUE;
            Entity maxEntity = players.get(0);
            for (Entity e : players) {
                if (e.distanceTo(this) < shortestDistance && (!(e instanceof Player player) || !player.getAbilities().instabuild)) {
                    shortestDistance = e.distanceTo(this);
                    maxEntity = e;
                }
            }
            return maxEntity;
        }
        if (monsters.size() > 0) {
            float shortestDistance = Float.MAX_VALUE;
            Entity maxEntity = monsters.get(0);
            for (Entity e : monsters) {
                if (e.distanceTo(this) < shortestDistance && (!(e instanceof Player player) || !player.getAbilities().instabuild)) {
                    shortestDistance = e.distanceTo(this);
                    maxEntity = e;
                }
            }
            return maxEntity;
        }
        if (entities.size() > 0) {
            float shortestDistance = Float.MAX_VALUE;
            Entity maxEntity = entities.get(0);
            for (Entity e : entities) {
                if (e.distanceTo(this) < shortestDistance && (!(e instanceof Player player) || !player.getAbilities().instabuild)) {
                    shortestDistance = e.distanceTo(this);
                    maxEntity = e;
                }
            }
            return maxEntity;
        }
        return null; // No entities within range
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TARGET_UUID, Optional.empty());
        entityData.define(DATA_AUTO_TARGET, true);
    }

    @Override
    public void tick() {
        if (tickCount >= 60 * 20) kill();
        if (level().isClientSide || level().hasChunkAt(this.blockPosition())) {

            speed *= 1.005;
            //super.tick();
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).normalize().scale(speed));
            level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
            if (getTargetUUID().isEmpty() || shouldAutoTarget()) {
                Entity e = findTarget();
                if (e != null) setTarget(e.getUUID());
                else setNoTarget();
            }

            Entity target = getTarget();

            if (target != null) {
                double x = target.getX() - getX();
                double y = (target.getY() + target.getEyeHeight()) - getY();
                double z = target.getZ() - getZ();
                double amt = 0.15;
                Vec3 vec = (new Vec3(x, y, z).normalize().multiply(amt, amt, amt).add(getDeltaMovement().normalize().multiply(1 - amt, 1 - amt, 1 - amt)));
                setDeltaMovement(vec.scale(speed));
                xPower = vec.x * speed;
                yPower = vec.y * speed;
                zPower = vec.z * speed;
            }
        } else {
            this.discard();
        }
    }

    @Override
    public void onHit(HitResult result) {
        Entity target = getTarget();
        if ((target != null && distanceToSqr(target) <= 9.0) || result instanceof BlockHitResult) {
            HitResult.Type type = result.getType();
            if (type == HitResult.Type.ENTITY) {
                this.onHitEntity((EntityHitResult) result);
                this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
            } else if (type == HitResult.Type.BLOCK) {
                BlockHitResult blockhitresult = (BlockHitResult) result;
                this.onHitBlock(blockhitresult);
                BlockPos blockpos = blockhitresult.getBlockPos();
                this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
            }
            if (!this.level().isClientSide) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
                this.discard();
            }
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity target = getTarget();
        if (target != null && distanceToSqr(target) <= 9.0) super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitresult) {
        super.onHitBlock(hitresult);
    }
}