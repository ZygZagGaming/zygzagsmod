package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.registry.EnchantmentRegistry;
import com.zygzag.zygzagsmod.common.registry.SocketRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class IridiumSwordItem extends SwordItem implements ISocketable {
    Supplier<Socket> socketSupplier;
    public IridiumSwordItem(Tier tier, int damage, float speed, Properties properties, Supplier<Socket> socketSupplier) {
        super(tier, damage, speed, properties);
        this.socketSupplier = socketSupplier;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.item;
        MutableComponent m;
        if (s != SocketRegistry.NONE.get() && world != null) {
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
            m.append(Component.translatable( str + "_ability.zygzagsmod.sword." + socket.name.toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description." + str + "_ability.zygzagsmod.sword." + socket.name.toLowerCase()));
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
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getSocket() == SocketRegistry.WITHER_SKULL.get()) {
            AABB aabb = player.getBoundingBox().inflate(8);
            List<Monster> monsters = world.getEntitiesOfClass(Monster.class, aabb, (m) -> m.getHealth() <= 0.0);
            //System.out.println(monsters);
            if (monsters.size() > 0) {
                player.heal(100f);
                ISocketable.addCooldown(player, stack, Config.witherSkullSwordCooldown);
                return InteractionResultHolder.consume(stack);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public Socket getSocket() {
        return socketSupplier.get();
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown();
    }

    @Override
    public boolean hasCooldown() {
        return getSocket() == SocketRegistry.WITHER_SKULL.get();
    }

    @Override
    public int getCooldown(ItemStack stack, Level world) {
        int cooldownLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.COOLDOWN_ENCHANTMENT.get(), stack);
        if (getSocket() == SocketRegistry.WITHER_SKULL.get()) return Config.witherSkullSwordCooldown / (cooldownLevel + 1);
        else return 0;
    }
}
