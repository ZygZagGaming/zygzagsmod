package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registries.BlockItemEntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.block.SculkJawBlock.CLOSED;
import static com.zygzag.zygzagsmod.common.block.SculkJawBlock.FACE;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SculkJawBlockEntity extends BlockEntity {
    @Nullable
    public Entity latchedEntity = null;
    public int ticksSinceEntityLatched = 0;
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.SCULK_JAW.getBlockEntityType(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        var isClient = world.isClientSide;

        if (latchedEntity instanceof ItemEntity item) item.age--;

        //Main.LOGGER.debug("Latched entity on " + (isClient ? "client" : "server") + ": " + latchedEntity + "; closed: " + state.getValue(CLOSED));
        if (shouldLetGoOfEntity(world, pos, state)) latchedEntity = null;
        if (latchedEntity == null) {
            ticksSinceEntityLatched++;
            if (!isClient && state.getValue(CLOSED) > 0 && ticksSinceEntityLatched % 6 == 0) world.setBlockAndUpdate(pos, state.setValue(CLOSED, state.getValue(CLOSED) - 1));
        } else {
            ticksSinceEntityLatched = 0;
            int closed = state.getValue(CLOSED);
            var targetPos = new Vec3(pos.getX() + 0.5, pos.getY() - ((state.getValue(FACE) == Direction.DOWN) ? (latchedEntity instanceof ItemEntity ? 0.5 : latchedEntity.getBoundingBox().getYsize() - 1) : 0), pos.getZ() + 0.5);
            if (closed < 3) {
                if (!isClient) world.setBlockAndUpdate(pos, state.setValue(CLOSED, closed + 1));
                latchedEntity.setPos(latchedEntity.position().scale((3.5 - closed) / (4 - closed)).add(targetPos.scale(0.5 / (4 - closed))));
            } else {
                latchedEntity.setPos(targetPos.add(latchedEntity.position()).scale(0.5));
                latchedEntity.setDeltaMovement(Vec3.ZERO);
                /*if (latchedEntity instanceof LivingEntity livingEntity) {
                    var attributeInstance = livingEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
                    if (attributeInstance != null) attributeInstance.addTransientModifier(new AttributeModifier("sculk jaw", 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }*/
            }
        }
    }

    public void latchOnto(Level world, BlockPos pos, BlockState state, Entity entity) {
        latchedEntity = entity;
        ticksSinceEntityLatched = 0;
        //Main.LOGGER.debug("latching");
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ticksSinceEntityLatched = tag.getInt("ticks_since_entity_latched");
        if (tag.contains("latched_entity") && level != null) {
            var uuid = tag.getUUID("latched_entity");
            var entities = level.getEntities((Entity) null, AABB.ofSize(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), 1, 1, 1), (entity) -> entity.getUUID().equals(uuid));
            if (!entities.isEmpty() && !entities.get(0).isRemoved()) latchedEntity = entities.get(0);
            else latchedEntity = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ticks_since_entity_latched", ticksSinceEntityLatched);
        if (latchedEntity != null) tag.putUUID("latched_entity", latchedEntity.getUUID());
    }

    public boolean shouldLetGoOfEntity(Level world, BlockPos pos, BlockState state) {
        var signal = world.getBestNeighborSignal(pos);
        if (signal > 0) return true;
        if (latchedEntity != null) {
            var intersectsBounds = state.getShape(world, pos, CollisionContext.empty()).bounds().move(pos).intersects(latchedEntity.getBoundingBox());
            return !intersectsBounds || latchedEntity.isRemoved() || (latchedEntity instanceof Player player && player.isSpectator());
        }
        return false;
    }
}
