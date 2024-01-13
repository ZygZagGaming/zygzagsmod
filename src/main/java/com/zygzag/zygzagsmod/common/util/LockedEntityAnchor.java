package com.zygzag.zygzagsmod.common.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LockedEntityAnchor extends SimplPositionAnchor {
    public Entity target;
    public Function<Entity, Vec3> positionGetter;
    public LockedEntityAnchor(Entity target, Function<Entity, Vec3> positionGetter) {
        super();
        this.target = target;
        this.positionGetter = positionGetter;
    }
    public LockedEntityAnchor(Entity target) {
        this(target, Entity::position);
    }

    @Override
    public void tick() {
        set(positionGetter.apply(target));
        super.tick();
    }

    public static LockedEntityAnchor eyes(Entity target) {
        return new LockedEntityAnchor(target, Entity::getEyePosition);
    }
}
