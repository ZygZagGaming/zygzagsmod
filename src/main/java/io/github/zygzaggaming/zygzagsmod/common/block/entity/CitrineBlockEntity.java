package io.github.zygzaggaming.zygzagsmod.common.block.entity;

import io.github.zygzaggaming.zygzagsmod.common.block.CitrineBlock;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CitrineBlockEntity extends BlockEntity {
    public CitrineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.CITRINE.getBlockEntityType(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world instanceof ServerLevel s && s.getData(AttachmentTypeRegistry.LEVEL_TICK_COUNT_ATTACHMENT) % 2 == 0 && !state.getValue(CitrineBlock.NATURAL)) {
            boolean alive = state.getValue(CitrineBlock.ALIVE);
            boolean newAlive = alive;

            if (world.hasNeighborSignal(pos)) newAlive = true;
            else {
                int livingNeighbors = alive ? -1 : 0;
                for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++)
                        for (int k = -1; k < 2; k++) {
                            BlockState otherState = world.getBlockState(pos.offset(i, j, k));
                            if (otherState.is(state.getBlock()) && otherState.getValue(CitrineBlock.ALIVE)) livingNeighbors++;
                        }
                if (livingNeighbors < 2 || livingNeighbors > 3) newAlive = false;
                else if (livingNeighbors == 3) newAlive = true;
            }

            if (alive != newAlive) world.scheduleTick(pos, state.getBlock(), 1);
        }
    }
}
