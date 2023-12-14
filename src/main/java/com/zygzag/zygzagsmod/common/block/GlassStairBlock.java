package com.zygzag.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GlassStairBlock extends StairBlock {
    public GlassStairBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
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
        VoxelShape state1Shape = (state1.getValue(HALF) == Half.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[getShapeIndex(state1)]];
        VoxelShape state2Shape = (state2.getValue(HALF) == Half.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[getShapeIndex(state2)]];
        return (state2.is(this) && state1Shape.getFaceShape(dir).equals(state2Shape.getFaceShape(dir.getOpposite()))) || super.skipRendering(state1, state2, dir);
    }
}
