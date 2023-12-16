package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.zygzag.zygzagsmod.common.block.MagmaticNetherBrickBlock.CHARGES;
import static com.zygzag.zygzagsmod.common.block.MagmaticNetherBrickBlock.PULSE;

public class MagmaticNetherBrickBlockEntity extends BlockEntity {
    public MagmaticNetherBrickBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlockEntityType(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        int pulse = state.getValue(PULSE);
        if (pulse > 0) world.setBlockAndUpdate(pos, state.setValue(PULSE, pulse - 1));
        if (pulse == 0 && state.getValue(CHARGES) == 4) world.setBlockAndUpdate(pos, state.setValue(CHARGES, 0));
    }
}
