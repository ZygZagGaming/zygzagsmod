package io.github.zygzaggaming.zygzagsmod.common.entity;

import com.google.common.collect.Maps;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ParticleTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.ModUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SphereAreaEffectCloud extends AreaEffectCloud {
    private final int duration = 5 * 20;
    private final float radius = 5.5f;
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public SphereAreaEffectCloud(EntityType<? extends SphereAreaEffectCloud> type, Level world) {
        super(type, world);
        this.potionContents = new PotionContents(Optional.empty(), Optional.empty(), List.of(new MobEffectInstance(MobEffects.WEAKNESS, 1, 0, true, false, false)));
    }

    public SphereAreaEffectCloud(Level world) {
        this(EntityTypeRegistry.SPHERE_AREA_EFFECT_CLOUD.get(), world);
    }

    @Override
    public float getRadius() {
        return radius;
    }

    public void tick() {
        baseTick();
        if (level().isClientSide) {
            for (int particles = 0; particles < 40; ++particles) {
                double x = random.nextGaussian() * 0.5;
                double y = random.nextGaussian() * 0.5;
                double z = random.nextGaussian() * 0.5;
                double multiplier = Math.pow(random.nextGaussian(), 2) / Math.sqrt(x * x + y * y + z * z) * radius / 6;

                level().addAlwaysVisibleParticle(ParticleTypeRegistry.OVERHEAT_SPHERE_PARTICLES.get(), getX() + x, getY() + radius + y, getZ() + z, x * multiplier, y * multiplier, z * multiplier);
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
                        if (!victims.containsKey(living) && living.isAffectedByPotions() && living.getBoundingBox().getCenter().distanceToSqr(getBoundingBox().getCenter()) <= radius * radius * 1.5) {
                            victims.put(living, tickCount + 20);
                        }
                    }
                }
            }
            for (var victim : victims.keySet()) if (victim instanceof LivingEntity living) ModUtil.incrementEntityOverheat(living, 1);
        }
    }

    @Override
    protected AABB makeBoundingBox() {
        return new AABB(position().x() - radius, position().y(), position().z() - radius, position().x() + radius, position().y() + 2 * radius, position().z() + radius);
    }

    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(2 * radius, 2 * radius);
    }
}
