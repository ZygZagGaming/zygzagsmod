package io.github.zygzaggaming.zygzagsmod.common.block;

import io.github.zygzaggaming.zygzagsmod.common.block.entity.MagmaticNetherBrickBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.OverheatBeamAreaEffectCloud;
import io.github.zygzaggaming.zygzagsmod.common.entity.SphereAreaEffectCloud;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import io.github.zygzaggaming.zygzagsmod.common.Config;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagmaticNetherBrickBlock extends Block implements EntityBlock {
    public static final IntegerProperty PULSE = IntegerProperty.create("pulse", 0, 103);
    public static final IntegerProperty CHARGES = IntegerProperty.create("charges", 0, 4);
    public static final int[] LIGHT = { 3, 6, 9, 12, 15 };

    public MagmaticNetherBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PULSE, CHARGES);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return LIGHT[state.getValue(CHARGES)];
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(world, pos, state, player);
        if (!player.isCreative()) {
            SphereAreaEffectCloud cloud = new SphereAreaEffectCloud(world);
            cloud.setPos(pos.getX() + 0.5, pos.getY() + 0.5 - cloud.getRadius(), pos.getZ() + 0.5);
            world.addFreshEntity(cloud);
        }
        return state;
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        chargeOrRelease(state, world, pos, Config.magmaticNetherBrickHitCooldown);
    }

    public void chargeOrRelease(BlockState state, Level world, BlockPos pos, int cooldown) {
        int charges = state.getValue(CHARGES);
        int pulses = state.getValue(PULSE);
        if (pulses == 0) {
            if (charges < 3) {
                world.setBlockAndUpdate(pos, state.setValue(CHARGES, charges + 1).setValue(PULSE, cooldown));
            } else {
                for (Direction dir : Direction.values())
                    if (!world.getBlockState(pos.relative(dir)).isFaceSturdy(world, pos.relative(dir), dir.getOpposite())) {
                        OverheatBeamAreaEffectCloud.spawn(world, pos, dir);
                    }
                world.setBlockAndUpdate(pos, state.setValue(CHARGES, 4).setValue(PULSE, 103));
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagmaticNetherBrickBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlockEntityType())
            return (a, b, c, d) -> ((MagmaticNetherBrickBlockEntity) d).tick(a, b, c);
        else return null;
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult result, Projectile projectile) {
        chargeOrRelease(state, world, result.getBlockPos(), Config.magmaticNetherBrickProjectileCooldown);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity stander) {
        chargeOrRelease(state, world, pos, Config.magmaticNetherBrickStepCooldown);

        super.stepOn(world, pos, state, stander);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rng) {
        if (!world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP)) world.addParticle(ParticleTypes.DRIPPING_LAVA, pos.getX() + 0.5 + (0.4 * rng.nextDouble() - 0.2), pos.getY() - 0.05, pos.getZ() + 0.5 + (0.4 * rng.nextDouble() - 0.2), 0.0, 0.0, 0.0);
    }
}
