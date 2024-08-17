package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BeamAreaEffectCloud extends AbstractBeamAreaEffectCloud {
    public BeamAreaEffectCloud(EntityType<? extends BeamAreaEffectCloud> type, Level world) {
        super(type, world);
    }

    public BeamAreaEffectCloud(Level world) {
        this(EntityTypeRegistry.BEAM_AREA_EFFECT_CLOUD.get(), world);
    }

    @Override
    public ParticleOptions getParticle() {
        return entityData.get(DATA_PARTICLE);
    }

    @Override
    public void afflict(LivingEntity living) {
        for (MobEffectInstance instance : potionContents.getAllEffects()) {
            living.addEffect(new MobEffectInstance(instance.getEffect(), instance.getAmplifier(), instance.getDuration()));
        }
    }

    @Override
    public float getBeamHeight() {
        return 7.5f;
    }

    @Override
    public int getDurationInTicks() {
        return 2 * 20;
    }

    public static void spawn(Level world, BlockPos pos, Direction direction, PotionContents potion) {
        BeamAreaEffectCloud cloud = new BeamAreaEffectCloud(world);
        cloud.setDirection(direction);
        cloud.setPos(pos.getX() + 0.5 + direction.getNormal().getX(), pos.getY() + 0.5 + direction.getNormal().getY(), pos.getZ() + 0.5 + direction.getNormal().getZ());
        cloud.setPotionContents(potion);
        world.addFreshEntity(cloud);
    }
}
