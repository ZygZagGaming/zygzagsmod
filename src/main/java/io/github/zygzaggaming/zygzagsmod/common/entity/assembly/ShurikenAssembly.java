package io.github.zygzaggaming.zygzagsmod.common.entity.assembly;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
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
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShurikenAssembly extends Entity implements GeoEntity {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.shuriken_assembly.idle"),
            SPIN_UP = RawAnimation.begin().thenPlay("animation.shuriken_assembly.spin_up").thenLoop("animation.shuriken_assembly.fast");
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID,  "shuriken_assembly"));
    private @Nullable Player target = null;
    private final AnimationController<ShurikenAssembly> controller = new AnimationController<>(this, "main", 0, (animState) -> animState.setAndContinue(target == null ? IDLE : SPIN_UP));
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final boolean isFast, breaksOnHittingABlock;
    private int health, damage;

    public ShurikenAssembly(EntityType<? extends ShurikenAssembly> type, Level world) {
        this(type, world, false, false, 3, 4);
    }

    public ShurikenAssembly(EntityType<? extends ShurikenAssembly> type, Level world, boolean isFast, boolean breaksOnHittingABlock, int health, int damage) {
        super(type, world);
        this.isFast = isFast;
        this.breaksOnHittingABlock = breaksOnHittingABlock;
        this.health = health;
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    public boolean canTarget(Player player) {
        return !player.isSpectator() && !player.getAbilities().instabuild && player.isAlive();
    }

    public DamageSource damageSource(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DAMAGE_TYPE), this/*, queen*/);
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(20), this::canTarget);
            if (!players.isEmpty()) target = players.get(level().random.nextInt(players.size()));
        } else {
            if (!canTarget(target)) target = null;
            else {
                Vec3 diff = target.position().add(0, 0.5, 0).subtract(position());
                if (diff.lengthSqr() >= 0.05) {
                    if (isFast) {
                        diff = diff.normalize().scale(0.1);
                        move(MoverType.SELF, getDeltaMovement());
                        setDeltaMovement(getDeltaMovement().add(diff).scale(0.95));
                    } else {
                        diff = diff.multiply(1, 0, 1).normalize().scale(0.15).add(diff.multiply(0, 0.25, 0));
                        move(MoverType.SELF, diff);
                        setDeltaMovement(getDeltaMovement().add(diff).scale(0.95));
                    }
                }
            }
        }

        DamageSource source = damageSource(level().registryAccess());
        List<LivingEntity> entitiesToHit = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(3), (entity) -> entity.getBoundingBox().intersects(getBoundingBox()));
        for (LivingEntity entity : entitiesToHit) entity.hurt(source, damage);

        if (level().isClientSide) level().addParticle(ParticleTypes.SMOKE, getX() + 2 * Math.random() - 1, getY() + 0.4, getZ() + 2 * Math.random() - 1, 0, 0, 0);
    }

    @Override
    public void move(MoverType type, Vec3 delta) {
        if (noPhysics) setPos(getX() + delta.x, getY() + delta.y, getZ() + delta.z);
        else {
            wasOnFire = isOnFire();

            level().getProfiler().push("move");
            double length = delta.length();
            if (stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
                delta = delta.multiply(stuckSpeedMultiplier);
                stuckSpeedMultiplier = Vec3.ZERO;
                setDeltaMovement(Vec3.ZERO);
            }

            BlockHitResult result = level().clip(new ClipContext(position(), position().add(delta), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            if (result.getType() != HitResult.Type.MISS && breaksOnHittingABlock) {
                kill();
                return;
            }
            while (result.getType() != HitResult.Type.MISS) {
                length -= result.getLocation().distanceTo(position());
                setPos(result.getLocation());
                Vec3i normali = result.getDirection().getNormal();
                Vec3 normal = new Vec3(normali.getX(), normali.getY(), normali.getZ());
                delta = delta.subtract(normal.scale(2 * delta.dot(normal))).normalize().scale(length);
                result = level().clip(new ClipContext(position(), position().add(delta), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            }

            double lengthSqr = delta.lengthSqr();
            if (lengthSqr > 1e-7) {
                if (fallDistance != 0f && lengthSqr >= 1) {
                    BlockHitResult hitResult = level().clip(new ClipContext(position(), position().add(delta), ClipContext.Block.FALLDAMAGE_RESETTING, ClipContext.Fluid.WATER, this));
                    if (hitResult.getType() != HitResult.Type.MISS) resetFallDistance();
                }

                setPos(getX() + delta.x, getY() + delta.y, getZ() + delta.z);
                setDeltaMovement(delta);
            }

            level().getProfiler().pop();
            level().getProfiler().push("rest");
            boolean xCollision = !Mth.equal(delta.x, delta.x);
            boolean zCollision = !Mth.equal(delta.z, delta.z);
            horizontalCollision = xCollision || zCollision;
            verticalCollision = delta.y != delta.y;
            verticalCollisionBelow = verticalCollision && delta.y < 0;
            minorHorizontalCollision = horizontalCollision && isHorizontalCollisionMinor(delta);

            setOnGroundWithMovement(verticalCollisionBelow, delta);
            BlockPos onPos = getOnPos();
            BlockState onState = level().getBlockState(onPos);
            checkFallDamage(delta.y, onGround(), onState, onPos);
            if (isRemoved()) level().getProfiler().pop();
            else {
                if (horizontalCollision) {
                    Vec3 delta3 = getDeltaMovement();
                    setDeltaMovement(xCollision ? 0 : delta3.x, delta3.y, zCollision ? 0 : delta3.z);
                }

                Block block = onState.getBlock();
                if (delta.y != delta.y) block.updateEntityAfterFallOn(level(), this);
                if (onGround()) block.stepOn(level(), onPos, onState, this);

                Entity.MovementEmission movementEmission = getMovementEmission();
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
    public boolean hurt(DamageSource source, float amount) {
        if (amount > 2) {
            if (source.is(DamageTypes.ARROW)) health -= 2;
            else if (source.is(DamageTypes.FIREWORKS)) health -= 3;
            else health--;

            if (health <= 0) kill();
        }
        return amount > 2;
    }

//    @Override
//    public void remove(RemovalReason reason) {
//        super.remove(reason);
//        //if (reason == RemovalReason.KILLED && level().isClientSide) for (int i = 0; i < 10; i++) level().addParticle(ParticleTypes.SMOKE, getX() + 2 * Math.random() - 1, getY() + 0.4, getZ() + 2 * Math.random() - 1, 0, 0, 0);
//    }


}
