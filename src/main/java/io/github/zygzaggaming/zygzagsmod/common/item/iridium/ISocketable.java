package io.github.zygzaggaming.zygzagsmod.common.item.iridium;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public interface ISocketable {
    static void addCooldown(Player player, ItemStack stack, int amount) {
        if (!player.getAbilities().instabuild) {
            player.getCooldowns().addCooldown(stack.getItem(), (int) Math.round(amount * player.getAttributeValue(AttributeRegistry.ABILITY_COOLDOWN_LENGTH)));
        }
    }

    static int getColor(ItemStack stack, int layer) {
        if (stack.getItem() instanceof ISocketable socketable && layer == 1) {
            return 0xff000000 + socketable.getSocket().color;
        } else return 0xffffffff;
    }

    Socket getSocket();

    boolean hasCooldown();

    boolean hasUseAbility();

    default int getBaseCooldown(ItemStack stack, Level world) {
        return 0;
    }

    default void addCooldown(Player player, ItemStack stack) {
        addCooldown(player, stack, getBaseCooldown(stack, player.level()));
    }

    default void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> text, TooltipFlag flag, String type) {
        Socket s = getSocket();
        Item i = s.itemSupplier.get();
        Level world = ctx.level();
        if (s != Socket.NONE && world != null) {
            String str = hasUseAbility() ? "use" : "passive";
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            MutableComponent m;
            if (hasUseAbility())
                m = Minecraft.getInstance().options.keyUse.getKey().getDisplayName().copy().withStyle(ChatFormatting.GRAY);
            else m = Component.translatable(str + ".zygzagsmod").withStyle(ChatFormatting.GRAY);
            m.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable(str + "_ability.zygzagsmod." + type + "." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description." + str + "_ability.zygzagsmod." + type + "." + socket.name().toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                // TODO: predict cooldown length taking into account attribute value
                comp.append(Component.literal(getBaseCooldown(stack, world) / 20f + " ").withStyle(ChatFormatting.GOLD));
                //text.add(Component.literal("\n"));
                text.add(comp);
            }
        }
    }
}