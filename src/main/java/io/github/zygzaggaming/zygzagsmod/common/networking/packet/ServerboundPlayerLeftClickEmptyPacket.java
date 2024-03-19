package io.github.zygzaggaming.zygzagsmod.common.networking.packet;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ServerboundPlayerLeftClickEmptyPacket() implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "left_click_empty");

    public ServerboundPlayerLeftClickEmptyPacket(FriendlyByteBuf buf) {
        this();
    }

    @Override
    public void write(FriendlyByteBuf buf) { }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
