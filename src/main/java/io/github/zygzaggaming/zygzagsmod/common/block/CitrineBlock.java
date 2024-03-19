package io.github.zygzaggaming.zygzagsmod.common.block;

import com.mojang.serialization.MapCodec;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.CitrineBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.MagmaticNetherBrickBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CitrineBlock extends BaseEntityBlock {
    public static final BooleanProperty ALIVE = BooleanProperty.create("alive");
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final MapCodec<CacheBlock> CODEC = simpleCodec(CacheBlock::new);
    public CitrineBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ALIVE, false).setValue(NATURAL, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ALIVE, NATURAL);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rng) {
        world.setBlockAndUpdate(pos, state.cycle(ALIVE));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(ALIVE) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CitrineBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == BlockItemEntityRegistry.CITRINE.getBlockEntityType())
            return (a, b, c, d) -> ((CitrineBlockEntity) d).tick(a, b, c);
        else return null;
    }
}
