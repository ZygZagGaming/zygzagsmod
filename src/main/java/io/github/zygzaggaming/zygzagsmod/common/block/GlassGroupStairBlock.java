package io.github.zygzaggaming.zygzagsmod.common.block;

import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GlassGroupStairBlock extends GlassStairBlock {
    private TagKey<Block> group;
    public GlassGroupStairBlock(TagKey<Block> group, Supplier<BlockState> state, Properties properties) {
        super(state, properties);
        this.group = group;
    }

    @Override
    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        return state2.is(group) || super.skipRendering(state1, state2, dir);
    }
}
