package io.github.zygzaggaming.zygzagsmod.common.networking.handler;

import io.github.zygzaggaming.zygzagsmod.common.item.FlailItem;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ServerboundPlayerLeftClickEmptyPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerboundPlayerLeftClickEmptyPacketHandler {
    private static final ServerboundPlayerLeftClickEmptyPacketHandler INSTANCE = new ServerboundPlayerLeftClickEmptyPacketHandler();

    public static ServerboundPlayerLeftClickEmptyPacketHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final ServerboundPlayerLeftClickEmptyPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof FlailItem flailItem) flailItem.onSwing(player, stack);
        });
    }
}
