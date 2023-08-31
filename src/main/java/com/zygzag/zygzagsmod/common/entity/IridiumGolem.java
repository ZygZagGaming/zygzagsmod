package com.zygzag.zygzagsmod.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class IridiumGolem extends AbstractGolem implements NeutralMob, GeoAnimatable {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(10 * 60, 20 * 60);
    protected static final RawAnimation START_WALKING = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk").thenLoop("animation.iridium_golem.walk_cycle");
    protected static final RawAnimation STOP_WALKING = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.iridium_golem.walk_cycle");
    protected static final RawAnimation WALK_START = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk");
    protected static final RawAnimation WALK_STOP = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk");
    protected static final RawAnimation IDLE1 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle1");
    protected static final RawAnimation IDLE2 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle2");
    protected static final RawAnimation IDLE3 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle3");
    protected static final RawAnimation IDLE_LOOP = RawAnimation.begin().thenLoop("animation.iridium_golem.idle_loop");

    protected static final EntityDataAccessor<Boolean> DATA_IS_MOVING = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;

    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new IridiumGolemMeleeAttackGoal(this, 5, true));
        this.goalSelector.addGoal(2, new IridiumGolemMoveTowardsTargetGoal(this, 5, 64f));
        this.goalSelector.addGoal(3, new IridiumGolemRandomStrollGoal(this, 1));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        entityData.define(DATA_IS_MOVING, false);
    }

    public boolean isMoving() {
        return entityData.get(DATA_IS_MOVING);
    }

    public void setMoving(boolean isMoving) {
        entityData.set(DATA_IS_MOVING, isMoving);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int amt) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, amt);
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID amt) {
        this.persistentAngerTarget = amt;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void tick() {
        //walkAnimController.setAnimation(WALK_ANIM);
        setOldPosAndRot();
        super.tick();
        //sinceMotionChange++;
    }

//    @Override
//    public Vec3 getDeltaMovement() {
//        return new Vec3(xxa, yya, zza);
//    }
//
//    @Override
//    public void setDeltaMovement(Vec3 vec) {
//        xxa = (float) vec.x();
//        yya = (float) vec.y();
//        zza = (float) vec.z();
//    }
//
//    @Override
//    public void addDeltaMovement(Vec3 vec) {
//        xxa += (float) vec.x();
//        yya += (float) vec.y();
//        zza += (float) vec.z();
//    }
//
//    @Override
//    public void setDeltaMovement(double x, double y, double z) {
//        xxa = (float) x;
//        yya = (float) y;
//        zza = (float) z;
//    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.08).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(walkAnimationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    private final AnimationController<IridiumGolem> walkAnimationController = new AnimationController<>(this, "walk", 5, (animState) -> {
        AnimationController<IridiumGolem> animController = animState.getController();
        if (isMoving()) {
            animController.setAnimation(START_WALKING);
        } else {
            if (animController.getCurrentRawAnimation() != null) {
                if (animController.getCurrentRawAnimation().equals(STOP_WALKING)) {
                    if (animController.hasAnimationFinished()) {
                        animController.setAnimation(IDLE_LOOP);
                    }
                } else if (animController.getCurrentRawAnimation().equals(IDLE_LOOP)) {
                    if (Math.random() < 0.001) animController.setAnimation(IDLE1);
                    else if (Math.random() < 0.001) animController.setAnimation(IDLE2);
                    else if (Math.random() < 0.001) animController.setAnimation(IDLE3);
                } else {
                    if (animController.hasAnimationFinished()) animController.setAnimation(IDLE_LOOP);
                }
            } else {
                animController.setAnimation(IDLE_LOOP);
            }
        }
        return PlayState.CONTINUE;

    });
    private final AnimationController<IridiumGolem> idleAnimationController = new AnimationController<>(this, "idle", 5, (animState) -> {
        AnimationController<IridiumGolem> animController = animState.getController();
        if (isMoving()) return PlayState.STOP;
        else if (animController.hasAnimationFinished()) {
            if (Math.random() < 0.01) animController.setAnimation(IDLE1);
            else if (Math.random() < 0.01) animController.setAnimation(IDLE2);
            else if (Math.random() < 0.01) animController.setAnimation(IDLE3);
            else animController.setAnimation(IDLE_LOOP);
        }
        return PlayState.CONTINUE;
    });

            /*.setParticleKeyframeHandler((event) -> {
        var data = event.getKeyframeData();
        var controller = event.getController();
        var effect = data.getEffect();
        var locator = data.getLocator();

    });*/

    private class IridiumGolemRandomStrollGoal extends RandomStrollGoal {
        public IridiumGolemRandomStrollGoal(PathfinderMob entity, double speedModifier) {
            super(entity, speedModifier);
        }

        @Override
        public void start() {
            super.start();
            setMoving(true);
        }

        @Override
        public void stop() {
            super.stop();
            setMoving(false);
        }
    }

    private class IridiumGolemMoveTowardsTargetGoal extends MoveTowardsTargetGoal {
        public IridiumGolemMoveTowardsTargetGoal(PathfinderMob entity, double speedModifier, float within) {
            super(entity, speedModifier, within);
        }

        @Override
        public void start() {
            super.start();
            setMoving(true);
        }

        @Override
        public void stop() {
            super.stop();
            setMoving(false);
        }
    }

    private class IridiumGolemMeleeAttackGoal extends MeleeAttackGoal {
        public IridiumGolemMeleeAttackGoal(PathfinderMob entity, double speedModifier, boolean objectPermanence) {
            super(entity, speedModifier, objectPermanence);
        }

        @Override
        public void start() {
            super.start();
            setMoving(true);
        }

        @Override
        public void stop() {
            super.stop();
            setMoving(false);
        }
    }
}
