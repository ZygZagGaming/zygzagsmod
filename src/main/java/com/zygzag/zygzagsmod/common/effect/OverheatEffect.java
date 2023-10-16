package com.zygzag.zygzagsmod.common.effect;

import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverheatEffect extends MobEffect {
    public static final ResourceKey<DamageType> OVERHEAT_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "overheat"));
    public static DamageSource overheatDamage(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OVERHEAT_DAMAGE_TYPE));
    }

    public OverheatEffect() {
        super(MobEffectCategory.HARMFUL, 0xff6633);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        entity.setRemainingFireTicks((amplifier + 1) * 3 * 20);
        int fireResistance = GeneralUtil.fireResistance(entity);
        float dmg = 8 - 8 * GeneralUtil.clamp(fireResistance / 10f, 0, 1);
        if (dmg > 0) entity.hurt(overheatDamage(entity.level().registryAccess()), dmg);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
