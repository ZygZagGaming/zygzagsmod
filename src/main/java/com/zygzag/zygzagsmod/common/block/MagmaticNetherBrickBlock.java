package com.zygzag.zygzagsmod.common.block;

import com.zygzag.zygzagsmod.common.block.entity.MagmaticNetherBrickBlockEntity;
import com.zygzag.zygzagsmod.common.entity.BeamAreaEffectCloud;
import com.zygzag.zygzagsmod.common.entity.SphereAreaEffectCloud;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
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
    public static final IntegerProperty PULSE = IntegerProperty.create("pulse", 0, 104);
    public static final IntegerProperty CHARGES = IntegerProperty.create("charges", 0, 3);

    public MagmaticNetherBrickBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PULSE, CHARGES);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(world, pos, state, player);
        SphereAreaEffectCloud cloud = new SphereAreaEffectCloud(world);
        cloud.setPos(pos.getX() + 0.5, pos.getY() + 0.5 - cloud.getRadius(), pos.getZ() + 0.5);
        world.addFreshEntity(cloud);
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        chargeOrRelease(state, world, pos);
    }

    public void chargeOrRelease(BlockState state, Level world, BlockPos pos) {
        int charges = state.getValue(CHARGES);
        if (charges < 3) {
            world.setBlockAndUpdate(pos, state.setValue(CHARGES, charges + 1).setValue(PULSE, 4));
        } else {
            for (Direction dir : Direction.values())
                if (!world.getBlockState(pos.relative(dir)).isFaceSturdy(world, pos.relative(dir), dir.getOpposite())) {
                    BeamAreaEffectCloud cloud = new BeamAreaEffectCloud(world);
                    cloud.setDirection(dir);
                    cloud.setPos(pos.getX() + 0.5 + dir.getNormal().getX(), pos.getY() + 0.5 + dir.getNormal().getY(), pos.getZ() + 0.5 + dir.getNormal().getZ());
                    world.addFreshEntity(cloud);
                }
            world.setBlockAndUpdate(pos, state.setValue(CHARGES, 0).setValue(PULSE, 104));
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
        chargeOrRelease(state, world, result.getBlockPos());
    }
}
