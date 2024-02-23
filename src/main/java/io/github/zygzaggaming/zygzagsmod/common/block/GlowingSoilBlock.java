package io.github.zygzaggaming.zygzagsmod.common.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GlowingSoilBlock extends CustomFarmland {
    public GlowingSoilBlock(Properties prop) {
        super(prop);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 12;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rng) {
        double x = (double) pos.getX() + (rng.nextDouble() * 3) - 1.5;
        double y = (double) pos.getY() + (rng.nextDouble() * 3) - 1.5;
        double z = (double) pos.getZ() + (rng.nextDouble() * 3) - 1.5;
        if (rng.nextInt(7) == 0) {
            world.addParticle(ParticleTypes.END_ROD, x, y, z, rng.nextGaussian() * 0.005D, rng.nextGaussian() * 0.005D, rng.nextGaussian() * 0.005D);
        }
    }
}