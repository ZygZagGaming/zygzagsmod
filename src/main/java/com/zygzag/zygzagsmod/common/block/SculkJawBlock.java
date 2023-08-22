package com.zygzag.zygzagsmod.common.block;

import com.zygzag.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import com.zygzag.zygzagsmod.common.registries.BlockEntityRegistry;
import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SculkJawBlock extends Block implements EntityBlock {
    public static final IntegerProperty CLOSED = IntegerProperty.create("closed", 0, 3);
    public static final DirectionProperty FACE = DirectionProperty.create("face");
    public static final VoxelShape[] SHAPES_FOR_CLOSING = {
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 0, 2, 16, 4, 14),
            Block.box(0, 0, 4, 16, 6, 12),
            Block.box(0, 0, 6, 16, 8, 10),
    };
    public SculkJawBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CLOSED, 0).setValue(FACE, Direction.DOWN));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_FOR_CLOSING[state.getValue(CLOSED)];
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (state.getValue(CLOSED) == 0) {
            if (entity.position().distanceToSqr(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) <= 0.125) {
                world.setBlockAndUpdate(pos, state.setValue(CLOSED, 1));
            }
        } else if (state.getValue(CLOSED) == 3) {
            // trap entity
        } else {
            world.setBlockAndUpdate(pos, state.setValue(CLOSED, state.getValue(CLOSED)));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLOSED, FACE);
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
}
