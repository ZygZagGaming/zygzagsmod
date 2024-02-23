package io.github.zygzaggaming.zygzagsmod.common.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GlassGroupBlock extends TransparentBlock {
    private TagKey<Block> group;
    public GlassGroupBlock(TagKey<Block> group, Properties properties) {
        super(properties);
        this.group = group;
    }

    @Override
    public boolean skipRendering(BlockState state1, BlockState state2, Direction dir) {
        return state2.is(group) || super.skipRendering(state1, state2, dir);
    }
}
