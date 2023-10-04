package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.AnimationRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.Supplier;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumGolem extends AbstractGolem implements NeutralMob, GeoAnimatable {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(10 * 60, 20 * 60);

    protected static final Supplier<List<AbstractAnimation>> IDLE_ANIMATIONS = () -> List.of(
            AnimationRegistry.IridiumGolem.IDLE_1.get(),
            AnimationRegistry.IridiumGolem.IDLE_2.get(),
            AnimationRegistry.IridiumGolem.IDLE_3.get()
    );

    public static final List<Function<IridiumGolem, StandingAttackGoal>> standstillAttacks = List.of(
            (golem) -> golem.new StandingAttackGoal(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"))
    );

    protected static final EntityDataAccessor<Animation> DATA_ANIMATION = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.ANIMATION.get());
    protected static final EntityDataAccessor<Optional<TransitionAnimation>> DATA_TRANSITION_ANIMATION = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.TRANSITION_ANIMATION.get());
    protected static final EntityDataAccessor<MindState> DATA_MIND_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_MIND_STATE.get());
    protected static final EntityDataAccessor<Integer> DATA_TICKS_REMAINING_IN_ANIMATION = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);


    private Animation lastNonTransitionAnimation;
    private AbstractAnimation lastAnimation;
    private int timeUntilIdleAnimation = 5 * 20; // 5 seconds of idle time after spawning, do idle animation

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;
    public boolean wasMoving = false;

    private final Queue<AbstractAnimation> queuedAnims = new LinkedList<>();
    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    protected void registerGoals() {
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
        entityData.define(DATA_ANIMATION, AnimationRegistry.IridiumGolem.IDLE_BASE.get());
        entityData.define(DATA_TRANSITION_ANIMATION, Optional.empty());
        entityData.define(DATA_MIND_STATE, MindState.IDLE);
        entityData.define(DATA_TICKS_REMAINING_IN_ANIMATION, 0);
    }

    public @Nullable Animation getAnimation() {
        return entityData.get(DATA_ANIMATION);
    }

    public void setAnimation(Animation anim) {
        if (!level().isClientSide()) {
            Animation oldAnim = getAnimation();
            if (oldAnim != anim) {
                entityData.set(DATA_ANIMATION, anim);
            }
        }
    }

    public @Nullable TransitionAnimation getTransitionAnimation() {
        return entityData.get(DATA_TRANSITION_ANIMATION).orElse(null);
    }

    public void setTransitionAnimation(@Nullable TransitionAnimation anim) {
        TransitionAnimation oldAnim = getTransitionAnimation();
        if (oldAnim != anim) {
            entityData.set(DATA_TRANSITION_ANIMATION, Optional.ofNullable(anim));
        }
    }

    public MindState getMindState() {
        return entityData.get(DATA_MIND_STATE);
    }

    public void playAnimation(AbstractAnimation animation) {
        if (animation instanceof Animation normalAnim) {
            setAnimation(normalAnim);
            setTransitionAnimation(null);
        } else if (animation instanceof TransitionAnimation transition) setTransitionAnimation(transition);
    }

    public void setMindState(MindState mindState) {
        entityData.set(DATA_MIND_STATE, mindState);
    }

    public int getTicksRemainingInAnimation() {
        return entityData.get(DATA_TICKS_REMAINING_IN_ANIMATION);
    }

    public void setTicksRemainingInAnimation(int ticksRemainingInAnimation) {
        entityData.set(DATA_TICKS_REMAINING_IN_ANIMATION, ticksRemainingInAnimation);
    }

    public boolean isIdle() {
        return getAnimation() == AnimationRegistry.IridiumGolem.IDLE_BASE.get();
    }

    @Override
    public void setAggressive(boolean value) {
        super.setAggressive(value);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("animation", Optional.ofNullable(getAnimation()).map((it) -> it.id().toString()).orElse("null"));
        tag.putString("mind_state", getMindState().name());
        tag.putInt("time_until_next_anim", getTicksRemainingInAnimation());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        try {
            Animation animation = Main.animationRegistry().getValue(new ResourceLocation(tag.getString("animation")));
            assert animation != null;
            setAnimation(animation);
        } catch (IllegalArgumentException ignored) { }
        try {
            MindState mindState = MindState.valueOf(tag.getString("mind_state"));
            setMindState(mindState);
        } catch (IllegalArgumentException ignored) { }
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
        var animation = getNavigation().getPath() == null || (getTransitionAnimation() != null && getTransitionAnimation().speedModifier(getTicksRemainingInAnimation()) == 0) ? getMindState().nonMovingAnim : getMindState().movingAnim;
        if (animation != null && !level().isClientSide()) setAnimation(animation);
        if (!level().isClientSide() && getTicksRemainingInAnimation() > 0) setTicksRemainingInAnimation(getTicksRemainingInAnimation() - 1);

        if (!level().isClientSide()) tickAnimations();

        lastAnimation = getTransitionAnimation() != null ? getTransitionAnimation() : getAnimation();
    }

    public void tickAnimations() {
        Animation animation = getAnimation();

        // Check if transition should be played
        if (animation != lastNonTransitionAnimation) {
            TransitionAnimation transitionAnim = GeneralUtil.getTransitionAnimation(lastNonTransitionAnimation, animation);
            if (transitionAnim != null) queueAnimation(transitionAnim);
            lastNonTransitionAnimation = animation;
        }

        // Choose Animation to play if the current one should be over
        if (getTicksRemainingInAnimation() <= 0 || lastAnimation == null || lastAnimation.canBeSkipped()) {
            AbstractAnimation anim;
            if (queuedAnims.isEmpty()) {
                //Does not continue with another Idle animation until the animation is complete
                if (animation == AnimationRegistry.IridiumGolem.IDLE_BASE.get() && timeUntilIdleAnimation <= 0) {
                    timeUntilIdleAnimation = (int) (20 * (10 + 15 * level().getRandom().nextDouble())); // 10-25s
                    anim = GeneralUtil.randomElement(IDLE_ANIMATIONS.get(), level().getRandom());
                } else {
                    anim = animation;
                }
            } else {
                anim = queuedAnims.poll();
            }

            if (anim != null) {
                playAnimation(anim);
                setTicksRemainingInAnimation(anim.lengthInTicks());
            }
        }
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

    public PlayState animate(software.bernie.geckolib.core.animation.AnimationState<IridiumGolem> animState) {
        AnimationController<IridiumGolem> animController = animState.getController();

        TransitionAnimation transition = getTransitionAnimation();
        Animation animation = getAnimation();
        animController.setAnimation(transition != null ? transition.raw() : (animation != null ? animation.raw() : null));

        return PlayState.CONTINUE;
    }

    private final AnimationController<IridiumGolem> bodyAnimController = new AnimationController<>(this, "body", 0, (animState) -> animState.getAnimatable().animate(animState));

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

    public enum MindState {
        IDLE(AnimationRegistry.IridiumGolem.IDLE_BASE.get(), AnimationRegistry.IridiumGolem.WALK_BASE.get()),
        AGRO(AnimationRegistry.IridiumGolem.AGRO_BASE.get(), AnimationRegistry.IridiumGolem.RUN_BASE.get()),
        ATTACK_2(AnimationRegistry.IridiumGolem.ATTACK_SMASH.get(), null);

        public final @Nullable Animation nonMovingAnim;
        public final @Nullable Animation movingAnim;
        MindState(@Nullable Animation nonMovingAnim, @Nullable Animation movingAnim) {
            this.nonMovingAnim = nonMovingAnim;
            this.movingAnim = movingAnim;
        }
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
            getNavigation().setSpeedModifier(0);
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
            getNavigation().setSpeedModifier(1);
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
                getNavigation().setSpeedModifier(getTicksRemainingInAnimation() > 0 && lastAnimation instanceof TransitionAnimation trans ? trans.speedModifier(getTicksRemainingInAnimation()) : speedModifier);
            }
        }

        protected void checkAndPerformAttack() {
            LivingEntity target = getTarget();
            if (target != null) {
                if (ticksUntilNextAttack == attackDuration + endLag) {
                    if (getTransitionAnimation() == null && target.getBoundingBox().intersects(getAttackHitbox()))
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
    public void queueAnimation(AbstractAnimation anim) {
        queuedAnims.add(anim);
    }
}
