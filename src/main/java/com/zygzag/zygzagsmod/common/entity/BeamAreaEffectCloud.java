package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
        for (MobEffectInstance instance : getPotion().getEffects()) {
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
}
