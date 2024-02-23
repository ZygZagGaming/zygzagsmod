package io.github.zygzaggaming.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GlassGroupSlabBlock extends GlassSlabBlock {
    private TagKey<Block> group;
    public GlassGroupSlabBlock(TagKey<Block> group, BlockBehaviour.Properties properties) {
        super(properties);
        this.group = group;
    }

    @Override
    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        return state2.is(group) || super.skipRendering(state1, state2, dir);
    }
}
