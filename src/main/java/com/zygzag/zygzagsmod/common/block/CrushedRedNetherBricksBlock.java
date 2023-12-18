package com.zygzag.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrushedRedNetherBricksBlock extends ColoredFallingBlock {
    public CrushedRedNetherBricksBlock(Properties properties) {
        super(new ColorRGBA(0x5c1c1b), properties);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState state2, boolean flag) {
        if (world.getBestNeighborSignal(pos) > 0) world.scheduleTick(pos, this, 2);
    }

    @Override
    public BlockState updateShape(BlockState state1, Direction dir, BlockState state2, LevelAccessor world, BlockPos pos1, BlockPos pos2) {
        if (world.getBestNeighborSignal(pos1) > 0) world.scheduleTick(pos1, this, 2);
        return state1;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos otherPos, boolean movedByPiston) {
        super.neighborChanged(state, world, pos, otherBlock, otherPos, movedByPiston);
        if (world.getBestNeighborSignal(pos) > 0) world.scheduleTick(pos, this, 2);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rng) {
        if (isFree(world.getBlockState(pos.below())) && pos.getY() >= world.getMinBuildHeight()) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(world, pos, state);
            this.falling(fallingblockentity);
            for (Direction dir : Direction.values()) {
                if (world.getBlockState(pos.relative(dir)).is(this)) world.scheduleTick(pos.relative(dir), this, 2);
            }
        }
    }
}
