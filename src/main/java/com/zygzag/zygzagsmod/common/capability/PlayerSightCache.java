package com.zygzag.zygzagsmod.common.capability;

import com.zygzag.zygzagsmod.common.effect.SightEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.awt.*;
import java.util.Map;

@AutoRegisterCapability
public interface PlayerSightCache {
    Map<SightEffect, Map<BlockPos, Color>> blockStateColorMap();
    Player getPlayer();
    void update(SightEffect s, int amplifier);
}
