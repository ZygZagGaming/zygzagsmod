package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.block.CacheBlock;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CacheBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private final ChestLidController chestLidController = new ChestLidController();

    public CacheBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.getBlockEntityType(), pos, state);
    }

    @Override
    public float getOpenNess(float partialTick) {
        return chestLidController.getOpenness(partialTick);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.cache");
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, items);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, items);
        }
    }
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, id, playerInventory, this, 1);
    }

    void updateBlockState(BlockState state, boolean pushed) {
        if (level != null) level.setBlockAndUpdate(getBlockPos(), state.setValue(BarrelBlock.OPEN, pushed));
    }

    void playSound(BlockState state, SoundEvent sound) {
        if (level != null) {
            Vec3i facingNormal = state.getValue(CacheBlock.FACING).getNormal();
            double x = worldPosition.getX() + 0.5 + facingNormal.getX() * 0.5;
            double y = worldPosition.getY() + 0.5 + facingNormal.getY() * 0.5;
            double z = worldPosition.getZ() + 0.5 + facingNormal.getZ() * 0.5;
            level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
        }
    }

    public static void lidAnimateTick(Level world, BlockPos pos, BlockState state, CacheBlockEntity be) {
        be.chestLidController.tickLid();
    }
}
