package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.entity.animation.ActingEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.registry.ActionRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ItemRegistry;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumGolem extends AbstractGolem implements NeutralMob, ActingEntity<IridiumGolem> {
    public static final List<Function<IridiumGolem, StandingAttackGoal>> standstillAttacks = List.of(
            (golem) -> golem.new StandingAttackGoal(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"))
    );
    protected static final Supplier<List<Action>> IDLE_ANIMATIONS = () -> List.of(
            ActionRegistry.IridiumGolem.IDLE_1.get(),
            ActionRegistry.IridiumGolem.IDLE_2.get(),
            ActionRegistry.IridiumGolem.IDLE_3.get()
    );
    protected static final EntityDataAccessor<MindState> DATA_MIND_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.IRIDIUM_GOLEM_MIND_STATE.get());
    protected static final EntityDataAccessor<Integer> DATA_TICKS_REMAINING_IN_ANIMATION = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Actor.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializerRegistry.ACTOR_STATE.get());
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IridiumGolem.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(10 * 60, 20 * 60);
    private final Actor<IridiumGolem> actor = new Actor<>(this, ActionRegistry.IridiumGolem.IDLE_BASE.get());

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;

    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.12).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.STEP_HEIGHT, 1.2f);
    }

    protected void registerGoals() {
        for (var goal : standstillAttacks) {
            this.goalSelector.addGoal(1, goal.apply(this));
        }
        this.goalSelector.addGoal(3, new IridiumGolemRandomStrollGoal(this, 1, 5));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
        builder.define(DATA_MIND_STATE, MindState.IDLE);
        builder.define(DATA_TICKS_REMAINING_IN_ANIMATION, 0);
        builder.define(DATA_ANIMATOR_STATE, new Actor.State(
                ActionRegistry.IridiumGolem.IDLE_BASE.get(),
                ActionRegistry.IridiumGolem.IDLE_BASE.get(),
                null,
                5 * 20,
                160,
                new LinkedList<>()
        ));
    }

    @Override
    public @Nullable Action getActionChange() {
        Action anim = getNavigation().getPath() == null ?
                getMindState().nonMovingAnim : getMindState().movingAnim;
        //System.out.println(anim);
        return anim;
    }

    @Override
    public Actor<IridiumGolem> getActor() {
        return actor;
    }

    @Override
    public List<Action> idleActions() {
        return IDLE_ANIMATIONS.get();
    }

    @Override
    public EntityDataAccessor<Actor.State> actionStateAccessor() {
        return DATA_ANIMATOR_STATE;
    }

    public MindState getMindState() {
        return entityData.get(DATA_MIND_STATE);
    }

    public void setMindState(MindState mindState) {
        entityData.set(DATA_MIND_STATE, mindState);
    }

    public int getTicksRemainingInAnimation() {
        return entityData.get(DATA_TICKS_REMAINING_IN_ANIMATION);
    }

    @Override
    public boolean isIdle() {
        return actor.getNextAction() == ActionRegistry.IridiumGolem.IDLE_BASE.get();
    }

    @Override
    public void setAggressive(boolean value) {
        super.setAggressive(value);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        //tag.putString("animation", Optional.ofNullable(getAnimation()).map((it) -> it.id().toString()).orElse("null"));
        tag.putString("mind_state", getMindState().name());
        tag.putInt("time_until_next_anim", getTicksRemainingInAnimation());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        try {
            MindState mindState = MindState.valueOf(tag.getString("mind_state"));
            setMindState(mindState);
        } catch (IllegalArgumentException ignored) {
        }
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
        actor.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        actor.register(controllerRegistrar);
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
    public boolean doesIdleActions() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return ItemRegistry.IRIDIUM_GOLEM_SPAWN_EGG.get().getDefaultInstance();
    }

    public enum MindState {
        IDLE(ActionRegistry.IridiumGolem.IDLE_BASE.get(), ActionRegistry.IridiumGolem.WALK_BASE.get()),
        AGRO(ActionRegistry.IridiumGolem.AGRO_BASE.get(), ActionRegistry.IridiumGolem.RUN_BASE.get()),
        ATTACK_2(ActionRegistry.IridiumGolem.ATTACK_SMASH.get(), null);

        public final @Nullable Action nonMovingAnim;
        public final @Nullable Action movingAnim;

        MindState(@Nullable Action nonMovingAnim, @Nullable Action movingAnim) {
            this.nonMovingAnim = nonMovingAnim;
            this.movingAnim = movingAnim;
        }
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

    public class StandingAttackGoal extends Goal {
        public final RawAnimation animation;
        public final int attackDuration = 20;
        public final int endLag = 28;
        public final float speedModifier = 7.5f;
        private final boolean canPenalize = false;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;
        private int failedPathFindingPenalty = 0;

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
            return (!(target == null || !target.isAlive() || !isWithinRestriction(target.blockPosition())) && (!(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative()));
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
                double perceivedDistanceSqr = getAttackReachSqr(target);
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

                if (ticksUntilNextAttack != attackDuration + endLag)
                    ticksUntilNextAttack = Math.max(ticksUntilNextAttack - 1, 0);
                checkAndPerformAttack();
            }
        }

        protected void checkAndPerformAttack() {
            LivingEntity target = getTarget();
            if (target != null) {
                if (ticksUntilNextAttack == attackDuration + endLag) {
                    if (actor.getTransientAction() == null && actor.getNextAction().is(ActionRegistry.IridiumGolem.AGRO_BASE) && target.getBoundingBox().intersects(getAttackHitbox()))
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
    }
}
