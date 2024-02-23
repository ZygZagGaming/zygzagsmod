package io.github.zygzaggaming.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlessedSoilBlock extends CustomFarmland {
    public BlessedSoilBlock(Properties prop) {
        super(prop);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        BlockPos abovePos = pos.above();
        BlockState above = world.getBlockState(abovePos);
        if (above.getBlock() instanceof CropBlock crop && above.getValue(CropBlock.AGE) < crop.getMaxAge() && Math.random() <= .25) {
            crop.growCrops(world, abovePos, above);
            world.levelEvent(1505, abovePos, 0);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rng) {
        double x = (double) pos.getX() + (rng.nextDouble() * 3) - 1.5;
        double y = (double) pos.getY() + (rng.nextDouble() * 3) - 1.5;
        double z = (double) pos.getZ() + (rng.nextDouble() * 3) - 1.5;
        if (rng.nextInt(7) == 0) {
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, rng.nextGaussian() * 0.005D, rng.nextGaussian() * 0.005D, rng.nextGaussian() * 0.005D);
        }
    }
}