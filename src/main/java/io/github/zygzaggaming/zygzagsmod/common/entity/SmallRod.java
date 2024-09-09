package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Actor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.SynchedEntityData;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityDataSerializerRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallRod extends Entity implements GeoEntity {
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation SPIN_0 = RawAnimation.begin().thenLoop("animation.small_rod.spin_0"),
            SPIN_1 = RawAnimation.begin().thenLoop("animation.small_rod.spin_1");
    private final RawAnimation SPIN_BASE = (Math.random() <= 0.5) ? SPIN_0 : SPIN_1;
    private @Nullable Player target = null;
    //private @Nullable LivingEntity target = null;
    private final AnimationController<SmallRod> controller = new AnimationController<>(this, "main", 0, (animState) -> animState.setAndContinue(SPIN_BASE));
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID,  "shuriken_assembly"));
    private int health;

    public SmallRod(EntityType<? extends SmallRod> type, Level world) {
        this(type, world, 3);
    }

    public SmallRod(EntityType<? extends SmallRod> type, Level world, int health) {
        super(type, world);
        this.health = health;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.FOLLOW_RANGE, 48.0D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (amount > 2) {
            if (source.is(DamageTypes.ARROW)) health -= 2;
            else if (source.is(DamageTypes.FIREWORKS)) health -= 3;
            else if (isReflectedFireball(source)) health -=3;
            else health--;

            if (health <= 0) kill();
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

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    public DamageSource damageSource(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DAMAGE_TYPE), this/*, queen*/);
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(20), this::canTarget);
            if (!players.isEmpty())
                target = players.get(level().random.nextInt(players.size()));
        } else {
            if (!canTarget(target)) target = null;
            else {
                RandomSource randomsource = this.getRandom();
                double d0 = this.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 8.0F);
                double d1 = this.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 8.0F);
                double d2 = this.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 8.0F);
                Vec3 newLocation = new Vec3(d0, d1, d2);
                move(MoverType.SELF, newLocation);
            }
        }

        DamageSource source = damageSource(level().registryAccess());
    }

    @Override
    public void move(MoverType type, Vec3 delta) {
        if (this.noPhysics) {
            this.setPos(this.getX() + delta.x, this.getY() + delta.y, this.getZ() + delta.z);
        } else {
            this.wasOnFire = this.isOnFire();

            this.level().getProfiler().push("move");
            if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
                delta = delta.multiply(this.stuckSpeedMultiplier);
                this.stuckSpeedMultiplier = Vec3.ZERO;
                this.setDeltaMovement(Vec3.ZERO);
            }

            Vec3 vec3 = this.collide(delta);
            double d0 = vec3.lengthSqr();
            if (d0 > 1.0E-7) {
                if (this.fallDistance != 0.0F && d0 >= 1.0) {
                    BlockHitResult blockhitresult = this.level()
                            .clip(
                                    new ClipContext(this.position(), this.position().add(vec3), ClipContext.Block.FALLDAMAGE_RESETTING, ClipContext.Fluid.WATER, this)
                            );
                    if (blockhitresult.getType() != HitResult.Type.MISS) {
                        this.resetFallDistance();
                    }
                }

                this.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
            }

            this.level().getProfiler().pop();
            this.level().getProfiler().push("rest");
            boolean xCollision = !Mth.equal(delta.x, vec3.x);
            boolean zCollision = !Mth.equal(delta.z, vec3.z);
            this.horizontalCollision = xCollision || zCollision;
            this.verticalCollision = delta.y != vec3.y;
            this.verticalCollisionBelow = this.verticalCollision && delta.y < 0.0;
            if (this.horizontalCollision) {
                this.minorHorizontalCollision = this.isHorizontalCollisionMinor(vec3);
            } else {
                this.minorHorizontalCollision = false;
            }

            this.setOnGroundWithMovement(this.verticalCollisionBelow, vec3);
            BlockPos blockpos = this.getOnPosLegacy();
            BlockState blockstate = this.level().getBlockState(blockpos);
            BlockPos onPos = getOnPos();
            BlockState onState = level().getBlockState(onPos);
            this.checkFallDamage(vec3.y, this.onGround(), blockstate, blockpos);
            if (isRemoved()) level().getProfiler().pop();
            else {
                if (horizontalCollision) {
                    Vec3 delta3 = getDeltaMovement();
                    setDeltaMovement(xCollision ? 0 : delta3.x, delta3.y, zCollision ? 0 : delta3.z);
                }

                Block block = onState.getBlock();
                if (delta.y != delta.y) block.updateEntityAfterFallOn(level(), this);
                if (onGround()) block.stepOn(level(), onPos, onState, this);

                MovementEmission movementEmission = getMovementEmission();
                if (movementEmission.emitsAnything() && !isPassenger()) {
                    double dx = delta.x, dy = delta.y, dz = delta.z;
                    flyDist = (float) (flyDist + delta.length() * 0.6);

                    walkDist += (float) delta.horizontalDistance() * 0.6f;
                    moveDist += (float) Math.sqrt(dx * dx + dy * dy + dz * dz) * 0.6f;
                }

                tryCheckInsideBlocks();

                level().getProfiler().pop();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

}
