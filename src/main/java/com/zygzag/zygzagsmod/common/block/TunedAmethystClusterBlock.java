package com.zygzag.zygzagsmod.common.block;

import com.zygzag.zygzagsmod.common.block.entity.TunedAmethystClusterBlockEntity;
import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import com.zygzag.zygzagsmod.common.registries.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TunedAmethystClusterBlock extends AmethystClusterBlock implements EntityBlock {
    public TunedAmethystClusterBlock(Properties properties) {
        super(5, 3, properties);
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hitResult, Projectile projectile) {
        if (!world.isClientSide) {
            BlockPos blockpos = hitResult.getBlockPos();
            world.playSound(null, blockpos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            var be = world.getBlockEntity(hitResult.getBlockPos());
            if (be instanceof TunedAmethystClusterBlockEntity tacbe && tacbe.currentSound != null) {
                world.playSound(null, blockpos, tacbe.currentSound, SoundSource.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TunedAmethystClusterBlockEntity(pos, state);
    }

    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockentity = world.getBlockEntity(pos);
        if (blockentity instanceof TunedAmethystClusterBlockEntity tunedAmethyst) {
            if (!world.isClientSide && player.isCreative()) {
                ItemStack itemstack = BlockItemEntityRegistry.TUNED_AMETHYST_CLUSTER.getDefaultItemStack();
                blockentity.saveToItem(itemstack);

                ItemEntity itementity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                world.addFreshEntity(itementity);
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> drops = super.getDrops(state, builder);
        if (blockentity instanceof TunedAmethystClusterBlockEntity tacbe) {
            for (ItemStack drop : drops) {
                if (drop.is(BlockItemEntityRegistry.TUNED_AMETHYST_CLUSTER.getItem())) {

                }
            }
        }

        return drops;
    }
}

