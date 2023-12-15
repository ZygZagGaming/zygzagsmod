package com.zygzag.zygzagsmod.common.registry.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public record BlockWithItemSupplier<B extends Block, I extends BlockItem>(ResourceLocation id, Supplier<B> block, Supplier<I> item) {
    public I getItem() {
        return item.get();
    }

    public B getBlock() {
        return block.get();
    }

    public ItemStack getDefaultItemStack() {
        return getItem().getDefaultInstance();
    }

    public BlockState getDefaultBlockState() {
        return getBlock().defaultBlockState();
    }
}