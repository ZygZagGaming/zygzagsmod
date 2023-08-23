package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import com.zygzag.zygzagsmod.common.registry.DamageTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.block.SculkJawBlock.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SculkJawBlockEntity extends BlockEntity {
    public static final DamageSource SCULK_JAW_DAMAGE = new DamageSource(DamageTypeRegistry.SCULK_JAW.getHolder().orElseThrow());

    @Nullable
    public Entity latchedEntity = null;
    @Nullable
    public Entity lastLatchedEntity = null;
    public int ticksSinceEntityLatched = 0, ticksEntityLatched = 0;
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.SCULK_JAW.getBlockEntityType(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        sculkSpreader.updateCursors(world, pos, world.getRandom(), true);

        var isClient = world.isClientSide;

        //Main.LOGGER.debug("Latched entity on " + (isClient ? "client" : "server") + ": " + latchedEntity + "; closed: " + state.getValue(CLOSED));
        if (shouldLetGoOfEntity(world, pos, state)) latchedEntity = null;
        if (latchedEntity == null) {
            ticksSinceEntityLatched++;
            ticksEntityLatched = 0;
            if (!isClient && state.getValue(CLOSED) > 0 && ticksSinceEntityLatched % 6 == 0) world.setBlockAndUpdate(pos, state.setValue(CLOSED, state.getValue(CLOSED) - 1));
        } else {
            ticksSinceEntityLatched = 0;
            ticksEntityLatched++;
            int closed = state.getValue(CLOSED);
            var targetPos = new Vec3(
                    pos.getX() + 0.5,
                    pos.getY() - (state.getValue(FACE) == Direction.DOWN ?
                            (latchedEntity instanceof ItemEntity ? 0.5 : latchedEntity.getBoundingBox().getYsize() - 1) :
                            (state.getValue(FACE) != Direction.UP && latchedEntity instanceof ItemEntity ? -0.15 : 0)
                    ),
                    pos.getZ() + 0.5
            );
            if (closed < 3) {
                if (!isClient) world.setBlockAndUpdate(pos, state.setValue(CLOSED, closed + 1));
                latchedEntity.setPos(latchedEntity.position().scale((3.5 - closed) / (4 - closed)).add(targetPos.scale(0.5 / (4 - closed))));
            } else {
                latchedEntity.setPos(targetPos.add(latchedEntity.position()).scale(0.5));
                latchedEntity.setDeltaMovement(Vec3.ZERO);
                if (state.getValue(DEAL_DAMAGE) && latchedEntity instanceof LivingEntity living && ticksEntityLatched % 10 == 5) {
                    int xp;
                    if (living instanceof Player player && (player.experienceLevel > 0 || player.experienceProgress > 0)) {
                        xp = Math.min(10, player.totalExperience);
                        player.giveExperiencePoints(-xp);
                    } else {
                        float originalHealth = living.getHealth();
                        living.hurt(SCULK_JAW_DAMAGE, 1.5f);
                        float healthChange = originalHealth - living.getHealth();
                        xp = (int) Math.floor(healthChange * (living instanceof Player ? 30 : living.getExperienceReward()) / living.getMaxHealth() + Math.random()/* + Math.random()*/);
                        if (living.getHealth() == 0) living.skipDropExperience();
                    }

                    if (xp > 0) {
                        if (world.getBlockState(pos.relative(state.getValue(FACE).getOpposite())).is(BlockTags.SCULK_REPLACEABLE)) world.setBlockAndUpdate(pos.relative(state.getValue(FACE).getOpposite()), Blocks.SCULK.defaultBlockState());
                        this.sculkSpreader.addCursors(pos.relative(state.getValue(FACE).getOpposite()), xp);
                    }
                }
            }
        }

        if (lastLatchedEntity != null && latchedEntity == null) {
            if (lastLatchedEntity instanceof ItemEntity item) {
                item.setExtendedLifetime();
                item.setNoPickUpDelay();
            }
        }

        if (latchedEntity instanceof ItemEntity item && lastLatchedEntity == null) {
            //item.setUnlimitedLifetime();
            item.setNeverPickUp();
        }

        lastLatchedEntity = latchedEntity;
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
        ticksEntityLatched = tag.getInt("ticks_entity_latched");
        if (tag.contains("latched_entity") && level != null) {
            var uuid = tag.getUUID("latched_entity");
            var entities = level.getEntities((Entity) null, AABB.ofSize(new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), 1, 1, 1), (entity) -> entity.getUUID().equals(uuid));
            if (!entities.isEmpty() && !entities.get(0).isRemoved()) latchedEntity = entities.get(0);
            else latchedEntity = null;
        }
        sculkSpreader.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        sculkSpreader.save(tag);
        tag.putInt("ticks_since_entity_latched", ticksSinceEntityLatched);
        tag.putInt("ticks_entity_latched", ticksEntityLatched);
        if (latchedEntity != null) tag.putUUID("latched_entity", latchedEntity.getUUID());
        super.saveAdditional(tag);
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

    @Override
    public void setRemoved() {
        if (latchedEntity instanceof ItemEntity item) {
            item.setExtendedLifetime();
            item.setNoPickUpDelay();
        }
        super.setRemoved();
    }
}