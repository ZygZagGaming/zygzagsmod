package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.block.CacheBlock;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CacheBlockEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity {
    public static final int[] ROTATION_CHECK_ORDER = { 90, 270, 180, 0 };
    public static final String[] OPEN_ANIMS = {
            "animation.red_nether_brick_cache.open_left",
            "animation.red_nether_brick_cache.open_right",
            "animation.red_nether_brick_cache.open_back",
            "animation.red_nether_brick_cache.open_front"
    };
    public static final String[] CLOSE_ANIMS = {
            "animation.red_nether_brick_cache.close_left",
            "animation.red_nether_brick_cache.close_right",
            "animation.red_nether_brick_cache.close_back",
            "animation.red_nether_brick_cache.close_front"
    };

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            playSound(SoundEvents.BARREL_OPEN);
            if (level != null) level.setBlockAndUpdate(getBlockPos(), state.setValue(CacheBlock.OPEN, true));
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            playSound(SoundEvents.BARREL_CLOSE);
            if (level != null) level.setBlockAndUpdate(getBlockPos(), state.setValue(CacheBlock.OPEN, false));
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldCount, int newCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof ChestMenu menu && menu.getContainer() == CacheBlockEntity.this;
        }
    };

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private final AnimationController<CacheBlockEntity> controller = new AnimationController<>(this, this::updateAnimations);

    public CacheBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.getBlockEntityType(), pos, state);
        for (int i = 0; i < 4; i++) {
            controller.triggerableAnim(OPEN_ANIMS[i], RawAnimation.begin().thenPlayAndHold(OPEN_ANIMS[i]));
            controller.triggerableAnim(CLOSE_ANIMS[i], RawAnimation.begin().thenPlayAndHold(CLOSE_ANIMS[i]));
        }
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

    void playSound(SoundEvent sound) {
        if (level != null) {
            double x = worldPosition.getX() + 0.5;
            double y = worldPosition.getY() + 1;
            double z = worldPosition.getZ() + 0.5;
            level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(controller);
    }

    protected PlayState updateAnimations(final AnimationState<CacheBlockEntity> state) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void startOpen(Player player) {
        if (!remove && !player.isSpectator()) {
            openersCounter.incrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!remove && !player.isSpectator()) {
            openersCounter.decrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    @Override
    public void setBlockState(BlockState state) {
        boolean wasOpen = getBlockState().getValue(CacheBlock.OPEN);
        boolean isOpen = state.getValue(CacheBlock.OPEN);
        if (wasOpen != isOpen && level.isClientSide) {
            String[] animPool = isOpen ? OPEN_ANIMS : CLOSE_ANIMS;
            Direction facingDirection = getBlockState().getValue(CacheBlock.FACING);
            for (int i = 0; i < 4; i++) {
                int rotDegrees = ROTATION_CHECK_ORDER[i];
                Direction directionToCheck = Direction.fromYRot(facingDirection.toYRot() + rotDegrees);
                if (level != null && !level.getBlockState(worldPosition.relative(directionToCheck)).getCollisionShape(level, getBlockPos()).isEmpty()) continue;
                triggerAnim(controller.getName(), animPool[i]);
                break;
            }
        }
        super.setBlockState(state);
    }

    @Override
    public boolean canOpen(Player player) {
        if (!super.canOpen(player)) return false;
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != Direction.Axis.Y && level.getBlockState(worldPosition.relative(direction)).isAir()) return true;
        }
        return false;
    }
}