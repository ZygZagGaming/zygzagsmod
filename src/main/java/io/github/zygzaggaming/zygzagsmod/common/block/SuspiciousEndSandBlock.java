package io.github.zygzaggaming.zygzagsmod.common.block;

import io.github.zygzaggaming.zygzagsmod.common.block.entity.CustomBrushableBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SuspiciousEndSandBlock extends BrushableBlock {
    public SuspiciousEndSandBlock(Properties pProperties, SoundEvent pBrushSound, SoundEvent pBrushCompletedSound) {
        super(BlockWithItemRegistry.END_SAND.getBlock(), pBrushSound, pBrushCompletedSound, pProperties);
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CustomBrushableBlockEntity(pPos, pState);
    }
}
