package com.zygzag.zygzagsmod.common.registry.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public record BlockItemEntitySupplier<B extends Block, I extends BlockItem, E extends BlockEntity>(
        ResourceLocation id, Supplier<B> block, Supplier<I> item,
        Supplier<BlockEntityType<E>> entity) {
    public I getItem() {
        return item.get();
    }

    public B getBlock() {
        return block.get();
    }

    public BlockEntityType<E> getBlockEntityType() {
        return entity.get();
    }

    public ItemStack getDefaultItemStack() {
        return getItem().getDefaultInstance();
    }

    public BlockState getDefaultBlockState() {
        return getBlock().defaultBlockState();
    }
}
