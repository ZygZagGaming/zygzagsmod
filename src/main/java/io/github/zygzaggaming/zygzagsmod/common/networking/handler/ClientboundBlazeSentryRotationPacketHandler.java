package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundBlazeSentryRotationPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundBlazeSentryRotationPacketHandler {
    private static final ClientboundBlazeSentryRotationPacketHandler INSTANCE = new ClientboundBlazeSentryRotationPacketHandler();

    public static ClientboundBlazeSentryRotationPacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundBlazeSentryRotationPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.level().getEntitiesOfClass(BlazeSentry.class, player.getBoundingBox().inflate(100), (it) -> it.getUUID().equals(data.uuid())).stream().findFirst().ifPresent(blaze -> {
                blaze.rotations.get(0).set(data.head().xRot, data.head().yRot);
                blaze.rotations.get(0).setOldXRot(data.head().oldXRot);
                blaze.rotations.get(0).setOldYRot(data.head().oldYRot);
                blaze.rotations.get(1).set(data.body().xRot, data.body().yRot);
                blaze.rotations.get(1).setOldXRot(data.body().oldXRot);
                blaze.rotations.get(1).setOldYRot(data.body().oldYRot);
            });
        }).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MODID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}
