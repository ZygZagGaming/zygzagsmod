package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.entity.animation.AnimatedEntity;
import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import com.zygzag.zygzagsmod.common.entity.animation.Animator;
import com.zygzag.zygzagsmod.common.registry.*;
import com.zygzag.zygzagsmod.common.util.LockedEntityAnchor;
import com.zygzag.zygzagsmod.common.util.LockedEntityRotation;
import com.zygzag.zygzagsmod.common.util.SimplEntityRotation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
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
import java.util.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeSentry extends Monster implements GeoAnimatable, AnimatedEntity<BlazeSentry> {
    protected static final EntityDataAccessor<Animator.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ANIMATOR_STATE.get());
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializers.OPTIONAL_UUID);
    //protected static final EntityDataAccessor<SimplEntityRotation> DATA_ROTATION = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ENTITY_ROTATION.get());
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Animator<BlazeSentry> animator = new Animator<>(this);
    public final LockedEntityRotation.RotationsFromDifference defaultRotations = (difference) -> {
        double horizDifference = difference.horizontalDistance();
        float yRot = (float) Math.atan2(difference.x(), difference.z());
        float xRot = (float) Math.atan2(difference.y(), horizDifference);
        return new float[]{xRot, xRot, yRot, yRot};
    };
    public static float[] maxRotationPerTick = {(float) (0.125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public SimplEntityRotation rotation = new SimplEntityRotation();
    {
        rotation.maxRotationPerTick = maxRotationPerTick;
        rotation.newBodyXRot = rotation.bodyXRot = rotation.oldBodyXRot = -0.5f * (float) Math.PI;
    }
    int windDownTicks = 0;
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
        //entityData.define(DATA_ROTATION, new SimplEntityRotation());
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
        LivingEntity currentTarget = getTarget();
        if ((entity == null && currentTarget == null) || (entity != null && currentTarget != null && entity.is(currentTarget))) return;
        super.setTarget(entity);
        if (!level().isClientSide) {
            if (entity == null) {
                animator.setAnimation(AnimationRegistry.BlazeSentry.IDLE_BASE.get());
                entityData.set(DATA_TARGET, Optional.empty());
                if (rotation instanceof LockedEntityRotation) {
                    rotation = new SimplEntityRotation(rotation);
                    rotation.newBodyXRot = -0.5f * (float) Math.PI;
                }
            } else {
                windDownTicks = 20;
                animator.setAnimation(AnimationRegistry.BlazeSentry.AGRO_BASE.get());
                entityData.set(DATA_TARGET, Optional.of(entity.getUUID()));
                if (!(rotation instanceof LockedEntityRotation)) {
                    rotation = new LockedEntityRotation(
                            LockedEntityAnchor.eyes(this),
                            LockedEntityAnchor.eyes(getTarget()),
                            defaultRotations,
                            rotation
                    );
                }
            }
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
        else return null;
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
        if (windDownTicks > 0) windDownTicks--;
        if (level().isClientSide) {
            if (getTarget() == null) {
                if (rotation instanceof LockedEntityRotation) {
                    rotation = new SimplEntityRotation(rotation);
                    rotation.newBodyXRot = -0.5f * (float) Math.PI;
                }
            } else {
                if (!(rotation instanceof LockedEntityRotation)) {
                    rotation = new LockedEntityRotation(
                            LockedEntityAnchor.eyes(this),
                            LockedEntityAnchor.eyes(getTarget()),
                            defaultRotations,
                            rotation
                    );
                }
            }
        }
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
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, (entity) -> entity instanceof Player player && !player.isCreative() && !player.isSpectator()));
        goalSelector.addGoal(2, new FireGoal());
        goalSelector.addGoal(2, new FireBigGoal());
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

    public class FireGoal extends Goal {
        public static final EnumSet<Goal.Flag> flags = EnumSet.of(Flag.LOOK, Flag.TARGET);
        public static final int windup = 60, ticksBetweenFireballs = 3, maxFireballs = 50, windDown = 60;
        public static final double power = 1.5;
        int ticks = 0;
        double chanceToUse = 0.66;

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public boolean canUse() {
            return windDownTicks <= 0 && !animator.isTransitioning() && animator.getAnimation() == AnimationRegistry.BlazeSentry.AGRO_BASE.get() && level().getRandom().nextDouble() < chanceToUse;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            //System.out.println("started");
            animator.setAnimation(AnimationRegistry.BlazeSentry.SHOOT_BASE.get());
            ticks = 0;
        }

        @Override
        public void tick() {
            //System.out.println("ticks: " + ticks);
            ticks++;
            if (ticks > windup && (ticks - windup) % ticksBetweenFireballs == 0) {
                LivingEntity target = getTarget();
                assert target != null;
                Vec3 angle = new Vec3(sin(rotation.bodyYRot) * cos(rotation.bodyXRot), sin(rotation.bodyXRot), cos(rotation.bodyYRot) * cos(rotation.bodyXRot));
                SmallFireball fireball = new SmallFireball(level(), BlazeSentry.this, angle.x, angle.y, angle.z);
                fireball.xPower *= power; fireball.yPower *= power; fireball.zPower *= power;
                fireball.setDeltaMovement(angle.scale(power));
                fireball.moveTo(getEyePosition().add(angle.scale(0.25)));
                level().addFreshEntity(fireball);
            }
        }

        @Override
        public void stop() {
            windDownTicks = windDown;
            animator.setAnimation(getTarget() == null ? AnimationRegistry.BlazeSentry.IDLE_BASE.get() : AnimationRegistry.BlazeSentry.AGRO_BASE.get());
        }

        @Override
        public boolean canContinueToUse() {
            return (ticks - windup) / ticksBetweenFireballs < maxFireballs && getTarget() != null;
        }
    }

    public class FireBigGoal extends Goal {
        public static final EnumSet<Goal.Flag> flags = EnumSet.of(Flag.LOOK, Flag.TARGET);
        public static final int windup = 20, windDown = 100;
        public static final double power = 2;
        int ticks = 0;

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public boolean canUse() {
            return windDownTicks <= 0 && !animator.isTransitioning() && animator.getAnimation() == AnimationRegistry.BlazeSentry.AGRO_BASE.get();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            //System.out.println("started");
            animator.queueAnimation(TransitionAnimationRegistry.BlazeSentry.AGRO_BASE_TO_SHOOT_BASE.get());
            animator.queueAnimation(AnimationRegistry.BlazeSentry.SHOOT_BIG_BASE.get());
            animator.queueAnimation(TransitionAnimationRegistry.BlazeSentry.SHOOT_BASE_TO_AGRO_BASE.get());
            ticks = 0;
        }

        @Override
        public void tick() {
            ticks++;
            if (ticks == windup) {
                LivingEntity target = getTarget();
                assert target != null;
                Vec3 angle = new Vec3(sin(rotation.bodyYRot) * cos(rotation.bodyXRot), sin(rotation.bodyXRot), cos(rotation.bodyYRot) * cos(rotation.bodyXRot));
                SmallFireball fireball = new SmallFireball(level(), BlazeSentry.this, angle.x, angle.y, angle.z);
                fireball.xPower *= power; fireball.yPower *= power; fireball.zPower *= power;
                fireball.setDeltaMovement(angle.scale(power));
                fireball.moveTo(getEyePosition().add(angle.scale(0.25)));
                level().addFreshEntity(fireball);
            }
        }

        @Override
        public void stop() {
            windDownTicks = windDown;
            animator.setAnimation(getTarget() == null ? AnimationRegistry.BlazeSentry.IDLE_BASE.get() : AnimationRegistry.BlazeSentry.AGRO_BASE.get());
        }

        @Override
        public boolean canContinueToUse() {
            return ticks < windup;
        }
    }
}
