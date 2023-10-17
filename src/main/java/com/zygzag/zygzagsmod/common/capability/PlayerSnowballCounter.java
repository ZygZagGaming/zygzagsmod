package com.zygzag.zygzagsmod.common.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Set;
import java.util.UUID;

public interface PlayerSnowballCounter {
    Player getPlayer();
    void checkTargets();
    Set<UUID> targets();
    void hitTarget(UUID targetUUID);
    int getCounterForTarget(UUID targetUUID);
}
