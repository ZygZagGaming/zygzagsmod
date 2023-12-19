package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverheatBeamAreaEffectCloud extends AbstractBeamAreaEffectCloud {
    private final int duration = 5 * 20;
    private final float height = 7.5f;
    private final BlockPos origin;
    private final MobEffectInstance overheatInstance = new MobEffectInstance(MobEffectRegistry.OVERHEAT_EFFECT.get(), 22, 0, true, false, false);
    public OverheatBeamAreaEffectCloud(EntityType<? extends OverheatBeamAreaEffectCloud> type, Level world, BlockPos origin) {
        super(type, world);
        this.origin = origin;
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
        living.addEffect(new MobEffectInstance(overheatInstance), this);
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
        cloud.setPos(pos.getX() + 0.5 + direction.getNormal().getX(), pos.getY() + 0.5 + direction.getNormal().getY(), pos.getZ() + 0.5 + direction.getNormal().getZ());
        world.addFreshEntity(cloud);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && !level().getBlockState(origin).is(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlock())) kill();
    }

    @Override
    public void afflictItem(ItemEntity item) {
        item.kill();
    }
}
