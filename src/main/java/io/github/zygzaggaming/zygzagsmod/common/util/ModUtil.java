package io.github.zygzaggaming.zygzagsmod.common.util;

import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundSelfOverheatUpdatePacket;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ModUtil {
    public static void incrementEntityOverheat(LivingEntity entity, int increment) {
        if (increment != 0) {
            int old = entity.getData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT.get());
            entity.setData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT.get(), old + increment);
            if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundSelfOverheatUpdatePacket(old + increment));
        }
    }

    public static void setEntityOverheat(LivingEntity entity, int newOverheat) {
        int old = entity.getData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT.get());
        if (old != newOverheat) {
            entity.setData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT.get(), newOverheat);
            if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundSelfOverheatUpdatePacket(newOverheat));
        }
    }
}
