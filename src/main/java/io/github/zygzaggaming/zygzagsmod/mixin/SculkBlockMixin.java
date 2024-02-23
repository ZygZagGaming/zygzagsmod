package io.github.zygzaggaming.zygzagsmod.mixin;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin extends DropExperienceBlock implements SculkBehaviour {
    public SculkBlockMixin(IntProvider range, Properties properties) {
        super(range, properties);
        throw new UnsupportedOperationException();
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "canPlaceGrowth(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Z")
    private static void canPlaceGrowth(LevelAccessor world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        BlockState blockstateAbove = world.getBlockState(pos.above());
        if (blockstateAbove.isAir() || blockstateAbove.is(Blocks.WATER) && blockstateAbove.getFluidState().is(Fluids.WATER)) {
            int numSculkGrowths = 0;

            for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 2, 4))) {
                BlockState blockstate1 = world.getBlockState(blockpos);
                if (Main.EXTRANEOUS_SCULK_GROWTHS.get().keySet().stream().anyMatch((it) -> blockstate1.is(it.getBlock()))) {
                    ++numSculkGrowths;
                }

                if (numSculkGrowths > 2) {
                    callback.setReturnValue(false);
                }
            }

            callback.setReturnValue(true);
        } else {
            callback.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "getRandomGrowthState(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Z)Lnet/minecraft/world/level/block/state/BlockState;")
    private void getRandomGrowthState(LevelAccessor world, BlockPos pos, RandomSource rng, boolean flag, CallbackInfoReturnable<BlockState> callback) {
        BlockState blockstate = GeneralUtil.randomElement(Main.EXTRANEOUS_SCULK_GROWTHS.get().keySet().stream().toList(), Main.EXTRANEOUS_SCULK_GROWTHS.get().values().stream().toList(), rng);
        if (blockstate.is(Blocks.SCULK_SHRIEKER)) blockstate = blockstate.setValue(SculkShriekerBlock.CAN_SUMMON, flag);

        callback.setReturnValue(blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && !world.getFluidState(pos).isEmpty() ? blockstate.setValue(BlockStateProperties.WATERLOGGED, true) : blockstate);
    }
}