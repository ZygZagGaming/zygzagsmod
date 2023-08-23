package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class DamageTypeRegistry extends Registry<DamageType> {
    public static final DamageTypeRegistry INSTANCE = new DamageTypeRegistry(DeferredRegister.create(Registries.DAMAGE_TYPE, MODID));
    public DamageTypeRegistry(DeferredRegister<DamageType> register) {
        super(register);
    }

    public static final RegistryObject<DamageType> SCULK_JAW = INSTANCE.register("sculk_jaw", () -> new DamageType("sculkJaw", 0.2F, DamageEffects.POKING));
}
