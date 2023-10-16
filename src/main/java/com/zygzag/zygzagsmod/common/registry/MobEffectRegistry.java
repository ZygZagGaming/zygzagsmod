package com.zygzag.zygzagsmod.common.registry;


import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.effect.OverheatEffect;
import com.zygzag.zygzagsmod.common.effect.SightEffect;
import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class MobEffectRegistry extends Registry<MobEffect> {
    public static final MobEffectRegistry INSTANCE = new MobEffectRegistry(DeferredRegister.create(Registries.MOB_EFFECT, MODID));
    public MobEffectRegistry(DeferredRegister<MobEffect> register) {
        super(register);
    }

    public static RegistryObject<MobEffect> SIGHT_EFFECT = INSTANCE.register(
            "sight",
            () -> new SightEffect(
                    MobEffectCategory.BENEFICIAL,
                    Config.sightEffectColor,
                    (b) -> b.is(Tags.Blocks.ORES),
                    GeneralUtil::getColor
            )
    );

    public static RegistryObject<MobEffect> OVERHEAT_EFFECT = INSTANCE.register(
            "overheat",
            OverheatEffect::new
    );
}
