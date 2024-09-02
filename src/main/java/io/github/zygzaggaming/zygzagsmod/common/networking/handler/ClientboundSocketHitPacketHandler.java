package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundSocketHitPacket;
import io.github.zygzaggaming.zygzagsmod.common.particle.SocketParticleOption;
import io.github.zygzaggaming.zygzagsmod.common.registry.ParticleTypeRegistry;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public class ClientboundSocketHitPacketHandler {
    private static final ClientboundSocketHitPacketHandler INSTANCE = new ClientboundSocketHitPacketHandler();

    public static ClientboundSocketHitPacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundSocketHitPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Random random = new Random();
//            if (true) {
                for (int i = 0; i < 100; i++) {
                    var speed = 0.3f + 0.1f * random.nextDouble();
                    double xd = random.nextGaussian(), yd = random.nextGaussian(), zd = random.nextGaussian();
                    double magnitude = Math.sqrt(xd * xd + yd * yd + zd * zd);
                    xd *= speed / magnitude;
                    yd *= speed / magnitude;
                    zd *= speed / magnitude;
                    context.player().level().addParticle(
                            new SocketParticleOption(ParticleTypeRegistry.SOCKET_HIT.get(), data.socket()),
                            data.location().x(), data.location().y(), data.location().z(),
                            xd, yd, zd
                    );
                }
//            } else {
//                var speed = 0.35f + 0.05f * random.nextDouble();
//                double xd = random.nextGaussian(), yd = random.nextGaussian(), zd = random.nextGaussian();
//                double magnitude = Math.sqrt(xd * xd + yd * yd + zd * zd);
//                xd *= speed / magnitude;
//                yd *= speed / magnitude;
//                zd *= speed / magnitude;
//
//                for (int i = 0; i < 100; i++) {
//                    var mult = random.nextDouble();
//                    var speed2 = 0.05f * random.nextDouble();
//                    double xd2 = random.nextGaussian(), yd2 = random.nextGaussian(), zd2 = random.nextGaussian();
//                    double magnitude2 = Math.sqrt(xd2 * xd2 + yd2 * yd2 + zd2 * zd2);
//                    xd2 *= speed2 / magnitude2;
//                    yd2 *= speed2 / magnitude2;
//                    zd2 *= speed2 / magnitude2;
//                    context.player().level().addParticle(
//                            new SocketParticleOption(ParticleTypeRegistry.SOCKET_HIT.get(), data.socket()),
//                            data.location().x(), data.location().y(), data.location().z(),
//                            mult * xd + xd2, mult * yd + yd2, mult * zd + zd2
//                    );
//                }
//            }
        });
    }
}
