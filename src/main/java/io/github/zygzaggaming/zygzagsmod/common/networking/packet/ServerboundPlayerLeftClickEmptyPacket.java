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
public record ServerboundPlayerLeftClickEmptyPacket() implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "left_click_empty");
    public static final Type<ServerboundPlayerLeftClickEmptyPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerLeftClickEmptyPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {},
            (buf) -> new ServerboundPlayerLeftClickEmptyPacket()
    );

    @Override
    public Type<ServerboundPlayerLeftClickEmptyPacket> type() {
        return TYPE;
    }
}
