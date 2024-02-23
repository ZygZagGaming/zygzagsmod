package io.github.zygzaggaming.zygzagsmod.common.block.entity;

import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.SoundEventRegistry;
import io.github.zygzaggaming.zygzagsmod.common.block.CacheBlock;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
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
            playSound(SoundEventRegistry.CACHE_OPEN.get());
            if (level != null) level.setBlockAndUpdate(getBlockPos(), state.setValue(CacheBlock.OPEN, true));
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            playSound(SoundEventRegistry.CACHE_CLOSE.get());
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
            level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.6f);
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
        Direction facing = state.getValue(CacheBlock.FACING);
        boolean isOpen = state.getValue(CacheBlock.OPEN);
        if (wasOpen != isOpen && level.isClientSide) {
            String[] animPool = isOpen ? OPEN_ANIMS : CLOSE_ANIMS;
            Direction facingDirection = getBlockState().getValue(CacheBlock.FACING);
            for (int i = 0; i < 4; i++) {
                int rotDegrees = ROTATION_CHECK_ORDER[i];
                Direction directionToCheck = Direction.fromYRot(facingDirection.toYRot() + rotDegrees);
                if (!canOpenInDirection(directionToCheck)) continue;
                triggerAnim(controller.getName(), animPool[i]);
                break;
            }
        }
        super.setBlockState(state);
    }

    public boolean canOpenInDirection(Direction direction) {
        Direction facing = getBlockState().getValue(CacheBlock.FACING);
        return level == null || canOpenIntoShape(level.getBlockState(worldPosition.relative(direction)).getShape(level, getBlockPos()), facing, direction, facing == direction);
    }

    public boolean canOpenIntoShape(VoxelShape collisionShape, Direction facing, Direction relativeDir, boolean isFrontOpen) {
        return !GeneralUtil.intersects(collisionShape, GeneralUtil.rotated(new AABB(0, isFrontOpen ? 0 : 0.5, 0, 1, 0.75, 0.5), Direction.Axis.Y, -1 + (int) (facing.toYRot() - relativeDir.toYRot()) / 90, new Vec3(0.5, 0.5, 0.5)));
    }

    @Override
    public boolean canOpen(Player player) {
        if (!super.canOpen(player)) return false;
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != Direction.Axis.Y && canOpenInDirection(direction)) return true;
        }
        return false;
    }
}