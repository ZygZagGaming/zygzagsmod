package com.zygzag.zygzagsmod.common.block;

import com.zygzag.zygzagsmod.common.registry.ParticleTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.SoundEventRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EndSandBlock extends Block {
    public static Property<Boolean> ETHEREAL = BooleanProperty.create("ethereal");
    public static Property<Integer> POWER = IntegerProperty.create("power", 0, 15);

    public EndSandBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(ETHEREAL, false).setValue(POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ETHEREAL);
        builder.add(POWER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(ETHEREAL)) {
            return Shapes.empty();//Block.box(0, 0, 0, 1, 1, 1);
        } else {
            return Shapes.block();
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        var redstonePower = world.getBestNeighborSignal(pos);
        var blockPower = 0;
        for (Direction direction : Direction.values()) {
            var relativePos = pos.relative(direction);
            var relativeState = world.getBlockState(relativePos);
            if (relativeState.is(this)) blockPower = Math.max(blockPower, relativeState.getValue(POWER));
        }
        var effectivePower = Math.max(redstonePower, blockPower - 1);
        world.setBlockAndUpdate(pos, state.setValue(POWER, effectivePower));
        if (effectivePower == 0 && state.getValue(POWER) > 0)
            world.playSound(null, pos, SoundEventRegistry.END_SAND_REFORM.get(), SoundSource.BLOCKS);
        world.scheduleTick(pos, this, 2);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rng) {
        world.setBlockAndUpdate(pos, state.setValue(ETHEREAL, state.getValue(POWER) > 0));
        if (state.getValue(POWER) > 0 && !state.getValue(ETHEREAL)) {
            displayParticles(pos, state, world);
            world.playSound(null, pos, SoundEventRegistry.END_SAND_DISSIPATE.get(), SoundSource.BLOCKS);
        }
    }

    public void displayParticles(BlockPos pos, BlockState state, Level world) {
        if (state.getBlock() == this && world instanceof ServerLevel sWorld) {
            sWorld.sendParticles(
                    ParticleTypeRegistry.END_SAND_PARTICLES.get(),
                    (double) pos.getX() + 0.5,
                    (double) pos.getY() + 0.5,
                    (double) pos.getZ() + 0.5,
                    8,
                    0.25,
                    0.25,
                    0.25,
                    0
            );
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        var pos = pContext.getClickedPos();
        var world = pContext.getLevel();
        var redstonePower = world.getBestNeighborSignal(pos);
        var blockPower = 0;
        for (Direction direction : Direction.values()) {
            var relativePos = pos.relative(direction);
            var relativeState = world.getBlockState(relativePos);
            if (relativeState.is(this)) blockPower = Math.max(blockPower, relativeState.getValue(POWER));
        }
        return defaultBlockState().setValue(POWER, Math.max(redstonePower, blockPower - 1)).setValue(ETHEREAL, Math.max(redstonePower, blockPower - 1) > 0);
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return (pState.getValue(POWER) > 0 && pAdjacentBlockState.is(this)) || super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return 15 - pState.getValue(POWER);
    }
}
