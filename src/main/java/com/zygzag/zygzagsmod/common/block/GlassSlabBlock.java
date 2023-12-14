package com.zygzag.zygzagsmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlassSlabBlock extends SlabBlock {
    public GlassSlabBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        if (!state2.is(this)) return super.skipRendering(state1, state2, dir);
        VoxelShape state1Shape = switch (state1.getValue(TYPE)) {
            case DOUBLE -> Shapes.block();
            case TOP -> TOP_AABB;
            default -> BOTTOM_AABB;
        };
        VoxelShape state2Shape = switch (state2.getValue(TYPE)) {
            case DOUBLE -> Shapes.block();
            case TOP -> TOP_AABB;
            default -> BOTTOM_AABB;
        };
        return (state2.is(this) && state1Shape.getFaceShape(dir).equals(state2Shape.getFaceShape(dir))) || super.skipRendering(state1, state2, dir);
    }
}
