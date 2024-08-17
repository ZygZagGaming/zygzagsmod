package io.github.zygzaggaming.zygzagsmod.common.registry.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public record BlockWithItemSupplier<B extends Block, I extends BlockItem>(ResourceLocation id, DeferredHolder<Block, B> block, DeferredHolder<Item, I> item) {
    public I getItem() {
        return item.value();
    }

    public B getBlock() {
        return block.value();
    }

    public ItemStack getDefaultItemStack() {
        return getItem().getDefaultInstance();
    }

    public BlockState getDefaultBlockState() {
        return getBlock().defaultBlockState();
    }
}