package com.zygzag.zygzagsmod.common.block;

import com.zygzag.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SculkJawBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final IntegerProperty CLOSED = IntegerProperty.create("closed", 0, 3);
    public static final DirectionProperty FACE = DirectionProperty.create("face");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final Map<Direction, VoxelShape[]> SHAPES_FOR_CLOSING = Map.of(
            Direction.UP,
            new VoxelShape[]{
                    Block.box(0, 0, 0, 16, 1, 16),
                    Block.box(2, 0, 0, 14, 4, 16),
                    Block.box(4, 0, 0, 12, 6, 16),
                    Block.box(6, 0, 0, 10, 8, 16),
            },
            Direction.DOWN,
            new VoxelShape[]{
                    Block.box(0, 15, 0, 16, 16, 16),
                    Block.box(2, 12, 0, 14, 16, 16),
                    Block.box(4, 10, 0, 12, 16, 16),
                    Block.box(6, 8, 0, 10, 16, 16),
            },
            Direction.EAST,
            new VoxelShape[]{
                    Block.box(0, 0, 0, 1, 16,16),
                    Block.box(0, 0, 2, 4, 16, 14),
                    Block.box(0, 0, 4, 6, 16, 12),
                    Block.box(0, 0, 6, 8, 16, 10),
            },
            Direction.WEST,
            new VoxelShape[]{
                    Block.box(15, 0, 0, 16, 16, 16),
                    Block.box(12, 0, 2, 16, 16, 14),
                    Block.box(10, 0, 4, 16, 16, 12),
                    Block.box(8, 0, 6, 16, 16, 10),
            },
            Direction.NORTH,
            new VoxelShape[]{
                    Block.box(0, 0, 15, 16, 16, 16),
                    Block.box(2, 0, 12, 14, 16, 16),
                    Block.box(4, 0, 10, 12, 16, 16),
                    Block.box(6, 0, 8, 10, 16, 16),
            },
            Direction.SOUTH,
            new VoxelShape[]{
                    Block.box(0, 0, 0, 16, 16,1),
                    Block.box(2, 0, 0, 14, 16, 4),
                    Block.box(4, 0, 0, 12, 16, 6),
                    Block.box(6, 0, 0, 10, 16, 8),
            }
    );
    public SculkJawBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CLOSED, 0).setValue(FACE, Direction.UP).setValue(POWERED, false).setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(FACE, context.getClickedFace()).setValue(WATERLOGGED, fluid.is(Fluids.WATER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_FOR_CLOSING.containsKey(state.getValue(FACE)) ? SHAPES_FOR_CLOSING.get(state.getValue(FACE))[state.getValue(CLOSED)] : Shapes.empty();
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        var be = world.getBlockEntity(pos);
        if (be instanceof SculkJawBlockEntity sculkJaw && sculkJaw.latchedEntity == null && state.getValue(CLOSED) == 0 && !state.getValue(POWERED) && getShape(state, world, pos, CollisionContext.empty()).bounds().move(pos).intersects(entity.getBoundingBox())) sculkJaw.latchOnto(world, pos, state, entity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLOSED, FACE, POWERED, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SculkJawBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == BlockItemEntityRegistry.SCULK_JAW.getBlockEntityType()) return (a, b, c, d) -> ((SculkJawBlockEntity) d).tick(a, b, c);
        else return null;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean idk) {
        var currentPowered = state.getValue(POWERED);
        var shouldBePowered = world.getBestNeighborSignal(pos) > 0;
        if (currentPowered != shouldBePowered) {
            world.setBlockAndUpdate(pos, state.setValue(POWERED, shouldBePowered));
        }
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var face = state.getValue(FACE);
        var otherState = world.getBlockState(pos.relative(face.getOpposite()));
        return otherState.isFaceSturdy(world, pos.relative(face.getOpposite()), face);
    }
}
