package com.zygzag.zygzagsmod.common.item.iridium.armor;

import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class IridiumChestplateItem extends ArmorItem implements ISocketable {
    Socket socket;
    public IridiumChestplateItem(ArmorMaterial material, Properties properties, Socket socket) {
        super(material, Type.CHESTPLATE, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, world, text, flag, "chestplate");
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean hasCooldown() {
        return false;
    }

    @Override
    public boolean hasUseAbility() {
        return false;
    }
}