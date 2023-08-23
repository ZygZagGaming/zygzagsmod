package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.GeneralUtil;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.registry.EnchantmentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumAxeItem extends AxeItem implements ISocketable {
    Socket socket;
    public IridiumAxeItem(Tier tier, float damage, float speed, Properties properties, Socket socket) {
        super(tier, damage, speed, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.i;
        MutableComponent m;
        if (s != Socket.NONE && world != null) {
            String str = hasUseAbility() ? "use" : "passive";
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            if (str.equals("passive")) m = Component.translatable(str + ".zygzagsmod").withStyle(ChatFormatting.GRAY);
            else m = Minecraft.getInstance().options.keyUse.getKey().getDisplayName().copy().withStyle(ChatFormatting.GRAY);
            m.append(Component.literal( ": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable( str + "_ability.zygzagsmod.axe." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description." + str + "_ability.zygzagsmod.axe." + socket.name().toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                comp.append(Component.literal(Float.toString(getCooldown(stack, world) / 20f) + " ").withStyle(ChatFormatting.GOLD));
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
        return socket == Socket.WITHER_SKULL;
    }

    @Override
    public boolean hasUseAbility() {
        return socket == Socket.WITHER_SKULL;
    }

    @Override
    public int getCooldown(ItemStack stack, Level world) {
        int cooldownLevel = EnchantmentHelper.getTagEnchantmentLevel(EnchantmentRegistry.COOLDOWN_ENCHANTMENT.get(), stack);
        return socket == Socket.WITHER_SKULL ? Config.witherSkullAxeCooldown / (cooldownLevel + 1) : 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (socket == Socket.WITHER_SKULL && !player.getCooldowns().isOnCooldown(this)) {
            var range = Config.witherSkullAxeRange;
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range + 1; y++) {
                    for (int z = -range; z <= range; z++) {
                        var blockPos = new BlockPos(
                                player.getBlockX() + x,
                                player.getBlockY() + y,
                                player.getBlockZ() + z
                        );
                        if (player.getBoundingBox().getCenter().distanceToSqr(Vec3.atCenterOf(blockPos)) > range * range) continue;
                        var blockState = world.getBlockState(blockPos);
                        if (blockState.is(Main.VEGETATION_TAG)) {
                            var fluid = blockState.getFluidState();
                            var toReplace = fluid.is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                            world.setBlockAndUpdate(blockPos, toReplace);

                            GeneralUtil.particles(
                                    world,
                                    ParticleTypes.SMOKE,
                                    blockPos,
                                    5,
                                    0, 0.1, 0
                            );
                        }
                    }
                }
            }
            if (!player.getAbilities().instabuild) player.getCooldowns().addCooldown(this, getCooldown(stack, world));
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Nonnull
    @Override
    public AABB getSweepHitBox(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Entity target) {
        if (stack.getItem() instanceof IridiumAxeItem axe && axe.getSocket() == Socket.DIAMOND) {
            return target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D);
        }
        return super.getSweepHitBox(stack, player, target);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (socket == Socket.DIAMOND && toolAction == ToolActions.SWORD_SWEEP) return true;
        return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
    }
}