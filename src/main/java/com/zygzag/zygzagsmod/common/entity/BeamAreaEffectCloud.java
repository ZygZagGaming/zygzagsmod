package com.zygzag.zygzagsmod.common.entity;

import com.google.common.collect.Maps;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BeamAreaEffectCloud extends AreaEffectCloud {
    private final int duration = 5 * 20;
    private final float height = 7.5f;
    private static final EntityDataAccessor<Direction> DATA_DIRECTION = SynchedEntityData.defineId(BeamAreaEffectCloud.class, EntityDataSerializers.DIRECTION);
    private final MobEffectInstance overheatInstance = new MobEffectInstance(MobEffectRegistry.OVERHEAT_EFFECT.get(), 22, 0, true, false, false);
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public BeamAreaEffectCloud(EntityType<? extends BeamAreaEffectCloud> type, Level world) {
        super(type, world);
    }

    public BeamAreaEffectCloud(Level world) {
        this(EntityTypeRegistry.BEAM_AREA_EFFECT_CLOUD.get(), world);
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

    public void tick() {
        baseTick();
        if (level().isClientSide) {
            for (int particles = 0; particles < 24; ++particles) {
                double x = getX() + random.nextGaussian() * 0.2;
                double y = getY() + random.nextGaussian() * 0.125;
                double z = getZ() + random.nextGaussian() * 0.2;
                Vec3i delta = getDirection().getNormal();

                level().addAlwaysVisibleParticle(ParticleTypeRegistry.OVERHEAT_BEAM_PARTICLES.get(), x, y, z, delta.getX(), delta.getY(), delta.getZ());
            }
        } else {
            if (tickCount >= duration) {
                discard();
                return;
            }

            if (tickCount % 5 == 0) {
                victims.entrySet().removeIf((entry) -> tickCount >= entry.getValue());

                List<LivingEntity> entitiesInBoundingBox = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
                if (!entitiesInBoundingBox.isEmpty()) {
                    for (LivingEntity living : entitiesInBoundingBox) {
                        if (!victims.containsKey(living) && living.isAffectedByPotions()) {
                            victims.put(living, tickCount + 20);
                            living.addEffect(new MobEffectInstance(overheatInstance), this);
                        }
                    }
                }
            }
        }

    }

    @Override
    protected AABB makeBoundingBox() {
        double minX = -0.5, minY = -0.5, minZ = -0.5, maxX = 0.5, maxY = 0.5, maxZ = 0.5;
        Direction direction = getDirection();
        if (direction == Direction.UP) maxY += height - 1;
        if (direction == Direction.DOWN) minY -= height - 1;
        if (direction == Direction.SOUTH) maxZ += height - 1;
        if (direction == Direction.NORTH) maxZ -= height - 1;
        if (direction == Direction.EAST) maxX += height - 1;
        if (direction == Direction.WEST) maxX -= height - 1;
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
}
