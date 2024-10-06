package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundBlazeSentryRotationPacket;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundRotationSetPacket;
import io.github.zygzaggaming.zygzagsmod.common.util.HasRotations;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundRotationSetPacketHandler {
    private static final ClientboundRotationSetPacketHandler INSTANCE = new ClientboundRotationSetPacketHandler();

    public static ClientboundRotationSetPacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundRotationSetPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Entity entity = player.level().getEntities().get(data.uuid());
            if (entity instanceof HasRotations<?> rEntity) {
                rEntity.setRotation(data.index(), data.newRotation());
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MODID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}
