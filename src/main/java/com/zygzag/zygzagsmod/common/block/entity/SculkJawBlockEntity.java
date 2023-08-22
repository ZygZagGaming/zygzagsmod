package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registries.BlockEntityRegistry;
import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SculkJawBlockEntity extends BlockEntity {
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.SCULK_JAW.getBlockEntityType(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {

    }
}
