package io.github.zygzaggaming.zygzagsmod.common.entity;

import io.github.zygzaggaming.zygzagsmod.common.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SmallMagmaticFireball extends Fireball {
    public SmallMagmaticFireball(EntityType<? extends SmallMagmaticFireball> p_37364_, Level p_37365_) {
        super(p_37364_, p_37365_);
    }

    public SmallMagmaticFireball(Level p_37375_, LivingEntity living, Vec3 p_347501_) {
        super(EntityType.SMALL_FIREBALL, living, p_347501_, p_37375_);
    }

    public SmallMagmaticFireball(Level p_37367_, double p_37368_, double p_37369_, double p_37370_, Vec3 p_347543_) {
        super(EntityType.SMALL_FIREBALL, p_37368_, p_37369_, p_37370_, p_347543_, p_37367_);
    }

    @Override
    protected void onHitEntity(EntityHitResult living) {
        super.onHitEntity(living);
        if (this.level() instanceof ServerLevel serverlevel) {
            Entity entity1 = living.getEntity();
            Entity $$4 = this.getOwner();
            int $$5 = entity1.getRemainingFireTicks();
            entity1.igniteForSeconds(5.0F);
            DamageSource $$6 = this.damageSources().fireball(this, $$4);
            if (!entity1.hurt($$6, 5.0F)) {
                entity1.setRemainingFireTicks($$5);
            } else {
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity1, $$6);
            }
            Difficulty difficultyInstance = level().getDifficulty();
            int i = 2;
            if (difficultyInstance == Difficulty.EASY) i = 1;
            else if (difficultyInstance == Difficulty.NORMAL) i = 2;
            else if (difficultyInstance == Difficulty.HARD) i = 3;
            ModUtil.incrementOnHitEntityOverheat(living, i);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult block) {
        super.onHitBlock(block);
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.neoforged.neoforge.event.EventHooks.canEntityGrief(this.level(), entity)) {
                BlockPos blockpos = block.getBlockPos().relative(block.getDirection());
                if (this.level().isEmptyBlock(blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource p_37381_, float p_37382_) {
        return false;
    }
}
