package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.util.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

import static java.lang.Math.atan2;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallRod extends FlyingMob implements GeoEntity {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(SmallRod.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation SPIN_0 = RawAnimation.begin().thenLoop("animation.small_rod.spin_0"),
            SPIN_1 = RawAnimation.begin().thenLoop("animation.small_rod.spin_1");
    private final RawAnimation SPIN_BASE = (Math.random() <= 0.5) ? SPIN_0 : SPIN_1;
    public static float[] maxRotationPerTick = {(float) (0.03125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public RotationArray rotations = new RotationArray(new Rotation[]{
            new LimitedRotation(0, 0, 0, 0, maxRotationPerTick[0])
    });
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(SmallRod.class, EntityDataSerializers.OPTIONAL_UUID);
    private @Nullable Player target = null;
    //private @Nullable LivingEntity target = null;
    private int explosionPower = 0;
    private final AnimationController<SmallRod> controller = new AnimationController<>(this, "main", 0, (animState) -> animState.setAndContinue(SPIN_BASE));
    private int health;

    public SmallRod(EntityType<? extends SmallRod> type, Level world) {
        this(type, world, 3);
    }

    public SmallRod(EntityType<? extends SmallRod> type, Level world, int health) {
        super(type, world);
        this.health = health;
        this.moveControl = new SmallRod.RodMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new SmallRod.RandomFloatAroundGoal(this));
//        this.goalSelector.addGoal(7, new SmallRod.RodLookGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, AbstractGolem.class, 8.0F));
        this.goalSelector.addGoal(7, new SmallRod.RodShootFireballGoal(this));
        targetSelector.addGoal(3, new SmallRod.OsuNATGoal<>(this, LivingEntity.class, 1, true, false, (entity) -> (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) || entity instanceof AbstractGolem));    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_CHARGING, false);
        builder.define(DATA_TARGET, Optional.empty());
    }

    public boolean canTarget(Player player) {
        return !player.isSpectator() && !player.getAbilities().instabuild && player.isAlive();
    }

    public int getExplosionPower() {
        return this.explosionPower;
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
            if (source.is(DamageTypes.ARROW)) health -= 2;
            else if (source.is(DamageTypes.FIREWORKS)) health -= 3;
            else if (isReflectedFireball(source)) health -=3;
            else health--;

            if (health <= 0) super.hurt(source, 1000);
        }
        return amount > 2;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(controller);
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
        if (p_32733_.contains("ExplosionPower", 99)) {
            this.explosionPower = p_32733_.getByte("ExplosionPower");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_32744_) {
        super.addAdditionalSaveData(p_32744_);
        p_32744_.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    public void resetBodyRotation() {
        rotations.get(0).set(-0.5f * (float) Math.PI, 0);
    }

    public void lookAt(LivingEntity target) {
        rotations.get(0).set(GeneralUtil.rectangularToXYRot(target.getEyePosition().subtract(getEyePosition()))); // rotate body towards target
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = getTarget();
        if (livingEntity != null) {
            getLookControl().setLookAt(livingEntity, 90.0F, 90.0F);
            //setLookAt(double wantedX, double wantedY, double wantedZ, float speedX, float speedY)
            getLookControl().tick();
            this.lookAt(livingEntity);
            this.setYRot(this.yHeadRot);
        }

        rotations.tick();
        super.tick();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    static class RodMoveControl extends MoveControl {
        private final SmallRod smallRod;
        private int floatDuration;

        public RodMoveControl(SmallRod rod) {
            super(rod);
            this.smallRod = rod;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.smallRod.getTarget();
            if (livingentity != null) {
                if (this.operation == Operation.MOVE_TO) {
                    if (this.floatDuration-- <= 0) {
                        this.floatDuration = this.floatDuration + this.smallRod.getRandom().nextInt(5) + 2;
                        Vec3 vec3 = new Vec3(this.wantedX - this.smallRod.getX(), this.wantedY - this.smallRod.getY(), this.wantedZ - this.smallRod.getZ());
                        double d0 = vec3.length();
                        vec3 = vec3.normalize();
                        if (this.canReach(vec3, Mth.ceil(d0))) {
                            this.smallRod.setDeltaMovement(this.smallRod.getDeltaMovement().add(vec3.scale(0.1)));
                        } else {
                            this.operation = Operation.WAIT;
                        }
                    }
                }
            }
        }

        private boolean canReach(Vec3 pos, int p_32772_) {
            AABB aabb = this.smallRod.getBoundingBox();

            for (int i = 1; i < p_32772_; i++) {
                aabb = aabb.move(pos);
                if (!this.smallRod.level().noCollision(this.smallRod, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class RodLookGoal extends MoveControl {
        private final SmallRod smallRod;


        public RodLookGoal(SmallRod rod) {
            super(rod);
            this.smallRod = rod;
            //this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public void tick() {
            if (this.smallRod.getTarget() == null) {
                Vec3 vec3 = this.smallRod.getDeltaMovement();
                this.smallRod.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float)Math.PI));
                this.smallRod.yBodyRot = this.smallRod.getYRot();
            } else {
                LivingEntity livingentity = this.smallRod.getTarget();
                double v0 = 64.0;
                if (livingentity.distanceToSqr(this.smallRod) < 4096.0) {
                    double v1 = livingentity.getX() - this.smallRod.getX();
                    double v2 = livingentity.getZ() - this.smallRod.getZ();
                    this.smallRod.setYRot(-((float) Mth.atan2(v1, v2)) * (180.0F / (float) Math.PI));
                    this.smallRod.yBodyRot = this.smallRod.getYRot();

                    Vec3 vec3 = new Vec3(
                            livingentity.getX() - this.smallRod.getX(),
                            livingentity.getY() - this.smallRod.getY(),
                            livingentity.getZ() - this.smallRod.getZ());
                    double d0 = vec3.length();
                    double d1 = vec3.x / d0;
                    double d2 = vec3.y / d0;
                    double d3 = vec3.z / d0;
                    float f = (float) (Mth.atan2(vec3.z, vec3.x) * 180.0F / (float) Math.PI) - 90.0F;
                    this.smallRod.setYRot(this.rotlerp(this.smallRod.getYRot(), f, 90.0F));
                    this.smallRod.yBodyRot = this.smallRod.getYRot();
                    LookControl lookcontrol = this.smallRod.getLookControl();
                    double d8 = this.smallRod.getX() + d1 * 2.0;
                    double d9 = this.smallRod.getEyeY() + d2 / d0;
                    double d10 = this.smallRod.getZ() + d3 * 2.0;
                    double d11 = lookcontrol.getWantedX();
                    double d12 = lookcontrol.getWantedY();
                    double d13 = lookcontrol.getWantedZ();
                    if (!lookcontrol.isLookingAtTarget()) {
                        d11 = d8;
                        d12 = d9;
                        d13 = d10;
                    }

                    this.smallRod.getLookControl().setLookAt(Mth.lerp(0.125, d11, d8), Mth.lerp(0.125, d12, d9), Mth.lerp(0.125, d13, d10), 10.0F, 40.0F);
                }
            }
        }
    }


    static class RodShootFireballGoal extends Goal {
        private final SmallRod smallRod;
        public int chargeTime;

        public RodShootFireballGoal(SmallRod rod) {
            this.smallRod = rod;
        }

        @Override
        public boolean canUse() {
            return this.smallRod.getTarget() != null;
        }

        @Override
        public void start() {
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            this.smallRod.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.smallRod.getTarget();
            if (livingentity != null) {
                double d0 = 64.0;
                if (livingentity.distanceToSqr(this.smallRod) < 4096.0 && this.smallRod.hasLineOfSight(livingentity)) {
                    Level level = this.smallRod.level();
                    this.chargeTime++;
                    if (this.chargeTime == 10 && !this.smallRod.isSilent()) {
                        level.levelEvent(null, 1015, this.smallRod.blockPosition(), 0);
                    }

//                    if (this.chargeTime == 60) {
//                        this.smallRod.lookAt(livingentity);
//                    }

                    //Shooting
                    if (this.chargeTime == 100) {
                        double d1 = 4.0;
                        Vec3 vec3 = this.smallRod.getViewVector(1.0F);
                        double d2 = livingentity.getX() - (this.smallRod.getX() + vec3.x * 4.0);
                        double d3 = livingentity.getY(0.85) - (this.smallRod.getY(0.5));
                        double d4 = livingentity.getZ() - (this.smallRod.getZ() + vec3.z * 4.0);
                        Vec3 vec31 = new Vec3(d2, d3, d4);
                        if (!this.smallRod.isSilent()) {
                            level.levelEvent(null, 1016, this.smallRod.blockPosition(), 0);
                        }

                        SmallMagmaticFireball fireball = new SmallMagmaticFireball(level, this.smallRod, vec31.normalize(), this.smallRod.getExplosionPower());
                        fireball.setPos(this.smallRod.getX(), this.smallRod.getY(), fireball.getZ() + vec3.z);
                        level.addFreshEntity(fireball);
                        this.chargeTime = -120;
//                        this.smallRod.resetBodyRotation();
                    }
                } else if (this.chargeTime > 0) {
                    this.chargeTime--;
                }

                this.smallRod.setCharging(this.chargeTime > 10);
            }
        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final SmallRod smallRod;

        public RandomFloatAroundGoal(SmallRod p_32783_) {
            this.smallRod = p_32783_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = this.smallRod.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.smallRod.getX();
                double d1 = movecontrol.getWantedY() - this.smallRod.getY();
                double d2 = movecontrol.getWantedZ() - this.smallRod.getZ();
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
            RandomSource randomsource = this.smallRod.getRandom();
            double d0 = this.smallRod.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 4.0F);
            double d1 = this.smallRod.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 1.1F);
            double d2 = this.smallRod.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 4.0F);
            this.smallRod.getMoveControl().setWantedPosition(d0, d1, d2, 0.0005);
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

}
