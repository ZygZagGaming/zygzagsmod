package com.zygzag.zygzagsmod.common.entity;

import com.google.common.collect.Maps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractBeamAreaEffectCloud extends AreaEffectCloud {
    private static final EntityDataAccessor<Direction> DATA_DIRECTION = SynchedEntityData.defineId(AbstractBeamAreaEffectCloud.class, EntityDataSerializers.DIRECTION);
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public AbstractBeamAreaEffectCloud(EntityType<? extends AbstractBeamAreaEffectCloud> type, Level world) {
        super(type, world);
    }

    public Direction getDirection() {
        return entityData.get(DATA_DIRECTION);
    }

    public void setDirection(Direction direction) {
        entityData.set(DATA_DIRECTION, direction);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_DIRECTION, Direction.UP);
    }

    public void addParticles() {
        ParticleOptions particle = getParticle();
        for (int particles = 0; particles < 24; ++particles) {
            double x = getX() + random.nextGaussian() * 0.2;
            double y = getY() + random.nextGaussian() * 0.2;
            double z = getZ() + random.nextGaussian() * 0.2;
            Vec3i delta = getDirection().getNormal();
 
            level().addAlwaysVisibleParticle(particle, x, y, z, delta.getX(), delta.getY(), delta.getZ());
        }
    }

    public abstract ParticleOptions getParticle();

    public void tick() {
        baseTick();
        if (level().isClientSide) addParticles();
        else {
            if (tickCount >= getDurationInTicks()) {
                discard();
                return;
            }

            if (tickCount % 5 == 0) {
                victims.entrySet().removeIf((entry) -> tickCount >= entry.getValue());

                List<LivingEntity> entitiesInBoundingBox = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
                if (!entitiesInBoundingBox.isEmpty()) {
                    for (LivingEntity living : entitiesInBoundingBox) {
                        if (!victims.containsKey(living) && living.isAffectedByPotions()) {
                            victims.put(living, tickCount + reHitCooldown());
                            afflict(living);
                        }
                    }
                }

                List<ItemEntity> itemEntitiesInBoundingBox = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox());
                if (!itemEntitiesInBoundingBox.isEmpty()) {
                    for (ItemEntity item : itemEntitiesInBoundingBox) afflictItem(item);
                }
            }
        }
    }

    public void afflictItem(ItemEntity item) { }

    public abstract void afflict(LivingEntity living);

    public int reHitCooldown() {
        return 20;
    }

    @Override
    protected AABB makeBoundingBox() {
        double minX = -0.5, minY = -0.5, minZ = -0.5, maxX = 0.5, maxY = 0.5, maxZ = 0.5;
        Direction direction = getDirection();
        switch (direction) {
            case UP -> maxY += getBeamHeight() - 1;
            case DOWN -> minY -= getBeamHeight() - 1;
            case SOUTH -> maxZ += getBeamHeight() - 1;
            case NORTH -> maxZ -= getBeamHeight() - 1;
            case EAST -> maxX += getBeamHeight() - 1;
            case WEST -> maxX -= getBeamHeight() - 1;
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ).move(position());
    }

    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(1f, 1f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag json) {
        super.readAdditionalSaveData(json);
        setDirection(Direction.valueOf(json.getString("direction").toUpperCase(Locale.ROOT)));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag json) {
        super.addAdditionalSaveData(json);
        json.putString("direction", getDirection().toString().toLowerCase(Locale.ROOT));
    }

    public abstract float getBeamHeight();
    public abstract int getDurationInTicks();
}
