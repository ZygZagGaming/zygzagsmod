package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.entity.animation.AnimatedEntity;
import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import com.zygzag.zygzagsmod.common.entity.animation.Animator;
import com.zygzag.zygzagsmod.common.registry.AnimationRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import com.zygzag.zygzagsmod.common.util.LockedEntityAnchor;
import com.zygzag.zygzagsmod.common.util.LockedEntityRotation;
import com.zygzag.zygzagsmod.common.util.SimplEntityRotation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeSentry extends Monster implements GeoAnimatable, AnimatedEntity<BlazeSentry> {
    public static final LockedEntityRotation.AnglesFromAnchors DEFAULT_ROTATIONS = (difference) -> {
        double horizDifference = difference.horizontalDistance();
        float yRot = (float) Math.atan2(difference.x(), difference.z());
        float xRot = (float) Math.atan2(difference.y(), horizDifference);
        return new float[]{xRot, (float) ((xRot + 0.5 * Math.PI) % (2 * Math.PI)), yRot, yRot};
    };
    protected static final EntityDataAccessor<Animator.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ANIMATOR_STATE.get());
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<SimplEntityRotation> DATA_ROTATION = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ENTITY_ROTATION.get());
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Animator<BlazeSentry> animator = new Animator<>(this);
    public SimplEntityRotation rotation = new SimplEntityRotation();
    @Nullable
    private LivingEntity cachedTarget;

    public BlazeSentry(EntityType<? extends BlazeSentry> type, Level world) {
        super(type, world);
        setPathfindingMalus(BlockPathTypes.WATER, -1);
        setPathfindingMalus(BlockPathTypes.LAVA, 8);
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0);
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0);
        xpReward = 10;
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return 2.2f;
    }

    public BlazeSentry(Level world) {
        this(EntityTypeRegistry.BLAZE_SENTRY.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ANIMATOR_STATE, new Animator.State(
                AnimationRegistry.BlazeSentry.IDLE_BASE.get(),
                null,
                99999999,
                60,
                new LinkedList<>(),
                AnimationRegistry.BlazeSentry.IDLE_BASE.get(),
                AnimationRegistry.BlazeSentry.IDLE_BASE.get()
        ));
        entityData.define(DATA_TARGET, Optional.empty());
        entityData.define(DATA_ROTATION, new SimplEntityRotation());
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        Optional<UUID> targetUUID = entityData.get(DATA_TARGET);
        if (!((cachedTarget == null && targetUUID.isEmpty()) || (cachedTarget != null && targetUUID.isPresent() && cachedTarget.getUUID().equals(targetUUID.get()))))
            cachedTarget = targetUUID.flatMap((uuid) -> level().getEntities(this, getBoundingBox().inflate(50), (it) -> it.getUUID().equals(uuid)).stream().findFirst()).flatMap((it) -> it instanceof LivingEntity living ? Optional.of(living) : Optional.empty()).orElse(null);
        return cachedTarget;
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        super.setTarget(entity);
        if (!level().isClientSide) {
            if (entity == null) entityData.set(DATA_TARGET, Optional.empty());
            else entityData.set(DATA_TARGET, Optional.of(entity.getUUID()));
            if (!(rotation instanceof LockedEntityRotation)) rotation = new LockedEntityRotation(
                    new LockedEntityAnchor(this),
                    new LockedEntityAnchor(getTarget())
            );
        }
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        animator.register(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    @Override
    public boolean isIdle() {
        return getTarget() == null;
    }

    @Override
    public Animator<BlazeSentry> getAnimator() {
        return animator;
    }

    @Override
    public @Nullable Animation getAnimationChange() {
        if (getTarget() == null) return AnimationRegistry.BlazeSentry.IDLE_BASE.get();
        else return AnimationRegistry.BlazeSentry.SHOOT_BASE.get();
    }

    @Override
    public List<Animation> idleAnimations() {
        return List.of();
    }

    @Override
    public EntityDataAccessor<Animator.State> animatorStateAccessor() {
        return DATA_ANIMATOR_STATE;
    }

    @Override
    public void tick() {
//        if (level().isClientSide) rotation = entityData.get(DATA_ROTATION);
//        else {
            if (getTarget() == null) {
                if (rotation instanceof LockedEntityRotation) rotation = new SimplEntityRotation();
            } else {
                if (!(rotation instanceof LockedEntityRotation)) rotation = new LockedEntityRotation(
                        LockedEntityAnchor.eyes(this),
                        LockedEntityAnchor.eyes(getTarget()),
                        DEFAULT_ROTATIONS
                );
            }
            //entityData.set(DATA_ROTATION, rotation);
        //}
        rotation.tick();

        setRot(0, 0);
        setYHeadRot(0);
        setYBodyRot(0);
        setNoGravity(true);
        super.tick();
        animator.tick();
    }

    @Override
    public boolean doesIdleAnimations() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return ItemRegistry.BLAZE_SENTRY_SPAWN_EGG.get().getDefaultInstance();
    }
}
