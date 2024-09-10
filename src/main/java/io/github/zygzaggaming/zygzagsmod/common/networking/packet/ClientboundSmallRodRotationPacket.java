package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.util.LerpedRotation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ClientboundSmallRodRotationPacket(UUID uuid, LerpedRotation rodBone) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "small_rod_rotation");
    public static final Type<ClientboundSmallRodRotationPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundSmallRodRotationPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUUID(packet.uuid);
                buf.writeFloat(packet.rodBone.xRot);
                buf.writeFloat(packet.rodBone.oldXRot);
                buf.writeFloat(packet.rodBone.yRot);
                buf.writeFloat(packet.rodBone.oldYRot);
            },
            buf -> new ClientboundSmallRodRotationPacket(buf.readUUID(), new LerpedRotation(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat()))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
