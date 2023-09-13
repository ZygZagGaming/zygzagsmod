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
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumGolem extends AbstractGolem implements NeutralMob, GeoAnimatable {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(10 * 60, 20 * 60);
    protected static final RawAnimation START_WALKING = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk").thenLoop("animation.iridium_golem.walk_cycle");
    protected static final RawAnimation STOP_WALKING = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk");
    protected static final RawAnimation START_RUNNING = RawAnimation.begin().thenPlay("animation.iridium_golem.start_run").thenLoop("animation.iridium_golem.run_cycle");
    protected static final RawAnimation STOP_RUNNING = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_run");
    protected static final RawAnimation WALK_START = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk");
    protected static final RawAnimation WALK_STOP = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk");
    protected static final RawAnimation IDLE1 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle1");
    protected static final RawAnimation IDLE2 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle2");
    protected static final RawAnimation IDLE3 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle3");
    protected static final RawAnimation IDLE_LOOP = RawAnimation.begin().thenLoop("animation.iridium_golem.idle_loop");
    protected static final Map<State, RawAnimation> ANIMATION_LOOPS = Map.of(
            State.IDLE, RawAnimation.begin().thenLoop("animation.iridium_golem.idle_loop"),
            State.WALKING, RawAnimation.begin().thenLoop("animation.iridium_golem.walk_cycle"),
            State.RUNNING, RawAnimation.begin().thenLoop("animation.iridium_golem.run_cycle"),
            State.AGRO, RawAnimation.begin().thenLoop("animation.iridium_golem.agro_idle")
    );
    protected static final Map<Pair<State, State>, RawAnimation> TRANSITION_ANIMATIONS = Map.of(
            new Pair<>(State.IDLE, State.WALKING), RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk"),
            new Pair<>(State.WALKING, State.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk"),
            new Pair<>(State.IDLE, State.AGRO), RawAnimation.begin().thenPlay("animation.iridium_golem.agro"),
            new Pair<>(State.AGRO, State.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"),
            new Pair<>(State.IDLE, State.RUNNING), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run_from_idle"),
            new Pair<>(State.RUNNING, State.IDLE), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"),
            new Pair<>(State.AGRO, State.WALKING), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro").thenPlay("animation.iridium_golem.start_walk"),
            new Pair<>(State.WALKING, State.AGRO), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.agro")
    );
    protected final Lazy<List<StandingAttackGoal>> standstillAttacks = Lazy.of(() -> List.of(

    ));

    protected static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_STATE.get());
    protected static final EntityDataAccessor<Optional<CurrentAttack>> DATA_ATTACK = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_ATTACK.get());

    private State lastState = State.IDLE;
    private int timeUntilIdleAnimation = 5 * 20; // 5 seconds of idle time after spawning, do idle animation

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;

    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new IridiumGolemMeleeAttackGoal(this, 5, true));
//        this.goalSelector.addGoal(2, new IridiumGolemMoveTowardsTargetGoal(this, 5, 64f));
        for (StandingAttackGoal goal : standstillAttacks.get()) {
            this.goalSelector.addGoal(1, goal);
        }
        this.goalSelector.addGoal(3, new IridiumGolemRandomStrollGoal(this, 1, 5));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        entityData.define(DATA_STATE, State.IDLE);
        entityData.define(DATA_ATTACK, Optional.empty());
    }

    public State getState() {
        return entityData.get(DATA_STATE);
    }

    public void setState(State state) {
        entityData.set(DATA_STATE, state);
    }

    public @Nullable CurrentAttack getCurrentAttack() {
        return entityData.get(DATA_ATTACK).orElse(null);
    }

    public void setCurrentAttack(@Nullable CurrentAttack attack) {
        entityData.set(DATA_ATTACK, Optional.ofNullable(attack));
    }

    public boolean isIdle() {
        return getState() == State.IDLE;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("state", getState().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        try {
            State state = State.valueOf(tag.getString("state"));
            setState(state);
        } catch (IllegalArgumentException ignored) { }
    }

    public State getIdleState() {
        return getTarget() == null ? State.IDLE : State.AGRO;
    }

    public State getWalkingState() {
        return getTarget() == null ? State.WALKING : State.RUNNING;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        State state = getState();
        if (target == null) {
            if (state == State.AGRO) setState(State.IDLE);
            if (state == State.RUNNING) setState(State.WALKING);
        } else {
            if (state == State.IDLE) setState(State.AGRO);
            if (state == State.WALKING) setState(State.RUNNING);
        }
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

    private final AnimationController<IridiumGolem> bodyAnimController = new AnimationController<>(this, "body", 0, (animState) -> {
        AnimationController<IridiumGolem> animController = animState.getController();
        State state = getState();
        if (state != lastState) {
            //System.out.println("last state " + lastState + ", state " + state);
            RawAnimation transitionAnim = TRANSITION_ANIMATIONS.get(new Pair<>(lastState, state));
            animController.setAnimation(transitionAnim == null ? ANIMATION_LOOPS.get(state) : transitionAnim);
        } else if (animController.hasAnimationFinished() || animController.getCurrentRawAnimation() == null) {
            if (state == State.IDLE && timeUntilIdleAnimation <= 0) {
                doIdleAnimation(animController);
            } else {
                RawAnimation anim = ANIMATION_LOOPS.get(state);
                animController.setAnimation(anim);
            }
        }
        lastState = state;

        return PlayState.CONTINUE;

    });

    private final AnimationController<IridiumGolem> attackAnimController = new AnimationController<>(this, "attack", 0, (animState) -> {
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
        }

        @Override
        public void start() {
            getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, getTarget() == null ? speedModifier : runningSpeedModifier);
            setState(getWalkingState());
        }

        @Override
        public void stop() {
            super.stop();
            setState(getIdleState());
        }
    }

    public enum State {
        IDLE,
        WALKING,
        RUNNING,
        AGRO;
    }

    public enum CurrentAttack {
        ATTACK2;
    }

    public abstract class StandingAttackGoal extends Goal {
        public static final int GLOBAL_ATTACK_COOLDOWN = 40;
        public static int attackCooldown = 0;
        public final RawAnimation animation;
        public StandingAttackGoal(RawAnimation animation) {
            this.animation = animation;
        }

        @Override
        public void start() {
            super.start();

        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public boolean canUse() {
            return getState() == State.AGRO;
        }
    }

    public class AttackGoal2 extends StandingAttackGoal {
        public AttackGoal2() {
            super(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"));
        }

        @Override
        public boolean canUse() {
            return getState() == State.IDLE;
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
}
