package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ParticleTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.ModUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverheatBeamAreaEffectCloud extends AbstractBeamAreaEffectCloud {
    private final int duration = 5 * 20;
    private final float height = 7.5f;
    private final BlockPos origin;
    public OverheatBeamAreaEffectCloud(EntityType<? extends OverheatBeamAreaEffectCloud> type, Level world, BlockPos origin) {
        super(type, world);
        this.origin = origin;
        this.setPos(origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5);
    }
    public OverheatBeamAreaEffectCloud(EntityType<? extends OverheatBeamAreaEffectCloud> type, Level world) {
        this(type, world, BlockPos.ZERO);
    }

    public OverheatBeamAreaEffectCloud(Level world, BlockPos origin) {
        this(EntityTypeRegistry.OVERHEAT_BEAM_AREA_EFFECT_CLOUD.get(), world, origin);
    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleTypeRegistry.OVERHEAT_BEAM_PARTICLES.get();
    }

    @Override
    public void afflict(LivingEntity living) {
        ModUtil.incrementEntityOverheat(living, 1);
    }

    @Override
    public int getDurationInTicks() {
        return duration;
    }

    @Override
    public float getBeamHeight() {
        return height;
    }

    public static void spawn(Level world, BlockPos pos, Direction direction) {
        OverheatBeamAreaEffectCloud cloud = new OverheatBeamAreaEffectCloud(world, pos);
        cloud.setDirection(direction);
        cloud.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.addFreshEntity(cloud);
    }

    @Override
    public void tick() {
        baseTick();
        if (level().isClientSide) addParticles();
        else {
            if (tickCount >= getDurationInTicks()) {
                discard();
                return;
            }

            List<LivingEntity> entitiesInBoundingBox = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
            if (!entitiesInBoundingBox.isEmpty()) for (LivingEntity living : entitiesInBoundingBox) if (living.isAffectedByPotions()) {
                afflict(living);
            }

            List<ItemEntity> itemEntitiesInBoundingBox = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox());
            if (!itemEntitiesInBoundingBox.isEmpty()) {
                for (ItemEntity item : itemEntitiesInBoundingBox) afflictItem(item);
            }
        }
    }

    @Override
    public void afflictItem(ItemEntity item) {
        if (!item.fireImmune()) item.kill();
    }
}
