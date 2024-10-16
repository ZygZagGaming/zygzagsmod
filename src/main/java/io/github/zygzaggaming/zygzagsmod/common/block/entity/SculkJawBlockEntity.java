package io.github.zygzaggaming.zygzagsmod.common.block.entity;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.SoundEventRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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

import static io.github.zygzaggaming.zygzagsmod.common.block.SculkJawBlock.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SculkJawBlockEntity extends BlockEntity {
    public static final ResourceKey<DamageType> SCULK_JAW_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID,  "sculk_jaw"));
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();
    @Nullable
    public Entity latchedEntity = null;
    @Nullable
    public Entity lastLatchedEntity = null;
    public int ticksSinceEntityLatched = 0, ticksEntityLatched = 0;
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.SCULK_JAW.getBlockEntityType(), pos, state);
    }

    public static DamageSource sculkJawDamage(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SCULK_JAW_DAMAGE_TYPE));
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        sculkSpreader.updateCursors(world, pos, world.getRandom(), true);

        var isClient = world.isClientSide;

        //Main.LOGGER.debug("Latched entity on " + (isClient ? "client" : "server") + ": " + latchedEntity + "; closed: " + state.getValue(CLOSED));
        if (shouldLetGoOfEntity(world, pos, state)) latchedEntity = null;
        if (latchedEntity == null) {
            ticksSinceEntityLatched++;
            ticksEntityLatched = 0;
            if (!isClient && state.getValue(CLOSED) > 0 && ticksSinceEntityLatched % 6 == 0) {
                world.setBlockAndUpdate(pos, state.setValue(CLOSED, state.getValue(CLOSED) - 1));
            }
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
                if (closed == 0 && world instanceof ServerLevel serverWorld)
                    serverWorld.playSound(null, worldPosition, SoundEventRegistry.SCULK_JAW_CLOSE.get(), SoundSource.BLOCKS);
            } else {
                latchedEntity.setPos(targetPos.add(latchedEntity.position()).scale(0.5));
                latchedEntity.setDeltaMovement(Vec3.ZERO);
                if (state.getValue(DEAL_DAMAGE) && latchedEntity instanceof LivingEntity living && ticksEntityLatched % 10 == 5 && world instanceof ServerLevel sWorld) {
                    int xp;
                    if (living instanceof Player player && (player.experienceLevel > 0 || player.experienceProgress > 0)) {
                        xp = Math.min(10, player.totalExperience);
                        player.giveExperiencePoints(-xp);
                    } else {
                        float originalHealth = living.getHealth();
                        living.hurt(sculkJawDamage(world.registryAccess()), 1.5f);
                        float healthChange = originalHealth - living.getHealth();
                        xp = (int) Math.floor(healthChange * (living instanceof Player ? 30 : living.getExperienceReward(sWorld, null)) / living.getMaxHealth() + Math.random()/* + Math.random()*/);
                        if (living.getHealth() == 0) living.skipDropExperience();
                    }

                    if (xp > 0) {
                        if (world.getBlockState(pos.relative(state.getValue(FACE).getOpposite())).is(BlockTags.SCULK_REPLACEABLE))
                            world.setBlockAndUpdate(pos.relative(state.getValue(FACE).getOpposite()), Blocks.SCULK.defaultBlockState());
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
            if (!isClient) item.setUnlimitedLifetime();
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
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        sculkSpreader.save(tag);
        tag.putInt("ticks_since_entity_latched", ticksSinceEntityLatched);
        tag.putInt("ticks_entity_latched", ticksEntityLatched);
        if (latchedEntity != null) tag.putUUID("latched_entity", latchedEntity.getUUID());
        super.saveAdditional(tag, provider);
    }

    public boolean shouldLetGoOfEntity(Level world, BlockPos pos, BlockState state) {
        if (state.getValue(POWERED)) return true;
        if (latchedEntity != null) {
            var intersectsBounds = state.getShape(world, pos, CollisionContext.empty()).bounds().move(pos).intersects(latchedEntity.getBoundingBox());
            return !intersectsBounds || latchedEntity.isRemoved() || (latchedEntity instanceof Player player && player.isSpectator()) || latchedEntity.getType().is(Main.SCULK_JAW_IMMUNE);
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