package com.zygzag.zygzagsmod.common.entity;

import com.google.common.collect.Maps;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BeamAreaEffectCloud extends AreaEffectCloud {
    private final int duration = 5 * 20;
    private final float height = 7.5f;
    private final MobEffectInstance overheatInstance = new MobEffectInstance(MobEffectRegistry.OVERHEAT_EFFECT.get(), 22);
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public BeamAreaEffectCloud(EntityType<? extends BeamAreaEffectCloud> type, Level world) {
        super(type, world);
    }

    public BeamAreaEffectCloud(Level world) {
        this(EntityTypeRegistry.BEAM_AREA_EFFECT_CLOUD.get(), world);
    }

    public void tick() {
        baseTick();
        if (level().isClientSide) {
            for (int particles = 0; particles < 24; ++particles) {
                double x = getX() + random.nextGaussian() * 0.2;
                double y = getY() + random.nextGaussian() * 0.125;
                double z = getZ() + random.nextGaussian() * 0.2;

                level().addAlwaysVisibleParticle(ParticleTypeRegistry.OVERHEAT_BEAM_PARTICLES.get(), x, y, z, 0, 0, 0);
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

    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(1f, height);
    }
}
