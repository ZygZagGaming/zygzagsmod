package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.registries.EntityRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PlayerAlliedSkeleton extends WitherSkeleton {
    public UUID alliedPlayerUUID;
    public PlayerAlliedSkeleton(EntityType<? extends WitherSkeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        var optional = pLevel.players().stream().min((a, b) -> (int) (a.distanceTo(this) - b.distanceTo(this)));
        alliedPlayerUUID = optional.map(Entity::getUUID).orElse(null);
        this.setCanPickUpLoot(true);
    }

    public PlayerAlliedSkeleton(Level world, Player player) {
        this(EntityRegistry.PLAYER_ALLIED_SKELETON.get(), world);
        alliedPlayerUUID = player.getUUID();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        targetSelector.removeGoal(new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p) -> !p.getUUID().equals(alliedPlayerUUID)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false, (l) -> !(l instanceof PlayerAlliedSkeleton pas) || !pas.alliedPlayerUUID.equals(alliedPlayerUUID)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putUUID("alliedPlayerUUID", alliedPlayerUUID);
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        alliedPlayerUUID = pCompound.getUUID("alliedPlayerUUID");
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return pPlayer.getUUID() != alliedPlayerUUID;
    }
}
