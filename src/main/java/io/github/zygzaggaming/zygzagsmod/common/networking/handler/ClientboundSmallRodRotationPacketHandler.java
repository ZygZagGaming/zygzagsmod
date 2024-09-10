package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundSmallRodRotationPacket;
import io.github.zygzaggaming.zygzagsmod.common.entity.SmallRod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientboundSmallRodRotationPacketHandler {
    private static final ClientboundSmallRodRotationPacketHandler INSTANCE = new ClientboundSmallRodRotationPacketHandler();

    public static ClientboundSmallRodRotationPacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ClientboundSmallRodRotationPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.level().getEntitiesOfClass(SmallRod.class, player.getBoundingBox().inflate(100), (it) -> it.getUUID().equals(data.uuid())).stream().findFirst().ifPresent(thisRod -> {
                thisRod.rotations.get(0).set(data.rodBone().xRot, data.rodBone().yRot);
                thisRod.rotations.get(0).setOldXRot(data.rodBone().oldXRot);
                thisRod.rotations.get(0).setOldYRot(data.rodBone().oldYRot);
            });
        }).exceptionally(e -> {
            context.disconnect(Component.translatable(Main.MODID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}
