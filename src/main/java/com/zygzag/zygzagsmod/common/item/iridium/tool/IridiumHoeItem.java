package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumHoeItem extends HoeItem implements ISocketable {
    Socket socket;
    public IridiumHoeItem(Tier tier, int damage, float speed, Properties properties, Socket socket) {
        super(tier, damage, speed, properties);
        this.socket = socket;
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.i;
        MutableComponent m;
        if (s != Socket.NONE && world != null) {
            String str = hasCooldown() ? "use" : "passive";
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            if (str.equals("passive")) m = Component.translatable(str + ".zygzagsmod").withStyle(ChatFormatting.GRAY);
            else m = Minecraft.getInstance().options.keyUse.getKey().getDisplayName().copy().withStyle(ChatFormatting.GRAY);
            m.append(Component.literal( ": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable( str + "_ability.zygzagsmod.hoe." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description." + str + "_ability.zygzagsmod.hoe." + socket.name().toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                comp.append(Component.literal(getCooldown(stack, world) / 20f + " ").withStyle(ChatFormatting.GOLD));
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
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (socket == Socket.WITHER_SKULL && toolAction == ToolActions.SWORD_SWEEP) return true;
        return super.canPerformAction(stack, toolAction);
    }

    @NotNull
    @Override
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        if (socket == Socket.WITHER_SKULL) return target.getBoundingBox().inflate(5f);
        return super.getSweepHitBox(stack, player, target);
    }
}