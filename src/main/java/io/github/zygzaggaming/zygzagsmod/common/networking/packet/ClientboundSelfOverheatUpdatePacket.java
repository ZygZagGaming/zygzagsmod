package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ClientboundSelfOverheatUpdatePacket(int overheat) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "self_overheat_update");
    public static final Type<ClientboundSelfOverheatUpdatePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ClientboundSelfOverheatUpdatePacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeInt(packet.overheat),
            (buf) -> new ClientboundSelfOverheatUpdatePacket(buf.readInt())
    );

    @Override
    public Type<ClientboundSelfOverheatUpdatePacket> type() {
        return TYPE;
    }
}
