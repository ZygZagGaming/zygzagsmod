package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundBlazeSentryRotationPacket;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundSelfOverheatUpdatePacket;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientboundSelfOverheatUpdatePacketHandler {
    private static final ClientboundSelfOverheatUpdatePacketHandler INSTANCE = new ClientboundSelfOverheatUpdatePacketHandler();

    public static ClientboundSelfOverheatUpdatePacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundSelfOverheatUpdatePacket data, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            context.player().ifPresent(player -> player.setData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT_ATTACHMENT.get(), data.overheat()));
        });
    }
}
