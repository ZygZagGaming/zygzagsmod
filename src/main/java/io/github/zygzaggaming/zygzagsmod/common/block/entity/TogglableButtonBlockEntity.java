package io.github.zygzaggaming.zygzagsmod.common.block.entity;

import io.github.zygzaggaming.zygzagsmod.common.block.TogglableButtonBlock;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TogglableButtonBlockEntity extends BlockEntity {
    public int ticksPressed = 0;
    public final int ticksToStayPressed;
    public TogglableButtonBlockEntity(BlockPos pos, BlockState state, int ticksToStayPressed) {
        super(BlockEntityRegistry.TOGGLABLE_BUTTON.get(), pos, state);
        this.ticksToStayPressed = ticksToStayPressed;
    }

    public TogglableButtonBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 2 * 60 * 20);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (ticksPressed > 0) {
            ticksPressed--;
            if (ticksPressed == 0) depress(world, pos, state);
        }
    }

    public void depress(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof TogglableButtonBlock tbb) tbb.depress(world, pos, state);
        ticksPressed = 0;
    }

    public void pressed() {
        ticksPressed = ticksToStayPressed;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("ticks_pressed", ticksPressed);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        ticksPressed = tag.getInt("ticks_pressed");
        super.loadAdditional(tag, provider);
    }
}
