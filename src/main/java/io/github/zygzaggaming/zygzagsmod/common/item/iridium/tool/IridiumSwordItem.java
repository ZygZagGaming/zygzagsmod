package io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool;

import io.github.zygzaggaming.zygzagsmod.common.Config;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class IridiumSwordItem extends SwordItem implements ISocketable {
    Socket socket;

    public IridiumSwordItem(Tier tier, Properties properties, Socket socket) {
        super(tier, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, ctx, text, flag, "sword");
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (socket == Socket.WITHER_SKULL) {
            AABB aabb = player.getBoundingBox().inflate(8);
            List<Monster> monsters = world.getEntitiesOfClass(Monster.class, aabb, (m) -> m.getHealth() <= 0.0);
            //System.out.println(monsters);
            if (!monsters.isEmpty()) {
                player.heal(100f);
                ISocketable.addCooldown(player, stack, Config.witherSkullSwordCooldown);
                return InteractionResultHolder.consume(stack);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown();
    }

    @Override
    public boolean hasCooldown() {
        return socket == Socket.WITHER_SKULL;
    }

    @Override
    public int getBaseCooldown(ItemStack stack, Level world) {
        if (socket == Socket.WITHER_SKULL) return Config.witherSkullSwordCooldown;
        else return 0;
    }
}
