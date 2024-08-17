package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.util.LerpedRotation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ClientboundBlazeSentryRotationPacket(UUID uuid, LerpedRotation head, LerpedRotation body) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "bsentry_rotation");
    public static final Type<ClientboundBlazeSentryRotationPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundBlazeSentryRotationPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUUID(packet.uuid);
                buf.writeFloat(packet.head.xRot);
                buf.writeFloat(packet.head.oldXRot);
                buf.writeFloat(packet.head.yRot);
                buf.writeFloat(packet.head.oldYRot);
                buf.writeFloat(packet.body.xRot);
                buf.writeFloat(packet.body.oldXRot);
                buf.writeFloat(packet.body.yRot);
                buf.writeFloat(packet.body.oldYRot);
            },
            buf -> new ClientboundBlazeSentryRotationPacket(buf.readUUID(), new LerpedRotation(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()), new LerpedRotation(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
