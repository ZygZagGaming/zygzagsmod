package io.github.zygzaggaming.zygzagsmod.common.registry;


import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import io.github.zygzaggaming.zygzagsmod.common.Config;
import io.github.zygzaggaming.zygzagsmod.common.effect.SightEffect;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class MobEffectRegistry extends AkomiRegistry<MobEffect> {
    public static final MobEffectRegistry INSTANCE = new MobEffectRegistry(DeferredRegister.create(Registries.MOB_EFFECT, MODID));
    public static Supplier<MobEffect> SIGHT_EFFECT = INSTANCE.register(
            "sight",
            () -> new SightEffect(
                    MobEffectCategory.BENEFICIAL,
                    Config.sightEffectColor,
                    (b) -> b.is(Tags.Blocks.ORES),
                    GeneralUtil::getColor
            )
    );

    public MobEffectRegistry(DeferredRegister<MobEffect> register) {
        super(register);
    }
}
