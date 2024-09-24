package io.github.zygzaggaming.zygzagsmod.common.entity.assembly;

import java.util.*;
import java.util.function.Predicate;

import io.github.zygzaggaming.zygzagsmod.common.entity.SmallMagmaticFireball;
import io.github.zygzaggaming.zygzagsmod.common.entity.SmallRod;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.ActingEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.registry.ActionRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ParticleTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.LimitedRotation;
import io.github.zygzaggaming.zygzagsmod.common.util.Rotation;
import io.github.zygzaggaming.zygzagsmod.common.util.RotationArray;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SentryAAssembly extends Monster implements GeoAnimatable, ActingEntity<SentryAAssembly> {
    private float allowedHeightOffset = 0.5F;
    private int nextHeightOffsetChangeTick;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SentryAAssembly.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Actor.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(SentryAAssembly.class, EntityDataSerializerRegistry.ACTOR_STATE.get());
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Actor<SentryAAssembly> actor = new Actor<>(this, ActionRegistry.SentryAAssembly.SPIN_BASE.get());
    public static float[] maxRotationPerTick = {(float) (0.03125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public RotationArray rotations = new RotationArray(new Rotation[]{
            new LimitedRotation(0, 0, 0, 0, maxRotationPerTick[0])
    });
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(SentryAAssembly.class, EntityDataSerializers.OPTIONAL_UUID);
    @Nullable
    private LivingEntity cachedTarget;
    private int health;

    public SentryAAssembly(EntityType<? extends SentryAAssembly> p_32219_, Level p_32220_, int health) {
        super(p_32219_, p_32220_);
        this.health = health;
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new SentryAAssembly.RodAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        targetSelector.addGoal(2, new SentryAAssembly.OsuNATGoal<>(this, LivingEntity.class, 1, true, false, (entity) -> (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) || entity instanceof AbstractGolem));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.FOLLOW_RANGE, 48.0).add(Attributes.MAX_HEALTH, 500);
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
        if (!level().isClientSide()) {
            entityData.set(DATA_TARGET, Optional.ofNullable(entity).map(Entity::getUUID));
        }
    }

    private static boolean isReflectedFireball(DamageSource entity) {
        return entity.getDirectEntity() instanceof LargeFireball && entity.getEntity() instanceof Player;
    }
    @Override
    public boolean isInvulnerableTo(DamageSource fireball) {
        return this.isInvulnerable() && !fireball.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isReflectedFireball(fireball) && super.isInvulnerableTo(fireball);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (amount > 2) {
            super.hurt(source, 0);
            if (source.is(DamageTypes.ARROW)) health -= 2;
            else if (source.is(DamageTypes.FIREWORKS)) health -= 3;
            else if (isReflectedFireball(source)) health -=3;
            else health--;

            if (health <= 0) super.hurt(source, 1000);
        }
        return amount > 2;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANIMATOR_STATE, new Actor.State(
                ActionRegistry.SentryAAssembly.SPIN_BASE.get(),
                ActionRegistry.SentryAAssembly.SPIN_BASE.get(),
                null,
                99999999,
                40,
                new LinkedList<>()
        ));
        builder.define(DATA_TARGET, Optional.empty());
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public boolean isIdle() {
        return getTarget() == null;
    }

    @Override
    public Actor<SentryAAssembly> getActor() {
        return actor;
    }

    @Override
    public @Nullable Action getActionChange() {
        if (getTarget() == null) return ActionRegistry.SentryAAssembly.SPIN_BASE.get();
        else return null;
    }

    @Override
    public List<Action> idleActions() {
        return List.of();
    }

    @Override
    public EntityDataAccessor<Actor.State> actionStateAccessor() {
        return DATA_ANIMATOR_STATE;
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
        return false;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void resetBodyRotation() {
        rotations.get(0).set(-0.5f * (float) Math.PI, 0);
    }

    public void lookAt(LivingEntity target) {
        rotations.get(0).set(GeneralUtil.rectangularToXYRot(target.getBoundingBox().getCenter().subtract(getEyePosition())));
    }

    @Override
    public void aiStep() {
        if (!this.onGround() && this.getDeltaMovement().y < 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
        super.aiStep();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        actor.register(controllerRegistrar);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        this.nextHeightOffsetChangeTick--;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = (float)this.random.triangle(0.5, 6.891);
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double)this.allowedHeightOffset && this.canAttack(livingentity)) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, (0.3F - vec3.y) * 0.3F, 0.0));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }

    public void tick() {
        var target = getTarget();
        if (target == null/* || !target.isAlive()*/) {
            resetBodyRotation();
            actor.setNextAction(ActionRegistry.SentryAAssembly.IDLE_BASE.get());
        } else {
            lookAt(target);
            if (actor.getCurrentAction().is(ActionRegistry.SentryAAssembly.IDLE_BASE)) actor.setNextAction(ActionRegistry.SentryAAssembly.SPIN_BASE.get());
        }

        rotations.tick();

        setRot(0, 0);
        setYHeadRot(0);
        setYBodyRot(0);
        super.tick();
        actor.tick();
    }

    @Override
    public boolean isOnFire() {
        return this.isCharged();
    }

    private boolean isCharged() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    void setCharged(boolean p_32241_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_32241_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    static class RodAttackGoal extends Goal {
        private final SentryAAssembly sentryAAssembly;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        public RodAttackGoal(SentryAAssembly rods) {
            this.sentryAAssembly = rods;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.sentryAAssembly.getTarget();
            return livingentity != null && livingentity.isAlive() && this.sentryAAssembly.canAttack(livingentity);
        }

        @Override
        public void start() {
            this.attackStep = 0;
        }

        @Override
        public void stop() {
            this.sentryAAssembly.setCharged(false);
            this.lastSeen = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.attackTime--;
            LivingEntity livingentity = this.sentryAAssembly.getTarget();
            if (livingentity != null) {
                this.lastSeen = 0;

                double d0 = this.sentryAAssembly.distanceToSqr(livingentity);
                if (d0 < 4.0) {

                    if (this.attackTime <= 0) {
                        this.attackTime = 40;
                        this.sentryAAssembly.doHurtTarget(livingentity);
                    }

                    this.sentryAAssembly.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
                    double d1 = livingentity.getX() - this.sentryAAssembly.getX();
                    double d2 = livingentity.getY(0.5) - this.sentryAAssembly.getY(0.5);
                    double d3 = livingentity.getZ() - this.sentryAAssembly.getZ();
                    if (this.attackTime <= 0) {
                        this.attackStep++;
                        if (this.attackStep == 1) {
                            this.attackTime = 140;
                            this.sentryAAssembly.setCharged(true);
                        } else if (this.attackStep <= 15) {
                            this.attackTime = 5;
                        } else {
                            this.attackTime = 120;
                            this.attackStep = 0;
                            this.sentryAAssembly.setCharged(false);
                        }

                        if (this.attackStep > 1) {
                            double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5;
                            if (!this.sentryAAssembly.isSilent()) {
                                this.sentryAAssembly.level().levelEvent(null, 1018, this.sentryAAssembly.blockPosition(), 0);
                            }

                            for (int i = 0; i < 1; i++) {
                                Vec3 vec3 = new Vec3(this.sentryAAssembly.getRandom().triangle(d1, 2.297 * d4), d2, this.sentryAAssembly.getRandom().triangle(d3, 2.297 * d4));
                                SmallMagmaticFireball smallfireball = new SmallMagmaticFireball(this.sentryAAssembly.level(), this.sentryAAssembly, vec3.normalize());
                                smallfireball.setPos(smallfireball.getX(), this.sentryAAssembly.getY(0.5) + 0.5, smallfireball.getZ());
                                this.sentryAAssembly.level().addFreshEntity(smallfireball);
                            }
                        }
                    }

                } else if (this.lastSeen < 5) {
                    this.sentryAAssembly.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0);
                }

                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.sentryAAssembly.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }

    class OsuNATGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private final @Nullable Predicate<LivingEntity> predicate;
        public OsuNATGoal(Mob parent, Class<T> targetType, int interval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> predicate) {
            super(parent, targetType, interval, mustSee, mustReach, predicate);
            this.predicate = predicate;
        }

        @Override
        protected void findTarget() {
            mob.level()
                    .getEntitiesOfClass(
                            targetType,
                            getTargetSearchArea(getFollowDistance()),
                            (it) -> it != null && it.isAlive() && (predicate == null || predicate.test(it))
                    ).stream()
                    .min((a, b) -> rating(a) - rating(b))
                    .ifPresentOrElse(t -> this.target = t, () -> this.target = null); // whew
            //if (target != null) System.out.printf("target found: %s%n", target);
        }

        private int rating(LivingEntity potentialTarget) {
            Vec3 thisCenter = getBoundingBox().getCenter(), otherCenter = potentialTarget.getBoundingBox().getCenter();
            Vec3 difference = thisCenter.subtract(otherCenter);
            float[] rotationWanted = GeneralUtil.rectangularToXYRot(difference);
            return (int) (thisCenter.distanceTo(otherCenter) + GeneralUtil.angleDifferenceSpherical(Math.PI * 0.5 - rotations.get(0).getXRot(), rotations.get(0).getYRot(), rotationWanted[0], rotationWanted[1]) * 200);        }
    }
}

