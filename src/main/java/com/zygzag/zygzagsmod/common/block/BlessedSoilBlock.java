package com.zygzag.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlessedSoilBlock extends CustomFarmland {
    public BlessedSoilBlock(Properties prop) {
        super(prop);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        BlockPos abovePos = pos.above();
        BlockState above = world.getBlockState(abovePos);
        if (above.getBlock() instanceof CropBlock crop && above.getValue(CropBlock.AGE) < crop.getMaxAge() && Math.random() <= .25) {
            crop.growCrops(world, abovePos, above);
            world.levelEvent(1505, abovePos, 0);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}