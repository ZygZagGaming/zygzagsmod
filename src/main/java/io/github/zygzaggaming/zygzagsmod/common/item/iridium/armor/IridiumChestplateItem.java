package io.github.zygzaggaming.zygzagsmod.common.item.iridium.armor;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class IridiumChestplateItem extends ArmorItem implements ISocketable {
    Socket socket;

    public IridiumChestplateItem(Holder<ArmorMaterial> material, Properties properties, Socket socket) {
        super(material, Type.CHESTPLATE, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, ctx, text, flag, "chestplate");
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