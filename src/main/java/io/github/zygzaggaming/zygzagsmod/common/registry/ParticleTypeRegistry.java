package io.github.zygzaggaming.zygzagsmod.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.loot.ExecutionerModifier;
import io.github.zygzaggaming.zygzagsmod.common.particle.SocketParticleOption;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
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
    public static Supplier<ParticleType<SocketParticleOption>> SOCKET_HIT = INSTANCE.register(
            "socket_hit",
            () -> new ParticleType<>(false) {
                public MapCodec<SocketParticleOption> codec() {
                    return RecordCodecBuilder.mapCodec(
                            (RecordCodecBuilder.Instance<SocketParticleOption> inst) -> inst
                                    .group(
                                            Codec.INT.comapFlatMap(
                                                    (i) -> 0 <= i && i < Socket.values().length ? DataResult.success(Socket.values()[i]) : DataResult.error(() -> "index out of bounds"),
                                                    Enum::ordinal
                                            ).fieldOf("socket_ordinal").forGetter(SocketParticleOption::socket)
                                    ).apply(inst, (socket) -> new SocketParticleOption(this, socket))
                    );
                }

                public StreamCodec<? super RegistryFriendlyByteBuf, SocketParticleOption> streamCodec() {
                    return StreamCodec.composite(ByteBufCodecs.INT, (option) -> option.socket().ordinal(), (i) -> new SocketParticleOption(this, Socket.values()[i]));
                }
            }
    );
    public ParticleTypeRegistry(DeferredRegister<ParticleType<?>> register) {
        super(register);
    }
}
