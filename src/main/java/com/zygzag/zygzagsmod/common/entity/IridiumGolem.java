package com.zygzag.zygzagsmod.common.entity;

import com.mojang.datafixers.util.Pair;
import com.zygzag.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumGolem extends AbstractGolem implements NeutralMob, GeoAnimatable {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(10 * 60, 20 * 60);
    protected static final RawAnimation IDLE1 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle1");
    protected static final RawAnimation IDLE2 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle2");
    protected static final RawAnimation IDLE3 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle3");
    protected static final Map<AnimationState, RawAnimation> ANIMATION_LOOPS = Map.of(
            AnimationState.IDLE, RawAnimation.begin().thenLoop("animation.iridium_golem.idle_loop"),
            AnimationState.WALKING, RawAnimation.begin().thenLoop("animation.iridium_golem.walk_cycle"),
            AnimationState.RUNNING, RawAnimation.begin().thenLoop("animation.iridium_golem.run_cycle"),
            AnimationState.AGRO, RawAnimation.begin().thenLoop("animation.iridium_golem.agro_idle"),
            AnimationState.ATTACK_2, RawAnimation.begin().thenPlay("animation.iridium_golem.attack2")
    );
    protected static final Map<Pair<AnimationState, AnimationState>, RawAnimation> TRANSITION_ANIMATIONS = Map.of(
            new Pair<>(AnimationState.IDLE, AnimationState.WALKING), RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk"),
            new Pair<>(AnimationState.WALKING, AnimationState.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk"),
            new Pair<>(AnimationState.IDLE, AnimationState.AGRO), RawAnimation.begin().thenPlay("animation.iridium_golem.agro"),
            new Pair<>(AnimationState.AGRO, AnimationState.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"),
            new Pair<>(AnimationState.IDLE, AnimationState.RUNNING), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run_from_idle"),
            new Pair<>(AnimationState.RUNNING, AnimationState.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"),
            new Pair<>(AnimationState.AGRO, AnimationState.WALKING), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro").thenPlay("animation.iridium_golem.start_walk"),
            new Pair<>(AnimationState.WALKING, AnimationState.AGRO), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.agro")
    );
    public static final List<Function<IridiumGolem, StandingAttackGoal>> standstillAttacks = List.of(
            (golem) -> golem.new StandingAttackGoal(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"))
    );

    protected static final EntityDataAccessor<AnimationState> DATA_ANIMATION_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_ANIMATION_STATE.get());
    protected static final EntityDataAccessor<MindState> DATA_MIND_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_MIND_STATE.get());
    protected static final EntityDataAccessor<Optional<CurrentAttack>> DATA_ATTACK = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_ATTACK.get());

    private AnimationState lastAnimationState = AnimationState.IDLE;
    private int timeUntilIdleAnimation = 5 * 20; // 5 seconds of idle time after spawning, do idle animation

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;
    public boolean wasMoving = false;


    private @Nullable RawAnimation queuedAnim = null;
    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new IridiumGolemMeleeAttackGoal(this, 5, true));
//        this.goalSelector.addGoal(2, new IridiumGolemMoveTowardsTargetGoal(this, 5, 64f));
        for (var goal : standstillAttacks) {
            this.goalSelector.addGoal(1, goal.apply(this));
        }
        this.goalSelector.addGoal(3, new IridiumGolemRandomStrollGoal(this, 1, 5));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        entityData.define(DATA_ANIMATION_STATE, AnimationState.IDLE);
        entityData.define(DATA_MIND_STATE, MindState.IDLE);
        entityData.define(DATA_ATTACK, Optional.empty());
    }

    public AnimationState getAnimationState() {
        return entityData.get(DATA_ANIMATION_STATE);
    }

    public void setAnimationState(AnimationState animationState) {
        entityData.set(DATA_ANIMATION_STATE, animationState);
    }

    public MindState getMindState() {
        return entityData.get(DATA_MIND_STATE);
    }

    public void setMindState(MindState mindState) {
        entityData.set(DATA_MIND_STATE, mindState);
    }

    public @Nullable CurrentAttack getCurrentAttack() {
        return entityData.get(DATA_ATTACK).orElse(null);
    }

    public void setCurrentAttack(@Nullable CurrentAttack attack) {
        entityData.set(DATA_ATTACK, Optional.ofNullable(attack));
    }

    public boolean isIdle() {
        return getAnimationState() == AnimationState.IDLE;
    }

    @Override
    public void setAggressive(boolean value) {
        getNavigation().setSpeedModifier(value ? 3 : 1);
        super.setAggressive(value);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("animation_state", getAnimationState().name());
        tag.putString("mind_state", getMindState().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        try {
            AnimationState animationState = AnimationState.valueOf(tag.getString("animation_state"));
            setAnimationState(animationState);
        } catch (IllegalArgumentException ignored) { }
        try {
            MindState mindState = MindState.valueOf(tag.getString("mind_state"));
            setMindState(mindState);
        } catch (IllegalArgumentException ignored) { }
    }

    public AnimationState getIdleState() {
        return getTarget() == null ? AnimationState.IDLE : AnimationState.AGRO;
    }

    public AnimationState getWalkingState() {
        return getTarget() == null ? AnimationState.WALKING : AnimationState.RUNNING;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (getMindState() == MindState.IDLE && target != null) setMindState(MindState.AGRO);
        if (getMindState() == MindState.AGRO && target == null) setMindState(MindState.IDLE);
    }

    public void setNeutralState() {
        if (getTarget() != null) setMindState(MindState.AGRO);
        else setMindState(MindState.IDLE);
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
        setOldPosAndRot();
        super.tick();
        if (isIdle()) timeUntilIdleAnimation--;
        var animationState = getNavigation().getPath() == null ? getMindState().nonMovingState : getMindState().movingState;
        if (animationState != null && !level().isClientSide) setAnimationState(animationState);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.08).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(bodyAnimController);
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
    public float getStepHeight() {
        return getTarget() == null ? 0.2f : 1.2f;
    }

    private final AnimationController<IridiumGolem> bodyAnimController = new AnimationController<>(this, "body", 0, (animState) -> {
        AnimationController<IridiumGolem> animController = animState.getController();
        AnimationState animationState = getAnimationState();
        if (queuedAnim == null) {
            if (animationState != lastAnimationState) {
                //System.out.println("last state " + lastState + ", state " + state);
                RawAnimation transitionAnim = TRANSITION_ANIMATIONS.get(new Pair<>(lastAnimationState, animationState));
                animController.setAnimation(transitionAnim == null ? ANIMATION_LOOPS.get(animationState) : transitionAnim);
            } else if (animController.hasAnimationFinished() || animController.getCurrentRawAnimation() == null) {
                if (animationState == AnimationState.IDLE && timeUntilIdleAnimation <= 0) {
                    doIdleAnimation(animController);
                } else {
                    RawAnimation anim = ANIMATION_LOOPS.get(animationState);
                    animController.setAnimation(anim);
                }
            }
        } else playQueuedAnimation(animController);
        lastAnimationState = animationState;


        return PlayState.CONTINUE;
    });

    public void doIdleAnimation(AnimationController<IridiumGolem> animationController) {
        timeUntilIdleAnimation = (int) (20 * (10 + 15 * Math.random())); // 10-25s
        RawAnimation animationToUse = new RawAnimation[]{
                IDLE1, IDLE2, IDLE3
        }[(int) (Math.random() * 3)];
        animationController.setAnimation(animationToUse);
    }

    private class IridiumGolemRandomStrollGoal extends RandomStrollGoal {
        double runningSpeedModifier;
        public IridiumGolemRandomStrollGoal(PathfinderMob entity, double speedModifier, double runningSpeedModifier) {
            super(entity, speedModifier);
            this.runningSpeedModifier = runningSpeedModifier;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public void start() {
            getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, getTarget() == null ? speedModifier : runningSpeedModifier);
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    public enum AnimationState {
        IDLE,
        WALKING,
        RUNNING,
        AGRO,
        ATTACK_2;
    }

    public enum MindState {
        IDLE(AnimationState.IDLE, AnimationState.WALKING),
        AGRO(AnimationState.AGRO, AnimationState.RUNNING),
        ATTACK_2(AnimationState.ATTACK_2, null);

        public final @Nullable AnimationState nonMovingState;
        public final @Nullable AnimationState movingState;
        MindState(@Nullable AnimationState nonMovingState, @Nullable AnimationState movingState) {
            this.nonMovingState = nonMovingState;
            this.movingState = movingState;
        }
    }

    public enum CurrentAttack {
        ATTACK2;
    }

    public class StandingAttackGoal extends Goal {
        public static final int GLOBAL_ATTACK_COOLDOWN = 40;
        public static int attackCooldown = 0;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;
        public final RawAnimation animation;
        public final int attackDuration = 20;
        public final int endLag = 15;
        public final float speedModifier = 7.5f;
        public StandingAttackGoal(RawAnimation animation) {
            this.animation = animation;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public void start() {
            super.start();

            getNavigation().moveTo(this.path, speedModifier);
            setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            ticksUntilNextAttack = attackDuration + endLag;
        }

        @Override
        public void stop() {
            super.stop();
            LivingEntity livingentity = getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                setTarget(null);
            }

            setAggressive(false);
            getNavigation().stop();
            resetAttackCooldown();
            setNeutralState();
        }

        @Override
        public boolean canUse() {
            long gameTime = level().getGameTime();
            if (gameTime - lastCanUseCheck < 20) {
                return false;
            } else {
                lastCanUseCheck = gameTime;
                LivingEntity target = getTarget();
                if (target == null || !target.isAlive()) {
                    return false;
                } else {
                    if (canPenalize) {
                        if (--ticksUntilNextPathRecalculation <= 0) {
                            path = getNavigation().createPath(target, 0);
                            ticksUntilNextPathRecalculation = 4 + getRandom().nextInt(7);
                            return path != null;
                        } else {
                            return true;
                        }
                    }
                    path = getNavigation().createPath(target, 0);
                    if (path != null) {
                        return true;
                    } else {
                        return getAttackReachSqr(target) >= distanceToSqr(target.getX(), target.getY(), target.getZ());
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity target = getTarget();
            return (!(target == null || !target.isAlive() || !isWithinRestriction(target.blockPosition())) && (!(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative()));
        }

        protected double getAttackReachSqr(LivingEntity living) {
            return (getBbWidth() * 2.0F * getBbWidth() * 2.0F + living.getBbWidth());
        }

        public AABB getAttackHitbox() {
            Vec3 facingDirectionHoriz = getLookAngle().multiply(1, 0, 1).normalize();
            // facing x (0, 1, 0) should be the right-facing vector
            Vec3 rightFacingHoriz = facingDirectionHoriz.cross(new Vec3(0, 1, 0)).normalize(); // should already be unit but whatever
            Vec3 centerOfAttack = position().add(facingDirectionHoriz.scale(1.2)).add(rightFacingHoriz.scale(0.6));
            double attackSizeHoriz = 1;
            double attackSizeY = 0.6;
            return new AABB(
                    centerOfAttack.x + attackSizeHoriz,
                    centerOfAttack.y + attackSizeY,
                    centerOfAttack.z + attackSizeHoriz,
                    centerOfAttack.x - attackSizeHoriz,
                    centerOfAttack.y - attackSizeY,
                    centerOfAttack.z - attackSizeHoriz
            );
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
        public void tick() {
            LivingEntity target = getTarget();
            //if (ticksUntilNextAttack >= attackDuration) getNavigation().moveTo(path, 10);
            if (target != null) {
                getLookControl().setLookAt(target, 30.0F, 30.0F);
                double perceivedDistanceSqr = getPerceivedTargetDistanceSquareForMeleeAttack(target);
                if (ticksUntilNextPathRecalculation > 0) ticksUntilNextPathRecalculation--;
                if (ticksUntilNextPathRecalculation <= 0 && (pathedTargetX == 0.0 && pathedTargetY == 0.0 && pathedTargetZ == 0.0 || target.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1.0D || getRandom().nextFloat() < 0.05F)) {
                    pathedTargetX = target.getX();
                    pathedTargetY = target.getY();
                    pathedTargetZ = target.getZ();
                    ticksUntilNextPathRecalculation = 4 + getRandom().nextInt(7);
                    if (canPenalize) {
                        ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                        if (getNavigation().getPath() != null) {
                            Node finalPathPoint = getNavigation().getPath().getEndNode();
                            if (finalPathPoint != null && target.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                                failedPathFindingPenalty = 0;
                            else
                                failedPathFindingPenalty += 10;
                        } else {
                            failedPathFindingPenalty += 10;
                        }
                    }
                    if (perceivedDistanceSqr > 1024) {
                        ticksUntilNextPathRecalculation += 10;
                    } else if (perceivedDistanceSqr > 256) {
                        ticksUntilNextPathRecalculation += 5;
                    }

                    if (ticksUntilNextAttack >= attackDuration && !getNavigation().moveTo(target, speedModifier)) {
                        ticksUntilNextPathRecalculation += 15;
                    }

                    ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
                }

                if (ticksUntilNextAttack != attackDuration + endLag) ticksUntilNextAttack = Math.max(ticksUntilNextAttack - 1, 0);
                checkAndPerformAttack();
            }
        }

        protected void checkAndPerformAttack() {
            LivingEntity target = getTarget();
            if (target != null) {
                if (ticksUntilNextAttack == attackDuration + endLag) {
                    if (target.getBoundingBox().intersects(getAttackHitbox()))
                        ticksUntilNextAttack--; // start attack
                } else {
                    if (ticksUntilNextAttack == endLag) doAttack();
                    setMindState(MindState.ATTACK_2);
                    getNavigation().stop();
                    if (ticksUntilNextAttack == 0) {
                        resetAttackCooldown();
                        setNeutralState();
                        ticksUntilNextPathRecalculation = 0;
                    }
                }
            }
        }

        protected void doAttack() {
            AABB hitbox = getAttackHitbox();
            List<Entity> entities = level().getEntities(IridiumGolem.this, hitbox, (it) -> it instanceof LivingEntity);
            for (Entity entity : entities) doHurtTarget(entity);
        }

        protected void resetAttackCooldown() {
            ticksUntilNextAttack = attackDuration + endLag;
        }

        protected boolean isTimeToAttack() {
            return ticksUntilNextAttack == endLag;
        }

        protected int getTicksUntilNextAttack() {
            return ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return adjustedTickDelay(20);
        }
    }

    public class AttackGoal2 extends StandingAttackGoal {
        public AttackGoal2() {
            super(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }
    public void queueAnimation(RawAnimation anim) {
        queuedAnim = anim;
    }

    public void playQueuedAnimation(AnimationController<IridiumGolem> controller) {
        if (queuedAnim != null) {
            controller.setAnimation(queuedAnim);
            queuedAnim = null;
        }
    }
}
