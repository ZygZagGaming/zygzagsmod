package io.github.zygzaggaming.zygzagsmod.common.block;

import io.github.zygzaggaming.zygzagsmod.common.block.entity.StructurePlacerBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructurePlacerBlock extends Block implements EntityBlock {
    public StructurePlacerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StructurePlacerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == BlockEntityRegistry.STRUCTURE_PLACER.get())
            return (a, b, c, d) -> ((StructurePlacerBlockEntity) d).tick(a, b, c);
        else return null;
    }
}
