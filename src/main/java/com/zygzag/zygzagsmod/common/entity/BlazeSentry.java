package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.entity.animation.AnimatedEntity;
import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import com.zygzag.zygzagsmod.common.entity.animation.Animator;
import com.zygzag.zygzagsmod.common.registry.AnimationRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
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

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.mod;
import static com.zygzag.zygzagsmod.common.util.GeneralUtil.radiansToDegrees;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeSentry extends Monster implements GeoAnimatable, AnimatedEntity<BlazeSentry> {
    protected static final EntityDataAccessor<Animator.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ANIMATOR_STATE.get());
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializers.OPTIONAL_UUID);
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Animator<BlazeSentry> animator = new Animator<>(this);
    public float headXRot = 0, headYRot = 0, oldHeadXRot = 0, oldHeadYRot = 0;
    public float bodyXRot = 0, bodyYRot = 0, oldBodyXRot = 0, oldBodyYRot = 0;
    public BlazeSentry(EntityType<? extends BlazeSentry> type, Level world) {
        super(type, world);
        setPathfindingMalus(BlockPathTypes.WATER, -1);
        setPathfindingMalus(BlockPathTypes.LAVA, 8);
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0);
        setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0);
        xpReward = 10;
    }

    public BlazeSentry(Level world) {
        this(EntityTypeRegistry.BLAZE_SENTRY.get(), world);
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
    }

    @Nullable
    private LivingEntity cachedTarget;

    @Nullable
    @Override
    public LivingEntity getTarget() {
        Optional<UUID> targetUUID = entityData.get(DATA_TARGET);
        if (!((cachedTarget == null && targetUUID.isEmpty()) || (cachedTarget != null && targetUUID.isPresent() && cachedTarget.getUUID().equals(targetUUID.get())))) cachedTarget = targetUUID.flatMap((uuid) -> level().getEntities(this, getBoundingBox().inflate(50), (it) -> it.getUUID().equals(uuid)).stream().findFirst()).flatMap((it) -> it instanceof LivingEntity living ? Optional.of(living) : Optional.empty()).orElse(null);
        return cachedTarget;
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        if (entity == null) entityData.set(DATA_TARGET, Optional.empty());
        else {
            entityData.set(DATA_TARGET, Optional.of(entity.getUUID()));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
        setRot(0, 0);
        setYHeadRot(0);
        setYBodyRot(0);
        setNoGravity(true);
        super.tick();
        animator.tick();
        oldHeadXRot = headXRot;
        oldHeadYRot = headYRot;
        oldBodyXRot = bodyXRot;
        oldBodyYRot = bodyYRot;
        if (getTarget() != null) aimAt(getTarget());
        else {
            headXRot = -90;
            bodyXRot = 90;
        }
    }

    public void aimAt(Entity entity) {
        Vec3 center = getBoundingBox().getCenter();
        double xDifference = entity.getX() - center.x();
        double zDifference = entity.getZ() - center.z();
        double yDifference;
        if (entity instanceof LivingEntity livingentity) {
            yDifference = livingentity.getEyeY() - center.y();
        } else {
            yDifference = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2 - center.y();
        }

        double horizDifference = Math.sqrt(xDifference * xDifference + zDifference * zDifference);
        float yRot = -90 - (float) (radiansToDegrees(Mth.atan2(zDifference, xDifference)));
        float xRot = -(float) (radiansToDegrees(Mth.atan2(yDifference, horizDifference)));
        float hXRot = 270 - xRot;
        float hYRot = 180 + yRot;
        bodyXRot = mod(xRot + 90, 360) - 90;
        bodyYRot = mod(yRot, 360);
        headXRot = mod(hXRot + 90, 360) - 90;
        headYRot = mod(hYRot, 360);
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
