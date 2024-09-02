package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.entity.animation.ActingEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ClientboundBlazeSentryRotationPacket;
import io.github.zygzaggaming.zygzagsmod.common.registry.*;
import io.github.zygzaggaming.zygzagsmod.common.registry.object.BlockWithItemSupplier;
import io.github.zygzaggaming.zygzagsmod.common.util.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import io.github.zygzaggaming.zygzagsmod.common.entity.SmallMagmaticFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeSentry extends Monster implements GeoAnimatable, ActingEntity<BlazeSentry> {
    protected static final EntityDataAccessor<Actor.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ACTOR_STATE.get());
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializers.OPTIONAL_UUID);
    //protected static final EntityDataAccessor<SimplEntityRotation> DATA_ROTATION = SynchedEntityData.defineId(BlazeSentry.class, EntityDataSerializerRegistry.ENTITY_ROTATION.get());
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Actor<BlazeSentry> actor = new Actor<>(this, ActionRegistry.BlazeSentry.IDLE_BASE.get());
    public static float[] maxRotationPerTick = {(float) (0.03125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public RotationArray rotations = new RotationArray(new Rotation[]{
            new LimitedRotation(0, 0, 0, 0, maxRotationPerTick[0]),
            new LimitedRotation(-0.5f * (float) Math.PI, 0, -0.5f * (float) Math.PI, 0, maxRotationPerTick[1]),
    });
    int windDownTicks = 0;
    @Nullable
    private LivingEntity cachedTarget;

    public BlazeSentry(EntityType<? extends BlazeSentry> type, Level world) {
        super(type, world);
        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(PathType.LAVA, 8);
        setPathfindingMalus(PathType.DANGER_FIRE, 0);
        setPathfindingMalus(PathType.DAMAGE_FIRE, 0);
        xpReward = 10;
        setPersistenceRequired();
    }

    public BlazeSentry(Level world) {
        this(EntityTypeRegistry.BLAZE_SENTRY.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    //irrelevant || causes jigsaw BS despawning
    public static boolean checkBlazeSentrySpawn(EntityType<BlazeSentry> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkAnyLightMonsterSpawnRules(entityType, level, spawnType, pos, random) && level.
                getBlockState(pos.below()).is(BlockWithItemRegistry.CHISELED_RED_NETHER_BRICKS.block());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANIMATOR_STATE, new Actor.State(
                ActionRegistry.BlazeSentry.IDLE_BASE.get(),
                ActionRegistry.BlazeSentry.IDLE_BASE.get(),
                null,
                99999999,
                60,
                new LinkedList<>()
        ));
        builder.define(DATA_TARGET, Optional.empty());
        //builder.define(DATA_ROTATION, new SimplEntityRotation());
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
            windDownTicks = 100;
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
    public boolean hurt(DamageSource fireball, float p_32731_) {
        if (isReflectedFireball(fireball)) {
            super.hurt(fireball, 1000.0F);
            return true;
        } else {
            return !this.isInvulnerableTo(fireball) && super.hurt(fireball, p_32731_);
        }
    }

    public void resetBodyRotation() {
        rotations.get(1).set(-0.5f * (float) Math.PI, 0);
    }

    public void lookAt(LivingEntity target) {
        rotations.get(0).set(GeneralUtil.rectangularToXYRot(target.getBoundingBox().getCenter().subtract(getEyePosition()))); // look towards target
        rotations.get(1).set(GeneralUtil.rectangularToXYRot(target.getBoundingBox().getCenter().subtract(getEyePosition()))); // rotate body towards target
    }

    public void lookAt(double x, double y, double z) {
        lookAt(new Vec3(x, y, z));
    }

    public void lookAt(Vec3 target) {
        rotations.get(0).set(GeneralUtil.rectangularToXYRot(target.subtract(getEyePosition()))); // look towards target
    }

    public boolean isSensitiveToWater() {
        return true;
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
    public boolean isIdle() {
        return getTarget() == null;
    }

    @Override
    public Actor<BlazeSentry> getActor() {
        return actor;
    }

    @Override
    public @Nullable Action getActionChange() {
        if (getTarget() == null) return ActionRegistry.BlazeSentry.IDLE_BASE.get();
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
    public void tick() {
        var target = getTarget();
        //if (target != null && !target.isAlive()) setTarget(null);
        if (windDownTicks > 0) windDownTicks--;
        if (target == null/* || !target.isAlive()*/) {
            resetBodyRotation();
            actor.setNextAction(ActionRegistry.BlazeSentry.IDLE_BASE.get());
        } else {
            lookAt(target);
            if (actor.getCurrentAction().is(ActionRegistry.BlazeSentry.IDLE_BASE)) actor.setNextAction(ActionRegistry.BlazeSentry.AGRO_BASE.get());
        }

        if (level().isClientSide && actor.getTopLevelAction().is(ActionRegistry.BlazeSentry.FLAMETHROW_BASE.get())) {
            Vec3 delta = rotations.get(1).directionVector();
            for (int particles = 0; particles < 6; ++particles) {
                double x = getX() + random.nextGaussian() * 0.2;
                double y = getEyeY() + random.nextGaussian() * 0.2;
                double z = getZ() + random.nextGaussian() * 0.2;

                level().addAlwaysVisibleParticle(ParticleTypeRegistry.FLAMETHROW_PARTICLES.get(), x, y, z, delta.x(), delta.y(), delta.z());
            }
        }
        rotations.tick();

        setRot(0, 0);
        setYHeadRot(0);
        setYBodyRot(0);
        setNoGravity(true);
        super.tick();
        actor.tick();
    }

    @Override
    public boolean doesIdleActions() {
        return false;
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(2, new OsuNATGoal<>(this, LivingEntity.class, 1, true, false, (entity) -> (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) || entity instanceof AbstractGolem));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
        goalSelector.addGoal(1, new FlamethrowGoal());
        goalSelector.addGoal(2, new FireGoal());
        goalSelector.addGoal(2, new FireBigGoal());
        goalSelector.addGoal(3, new RandomLookAroundGoal());
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
        public static final int windup = 60, ticksBetweenFireballs = 3, maxFireballs = 50, windDown = 60 + 80;
        public static final double power = 1.5;
        int ticks = 0;
        double chanceToUse = 0.66;

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public boolean canUse() {
            return windDownTicks <= 0 && !actor.isTransitioning() && actor.getNextAction().is(ActionRegistry.BlazeSentry.AGRO_BASE) && level().getRandom().nextDouble() < chanceToUse;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            //System.out.println("started");
            actor.setNextAction(ActionRegistry.BlazeSentry.SHOOT_BASE.get());
            ticks = 0;
        }

        @Override
        public void tick() {
            //System.out.println("ticks: " + ticks);
            ticks++;
            if (ticks > windup && (ticks - windup) % ticksBetweenFireballs == 0) {
                LivingEntity target = getTarget();
                assert target != null;
                Vec3 angle = rotations.get(1).directionVector();
                SmallMagmaticFireball fireball = new SmallMagmaticFireball(level(), BlazeSentry.this, new Vec3(angle.x, angle.y, angle.z));
                fireball.setDeltaMovement(angle.scale(power));
                fireball.moveTo(getEyePosition().add(angle.scale(0.25)));
                level().addFreshEntity(fireball);
            }
        }

        @Override
        public void stop() {
            windDownTicks = windDown;
            actor.setNextAction(getTarget() == null ? ActionRegistry.BlazeSentry.IDLE_BASE.get() : ActionRegistry.BlazeSentry.AGRO_BASE.get());
        }

        @Override
        public boolean canContinueToUse() {
            return (ticks - windup) / ticksBetweenFireballs < maxFireballs && getTarget() != null && getTarget().isAlive();
        }
    }

    public class FireBigGoal extends Goal {
        public static final EnumSet<Goal.Flag> flags = EnumSet.of(Flag.LOOK, Flag.TARGET);
        public static final int windup = 25, windDown = 53 - windup;
        public static final double power = 2.5;
        int ticks = 0;

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public boolean canUse() {
            return windDownTicks <= 0 && !actor.isTransitioning() && actor.getCurrentAction().is(ActionRegistry.BlazeSentry.AGRO_BASE);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            //System.out.println("started");
            actor.setNextAction(ActionRegistry.BlazeSentry.SHOOT_BIG_BASE.get());
            ticks = 0;
        }

        @Override
        public void tick() {
            ticks++;
            if (ticks == windup) {
                LivingEntity target = getTarget();
                assert target != null; //TODO: null check
                Vec3 angle = rotations.get(1).directionVector();
                LargeFireball fireball = new LargeFireball(level(), BlazeSentry.this, new Vec3(angle.x, angle.y, angle.z), 1);
                fireball.setDeltaMovement(angle.scale(power));
                fireball.moveTo(getEyePosition().add(angle.scale(0.25)));
                level().addFreshEntity(fireball);
            }
        }

        @Override
        public void stop() {
            windDownTicks = windDown;
            actor.setNextAction(getTarget() == null ? ActionRegistry.BlazeSentry.IDLE_BASE.get() : ActionRegistry.BlazeSentry.AGRO_BASE.get());
        }

        @Override
        public boolean canContinueToUse() {
            return ticks < windup;
        }
    }

    public class FlamethrowGoal extends Goal {
        public static final EnumSet<Goal.Flag> flags = EnumSet.of(Flag.LOOK, Flag.TARGET);
        public static final int windup = 70, maxDuration = 30 * 12 - windup, windDown = 40;
        public static final double range = 7, radiusSqr = 0.5;
        int ticks = 0;

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public boolean canUse() {
             return windDownTicks <= 0 && !actor.isTransitioning() && actor.getCurrentAction().is(ActionRegistry.BlazeSentry.AGRO_BASE) && getTarget() != null && getTarget().getBoundingBox().getCenter().add(getTarget().getDeltaMovement()).distanceToSqr(getEyePosition()) <= range * range;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            actor.setNextAction(ActionRegistry.BlazeSentry.FLAMETHROW_BASE.get());
            ticks = 0;
        }

        @Override
        public void tick() {
            ticks++;
            if (ticks > windup) {
                Vec3 angle = rotations.get(1).directionVector();
                List<Entity> entities = level().getEntities(BlazeSentry.this, getBoundingBox().inflate(range + radiusSqr + 12));
                for (Entity entity : entities) {
                    Vec3 targetCenter = entity.getBoundingBox().getCenter();
                    Vec3 toTarget = targetCenter.subtract(getEyePosition());
                    double cylDistance = angle.dot(toTarget);
                    double radialDistanceSqr = toTarget.subtract(angle.scale(cylDistance)).lengthSqr();
                    //System.out.printf("radial dist: %.4f, cyl dist: %.4f, this pos: (%.4f, %.4f, %.4f), other pos: (%.4f, %.4f, %.4f)%n", sqrt(radialDistanceSqr), cylDistance, getX(), getEyeY(), getZ(), targetCenter.x, targetCenter.y, targetCenter.z);
                    if (radialDistanceSqr < radiusSqr && cylDistance < range && cylDistance > 0) {
                        if (entity instanceof LivingEntity living) ModUtil.incrementEntityOverheat(living, 1);
                        else if (entity instanceof ItemEntity) entity.kill();
                    }
                }
            }
        }

        @Override
        public void stop() {
            windDownTicks = windDown;
            actor.setNextAction(getTarget() == null ? ActionRegistry.BlazeSentry.IDLE_BASE.get() : ActionRegistry.BlazeSentry.AGRO_BASE.get());
        }

        @Override
        public boolean canContinueToUse() {
            return ticks < windup + maxDuration && getTarget() != null && getTarget().getBoundingBox().getCenter().distanceToSqr(getEyePosition()) <= range * range * 1.1;
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
            return (int) (thisCenter.distanceTo(otherCenter) + GeneralUtil.angleDifferenceSpherical(Math.PI * 0.5 - rotations.get(1).getXRot(), rotations.get(1).getYRot(), rotationWanted[0], rotationWanted[1]) * 200);
        }
    }

    public class RandomLookAroundGoal extends Goal {
        private double relX;
        private double relZ;
        private int lookTime;

        public RandomLookAroundGoal() {
            setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return getRandom().nextFloat() < 0.02F && getTarget() == null && windDownTicks <= 0;
        }

        @Override
        public boolean canContinueToUse() {
            return lookTime >= 0;
        }

        @Override
        public void start() {
            double d0 = Math.PI * 2 * getRandom().nextDouble();
            relX = Math.cos(d0);
            relZ = Math.sin(d0);
            lookTime = 20 + getRandom().nextInt(20);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            lookTime--;
            lookAt(getX() + relX, getEyeY(), getZ() + relZ);
            level().players().forEach((it) -> {
                if (it instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundBlazeSentryRotationPacket(uuid, (LerpedRotation) rotations.get(0), (LerpedRotation) rotations.get(1)));
                }
            });
        }
    }
}
