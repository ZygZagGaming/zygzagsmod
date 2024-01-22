package com.zygzag.zygzagsmod.common.networking.packet;

import com.zygzag.zygzagsmod.common.util.LerpedRotation;
import com.zygzag.zygzagsmod.common.util.Rotation;
import com.zygzag.zygzagsmod.common.util.SimpleRotation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public record ClientboundBlazeSentryRotationPacket(UUID uuid, LerpedRotation head, LerpedRotation body) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(MODID, "bsentry_rotation");

    public ClientboundBlazeSentryRotationPacket(FriendlyByteBuf buf) {
        this(buf.readUUID(), new LerpedRotation(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()), new LerpedRotation(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeFloat(head.xRot); buf.writeFloat(head.oldXRot); buf.writeFloat(head.yRot); buf.writeFloat(head.oldYRot);
        buf.writeFloat(body.xRot); buf.writeFloat(body.oldXRot); buf.writeFloat(body.yRot); buf.writeFloat(body.oldYRot);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
