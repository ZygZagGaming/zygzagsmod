package io.github.zygzaggaming.zygzagsmod.common.entity.assembly;

import java.util.*;
import javax.annotation.Nullable;

import io.github.zygzaggaming.zygzagsmod.common.entity.animation.ActingEntity;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import io.github.zygzaggaming.zygzagsmod.common.registry.ActionRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.LimitedRotation;
import io.github.zygzaggaming.zygzagsmod.common.util.Rotation;
import io.github.zygzaggaming.zygzagsmod.common.util.RotationArray;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ShurikenAssembly extends FlyingMob implements GeoAnimatable, ActingEntity<ShurikenAssembly> {
    Vec3 moveTargetPoint = Vec3.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    ShurikenAssembly.AttackPhase attackPhase = ShurikenAssembly.AttackPhase.CIRCLE;
    private @Nullable Player cachedTarget = null;
    private boolean isFast = false;
    private int health;

    private final Actor<ShurikenAssembly> actor = new Actor<>(this, ActionRegistry.ShurikenAssembly.ASSEMBLY.get());
    protected static final EntityDataAccessor<Actor.State> DATA_ANIMATOR_STATE = SynchedEntityData.defineId(ShurikenAssembly.class, EntityDataSerializerRegistry.ACTOR_STATE.get());
    protected static final EntityDataAccessor<Optional<UUID>> DATA_TARGET = SynchedEntityData.defineId(ShurikenAssembly.class, EntityDataSerializers.OPTIONAL_UUID);
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    public static float[] maxRotationPerTick = {(float) (0.03125 * Math.PI), (float) (0.0166666667 * Math.PI)};
    public RotationArray rotations = new RotationArray(new Rotation[]{
            new LimitedRotation(0, 0, 0, 0, maxRotationPerTick[0])
    });


    public ShurikenAssembly(EntityType<? extends ShurikenAssembly> type, Level world, boolean isFast, int health) {
        super(type, world);
        this.isFast = isFast;
        this.health = health;
        this.moveControl = new ShurikenAssembly.RodMoveControl(this);
        this.lookControl = new ShurikenAssembly.RodLookControl(this);
        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(PathType.LAVA, 8);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ShurikenAssembly.RodBodyRotationControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ShurikenAssembly.RodAttackStrategyGoal());
        this.goalSelector.addGoal(2, new ShurikenAssembly.RodSweepAttackGoal());
        this.goalSelector.addGoal(3, new ShurikenAssembly.RodCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new ShurikenAssembly.RodAttackPlayerTargetGoal());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANIMATOR_STATE, new Actor.State(
                ActionRegistry.ShurikenAssembly.ASSEMBLY.get(),
                ActionRegistry.ShurikenAssembly.ASSEMBLY.get(),
                null,
                99999999,
                40,
                new LinkedList<>()
        ));
        builder.define(DATA_TARGET, Optional.empty());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.MAX_HEALTH, 100);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public boolean canTarget(Player player) {
        return !player.isSpectator() && !player.getAbilities().instabuild && player.isAlive();
    }

    public void resetBodyRotation() {
        rotations.get(0).set(0, 0);
    }

    public void lookAt(LivingEntity target) {
        rotations.get(0).set(GeneralUtil.rectangularToXYRot(target.getEyePosition().subtract(getEyePosition()))); // rotate body towards target
    }

    private int actorTickCount = 0;
    @Override
    public void tick() {
        var target = getTarget();
        if (actorTickCount <= 20) {
            actor.setNextAction(ActionRegistry.ShurikenAssembly.ASSEMBLY.get());
            actorTickCount++;
        }
        else
            actor.setNextAction(ActionRegistry.ShurikenAssembly.SPIN_UP.get());

        if (target == null /*|| chargeTime < 60 || chargeTime > 110 : breaks lookAT method for some reason*/) {
            //resetBodyRotation();
        } else {
            //lookAt(target);
        }
        //rotations.tick();

        super.tick();
        actor.tick();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public LivingEntity getTarget() {
        Optional<UUID> targetUUID = entityData.get(DATA_TARGET);
        if (!((cachedTarget == null && targetUUID.isEmpty()) || (cachedTarget != null && targetUUID.isPresent() && cachedTarget.getUUID().equals(targetUUID.get()))))
            cachedTarget = (Player) targetUUID.flatMap((uuid) -> level().getEntities(this, getBoundingBox().inflate(50), (it) -> it.getUUID().equals(uuid)).stream().findFirst()).flatMap((it) -> it instanceof LivingEntity living ? Optional.of(living) : Optional.empty()).orElse(null);
        return cachedTarget;
    }

    @Override
    public void setTarget(@org.jetbrains.annotations.Nullable LivingEntity entity) {
        LivingEntity currentTarget = getTarget();
        if ((entity == null && currentTarget == null) || (entity != null && currentTarget != null && entity.is(currentTarget))) return;
        super.setTarget(entity);
        if (!level().isClientSide()) {
            entityData.set(DATA_TARGET, Optional.ofNullable(entity).map(Entity::getUUID));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33126_, DifficultyInstance p_33127_, MobSpawnType p_33128_, @Nullable SpawnGroupData p_33129_) {
        this.anchorPoint = this.blockPosition().above(2);
        return super.finalizeSpawn(p_33126_, p_33127_, p_33128_, p_33129_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_33132_) {
        super.readAdditionalSaveData(p_33132_);
        if (p_33132_.contains("AX")) {
            this.anchorPoint = new BlockPos(p_33132_.getInt("AX"), p_33132_.getInt("AY"), p_33132_.getInt("AZ"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_33141_) {
        super.addAdditionalSaveData(p_33141_);
        p_33141_.putInt("AX", this.anchorPoint.getX());
        p_33141_.putInt("AY", this.anchorPoint.getY());
        p_33141_.putInt("AZ", this.anchorPoint.getZ());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (amount > 2) {
            super.hurt(source, 1);
            if (source.is(DamageTypes.ARROW)) health -= 2;
            else if (source.is(DamageTypes.FIREWORKS)) health -= 3;
            else if (isReflectedFireball(source)) health -=3;
            else health--;

            if (health <= 0) super.hurt(source, 1000);
        }
        return amount > 2;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_33107_) {
        return true;
    }

    @Override
    public boolean canAttackType(EntityType<?> p_33111_) {
        return true;
    }

    @Override
    public boolean isIdle() {
        return getTarget() == null;
    }

    @Override
    public Actor<ShurikenAssembly> getActor() {
        return actor;
    }

    @Override
    public @org.jetbrains.annotations.Nullable Action getActionChange() {
        if (getTarget() == null) return ActionRegistry.ShurikenAssembly.ASSEMBLY.get();
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

    private static boolean isReflectedFireball(DamageSource entity) {
        return entity.getDirectEntity() instanceof LargeFireball && entity.getEntity() instanceof Player;
    }
    @Override
    public boolean isInvulnerableTo(DamageSource fireball) {
        return this.isInvulnerable() && !fireball.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isReflectedFireball(fireball) && super.isInvulnerableTo(fireball);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            actor.register(controllers);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object object) {
        return tickCount;
    }

    static enum AttackPhase {
        CIRCLE,
        SWOOP;
    }

    class RodAttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);
        private int nextScanTick = reducedTickDelay(20);

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                this.nextScanTick--;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Player> list = ShurikenAssembly.this.level()
                        .getNearbyPlayers(this.attackTargeting, ShurikenAssembly.this, ShurikenAssembly.this.getBoundingBox().inflate(16.0, 64.0, 16.0));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Player, Double>comparing(Entity::getY).reversed());

                    for (Player player : list) {
                        if (ShurikenAssembly.this.canAttack(player, TargetingConditions.DEFAULT)) {
                            ShurikenAssembly.this.setTarget(player);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = ShurikenAssembly.this.getTarget();
            return livingentity != null ? ShurikenAssembly.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }
    }

    class RodAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        @Override
        public boolean canUse() {
            LivingEntity livingentity = ShurikenAssembly.this.getTarget();
            return livingentity != null ? ShurikenAssembly.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }

        @Override
        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            ShurikenAssembly.this.attackPhase = ShurikenAssembly.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        @Override
        public void stop() {
            ShurikenAssembly.this.anchorPoint = ShurikenAssembly.this.level()
                    .getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, ShurikenAssembly.this.anchorPoint)
                    .above(ShurikenAssembly.this.random.nextInt(5));
        }

        @Override
        public void tick() {
            if (ShurikenAssembly.this.attackPhase == ShurikenAssembly.AttackPhase.CIRCLE) {
                this.nextSweepTick--;
                if (this.nextSweepTick <= 0) {
                    ShurikenAssembly.this.attackPhase = ShurikenAssembly.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((20 + ShurikenAssembly.this.random.nextInt(4)) * 20);
                }
            }
        }

        private void setAnchorAboveTarget() {
            ShurikenAssembly.this.anchorPoint = ShurikenAssembly.this.getTarget().blockPosition().above(2 + ShurikenAssembly.this.random.nextInt(20));
            if (ShurikenAssembly.this.anchorPoint.getY() < ShurikenAssembly.this.level().getSeaLevel()) {
                ShurikenAssembly.this.anchorPoint = new BlockPos(
                        ShurikenAssembly.this.anchorPoint.getX(), ShurikenAssembly.this.level().getSeaLevel() + 1, ShurikenAssembly.this.anchorPoint.getZ()
                );
            }
        }
    }

    class RodBodyRotationControl extends BodyRotationControl {
        public RodBodyRotationControl(Mob p_33216_) {
            super(p_33216_);
        }

        @Override
        public void clientTick() {
            ShurikenAssembly.this.yHeadRot = ShurikenAssembly.this.yBodyRot;
            ShurikenAssembly.this.yBodyRot = ShurikenAssembly.this.getYRot();
        }
    }
    class RodCircleAroundAnchorGoal extends ShurikenAssembly.RodMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        @Override
        public boolean canUse() {
            return ShurikenAssembly.this.getTarget() == null || ShurikenAssembly.this.attackPhase == ShurikenAssembly.AttackPhase.CIRCLE;
        }

        @Override
        public void start() {
            this.distance = ShurikenAssembly.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + ShurikenAssembly.this.random.nextFloat() * 5F;
            this.clockwise = ShurikenAssembly.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        @Override
        public void tick() {
            if (ShurikenAssembly.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + ShurikenAssembly.this.random.nextFloat() * 5F;
            }

            if (ShurikenAssembly.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                this.distance++;
                if (this.distance > 10.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (ShurikenAssembly.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = ShurikenAssembly.this.random.nextFloat() * 2.0F * (float) Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (ShurikenAssembly.this.moveTargetPoint.y < ShurikenAssembly.this.getY() && !ShurikenAssembly.this.level().isEmptyBlock(ShurikenAssembly.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (ShurikenAssembly.this.moveTargetPoint.y > ShurikenAssembly.this.getY() && !ShurikenAssembly.this.level().isEmptyBlock(ShurikenAssembly.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }
        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(ShurikenAssembly.this.anchorPoint)) {
                ShurikenAssembly.this.anchorPoint = ShurikenAssembly.this.blockPosition();
            }

            this.angle = this.angle + this.clockwise * 15.0F * (float) (Math.PI / 180.0);
            ShurikenAssembly.this.moveTargetPoint = Vec3.atLowerCornerOf(ShurikenAssembly.this.anchorPoint)
                    .add((double)(this.distance * Mth.cos(this.angle)), (double)(this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class RodLookControl extends LookControl {
        public RodLookControl(Mob p_33235_) {
            super(p_33235_);
        }

        @Override
        public void tick() {
        }
    }

    class RodMoveControl extends MoveControl {
        private float speed = 0.1F;

        public RodMoveControl(Mob p_33241_) {
            super(p_33241_);
        }

        @Override
        public void tick() {
            if (ShurikenAssembly.this.horizontalCollision) {
                ShurikenAssembly.this.setYRot(ShurikenAssembly.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double d0 = ShurikenAssembly.this.moveTargetPoint.x - ShurikenAssembly.this.getX();
            double d1 = ShurikenAssembly.this.moveTargetPoint.y - ShurikenAssembly.this.getY();
            double d2 = ShurikenAssembly.this.moveTargetPoint.z - ShurikenAssembly.this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d3) > 1.0E-5F) {
                double d4 = 1.0 - Math.abs(d1 * 0.7F) / d3;
                d0 *= d4;
                d2 *= d4;
                d3 = Math.sqrt(d0 * d0 + d2 * d2);
                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = ShurikenAssembly.this.getYRot();
                float f1 = (float)Mth.atan2(d2, d0);
                float f2 = Mth.wrapDegrees(ShurikenAssembly.this.getYRot() + 90.0F);
                float f3 = Mth.wrapDegrees(f1 * (180.0F / (float)Math.PI));
                ShurikenAssembly.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
                ShurikenAssembly.this.yBodyRot = ShurikenAssembly.this.getYRot();
                if (Mth.degreesDifferenceAbs(f, ShurikenAssembly.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    if (ShurikenAssembly.this.attackPhase == AttackPhase.CIRCLE) Mth.approach(this.speed, 0.05F, 0.025F);
                    else this.speed = ShurikenAssembly.this.isFast ? Mth.approach(this.speed, 1F, 0.025F) : Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Mth.atan2(-d1, d3) * 180.0F / (float)Math.PI));
                ShurikenAssembly.this.setXRot(f4);
                float f5 = ShurikenAssembly.this.getYRot() + 90.0F;
                double d6 = (double)(this.speed * Mth.cos(f5 * (float) (Math.PI / 180.0))) * Math.abs(d0 / d5);
                double d7 = (double)(this.speed * Mth.sin(f5 * (float) (Math.PI / 180.0))) * Math.abs(d2 / d5);
                double d8 = (double)(this.speed * Mth.sin(f4 * (float) (Math.PI / 180.0))) * Math.abs(d1 / d5);
                Vec3 vec3 = ShurikenAssembly.this.getDeltaMovement();
                ShurikenAssembly.this.setDeltaMovement(vec3.add(new Vec3(d6, d8, d7).subtract(vec3).scale(0.2)));
            }
        }
    }

    abstract class RodMoveTargetGoal extends Goal {
        public RodMoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return ShurikenAssembly.this.moveTargetPoint.distanceToSqr(ShurikenAssembly.this.getX(), ShurikenAssembly.this.getY(), ShurikenAssembly.this.getZ()) < 4.0;
        }
    }

    class RodSweepAttackGoal extends ShurikenAssembly.RodMoveTargetGoal {
        @Override
        public boolean canUse() {
            return ShurikenAssembly.this.getTarget() != null && ShurikenAssembly.this.attackPhase == ShurikenAssembly.AttackPhase.SWOOP;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = ShurikenAssembly.this.getTarget();

            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof Player player && (livingentity.isSpectator() || player.isCreative())) {
                    return false;
                }
                if (!this.canUse()) {
                    return false;
                }
                return true;
            }
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            ShurikenAssembly.this.setTarget(null);
            ShurikenAssembly.this.attackPhase = ShurikenAssembly.AttackPhase.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = ShurikenAssembly.this.getTarget();
            if (livingentity != null) {
                ShurikenAssembly.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5), livingentity.getZ());
                if (ShurikenAssembly.this.getBoundingBox().inflate(0.2F).intersects(livingentity.getBoundingBox())) {
                    ShurikenAssembly.this.doHurtTarget(livingentity);
                    //ShurikenAssembly.this.attackPhase = ShurikenAssembly.AttackPhase.CIRCLE;
                    if (!ShurikenAssembly.this.isSilent()) {
                        ShurikenAssembly.this.level().levelEvent(1039, ShurikenAssembly.this.blockPosition(), 0);
                    }
                } else if (ShurikenAssembly.this.hurtTime > 0) {
                    ShurikenAssembly.this.attackPhase = ShurikenAssembly.AttackPhase.CIRCLE;
                }
            }
        }
    }
}
