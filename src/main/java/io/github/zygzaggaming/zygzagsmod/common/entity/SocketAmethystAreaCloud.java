package io.github.zygzaggaming.zygzagsmod.common.entity;

import com.google.common.collect.Maps;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.IridiumSwordItem;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ParticleTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.ModUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SocketAmethystAreaCloud extends AreaEffectCloud {
    private final int duration = 3;
    private final float radius = 0.5f;
    private final Map<Entity, Integer> victims = Maps.newHashMap();

    public SocketAmethystAreaCloud(EntityType<? extends SocketAmethystAreaCloud> type, Level world) {
        super(type, world);
    }

    public SocketAmethystAreaCloud(Level world) {
        this(EntityTypeRegistry.SOCKET_AMETHYST_AREA_CLOUD.get(), world);
    }

    @Override
    public float getRadius() {
        return radius;
    }

    public void tick() {
        baseTick();
        if (level().isClientSide) {
            for (int particles = 0; particles < 5; ++particles) {
                double x = random.nextGaussian() * 0.1;
                double y = random.nextGaussian() * 0.1;
                double z = random.nextGaussian() * 0.1;
                double multiplier = Math.pow(random.nextGaussian(), 2) / Math.sqrt(x * x + y * y + z * z) * radius / 6;

                level().addAlwaysVisibleParticle(ParticleTypeRegistry.AMETHYST_SPHERE_PARTICLES.get(), getX() + x, getY() + radius + y, getZ() + z, x * multiplier, y * multiplier, z * multiplier);
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
                            ModUtil.incrementEntityOverheat(living, 1);
                        }
                    }
                }
            }
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
