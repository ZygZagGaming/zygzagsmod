package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.LerpedRotation;
import io.github.zygzaggaming.zygzagsmod.common.util.WorldlyRotation;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ClientboundRotationSetPacket(UUID uuid, int index, WorldlyRotation newRotation) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Main.MODID,  "entity_rotation");
    public static final Type<ClientboundRotationSetPacket> TYPE = new Type<>(ID);
    public static final Codec<ClientboundRotationSetPacket> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    GeneralUtil.UUID_CODEC.fieldOf("uuid").forGetter(ClientboundRotationSetPacket::uuid),
                    Codec.INT.fieldOf("index").forGetter(ClientboundRotationSetPacket::index),
                    WorldlyRotation.CODEC.fieldOf("new_rotation").forGetter(ClientboundRotationSetPacket::newRotation)
            ).apply(inst, ClientboundRotationSetPacket::new)
    );
    public static final StreamCodec<ByteBuf, ClientboundRotationSetPacket> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
