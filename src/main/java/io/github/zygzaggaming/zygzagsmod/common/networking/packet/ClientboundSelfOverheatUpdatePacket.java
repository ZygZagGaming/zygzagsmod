package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundSelfOverheatUpdatePacket(int overheat) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "self_overheat_update");

    public ClientboundSelfOverheatUpdatePacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(overheat);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
