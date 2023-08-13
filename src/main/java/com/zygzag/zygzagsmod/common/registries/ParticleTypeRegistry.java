package com.zygzag.zygzagsmod.common.registries;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ParticleTypeRegistry extends Registry<ParticleType<?>> {
    public static final ParticleTypeRegistry INSTANCE = new ParticleTypeRegistry(DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID));
    public ParticleTypeRegistry(DeferredRegister<ParticleType<?>> register) {
        super(register);
    }

    public static RegistryObject<SimpleParticleType> END_SAND_PARTICLES = INSTANCE.register(
            "end_sand_particles",
            () -> new SimpleParticleType(false)
    );
}
