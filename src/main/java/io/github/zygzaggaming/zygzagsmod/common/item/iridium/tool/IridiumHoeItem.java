package io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumHoeItem extends HoeItem implements ISocketable {
    Socket socket;

    public IridiumHoeItem(Tier tier, int damage, float speed, Properties properties, Socket socket) {
        super(tier, properties);
        this.socket = socket;
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, ctx, text, flag, "hoe");
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
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        if (socket == Socket.WITHER_SKULL && ability == ItemAbilities.SWORD_SWEEP) return true;
        return super.canPerformAction(stack, ability);
    }

    @NotNull
    @Override
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        if (socket == Socket.WITHER_SKULL) return target.getBoundingBox().inflate(5f);
        return super.getSweepHitBox(stack, player, target);
    }
}