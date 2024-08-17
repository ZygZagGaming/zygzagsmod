package io.github.zygzaggaming.zygzagsmod.common.registry.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public record BlockItemEntitySupplier<B extends Block, I extends BlockItem, E extends BlockEntity>(
        ResourceLocation id, DeferredHolder<Block, B> block, DeferredHolder<Item, I> item,
        DeferredHolder<BlockEntityType<?>, BlockEntityType<E>> entity) {
    public I getItem() {
        return item.value();
    }

    public B getBlock() {
        return block.value();
    }

    public BlockEntityType<E> getBlockEntityType() {
        return entity.value();
    }

    public ItemStack getDefaultItemStack() {
        return getItem().getDefaultInstance();
    }

    public BlockState getDefaultBlockState() {
        return getBlock().defaultBlockState();
    }
}
