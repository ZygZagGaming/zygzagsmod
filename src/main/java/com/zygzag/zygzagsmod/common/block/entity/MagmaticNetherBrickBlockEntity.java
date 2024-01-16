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
        int charges = state.getValue(CHARGES);
        if (pulse >= 20) {
            int nextCharges = 4;
            if (pulse <= 80) {
                nextCharges--;
                if (pulse <= 60) {
                    nextCharges--;
                    if (pulse <= 40) {
                        nextCharges--;
                        if (pulse == 20) nextCharges--;
                    }
                }
            }
            world.setBlockAndUpdate(pos, state.setValue(CHARGES, nextCharges).setValue(PULSE, pulse - 1));
        } else if (pulse > 0) {
            world.setBlockAndUpdate(pos, state.setValue(PULSE, pulse - 1));
        } else if (pulse == 0 && charges == 4) {
            world.setBlockAndUpdate(pos, state.setValue(CHARGES, 0));
        }
    }
}
