package io.github.zygzaggaming.zygzagsmod.common.util;

import net.minecraft.world.entity.Entity;

public interface HasRotations<T extends Entity & HasRotations<T>> {
    WorldlyRotation[] getRotations();
    @Nullable WorldlyRotation getRotation(int index);
    void setRotation(int index, WorldlyRotation rotation);
}
