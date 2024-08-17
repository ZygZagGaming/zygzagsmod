package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundSelfOverheatUpdatePacket;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundSelfOverheatUpdatePacketHandler {
    private static final ClientboundSelfOverheatUpdatePacketHandler INSTANCE = new ClientboundSelfOverheatUpdatePacketHandler();

    public static ClientboundSelfOverheatUpdatePacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundSelfOverheatUpdatePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().setData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT_ATTACHMENT.get(), data.overheat());
        });
    }
}
