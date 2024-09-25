package io.github.zygzaggaming.zygzagsmod.common.entity.assembly;

import io.github.zygzaggaming.zygzagsmod.common.entity.animation.ActingEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.registry.ActionRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.LimitedRotation;
import io.github.zygzaggaming.zygzagsmod.common.util.Rotation;
import io.github.zygzaggaming.zygzagsmod.common.util.RotationArray;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
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
public class HelixAAssembly extends Monster implements GeoAnimatable, ActingEntity<HelixAAssembly> {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(HelixAAssembly.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Actor.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(HelixAAssembly.class, EntityDataSerializerRegistry.ACTOR_STATE.get());
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Actor<HelixAAssembly> actor = new Actor<>(this, ActionRegistry.HelixAAssembly.ASSEMBLY.get());
    public static float[] maxRotationPerTick = {(float) (0.03125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public RotationArray rotations = new RotationArray(new Rotation[]{
            new LimitedRotation(0, 0, 0, 0, maxRotationPerTick[0])
    });
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(HelixAAssembly.class, EntityDataSerializers.OPTIONAL_UUID);
    private @Nullable Player target = null;
    //private @Nullable LivingEntity target = null;
    private int health;
    private int chargeTime;

    public HelixAAssembly(Level world) {
        this(EntityTypeRegistry.HELIX_ASSEMBLY_A.get(), world, 3);
    }

    public HelixAAssembly(EntityType<? extends HelixAAssembly> type, Level world, int health) {
        super(type, world);
        this.health = health;
        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(PathType.LAVA, 8);
        this.moveControl = new HelixAAssembly.RodMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new HelixAAssembly.RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, AbstractGolem.class, 6.0F, 1.0, 1.2));
        targetSelector.addGoal(3, new HelixAAssembly.OsuNATGoal<>(this, LivingEntity.class, 1, true, false, (entity) -> (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) || entity instanceof AbstractGolem));    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.MAX_HEALTH, 1000);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANIMATOR_STATE, new Actor.State(
                ActionRegistry.HelixAAssembly.ASSEMBLY.get(),
                ActionRegistry.HelixAAssembly.ASSEMBLY.get(),
                null,
                99999999,
                20,
                new LinkedList<>()
        ));
        builder.define(DATA_IS_CHARGING, false);
        builder.define(DATA_TARGET, Optional.empty());
    }

    @Override
    public boolean isIdle() {
        return getTarget() == null;
    }

    @Override
    public Actor<HelixAAssembly> getActor() {
        return actor;
    }

    @Override
    public @Nullable Action getActionChange() {
        if (getTarget() == null) return ActionRegistry.HelixAAssembly.ASSEMBLY.get();
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

    public boolean canTarget(Player player) {
        return !player.isSpectator() && !player.getAbilities().instabuild && player.isAlive();
    }

    private static boolean isReflectedFireball(DamageSource entity) {
        return entity.getDirectEntity() instanceof LargeFireball && entity.getEntity() instanceof Player;
    }
    @Override
    public boolean isInvulnerableTo(DamageSource fireball) {
        return this.isInvulnerable() && !fireball.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isReflectedFireball(fireball) && super.isInvulnerableTo(fireball);
    }

    //Add Particles For Hurt()
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        actor.register(controllerRegistrar);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    public void setCharging(boolean p_32759_) {
        this.entityData.set(DATA_IS_CHARGING, p_32759_);
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    @Nullable
    private LivingEntity cachedTarget;
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

    @Override
    public void readAdditionalSaveData(CompoundTag p_32733_) {
        super.readAdditionalSaveData(p_32733_);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_32744_) {
        super.addAdditionalSaveData(p_32744_);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean doesIdleActions() {
        return false;
    }

    private int actorTickCount = 0;
    @Override
    public void tick() {
        if (actorTickCount <= 17) {
            actor.setNextAction(ActionRegistry.HelixAAssembly.ASSEMBLY.get());
            actorTickCount++;
        }
        else actor.setNextAction(ActionRegistry.HelixAAssembly.SPIN_BASE.get());

        setNoGravity(true);
        super.tick();
        actor.tick();
    }

    static class RandomFloatAroundGoal extends Goal {
        private final HelixAAssembly helixAAssembly;

        public RandomFloatAroundGoal(HelixAAssembly p_32783_) {
            this.helixAAssembly = p_32783_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = this.helixAAssembly.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.helixAAssembly.getX();
                double d1 = movecontrol.getWantedY() - this.helixAAssembly.getY();
                double d2 = movecontrol.getWantedZ() - this.helixAAssembly.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (this.helixAAssembly.getTarget() != null) {
                RandomSource randomsource = this.helixAAssembly.getRandom();
                double d0 = this.helixAAssembly.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 4.0F);
                double d1 = this.helixAAssembly.getY();
                double d2 = this.helixAAssembly.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 4.0F);
                this.helixAAssembly.getMoveControl().setWantedPosition(d0, d1, d2, 0.01); //minimum speed set
            }
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
            return (int) (thisCenter.distanceTo(otherCenter) + GeneralUtil.angleDifferenceSpherical(Math.PI * 0.5 - rotations.get(0).getXRot(), rotations.get(0).getYRot(), rotationWanted[0], rotationWanted[1]) * 200);
        }
    }

    static class RodMoveControl extends MoveControl {
        private final HelixAAssembly helixAAssembly;
        private int floatDuration;

        public RodMoveControl(HelixAAssembly p_32768_) {
            super(p_32768_);
            this.helixAAssembly = p_32768_;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration = this.floatDuration + this.helixAAssembly.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.helixAAssembly.getX(), this.wantedY - this.helixAAssembly.getY(), this.wantedZ - this.helixAAssembly.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.helixAAssembly.setDeltaMovement(this.helixAAssembly.getDeltaMovement().add(vec3.scale(0.1)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 p_32771_, int p_32772_) {
            AABB aabb = this.helixAAssembly.getBoundingBox();

            for (int i = 1; i < p_32772_; i++) {
                aabb = aabb.move(p_32771_);
                if (!this.helixAAssembly.level().noCollision(this.helixAAssembly, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

}
