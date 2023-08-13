package com.zygzag.zygzagsmod.common.item.iridium.armor;

import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class IridiumChestplateItem extends ArmorItem implements ISocketable {
    Socket socket;
    public IridiumChestplateItem(ArmorMaterial material, Properties properties, Socket socket) {
        super(material, EquipmentSlot.CHEST, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.i;
        MutableComponent m;
        if (s != Socket.NONE) {
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            m = Component.translatable("passive.zygzagsmod").withStyle(ChatFormatting.GRAY);
            m.append(Component.literal( ": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable("passive_ability.zygzagsmod.chestplate." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description.passive_ability.zygzagsmod.chestplate." + socket.name().toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                comp.append(Component.literal(Float.toString(getCooldown(stack, world) / 20f) + " ").withStyle(ChatFormatting.GOLD));
                comp.append(Component.translatable("zygzagsmod.seconds").withStyle(ChatFormatting.GRAY));
                //text.add(Component.literal("\n"));
                text.add(comp);
            }
        }
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