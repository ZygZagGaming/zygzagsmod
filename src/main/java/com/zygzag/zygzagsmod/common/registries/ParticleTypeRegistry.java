package com.zygzag.zygzagsmod.common.registries;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ParticleTypeRegistry extends Registry<ParticleType<?>> {
    public static final ParticleTypeRegistry INSTANCE = new ParticleTypeRegistry(DeferredRegister.create(Registries.PARTICLE_TYPE, MODID));
    public ParticleTypeRegistry(DeferredRegister<ParticleType<?>> register) {
        super(register);
    }

    public static RegistryObject<SimpleParticleType> END_SAND_PARTICLES = INSTANCE.register(
            "end_sand_particles",
            () -> new SimpleParticleType(false)
    );
}
