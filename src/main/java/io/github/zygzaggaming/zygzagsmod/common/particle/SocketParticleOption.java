package io.github.zygzaggaming.zygzagsmod.common.particle;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record SocketParticleOption(ParticleType<SocketParticleOption> type, Socket socket) implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
