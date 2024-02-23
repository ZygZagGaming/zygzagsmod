package io.github.zygzaggaming.zygzagsmod.common.capability;

import io.github.zygzaggaming.zygzagsmod.common.effect.SightEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerSightCache {
    public final Map<SightEffect, Map<BlockPos, Color>> map = new HashMap<>();

    public Map<SightEffect, Map<BlockPos, Color>> blockStateColorMap() {
        return map;
    }

    public void update(SightEffect sightEffect, Player player, int amplifier) {
        System.out.println("We are updating");
        var world = player.level();
        var sightRange = sightEffect.range(amplifier);
        var playerBlockPos = player.blockPosition();
        var minimap = map.computeIfAbsent(sightEffect, k -> new HashMap<>());
        minimap.clear();
        for (int blockX = playerBlockPos.getX() - sightRange; blockX <= playerBlockPos.getX() + sightRange; blockX++) {
            for (int blockY = playerBlockPos.getY() - sightRange; blockY <= playerBlockPos.getY() + sightRange; blockY++) {
                for (int blockZ = playerBlockPos.getZ() - sightRange; blockZ <= playerBlockPos.getZ() + sightRange; blockZ++) {
                    var blockPos = new BlockPos(blockX, blockY, blockZ);
                    BlockState blockStateToCheck = world.getBlockState(blockPos);
                    if (sightEffect.test(blockStateToCheck)) {
                        Color colorToUse = new Color(sightEffect.color(blockStateToCheck));
                        if (!colorToUse.equals(Color.BLACK)) {
                            var alpha = (int) (0xff * (1 - player.getEyePosition().distanceTo(Vec3.atCenterOf(blockPos)) / (sightRange * Math.sqrt(3))));
                            minimap.put(blockPos, new Color(colorToUse.getRed(), colorToUse.getGreen(), colorToUse.getBlue(), alpha));
                        }
                    }
                }
            }
        }
    }
}
