package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverheatBeamAreaEffectCloud extends AbstractBeamAreaEffectCloud {
    private final int duration = 5 * 20;
    private final float height = 7.5f;
    private final MobEffectInstance overheatInstance = new MobEffectInstance(MobEffectRegistry.OVERHEAT_EFFECT.get(), 22, 0, true, false, false);
    public OverheatBeamAreaEffectCloud(EntityType<? extends OverheatBeamAreaEffectCloud> type, Level world) {
        super(type, world);
    }

    public OverheatBeamAreaEffectCloud(Level world) {
        this(EntityTypeRegistry.OVERHEAT_BEAM_AREA_EFFECT_CLOUD.get(), world);
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
}
