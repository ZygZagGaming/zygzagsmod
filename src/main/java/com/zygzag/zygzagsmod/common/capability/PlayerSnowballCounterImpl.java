package com.zygzag.zygzagsmod.common.capability;

import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

public class PlayerSnowballCounterImpl implements PlayerSnowballCounter {
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public void checkTargets() {

    }

    @Override
    public Set<UUID> targets() {
        return null;
    }

    @Override
    public void hitTarget(UUID targetUUID) {

    }

    @Override
    public int getCounterForTarget(UUID targetUUID) {
        return 0;
    }
}
