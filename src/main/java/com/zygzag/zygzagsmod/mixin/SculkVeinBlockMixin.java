package com.zygzag.zygzagsmod.mixin;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin extends MultifaceBlock implements SculkBehaviour, SimpleWaterloggedBlock {
    public SculkVeinBlockMixin(Properties prop) {
        super(prop);
    }

    @Inject(at = @At("head"), cancellable = true, method = "hasSubstrateAccess(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Z")
    private static void hasSubstrateAccess(LevelAccessor world, BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        //Main.LOGGER.debug("deez");
        if (!state.is(Main.SCULK_VEIN_LIKE)) {
            callback.setReturnValue(false);
        } else {
            for (Direction direction : DIRECTIONS) {
                if (hasFace(state, direction) && world.getBlockState(pos.relative(direction)).is(BlockTags.SCULK_REPLACEABLE)) {
                    callback.setReturnValue(true);
                }
            }

            callback.setReturnValue(false);
        }
    }
}
