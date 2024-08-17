package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlailProjectile extends Projectile {
    public static final ResourceKey<DamageType> FLAIL_DMG_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID,  "flail"));
    public double flatDamage, damagePerBlockPerTick;
    public FlailProjectile(EntityType<? extends Projectile> type, Level world) {
        super(type, world);
    }

    public FlailProjectile(Level world, Player owner, Vec3 pos, Vec3 delta, float flatDamage, float damagePerBlockPerTick) {
        super(EntityTypeRegistry.FLAIL_PROJECTILE.get(), world);
        setPos(pos);
        setDeltaMovement(delta);
        setOwner(owner);
        noPhysics = false;
        this.flatDamage = flatDamage;
        this.damagePerBlockPerTick = damagePerBlockPerTick;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) { }

    @Override
    public void tick() {
        super.tick();

        Player owner = getPlayerOwner();
        if (owner == null) discard();
        else {
            Vec3 diff = getBoundingBox().getCenter().subtract(owner.getBoundingBox().getCenter());
            if (diff.lengthSqr() >= 60 * 60) discard();
//            else if (diff.lengthSqr() <= 1.2 * 1.2 && tickCount >= 5)
//                discard();
            else {
                Vec3 before = position();
                move(MoverType.SELF, getDeltaMovement());
                if (!isNoGravity()) {
                    Vec3 after = position();
                    Vec3 force = diff.scale(-0.02);
                    Vec3 parallel = owner.getLookAngle().scale(owner.getLookAngle().dot(force));
                    Vec3 ortho = force.subtract(parallel);
                    setDeltaMovement(getDeltaMovement().add(parallel).add(ortho.scale(3)).scale(0.75).add(0, isNoGravity() ? 0 : -0.1, 0));
                    Vec3 delta = after.subtract(before);

                    EntityHitResult result = ProjectileUtil.getEntityHitResult(level(), this, after, after.add(delta), getBoundingBox().expandTowards(delta).inflate(1), this::canHitEntity, 0.3f);
                    if (result != null) onHitEntity(result);
                }
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
        Player owner = getPlayerOwner();
        if (owner != null && player.is(owner) && !this.level().isClientSide) {
            level().playLocalSound(
                    getX(),
                    getY(),
                    getZ(),
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS,
                    0.2F,
                    (random.nextFloat() - random.nextFloat()) * 1.4F + 2.0F,
                    false
            );
            discard();
        }
    }

    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }

    public double damage() {
        return flatDamage + damagePerBlockPerTick * getDeltaMovement().length();
    }

    public DamageSource getDamageSource() {
        return new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FLAIL_DMG_TYPE), getPlayerOwner(), this, position());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity entity = result.getEntity();
        if (!(entity instanceof LivingEntity living) || living.hurt(getDamageSource(), (float) damage())) {
            Player owner = getPlayerOwner();
            assert owner != null;
            entity.setRemainingFireTicks(Math.max(entity.getRemainingFireTicks(), 20 * 3));
            setDeltaMovement(owner.getBoundingBox().getCenter().subtract(getBoundingBox().getCenter()).scale(0.15));
            setNoGravity(true);
        }
    }
}
