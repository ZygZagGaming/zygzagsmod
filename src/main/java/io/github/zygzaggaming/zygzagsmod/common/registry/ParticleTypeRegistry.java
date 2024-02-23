package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ParticleTypeRegistry extends AkomiRegistry<ParticleType<?>> {
    public static final ParticleTypeRegistry INSTANCE = new ParticleTypeRegistry(DeferredRegister.create(Registries.PARTICLE_TYPE, MODID));
    public static Supplier<SimpleParticleType> END_SAND_PARTICLES = INSTANCE.register(
            "end_sand_particles",
            () -> new SimpleParticleType(false)
    );
    public static Supplier<SimpleParticleType> SHOCKWAVE_PARTICLES = INSTANCE.register(
            "shockwave",
            () -> new SimpleParticleType(false)
    );
    public static Supplier<SimpleParticleType> OVERHEAT_BEAM_PARTICLES = INSTANCE.register(
            "overheat_beam",
            () -> new SimpleParticleType(false)
    );
    public static Supplier<SimpleParticleType> FLAMETHROW_PARTICLES = INSTANCE.register(
            "flamethrow",
            () -> new SimpleParticleType(false)
    );
    public static Supplier<SimpleParticleType> OVERHEAT_SPHERE_PARTICLES = INSTANCE.register(
            "overheat_sphere",
            () -> new SimpleParticleType(false)
    );
    public ParticleTypeRegistry(DeferredRegister<ParticleType<?>> register) {
        super(register);
    }
}
