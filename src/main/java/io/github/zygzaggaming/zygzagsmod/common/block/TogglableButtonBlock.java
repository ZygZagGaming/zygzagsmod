package io.github.zygzaggaming.zygzagsmod.common.block;

import io.github.zygzaggaming.zygzagsmod.common.block.entity.TogglableButtonBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TogglableButtonBlock extends ButtonBlock implements EntityBlock {
    private final int ticksToStayPressed;
    public TogglableButtonBlock(BlockSetType blockSetType, int ticksToStayPressed, Properties properties) {
        super(blockSetType, ticksToStayPressed, properties);
        this.ticksToStayPressed = ticksToStayPressed;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(POWERED)) {
            depress(world, pos, state);
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else if (world.getBlockEntity(pos) instanceof TogglableButtonBlockEntity tbbe) tbbe.pressed();
        return super.use(state, world, pos, player, hand, result);
    }

    public void depress(Level world, BlockPos pos, BlockState state) {
        world.setBlockAndUpdate(pos, state.setValue(POWERED, false));
        updateNeighbours(state, world, pos);
        playSound(null, world, pos, false);
        world.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
    }

    public static void updateNeighbours(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, state.getBlock());
        world.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), state.getBlock());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TogglableButtonBlockEntity(pos, state, ticksToStayPressed);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == BlockEntityRegistry.TOGGLABLE_BUTTON.get())
            return (a, b, c, d) -> ((TogglableButtonBlockEntity) d).tick(a, b, c);
        else return null;
    }

    @Override
    public void tick(BlockState p_220903_, ServerLevel p_220904_, BlockPos p_220905_, RandomSource p_220906_) { } // replace vanilla behavior
}
